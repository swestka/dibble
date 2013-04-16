package sk.fiit.martinfranta.disambiguation;

import java.util.ArrayList;

import org.apache.uima.jcas.tcas.Annotation;

import sk.fiit.martinfranta.tools.learning.PairwiseClassifier;

public class Main {

	static {
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
	}
	
	public static void main(String[] args) {
		KnowledgeBase kb = KnowledgeBase.getInstance();
		kb.initialize();
		
		Extractor e = new Extractor(args[0]);
		e
			.addType(Extractor.PERSON)
//			.addType(Extractor.ORGANIZATION)  
//			.addType(Extractor.LOCATION)
		;
		
		ArrayList<Annotation> entityMentions = e.extract();
		Disambiguator dis = new Disambiguator();
		dis.disambiguate(entityMentions);
		
		PairwiseClassifier.learnTrue(dis.getResolved());
		//dis.classify();
	}

}