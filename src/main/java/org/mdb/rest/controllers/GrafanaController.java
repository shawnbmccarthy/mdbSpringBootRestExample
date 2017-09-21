package org.mdb.rest.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

/*
 * TODO: Create Grafana Setup
 *
 * Basic Grafana Rest Controller -
 * Need to add steps to conform to grafana requirements
 */
@RestController
public class GrafanaController {
    @Autowired
    MongoOperations op;

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}", method=RequestMethod.GET, produces={"application/json"})
    public Map base(
            @PathVariable("dim") String dim,
            @PathVariable("period") String period
    ){
        Map<String, String> ret = new HashMap<>();
        ret.put("result", "success");
        return ret;
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/search", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> search(
            @PathVariable("dim") String dim,
            @PathVariable("period") String period
    ){
        /*
         * TODO: Execute Search
         * - looks like bootstrap does not have distinct - well this is a shame - here is the new query
         * db[period].aggregate([
         *   {$project: {_id: 0, val: 1},
         *   {$unwind: '$val'},
         *   {$group: {_id: '$val.dim.rep'}}
         * ])
         */
        ProjectionOperation project = Aggregation.project().andExclude("_id").andInclude("val");
        UnwindOperation unwind = Aggregation.unwind("$val");
        GroupOperation group = Aggregation.group("$val.dim." + dim);
        Aggregation agg = Aggregation.newAggregation(project, unwind, group);
        return op.aggregate(agg, period, Map.class).getMappedResults();
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/query", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> query(
            @PathVariable("dim") String dim,
            @PathVariable("period") String period
    ){
        /*
         * TODO: Execute query calls (need to account for JSON Body)
         */
        return null;
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/annotations", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> annotations(
            @PathVariable("dim") String dim,
            @PathVariable("period") String period
    ){
        /*
         * TODO: Execute Annotations call
         * No clear reason as to why this is needed at at this moment
         */
        return new LinkedList<Map>();
    }
}