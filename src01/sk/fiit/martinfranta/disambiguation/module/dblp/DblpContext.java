package sk.fiit.martinfranta.disambiguation.module.dblp;

import java.util.ArrayList;
import java.util.List;

import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.disambiguation.module.Context;

public final class DblpContext extends Context {
	
	public DblpContext (AuthorEntity e) {
		super(e);
	}
	
	@Override
	protected void assignAttributes() {
		
		
		ContextAttribute<List<AuthorEntity>> coauthors = new ContextAttribute<List<AuthorEntity>>(new ArrayList<AuthorEntity>(), "coauthors");
		ContextAttribute<List<AuthorEntity>> publications = new ContextAttribute<List<AuthorEntity>>(new ArrayList<AuthorEntity>(), "publications");
		ContextAttribute<String> workplace = new ContextAttribute<String>(new String(), "workplace");
		
		StringBuilder coauthorsSparql = new StringBuilder();
		coauthorsSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		coauthorsSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		coauthorsSparql.append("SELECT ?coname WHERE {\n");
		coauthorsSparql.append("$1$ opus:author ?coauthor.\n");
		coauthorsSparql.append("?coauthor foaf:name ?coname.\n");
		coauthorsSparql.append("}");
		
		
		String coauthorsString = coauthorsSparql.toString();
		coauthorsString = processQuery(coauthorsString, entity.getField("resource"));
		coauthors.setSparql(coauthorsString);
		
		StringBuilder publicationsSparql = new StringBuilder();
		publicationsSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		publicationsSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		publicationsSparql.append("SELECT ?art ?p WHERE {\n");
		publicationsSparql.append("?art opus:author [?collection $1$].\n");
		publicationsSparql.append("?art rdfs:label  ?p. \n");
		publicationsSparql.append(" }");
		
		String publicationsString = processQuery(publicationsSparql.toString(), entity.getField("resource"));
		publications.setSparql(publicationsString);
		
		StringBuilder workplaceSparql = new StringBuilder();
		workplaceSparql.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		workplaceSparql.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		workplaceSparql.append("SELECT ?p WHERE {\n");
		workplaceSparql.append("$1$ foaf:workplaceHomepage ?p. \n");
		workplaceSparql.append("} LIMIT 1");
		
		String workplaceString = processQuery(workplaceSparql.toString(), entity.getField("resource"));
		workplace.setSparql(workplaceString);
		
		addAttribute(coauthors);
		addAttribute(publications);
		addAttribute(workplace);
	}

	
	
}
