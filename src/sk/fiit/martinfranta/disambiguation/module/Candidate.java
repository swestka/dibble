package sk.fiit.martinfranta.disambiguation.module;

import java.util.Comparator;
import java.util.List;

import sk.fiit.martinfranta.tools.DataSet;
/**
 * Candidate is a subtype of Main Entity
 * @author 
 *
 */

public abstract class Candidate extends Entity {

	private static final long serialVersionUID = 1L;
	protected double confidenceScore = 1.;
	
	public Candidate(String identifier) {
		super(identifier);
	}

	public void addConfidenceScore(double d) {
		confidenceScore = confidenceScore + d;
	}
	
	public double getConfidenceScore() {
		return confidenceScore;
	}

	public void setConfidenceScore(double confidenceScore) {
		this.confidenceScore = confidenceScore;
	}
	
	@Override
	public boolean isCandidate() {
		return true;
	}
	
	@Override
	public String getCandidatesQuery(String... params) {
		return null;
	}
	
	/* MUST IMPLEMENT */
	
	public abstract void setCandidateResultSet(DataSet<?> resultSet);
	
	public abstract List<Integer> proximity(String window, int position);

	public abstract double popularity();
	
	public abstract int cooccurence(String text);
	
	public abstract double relationships(Entity entity);

}
