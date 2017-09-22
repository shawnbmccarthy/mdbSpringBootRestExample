package org.mdb.rest.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

/*
 * final example just return the documents
 */
@RestController
public class MdbRestControllerV3 {
    @Autowired
    MongoOperations op;

    @RequestMapping(value = "/api/v3/{period}", method = RequestMethod.GET, produces = {"application/json"})
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
            Query q = new Query();
            q.addCriteria(Criteria.where("userId").is(userId).andOperator(
                    Criteria.where("date").gte(new Date(fromDateTime.toInstant().toEpochMilli())),
                    Criteria.where("date").lte(new Date(toDateTime.toInstant().toEpochMilli())))
            );

            List<Map> results = op.find(q, Map.class, period);
            return results;
        }catch(Exception e){
            System.out.println("Excpetion: " + e.getLocalizedMessage());
            return new LinkedList<Map>();
        }
    }
}