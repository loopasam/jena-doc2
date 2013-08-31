package tuto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import org.apache.log4j.PropertyConfigurator;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class CreateRdf {

	public static void main(String[] args) throws FileNotFoundException {

		PropertyConfigurator.configure("/home/samuel/git/jena-doc2/svn/trunk/content/xp/lib/jena-log4j.properties");

		//More info: http://localhost/tutorials/rdf_api.html
		
		Model m = ModelFactory.createDefaultModel();
		
		String schemaDotOrgPrefix = "http://schema.org/";
		String localPrefix = "http://localhost/";
		String rdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String rdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
		
		m.setNsPrefix("schema", schemaDotOrgPrefix);
		m.setNsPrefix("", localPrefix);
		m.setNsPrefix("rdf", rdfPrefix);
		m.setNsPrefix("rdfs", rdfsPrefix);
		m.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		
		Property name = m.createProperty(schemaDotOrgPrefix + "name"); 
		Property endDate = m.createProperty(schemaDotOrgPrefix + "endDate");
		Property startDate = m.createProperty(schemaDotOrgPrefix + "startDate");
		Property location = m.createProperty(schemaDotOrgPrefix + "location");
		Property telephone = m.createProperty(schemaDotOrgPrefix + "telephone");
		Property geo = m.createProperty(schemaDotOrgPrefix + "geo");
		Property latitude = m.createProperty(schemaDotOrgPrefix + "latitude");
		Property longitude = m.createProperty(schemaDotOrgPrefix + "longitude");
		
		Resource event = m.createResource(schemaDotOrgPrefix + "Event");
		Resource musicEvent = m.createResource(schemaDotOrgPrefix + "MusicEvent");
		m.add(musicEvent, RDFS.subClassOf, event);
		Resource place = m.createResource(schemaDotOrgPrefix + "Place");
		Resource geoCoordinates = m.createResource(schemaDotOrgPrefix + "GeoCoordinates");
		
		Resource event1Id = m.createResource(localPrefix + UUID.randomUUID());
		m.add(event1Id, RDF.type, musicEvent);
		m.add(event1Id, name, "Paleo Festival");
		m.add(event1Id, startDate, m.createTypedLiteral("2011-05-05", XSDDatatype.XSDdate));
		m.add(event1Id, endDate, m.createTypedLiteral("2011-05-01", XSDDatatype.XSDdate));
		
		Resource location1Id = m.createResource(localPrefix + UUID.randomUUID());
		m.add(event1Id, location, location1Id);
		m.add(location1Id, RDF.type, place);
		m.add(location1Id, telephone, "05-4503-43");
		
		Resource geo1Id = m.createResource(localPrefix + UUID.randomUUID());
		m.add(location1Id, geo, geo1Id);
		m.add(geo1Id, RDF.type, geoCoordinates);
		m.add(geo1Id, latitude, m.createTypedLiteral(37, XSDDatatype.XSDint));
		m.add(geo1Id, longitude, m.createTypedLiteral(-122, XSDDatatype.XSDint));
		
		m.write(new FileOutputStream(new File("events.ttl")), "TURTLE");

	}

}
