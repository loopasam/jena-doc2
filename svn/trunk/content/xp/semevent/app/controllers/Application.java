package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void place(String placeId) {
    
    	//Does the query over Fuseki
    	String queryString = "PREFIX schema: <http://schema.org/> " +
    			"SELECT * " +
    			"WHERE {" +
    			placeId + " ?predicate ?object ." +
    			"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService();
		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results, query);
		qexec.close() ;

    	//Ask to the Json object and send it to render() - handle with knockout then
    	//or simply print out tags
    	render();
    }

}
