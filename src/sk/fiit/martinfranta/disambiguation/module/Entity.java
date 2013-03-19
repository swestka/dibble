package sk.fiit.martinfranta.disambiguation.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sk.fiit.martinfranta.disambiguation.Position;

public class Entity {
	protected Context context;
	protected String identifier;
	protected Position position;
	
	protected Map<String, String> fields = new HashMap<String, String>();
	
	protected List<IEntity> candidates;
	
	public List<IEntity> getCandidates() {
		if (candidates == null) 
			candidates = new ArrayList<IEntity>();
		
		return candidates;
	}

	public void setCandidates(List<IEntity> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(IEntity candidate) {
		if (candidates == null)
			candidates = new ArrayList<IEntity>();
		
		candidates.add(candidate);
	}
	

	protected boolean isCandidate;
	
	public Entity(String identifier) {
		this.identifier = identifier;
	}
	
	public void setField(String key, String value) {
		fields.put(key, value);
	}
	
	public String getField(String key) {
		return fields.get(key);
	}
	
	public String toString() {
		return identifier;
	}
	
	public Context getContext() {
		Logger.getLogger(Entity.class).debug("Super type entity context");
		return context;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public boolean isCandidate() {
		return isCandidate;
	}

	public void setCandidate(boolean isCandidate) {
		this.isCandidate = isCandidate;
	}

	
}
