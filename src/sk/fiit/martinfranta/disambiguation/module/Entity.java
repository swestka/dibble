package sk.fiit.martinfranta.disambiguation.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.jcas.tcas.Annotation;

import sk.fiit.martinfranta.disambiguation.Extractor;
import sk.fiit.martinfranta.disambiguation.Position;

public abstract class Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4163528267198462564L;

	protected Context context;
	protected String identifier;
	protected Position position = new Position(0, 0);

	protected Map<String, String> fields = new HashMap<String, String>();
	
	protected List<Candidate> candidates;
	protected Candidate linkedEntity = null;
	
	public Candidate getLinkedEntity() {
		return linkedEntity;
	}

	public void setLinkedEntity(Candidate linkedEntity) {
		this.linkedEntity = linkedEntity;
	}

	public Entity(String identifier) {
		this.identifier = identifier;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public void setField(String key, String value) {
		fields.put(key, value);
	}
	
	public String getField(String key) {
		return fields.get(key);
	}
	
	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(Candidate candidate) {
		if (candidates == null)
			candidates = new ArrayList<Candidate>();
		
		candidates.add(candidate);
	}
	
	public List<Candidate> getCandidates() {
		if (candidates == null) 
			candidates = new ArrayList<Candidate>();
		
		return candidates;
	}
	
	public void setPosition(int start, int end) {
		this.position = new Position(start, end);
	}
	
	public int getStartIndex() {
		return position.getStart();
	}

	public void setStartIndex(int startIndex) {
		this.position.setStart(startIndex);
	}

	public int getEndIndex() {
		return position.getEnd();
	}

	public void setEndIndex(int endIndex) {
		this.position.setEnd(endIndex);
	}
	
	public String toString() {
		return identifier;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public boolean isCandidate() {
		return false;
	}
	
	public void setCandidatesScore(double score) {
		for (Candidate c : getCandidates()) {
			c.setConfidenceScore(score);
		}
	}
	
	public void enhanceContext() {
		ArrayList<Annotation> annotations = Extractor.getMentions();
		this.getContext().addAttribute(new ContextAttribute<List<String>>(new ArrayList<String>(), "coauthors"));
		this.getContext().addAttribute(new ContextAttribute<List<String>>(new ArrayList<String>(), "collegues"));
		
		for (Annotation a : annotations) {
			((ArrayList<String>)this.getContext().getAttribute("coauthors")).add(a.getCoveredText());
			((ArrayList<String>)this.getContext().getAttribute("collegues")).add(a.getCoveredText());
		}
	}
	
	/** MUST IMPLEMENT */
	
	public abstract String getCandidatesQuery(String... params);
}
