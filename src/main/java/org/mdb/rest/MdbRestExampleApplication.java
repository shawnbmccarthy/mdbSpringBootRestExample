package org.mdb.rest;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class MdbRestExampleApplication {

	@Autowired
	private MongoTemplate mongoTemplate;

	public MdbRestExampleApplication(){
        mongoTemplate = new MongoTemplate(new MongoClient(),     "sample");
    }

	public static void main(String[] args) {
	    SpringApplication.run(MdbRestExampleApplication.class, args);
	}
}
