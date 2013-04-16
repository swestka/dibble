package sk.fiit.martinfranta.disambiguation.module.dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openrdf.query.BindingSet;

import sk.fiit.martinfranta.disambiguation.module.Candidate;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.tools.DataSet;

public class AuthorCandidate extends Candidate {
	private static final long serialVersionUID = 1L;

	public AuthorCandidate() {
		super("Unresolved candidate");
		this.context = new DblpContext(this);
	}
	
	public AuthorCandidate(String identifier) {
		super(identifier);
		this.context = new DblpContext(this);
	}

	@Override
	public void setCandidateResultSet(DataSet<?> resultSet) {
		BindingSet bindingSet = (BindingSet)resultSet.getDataSet(); 
		this.identifier = bindingSet.getValue("s").stringValue();
		fields.put("resource", "<"+identifier+">");
		fields.put("name", bindingSet.getValue("o").stringValue());
	}

	@SuppressWarnings("unchecked")
	public List<Integer> proximity(String text, int position) {
		Set<String> processed = new HashSet<String>();
		List<Integer> distances = new LinkedList<Integer>();
		for (AuthorEntity coauthor : (List<AuthorEntity>)(this.getContext().getAttribute("coauthors"))) {
			String lastName = lastName(coauthor.getField("name"));
			if (processed.contains(lastName)) {
				continue;
			}
			processed.add(lastName);
			int found = text.indexOf(lastName);
			
			if (found > -1) {
				distances.add(Math.abs(position - found));
			}
				
		}
		return distances;
	}
	
	@SuppressWarnings("unchecked")
	public double popularity() {
		 return (((ArrayList<AuthorEntity>)context.getAttribute("coauthors")).size()+((ArrayList<String>)context.getAttribute("publications")).size())/2;
	}
	
	public double relationships(Entity entity) {
		// execute once - candidate is not relevant in this implementation
		if (((AuthorEntity)entity).getPersonsMap() == null) {
			((AuthorEntity)entity).buildPersonsMap();
			int size = entity.getCandidates().size();
			for (String resource : ((AuthorEntity)entity).getPersonsMap().keySet()) {
				List<Candidate> list = ((AuthorEntity)entity).getPersonsMap().get(resource);
				for (Candidate c : list) {
					c.addConfidenceScore((double)list.size()/(double)size);
				}
			}
		}
		return .0;
	}
	
	@SuppressWarnings("unchecked")
	public int cooccurence(String text) {
		Set<String> found = new HashSet<String>();
		for (AuthorEntity coauthor : (List<AuthorEntity>)(this.getContext().getAttribute("coauthors"))) {
			String lastName = lastName(coauthor.getField("name"));
			if (text.indexOf(lastName)>-1) {
				found.add(lastName);
			}
		}
		for (AuthorEntity collegue : (List<AuthorEntity>)(this.getContext().getAttribute("collegues"))) {
			String lastName = lastName(collegue.getField("name"));
			if (text.indexOf(lastName)>-1) {
				found.add(lastName);
			}	
		}
		
		return found.size();
	}
	
	
	
	private String lastName(String authorName) {
		String[] words = authorName.split("\\s");
		if (words.length == 1)
			return authorName;
		
		String lastWord = words[words.length - 1];
		String[] banned = {"II", "III", "IV", "V", "VI"};
		for (String b : banned) {
			if (b.contentEquals(lastWord)) {
				lastWord = words[words.length - 2];
			}
		}
		if (lastWord.length() < 4) {
			lastWord = authorName;
		}
		return lastWord;
	}
}
