package tuto;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class SparqlQuery {

	public static void main(String[] args) {

		Model model = FileManager.get().loadModel("events.ttl");
		String queryString = "SELECT * WHERE { ?s ?p ?o . }";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results, query);
		qexec.close() ;

	}

}
