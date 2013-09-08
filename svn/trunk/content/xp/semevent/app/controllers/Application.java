package controllers;

import play.*;
import play.mvc.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import org.apache.jena.riot.RDFDataMgr;

import static jenajsonld.JenaJSONLD.JSONLD ;


import com.google.gson.Gson;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Application extends Controller {

	public static void index() {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX event: <http://localhost:9000/event/> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"CONSTRUCT {" +
				"?type rdfs:subClassOf schema:Event ." +
				"} WHERE { " +
				"?event a ?type ." +
				"?type rdfs:subClassOf schema:Event ." +
				"}";

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model types = qexec.execConstruct();
		types.write(System.out, "TURTLE");
		qexec.close();
		render(types);
	}
	
	public static void query(String query) {
		System.out.println(query);

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", query);
		ResultSet results = qexec.execSelect();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(baos, results);
		String stringResult = baos.toString();
		qexec.close();

		renderJSON(stringResult);
	}

	public static void events() {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX event: <http://localhost:9000/event/> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"CONSTRUCT {" +
				"?event a ?type ;" +
				" schema:endDate ?end ;" +
				" schema:startDate  ?start ;" +
				" schema:name ?name ;" +
				" schema:description ?description ." +
				"} WHERE { " +
				"?type rdfs:subClassOf schema:Event ." +
				"?event a ?type ;" +
				" schema:endDate ?end ;" +
				" schema:startDate  ?start ;" +
				" schema:name ?name ;" +
				" schema:description ?description ." +
				"}";

		
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model events = qexec.execConstruct();
		
		events.write(System.out, "TURTLE");
		qexec.close();
		render(events);
	}

	public static void event(String eventId) {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX event: <http://localhost:9000/event/> " +
				"DESCRIBE event:" + eventId;
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model event = qexec.execDescribe();
		event.write(System.out, "TURTLE");
		qexec.close();
		render(event);
	}


	public static void place(String placeId) {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX place: <http://localhost:9000/place/> " +
				"DESCRIBE place:" + placeId;

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model place = qexec.execDescribe();
		qexec.close();
		render(place);
	}

	public static void geocoordinates(String geoId) {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX geocoordinates: <http://localhost:9000/geocoordinates/> " +
				"DESCRIBE geocoordinates:" + geoId;

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model geo = qexec.execDescribe();
		qexec.close();
		render(geo);
	}

	public static void geocoordinatesJson() {
		String queryString = "PREFIX schema: <http://schema.org/> " +
				"PREFIX : <http://localhost:9000/> " +
				"PREFIX event: <http://localhost:9000/event/> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"CONSTRUCT {" +
				"?event rdf:type ?type ." +
				"?event schema:name ?name ." +
				"?event schema:description ?description ." +
				"?event schema:latitude ?latitude ." +
				"?event schema:longitude  ?longitude ." +
				"} WHERE { " +
				"?event rdf:type ?type ." +
				"?event schema:name ?name ." +
				"?event schema:description ?description ." +
				"?type rdfs:subClassOf schema:Event ." +
				"?event schema:location ?place ." +
				"?place schema:geo ?geocoordinate ." +
				"?geocoordinate schema:latitude ?latitude ." +
				"?geocoordinate schema:longitude  ?longitude ." +
				"}";

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		Model coor = qexec.execConstruct();

		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		RDFDataMgr.write(baos, coor, JSONLD) ;
		String stringResult = baos.toString();
		qexec.close();
		renderJSON(stringResult);
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
				":" + placeId + " schema:name ?name . " +
				"}";

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/public/query", queryString);
		ResultSet results = qexec.execSelect();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(baos, results);
		String stringResult = baos.toString();
		qexec.close();

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

	public static void map() {
		render();
	}


}
