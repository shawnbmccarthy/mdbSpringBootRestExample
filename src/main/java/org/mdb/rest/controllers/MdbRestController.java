package org.mdb.rest.controllers;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

/*
 * Basic RestController /nnas
 */
@RestController
public class MdbRestController {
    @Autowired
    MongoOperations op;

    private String mdbCollection;

    @RequestMapping(value = "/nnas", method = RequestMethod.GET, produces = {"application/json"})
    public List<Map> index(
            @ApiParam(value = "UserId", required=true) @RequestParam(value = "userId") String userId,
            @ApiParam(value="From Date yyyy-MM-dd'T'HH:mm:ss.SSSZ", required=true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDateTime,
            @ApiParam(value="To Date yyyy-MM-dd'T'HH:mm:ss.SSSZ", required=true)
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDateTime,
            @ApiParam(
                    value="CSV of Dimensions",
                    required=true,
                    allowableValues="DIRECTION,STATE,DIRECTION|ACCT_STATUS,DIRECTION|ACCT_TYPE,DIRECTION|ACCT_STATUS|REP,DIRECTION|ACCT_TYPE|REP"
            )
            @RequestParam(value = "dimensions") String dimensions,
            @ApiParam(value = "START POSITION", defaultValue = "0") @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @ApiParam(value = "PAGE SIZE", defaultValue = "1000") @RequestParam(value = "limit", required = false, defaultValue = "1000") int limit
    ) {
        try {
            AggregationOperation userDateMatch = Aggregation.match(
                    Criteria.where("userId").is(userId).andOperator(
                            Criteria.where("date").gte(new Date(fromDateTime.toInstant().toEpochMilli())),
                            Criteria.where("date").lte(new Date(toDateTime.toInstant().toEpochMilli()))
                    )
            );

            LimitOperation l = Aggregation.limit(10);

            /*
             * TODO: Need to figure out if it is monthly or something else
             * Description given originally was application defines it
             */

            ProjectionOperation filterDimensions = Aggregation.project().and(new AggregationExpression() {
                @Override
                public DBObject toDbObject(AggregationOperationContext aggregationOperationContext) {
                    // https://docs.mongodb.com/manual/reference/operator/aggregation/filter/index.html
                    DBObject filterExp = new BasicDBObject("input", "$val").append("as", "val");
                    List<DBObject> condExp = new ArrayList<>();

                    /*
                     * TODO: Get clarity on this rule
                     * for right now the assumption will be (until confirmed):
                     * dimmensions map right to the name of the dimensions stored in the document
                     * - rule: if the dimension is listed push on the condExp with an asterisk
                     */
                    String[] dims = dimensions.toLowerCase().split("\\|");
                    for(String dim : dims){
                        List<String> cond = new ArrayList<>();
                        cond.add("$$val.dim." + dim);
                        cond.add("*");
                        condExp.add(new BasicDBObject().append("$eq", cond));
                    }

                    filterExp.put("cond", new BasicDBObject("$and", condExp));
                    return new BasicDBObject("$filter", filterExp);
                }
            }).as("val").andInclude("date").andExclude("_id");

            UnwindOperation unwindVal = Aggregation.unwind("$val");


            /*
             * TODO: Understand what projections are needed for the team
             */
            ProjectionOperation projectDims = Aggregation.project()
                    .andInclude("date")
                    .and("$val.value").as("nna")
                    .and("$val.dim.rep").as("dimension.rep")
                    .and("$val.dim.dob_yr").as("dimension.dob_yr")
                    .and("$val.dim.acct_type").as("dimension.acct_type")
                    .and("$val.dim.state").as("dimension.state");

            Aggregation agg = Aggregation.newAggregation(userDateMatch, l, filterDimensions, unwindVal, projectDims);

            /*
             * TODO: only monthly here
             */
            AggregationResults<Map> results = op.aggregate(agg, mdbCollection, Map.class);
            return results.getMappedResults();
        }catch(Exception e){
            System.out.println("Excpetion: " + e.getLocalizedMessage());
            return new LinkedList<Map>();
        }
    }

    public MdbRestController(){
        mdbCollection = "monthly";
    }
}