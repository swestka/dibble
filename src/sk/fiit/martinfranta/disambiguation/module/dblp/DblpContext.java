package sk.fiit.martinfranta.disambiguation.module.dblp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;

import sk.fiit.martinfranta.disambiguation.module.Context;
import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.tools.DataSet;

public final class DblpContext extends Context {
	
	private static final long serialVersionUID = -2670796768058043300L;

	public DblpContext (Entity e) {
		super(e);
	}
	
	private static Logger logger = Logger.getLogger("DblpContext");
	

	ContextAttribute<List<AuthorEntity>> coauthors = new ContextAttribute<List<AuthorEntity>>(new ArrayList<AuthorEntity>(), "coauthors");
	ContextAttribute<List<AuthorEntity>> publications = new ContextAttribute<List<AuthorEntity>>(new ArrayList<AuthorEntity>(), "publications");
	ContextAttribute<String> workplace = new ContextAttribute<String>(new String(), "workplace");
	ContextAttribute<List<AuthorEntity>> collegues = new ContextAttribute<List<AuthorEntity>>(new ArrayList<AuthorEntity>(), "collegues");
	ContextAttribute<List<Integer>> yearsPublished = new ContextAttribute<List<Integer>>(new ArrayList<Integer>(), "yearsPublished");
		
	@Override
	public void assignAttributes() {
		
		/**/
		
		StringBuilder coauthorsSparql = new StringBuilder();
		coauthorsSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		coauthorsSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		coauthorsSparql.append("SELECT ?coname ?coauthor WHERE {\n");
		coauthorsSparql.append("?pub opus:author [?collection $1$]. \n");
		coauthorsSparql.append("?pub opus:author [?members ?coauthor].\n");
		coauthorsSparql.append("FILTER(?coauthor != $1$).\n");
		coauthorsSparql.append("?coauthor foaf:name ?coname.\n");
		coauthorsSparql.append("}");
		
		String coauthorsString = coauthorsSparql.toString();
		coauthorsString = processQuery(coauthorsString, entity.getField("resource"));
		coauthors.setQuery(coauthorsString);
		
		/**/
		
		StringBuilder publicationsSparql = new StringBuilder();
		publicationsSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		publicationsSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		publicationsSparql.append("SELECT ?art ?p WHERE {\n");
		publicationsSparql.append("?art opus:author [?collection $1$].\n");
		publicationsSparql.append("?art rdfs:label  ?p. \n");
		publicationsSparql.append(" }");
		
		String publicationsString = processQuery(publicationsSparql.toString(), entity.getField("resource"));
		publications.setQuery(publicationsString);

		/**/
		
		StringBuilder workplaceSparql = new StringBuilder();
		workplaceSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		workplaceSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		workplaceSparql.append("SELECT ?p WHERE {\n");
		workplaceSparql.append("$1$ foaf:workplaceHomepage ?p. \n");
		workplaceSparql.append("} LIMIT 1");
		
		String workplaceString = processQuery(workplaceSparql.toString(), entity.getField("resource"));
		workplace.setQuery(workplaceString);
		
		/**/
		
		StringBuilder colleguesSparql = new StringBuilder();
		colleguesSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		colleguesSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		colleguesSparql.append("SELECT ?p ?n WHERE {\n");
		colleguesSparql.append("$1$ foaf:workplaceHomepage ?x. \n");
		colleguesSparql.append("?p foaf:workplaceHomepage ?x. \n");
		colleguesSparql.append("?p foaf:name ?n. \n");
		colleguesSparql.append("} LIMIT 1");
		
		String colleguesString = processQuery(colleguesSparql.toString(), entity.getField("resource"));
		collegues.setQuery(colleguesString);
		
		/**/
		
		StringBuilder yearsSparql = new StringBuilder();
		yearsSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		yearsSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		yearsSparql.append("SELECT ?p WHERE {\n");
		yearsSparql.append("?art opus:author $1$. \n");
		yearsSparql.append("?art opus:year ?p. \n");
		yearsSparql.append("} LIMIT 1");
		
		String yearsString = processQuery(yearsSparql.toString(), entity.getField("resource"));
		yearsPublished.setQuery(yearsString);
		
		/**/
		addAttribute(coauthors);
		addAttribute(publications);
		addAttribute(workplace);
		addAttribute(collegues);
		addAttribute(yearsPublished);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setContextResultSet(ContextAttribute<?> attr, DataSet<?> resultSet) {
		BindingSet bindingSet = (BindingSet)resultSet.getDataSet();
		
		if ("coauthors".contentEquals(attr.getIdentifier())) {
		
			AuthorEntity coauthor = new AuthorEntity(bindingSet.getValue("coname").stringValue());
			coauthor.setField("name", bindingSet.getValue("coname").stringValue());
			coauthor.setField("resource", "<"+bindingSet.getValue("coauthor").stringValue()+">");
			((ArrayList<AuthorEntity>) this.getAttribute("coauthors")).add(coauthor);
			logger.info("Coauthor: "+bindingSet.getValue("coname").stringValue());
		}
		
		else if ("publications".contentEquals(attr.getIdentifier())) {
			
			((ArrayList<String>) this.getAttribute("publications")).add(new String(bindingSet.getValue("p").stringValue()));
			logger.info("publication: "+bindingSet.getValue("p").stringValue());
		}
		
		else if ("workplace".contentEquals(attr.getIdentifier())) {
		
			this.setAttribute("workplace", bindingSet.getValue("p").stringValue());
			logger.info("workplace: "+bindingSet.getValue("p").stringValue());
		}
		
		else if ("collegues".contentEquals(attr.getIdentifier())) {
			AuthorEntity collegue = new AuthorEntity(bindingSet.getValue("n").stringValue());
			collegue.setField("name", bindingSet.getValue("n").stringValue());
			collegue.setField("resource", "<"+bindingSet.getValue("p").stringValue()+">");
			((ArrayList<AuthorEntity>) this.getAttribute("collegues")).add(collegue);
			logger.info("collegue: "+bindingSet.getValue("n").stringValue());
		}
		
		else if ("yearsPublished".contentEquals(attr.getIdentifier())) {
			
			((ArrayList<Integer>) this.getAttribute("yearsPublished")).add(Integer.valueOf(bindingSet.getValue("p").stringValue()));
			logger.info("year: "+bindingSet.getValue("p").stringValue());
		}
	}
}
