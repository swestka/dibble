package sk.fiit.martinfranta.tools.sparql;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

import sk.fiit.martinfranta.tools.DataIterator;
import sk.fiit.martinfranta.tools.IExecutor;
import sk.fiit.martinfranta.tools.KnowledgeBaseException;

public class SparqlExecutor implements IExecutor{

	/**
	 * @param args
	 */
	
	private static SparqlExecutor executor;
	
	private String sesameServer;
	public static String repositoryId;
	private HTTPRepository httpRepository;
	private RepositoryConnection connection;
	private static Logger logger = Logger.getLogger(SparqlExecutor.class);
	
	public SparqlExecutor() {
		if (executor == null) {
			executor = new SparqlExecutor(repositoryId);
		}
	}

	private SparqlExecutor(String id) {
		
	}
	
	@Override
	public void initialize() {
		/** TODO: from properties file */
		
		sesameServer = "http://localhost:8080/openrdf-sesame";
		repositoryId = "njs";
		httpRepository = new HTTPRepository(sesameServer, repositoryId);
		
		try {	
			httpRepository.initialize();
			connection = httpRepository.getConnection();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		httpRepository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.SPARQL);
		logger.info("Executor successfully connected");
	}
	
	public TupleQueryResult query(String queryString) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		logger.info("Performing query: "+queryString);
		TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		TupleQueryResult result = tupleQuery.evaluate();
		return result;
	}
	
	public TupleQueryResult query(StringBuilder queryString) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		return query(new String(queryString));
	}
	
	public void close () {
		try {
			connection.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	public static void sparql() {
		SparqlExecutor exec = new SparqlExecutor();
		exec.initialize();
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
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		queryString.append("SELECT ?x WHERE {\n");
		queryString.append("?auth a ?x.\n");
		queryString.append("} LIMIT 1");

//		StringBuilder queryString = new StringBuilder();
//		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
//		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
//		queryString.append("SELECT ?t ?p WHERE {\n");
		
//		queryString.append("?art a opus:Article.\n");
//		queryString.append("OPTIONAL{?art a opus:Article_in_proceedings}.\n");
//		queryString.append("OPTIONAL{?art a opus:Book}.\n");
//		queryString.append("OPTIONAL{?art a opus:Publication}.\n");
//		queryString.append("?art a $t.\n");
//		queryString.append("?art opus:author [?collection <http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/b/Bielikov=aacute=:M=aacute=ria.html>].\n");
//		
//		queryString.append("?art rdfs:label  ?p. \n");
//		queryString.append(" }");

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
						bindingSet.getValue("auth").stringValue()+" "+ 
						bindingSet.getValue("x").stringValue()
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
	
	@Override
	public DataIterator<TupleQueryResult> executeQuery(String query) throws KnowledgeBaseException {
		try {
			return new DataIterator<TupleQueryResult>(query(query));
		} catch (Exception e) {
			throw new KnowledgeBaseException(e);
		}
	}

	@Override
	public void closeResult(DataIterator<?> result) throws QueryEvaluationException {
		result.<TupleQueryResult>getIterator().close();
	}
	
	@Override
	public boolean hasNext(DataIterator<?> result) throws QueryEvaluationException {
		return result.<TupleQueryResult>getIterator().hasNext();
	}
	
	@Override
	public BindingSet next(DataIterator<?> result) throws QueryEvaluationException {
		return result.<TupleQueryResult>getIterator().next();
	}
		

	
}

/*
 * * 
* opus http://lsdis.cs.uga.edu/projects/semdis/opus# SwetoDblp
* kwp http://knowledgeweb.semanticweb.org/semanticportal/OWL/Documentation_Ontology.owl# KnowledgeWeb Portal
* marcont http://www.marcont.org/ontology/marcont.owl# MarcOnt Initiative
* swrc http://swrc.ontoware.org/ontology# SWRC Ontology
* akt AKT Portal Ontology
* swpo http://sw-portal.deri.org/ontologies/swportal# SWPortal Ontology
* bibtex http://purl.org/net/nknouf/ns/bibtex# A BibTeX Ontology
* */
