package sk.fiit.martinfranta.disambiguation.module.dblp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;

import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.disambiguation.module.IEntity;

public class AuthorEntity extends Entity implements IEntity  {
	private static Logger logger = Logger.getLogger("AuthorEntity");
	
	
	public AuthorEntity() {
		super(null);
	}
	
	public AuthorEntity(String identifier) {
		super(identifier);
	}
	
	public DblpContext getContext() {
		Logger.getLogger(AuthorEntity.class).debug("Author entity context");
		if (context == null) 
			context = new DblpContext(this);
		
		return (DblpContext)context;
	}

	@Override
	public void setBindingSet(BindingSet bindingSet) {
		this.identifier = bindingSet.getValue("s").stringValue();
		fields.put("resource", "<"+identifier+">");
	}
	
	@Override
	public String getCandidatesSparql(String... params) {
		String regex = prepareRegex(identifier);
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("PREFIX opus: <http://lsdis.cs.uga.edu/projects/semdis/opus#>\n");
		queryString.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		queryString.append("SELECT ?s ?o WHERE {\n");
		queryString.append("?s foaf:name ?o.\n");
		queryString.append("FILTER regex(?o, \""+regex+"\", \"i\").\n");
		queryString.append("}");
		
		return queryString.toString();
	}
	
	
	@Override
	public void setIdentifier(String identifier) {
		super.setIdentifier(identifier);
		
	}

	@Override
	public void setCandidates(List<IEntity> candidates) {
		super.setCandidates(candidates);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setContextBindingSet(ContextAttribute<?> attr, BindingSet bindingSet) {
		if ("coauthors".contentEquals(attr.getIdentifier())) {
			((ArrayList<AuthorEntity>) this.getContext().getAttribute("coauthors")).add(new AuthorEntity(bindingSet.getValue("coname").stringValue()));
			logger.info("Coauthor: "+bindingSet.getValue("coname").stringValue());
		}
		else if ("publications".contentEquals(attr.getIdentifier())) {
			((ArrayList<String>) this.getContext().getAttribute("publications")).add(new String(bindingSet.getValue("p").stringValue()));
			logger.info("publication: "+bindingSet.getValue("p").stringValue());
		}
		else if ("workplace".contentEquals(attr.getIdentifier())) {
			this.getContext().setAttribute("workplace", bindingSet.getValue("p").stringValue());
			logger.info("workplace: "+bindingSet.getValue("p").stringValue());
		}
	}
	
	// PRIVATE METHODS ************************   //
	
	private String prepareRegex (String namedEntity) {
		String[] words = namedEntity.split("\\s");
		
		String firstLetters = String.valueOf(words[0].charAt(0));
		if (words[0].length() > 1) {
			firstLetters = firstLetters+words[0].charAt(1);
		}
		String lastWord = words[words.length - 1];
		
		return firstLetters+"([a-z ]*) "+lastWord;
	}

	
}
