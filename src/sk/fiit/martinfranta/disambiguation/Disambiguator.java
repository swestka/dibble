package sk.fiit.martinfranta.disambiguation;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.uima.jcas.tcas.Annotation;
import org.openrdf.OpenRDFException;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.disambiguation.module.Factory;
import sk.fiit.martinfranta.disambiguation.module.IEntity;
import sk.fiit.martinfranta.rdftools.SparqlExecutor;

public class Disambiguator {
	
	private static ArrayList<Annotation> mentions;
	private static Logger logger = Logger.getLogger(Disambiguator.class);
	
	public static void disambiguate (ArrayList<Annotation> foundMentions) {
		mentions = foundMentions;
		
		logger.info("Iterating entities");
		
		for (Annotation a : mentions) {
			if (a.getType().toString().equals(Extractor.PERSON)) {
				logger.info(a.getCoveredText()+": ");
				
				IEntity entity = Factory.createEntity();
				entity.setIdentifier(a.getCoveredText());
				entity.setCandidate(false);
				entity.setField("name", a.getCoveredText());
				
				fillCandidates(entity);
				
				int candidatesSize = entity.getCandidates().size();
				if (candidatesSize == 0) {
					logger.info("Nothing found");
				}
				
				else if (candidatesSize == 1) {
					logger.info(entity.getCandidates().get(0));
				}
				
				else if (candidatesSize < 5) {
					logger.info("There are more candidates! " + candidatesSize);
					for (IEntity t : entity.getCandidates()) {
						logger.info("candidate: "+t.getIdentifier());
						setEntityContext(t);
					}
				}
				else {
					logger.info("Too many candidates "+candidatesSize);
				}
			}
		}
	}
	
	public static void fillCandidates(IEntity entity) {
		SparqlExecutor exec = SparqlExecutor.create();
		
		try {

			TupleQueryResult result = exec.query(entity.getCandidatesSparql());
			
			try {
				while (result.hasNext()) {
					BindingSet bindingSet = result.next();
					IEntity candidate = Factory.createEntity();
					candidate.setCandidate(true);
					candidate.setBindingSet(bindingSet);
					entity.addCandidate(candidate);
				}
			}
			finally {
				result.close();
			}
		   
		}
		catch (OpenRDFException e) {
		   logger.error(e.getStackTrace());
		}
	}
	
	private static void setEntityContext(IEntity entity) {
		SparqlExecutor exec = SparqlExecutor.create();
		
		try {
			for (ContextAttribute<?> attr : entity.getContext().getContextAttributes().values()) {
				logger.info(attr.getIdentifier());
				TupleQueryResult result = exec.query(attr.getSparql()); 
				
				while (result.hasNext()) {
					BindingSet bindingSet = result.next();
					entity.setContextBindingSet(attr, bindingSet);
				}
				
				result.close();
			}
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void sparql () {
		SparqlExecutor exec = SparqlExecutor.create();
			
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?s ?n WHERE {\n");
//		queryString.append("?s foaf:homepage ?o.\n");
//		queryString.append("?s foaf:name ?n.\n");
//		queryString.append("FILTER regex(?n, \"Bielik*\", \"i\").\n");
//		queryString.append("}"); 
//		queryString.append("LIMIT 100");

//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?s WHERE {\n");
//		queryString.append("?s a <http://lsdis.cs.uga.edu/projects/semdis/opus#Article>.\n");
//		queryString.append("?p opus:Author ?o.");
//		queryString.append("FILTER regex(?s, \"Entity\", \"s\").");
//		queryString.append("} LIMIT 100");
//			
		
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
//		queryString.append("SELECT ?o ?s WHERE {\n");
//		queryString.append("?s a opus:Book.\n");
//		queryString.append("?s opus:author ?x.\n");
//		queryString.append("?x ?p ?y.\n");
//		queryString.append("?y a foaf:Person.\n");
//		queryString.append("?s rdfs:label ?o.\n");
//		queryString.append("FILTER regex(?o, \"semantic(.*)\", \"s\").\n");
//		queryString.append("} ");
//		queryString.append("LIMIT 100");
		
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?name ?coname WHERE {\n");
//		queryString.append("<http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/b/Bielikov=aacute=:M=aacute=ria.html> foaf:name ?name. \n");		
////		queryString.append("FILTER regex(?name, \"Bielik\", \"i\").\n");		
//		queryString.append("?art rdf:type opus:Article.\n");
//		queryString.append("?art opus:author [?collection <http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/b/Bielikov=aacute=:M=aacute=ria.html>].\n");
//		queryString.append("?art opus:author [?members ?coauthor].\n");
//		queryString.append("?coauthor foaf:name ?coname.\n");
//		queryString.append("FILTER (?name != ?coname). \n");
//
//		queryString.append("} LIMIT 100");			
				
		
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?s ?o WHERE {\n");
//		queryString.append("OPTIONAL {<http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/a/Amann:Bernhard.html> foaf:homepage ?s}.\n");
//		queryString.append("<http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/a/Amann:Bernhard.html> foaf:name ?o.\n");
//		queryString.append("}");
		
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?p ?x WHERE {\n");
//		queryString.append("?auth a foaf:Person. \n");
//		queryString.append("?auth foaf:workplaceHomepage ?p; foaf:name ?x. \n");
//		queryString.append("} LIMIT 1000");

		StringBuilder queryString = new StringBuilder();
		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		queryString.append("SELECT ?t ?p WHERE {\n");
		
//		queryString.append("?art a opus:Article.\n");
//		queryString.append("OPTIONAL{?art a opus:Article_in_proceedings}.\n");
//		queryString.append("OPTIONAL{?art a opus:Book}.\n");
//		queryString.append("OPTIONAL{?art a opus:Publication}.\n");
		queryString.append("?art a $t.\n");
		queryString.append("?art opus:author [?collection <http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/b/Bielikov=aacute=:M=aacute=ria.html>].\n");
		
		queryString.append("?art rdfs:label  ?p. \n");
		queryString.append(" }");

//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?p WHERE {\n");
//		queryString.append("?art rdf:type opus:Article.\n");
//
//		queryString.append("?art ?p ?x. \n");
//		queryString.append("LIMIT 15");
//		
//		queryString.append("}");
//		
		TupleQueryResult result;
		try {
			result = exec.query(queryString);
			
			while (result.hasNext()) {
				BindingSet bindingSet = result.next();
				System.out.println(
						bindingSet.getValue("t").stringValue()+" "+ 
						bindingSet.getValue("p").stringValue()
				);
			}
			result.close();
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}
			
			
	}
	
	public void proximity(Entity candidate) {
		
	}
	
	
}
