package sk.fiit.martinfranta.rdftools;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class SparqlExecutor {

	/**
	 * @param args
	 */
	
	private static SparqlExecutor executor;
	
	private String sesameServer;
	public static String repositoryId;
	private HTTPRepository httpRepository;
	private RepositoryConnection connection;
	private static Logger logger = Logger.getLogger(SparqlExecutor.class);
	
	public static SparqlExecutor create() {
		if (executor == null) {
			executor = new SparqlExecutor(repositoryId);
		}
		
		return executor;
	}
	
	
	private SparqlExecutor(String id) {
		sesameServer = "http://localhost:8080/openrdf-sesame";

		httpRepository = new HTTPRepository(sesameServer, id);
		
		try {	
			httpRepository.initialize();
			connection = httpRepository.getConnection();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		httpRepository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.SPARQL);
	}
	
	public TupleQueryResult query(String queryString) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		logger.info("Performing query\\n"+queryString);
		
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
