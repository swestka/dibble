package sk.fiit.martinfranta.disambiguation;

import java.util.ArrayList;

import org.apache.uima.jcas.tcas.Annotation;

import sk.fiit.martinfranta.rdftools.SparqlExecutor;

public class Main {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		
		SparqlExecutor.repositoryId = args[1];
//		Disambiguator.sparql();
		Extractor e = new Extractor(args[0]);
		e.addType(Extractor.PERSON)
//			.addType(Extractor.ORGANIZATION)  
//			.addType(Extractor.LOCATION)
		;

		ArrayList<Annotation> entityMentions = e.extract();
		Disambiguator.disambiguate(entityMentions);
	}

}