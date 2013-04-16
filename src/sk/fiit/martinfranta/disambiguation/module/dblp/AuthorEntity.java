package sk.fiit.martinfranta.disambiguation.module.dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import sk.fiit.martinfranta.disambiguation.module.Candidate;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.tools.learning.Record;

public class AuthorEntity extends Entity  {
	private static final long serialVersionUID = 5811095425481971007L;
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("AuthorEntity");
	private HashMap<String, LinkedList<Candidate>> personsMap;

	
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
	public String getCandidatesQuery(String... params) {
		String regex = prepareRegex(identifier);
		
		/** TODO: from config files */
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
	
	// PRIVATE METHODS ************************   //
	
	private String prepareRegex(String namedEntity) {
		String[] words = namedEntity.split("\\s");
		
		String firstLetters = String.valueOf(words[0].charAt(0));
		if (words[0].length() > 1) {
			firstLetters = firstLetters+words[0].charAt(1);
		}
		String lastWord = words[words.length - 1];
		
		return firstLetters+"([a-z ]*) "+lastWord+".?.?$";
	}
	
	@SuppressWarnings("unchecked")
	protected void buildPersonsMap() {
		personsMap = new HashMap<String, LinkedList<Candidate>>();
		for (Candidate candidate : (List<Candidate>)this.getCandidates()) {
			for (AuthorEntity person : (ArrayList<AuthorEntity>)candidate.getContext().getAttribute("coauthors")) {
				LinkedList<Candidate> list = personsMap.get(person.getField("resource"));
				if (list == null) {
					list = new LinkedList<Candidate>();
				}
				list.add((Candidate)candidate);
			}
		}
		for (Candidate candidate : (List<Candidate>)this.getCandidates()) {
			for (AuthorEntity person : (ArrayList<AuthorEntity>)candidate.getContext().getAttribute("collegues")) {
				LinkedList<Candidate> list = personsMap.get(person.getField("resource"));
				if (list == null) {
					list = new LinkedList<Candidate>();
				}
				list.add((Candidate)candidate);
			}
		}
	}

	public HashMap<String, LinkedList<Candidate>> getPersonsMap() {
		return personsMap;
	}
	
	
}
