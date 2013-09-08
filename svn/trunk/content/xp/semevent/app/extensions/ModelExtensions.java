package extensions;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import play.templates.JavaExtensions;

public class ModelExtensions extends JavaExtensions {

	public static String shortForm(String lonForm, Model model) {
		String shortForm = model.shortForm(lonForm);
		Pattern pattern = Pattern.compile(".*:(.*)");
		Matcher matcher = pattern.matcher(shortForm);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return shortForm;
	}
	
	public static String icon(Resource resource) {
		String type = value(resource, RDF.type);
		if(type.equals("http://schema.org/SportsEvent")){
			type = "glyphicon-bullhorn";
		}else if(type.equals("http://schema.org/MusicEvent")){
			type = "glyphicon-music";
		}
		return type;
	}

	public static String value(Model model, String property) {
		String fullUri = model.expandPrefix(property);
		String value = model.listObjectsOfProperty(model.getProperty(fullUri)).next().toString();
		return value;
	}

	public static List<Resource> list(Model model, String property) {
		return model.listSubjects().toList();
	}

	public static String value(Resource resource, String property) {
		Model m = ModelFactory.createDefaultModel();
		Property propertyObj = m.createProperty(property);
		String value = resource.getProperty(propertyObj).getString();
		return value;
	}

	public static String value(Resource resource, Property property) {
		String value = resource.getProperty(property).getResource().toString();
		return value;
	}



	public static List<RDFNode> values(Model model, String property) {
		String fullUri = model.expandPrefix(property);
		List<RDFNode> value = model.listObjectsOfProperty(model.getProperty(fullUri)).toList();
		return value;
	}

}
