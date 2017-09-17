package org.mdb.rest.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.*;

/*
 * TODO: Create Grafana Setup
 *
 * Basic RestController /nnas
 */
@RestController
public class GrafanaController {
    @Autowired
    MongoOperations op;

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}", method=RequestMethod.GET, produces={"application/json"})
    public Map base(){
        Map<String, String> ret = new HashMap<>();
        ret.put("result", "success");
        return ret;
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/search", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> search(){
        /*
         * TODO: Execute Search
         */
        return null;
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/annotations", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> annotations(){
        /*
         * TODO: Execute Annotations call
         */
        return null;
    }

    @RequestMapping(value="/api/v1/grafana/{dim}/{period}/query", method=RequestMethod.POST, produces={"application/json"})
    public List<Map> query(){
        /*
         * TODO: Execute query calls
         */
        return null;
    }
}