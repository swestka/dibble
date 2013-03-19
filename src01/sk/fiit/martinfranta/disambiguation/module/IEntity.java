package sk.fiit.martinfranta.disambiguation.module;

import java.util.List;

import org.openrdf.query.BindingSet;


public interface IEntity {
	 
	public String toString();

	public Context getContext();

	public void setBindingSet(BindingSet bindingSet);
	
	public String getIdentifier();
	
	public void setIdentifier(String identifier);
	
	public String getCandidatesSparql(String... params);
	
	public void setCandidate(boolean candidate);
	
	public boolean isCandidate();
	
	public List<IEntity> getCandidates();
	
	public void setCandidates(List<IEntity> candidates);
	
	public void addCandidate(IEntity candidate);

	public void setContextBindingSet(ContextAttribute<?> attr, BindingSet bindingSet);
	
	public String getField(String key);
	
	public void setField(String key, String value);
}
