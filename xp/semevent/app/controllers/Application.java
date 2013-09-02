package controllers;

import play.*;
import play.mvc.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

import com.google.gson.Gson;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void place(String placeId) {
    	render(placeId);
    }
    
    public static void geo() {
    	render();
    }

    
    public static void placeJson(String placeId) {
    	String queryString = "PREFIX schema: <http://schema.org/> " +
    			"PREFIX : <http://localhost/> " +
    			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
    			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
    			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
    			"SELECT * " +
    			"WHERE {" +
    			":" + placeId + " schema:telephone ?telephone . " +
    			":" + placeId + " a ?type . " +
    			":" + placeId + " schema:geo ?geo . " +
    			"?geo schema:latitude ?latitude . " +
    			"?geo schema:longitude ?longitude . " +
    			"}";
    	//Logger.info(queryString);
    	
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		ResultSet results = qexec.execSelect();
				
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(baos, results);
		String stringResult = baos.toString();
		qexec.close() ;
		
    	renderJSON(stringResult);
    }
    
    public static void geoJson() {
    	String queryString = "PREFIX schema: <http://schema.org/> " +
    			"PREFIX : <http://localhost/> " +
    			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
    			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
    			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
    			"SELECT * " +
    			"WHERE {" +
    			"?geo schema:latitude ?latitude . " +
    			"?geo schema:longitude ?longitude . " +
    			"?geo a schema:GeoCoordinates . " +
    			"}";    	
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		ResultSet results = qexec.execSelect();
				
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(baos, results);
		String stringResult = baos.toString();
		qexec.close() ;
		
    	renderJSON(stringResult);
    }


}
