package org.mdb.rest.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

/*
 *
 */
@RestController
public class MdbRestControllerV2 {
    @Autowired
    MongoOperations op;

    @RequestMapping(value = "/api/v2/{period}", method = RequestMethod.GET, produces = {"application/json"})
    public List<Map> index(
            @PathVariable("period") String period,
            @ApiParam(value="UserId", required=true) @RequestParam(value="userId") String userId,
            @ApiParam(value="From Date yyyy-MM-dd'T'HH:mm:ss.SSSZ", required=true)
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDateTime,
            @ApiParam(value="To Date yyyy-MM-dd'T'HH:mm:ss.SSSZ", required=true)
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDateTime,
            @ApiParam(value="rep code", required=false, defaultValue="*") @RequestParam(value="rep", required=false, defaultValue="*") String rep,
            @ApiParam(value="date of birth", required=false, defaultValue="*") @RequestParam(value="dob_yr", required=false, defaultValue="*") String dobYr,
            @ApiParam(value="account type", required=false, defaultValue="*") @RequestParam(value="acct_type", required=false, defaultValue="*") String acctType,
            @ApiParam(value="state", required=false, defaultValue="*") @RequestParam(value="state", required=false, defaultValue="*") String state,
            @ApiParam(value="start pos", defaultValue="0") @RequestParam(value="offset", required=false, defaultValue="0") int offset,
            @ApiParam(value="page size", defaultValue="1000") @RequestParam(value="limit", required=false, defaultValue="1000") int limit
    ) {
        try {
            AggregationOperation userDateMatch = Aggregation.match(
                    Criteria.where("userId").is(userId).andOperator(
                            Criteria.where("date").gte(new Date(fromDateTime.toInstant().toEpochMilli())),
                            Criteria.where("date").lte(new Date(toDateTime.toInstant().toEpochMilli()))
                    )
            );

            ProjectionOperation filterDimensions = Aggregation.project().and(new AggregationExpression() {
                @Override
                public DBObject toDbObject(AggregationOperationContext aggregationOperationContext) {
                    // https://docs.mongodb.com/manual/reference/operator/aggregation/filter/index.html
                    DBObject filterExp = new BasicDBObject("input", "$val").append("as", "val");
                    List<DBObject> condExp = new ArrayList<>();

                    List<String> repCond = new ArrayList<>();
                    List<String> dobCond = new ArrayList<>();
                    List<String> acctTypeCond = new ArrayList<>();
                    List<String> stateCond = new ArrayList<>();

                    repCond.add("$$val.dim.rep");
                    repCond.add(rep);
                    condExp.add(new BasicDBObject("$eq", repCond));

                    dobCond.add("$$val.dim.dob_yr");
                    dobCond.add(dobYr);
                    condExp.add(new BasicDBObject("$eq", dobCond));

                    acctTypeCond.add("$$val.dim.acct_type");
                    acctTypeCond.add(acctType);
                    condExp.add(new BasicDBObject("$eq", acctTypeCond));

                    stateCond.add("$$val.dim.state");
                    stateCond.add(state);
                    condExp.add(new BasicDBObject("$eq", stateCond));

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

            SkipOperation s = Aggregation.skip((long)offset*limit);
            LimitOperation l = Aggregation.limit(limit);
            Aggregation agg = Aggregation.newAggregation(userDateMatch, filterDimensions, unwindVal, projectDims, s, l);

            /*
             * TODO: only monthly here
             */
            AggregationResults<Map> results = op.aggregate(agg, period, Map.class);
            return results.getMappedResults();
        }catch(Exception e){
            System.out.println("Excpetion: " + e.getLocalizedMessage());
            return new LinkedList<Map>();
        }
    }
}