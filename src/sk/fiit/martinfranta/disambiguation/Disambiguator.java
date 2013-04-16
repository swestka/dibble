package sk.fiit.martinfranta.disambiguation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.uima.jcas.tcas.Annotation;

import sk.fiit.martinfranta.disambiguation.module.Candidate;
import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.tools.DataIterator;
import sk.fiit.martinfranta.tools.DataSet;
import sk.fiit.martinfranta.tools.Factory;
import sk.fiit.martinfranta.tools.KnowledgeBaseException;
import sk.fiit.martinfranta.tools.ScoreValueComparator;
import sk.fiit.martinfranta.tools.Serializer;
import sk.fiit.martinfranta.tools.Similarity;
import sk.fiit.martinfranta.tools.learning.PairwiseClassifier;
import sk.fiit.martinfranta.tools.learning.Record;

public class Disambiguator {
	
	private static ArrayList<Annotation> mentions;
	private ArrayList<Entity> resolved = new ArrayList<Entity>();
	private ArrayList<Entity> unresolved = new ArrayList<Entity>();
	
	private static Logger logger = Logger.getLogger(Disambiguator.class);
	private KnowledgeBase knowledgeBase;
	
	public Disambiguator () {
		knowledgeBase = KnowledgeBase.getInstance();
	}
	
	public void disambiguate (ArrayList<Annotation> foundMentions) {
		mentions = foundMentions;
		LinkedList<Entity> entities;
		entities = (LinkedList<Entity>)Serializer.deserialize();
		
		if (entities == null || entities.size() == 0) {
			entities = new LinkedList<Entity>();
			for (Annotation a : mentions) {
				if (a.getType().toString().equals(Extractor.PERSON)) {
					logger.info(a.getCoveredText()+": ");
					
					Entity entity = Factory.createEntity(a.getCoveredText());
					entity.setIdentifier(a.getCoveredText());
					entity.setField("name", a.getCoveredText());
					entity.setPosition(a.getBegin(), a.getEnd());
					
					fillCandidates(entity);
					
					logger.info("Found: "+entity.getCandidates().size());
					
					//TODO: 1 found - OK, next
					if (entity.getCandidates().size() == 1) {
						entity.setLinkedEntity(entity.getCandidates().get(0));
						resolved.add(entity);
					}
					
					if (entity.getCandidates().size() < 5) {
						
						for (Candidate t : entity.getCandidates()) {
							setEntityContext(t);
						}
					}
					//TODO: check duplicates
					if (entity.getCandidates().size() > 4) {
						for (Candidate t : entity.getCandidates()) {
							if (Similarity.isSimilar(entity.getField("name"), t.getField("name"), .9f)) {
								logger.info(entity.getField("name")+" "+t.getField("name")+" are similar");
								setEntityContext(t);
							}
						}
					}
					
					if (entity.getCandidates().size() > 0) {
						entity.setCandidatesScore(1.0/entity.getCandidates().size());
						logger.info("Score: "+1.0/entity.getCandidates().size());
						entities.add(entity);
					}
				}
			}
			
			Serializer.stdSerialize(entities);
		}
		
		scoreSet(entities);
	}
	
	public void classify() {
		for (Entity entity : unresolved) {
			Record r1 = PairwiseClassifier.buildRecord(entity);
			for (Candidate c : entity.getCandidates()) {
				
				Record r2 = PairwiseClassifier.buildRecord(c);
				if (PairwiseClassifier.classify(r1, r2)) {
					logger.info(c.getField("resource")+" - "+entity.getIdentifier());
				}
			}
		}
	}
	
	private void scoreSet(LinkedList<Entity> entities) {
		for (Entity entity : entities) {
			logger.info("Candidates for: "+entity.getField("name"));
			if (entity.getCandidates().size() == 1 || entity.getLinkedEntity()!=null) continue;
			Map<Candidate, Double> scores = new HashMap<Candidate, Double>();
			
			for (Candidate candidate : entity.getCandidates()) {
				proximity(entity, candidate);
				cooccurence(entity, candidate);
				popular(entity, candidate);
				relationships(entity, candidate);
				similarity(entity, candidate);
				
				scores.put(candidate, candidate.getConfidenceScore());
				logger.info(candidate.getField("resource")+" cf:"+candidate.getConfidenceScore());
			}
			
			ScoreValueComparator svc = new ScoreValueComparator(scores);
			TreeMap<Candidate, Double> candidateScores = new TreeMap<Candidate, Double>(svc);
			candidateScores.putAll(scores);
			
			Candidate first = candidateScores.firstEntry().getKey();
			candidateScores.remove(first);
			Candidate second = candidateScores.firstEntry().getKey();
			
			if (first.getConfidenceScore() < second.getConfidenceScore()*1.2) {
				resolved.add(entity);
				entity.setLinkedEntity(first);
				logger.info("best fit: "+first.getField("resource"));
			}
			else {
				unresolved.add(entity);
				logger.info("unresolved");
			}
		}
	}
	
	private void relationships(Entity entity, Candidate candidate) {
		candidate.relationships(entity);
	}

	private void popular(Entity entity, Candidate candidate) {
		candidate.addConfidenceScore(candidate.popularity()/100);
	}

	private void cooccurence(Entity entity, Candidate candidate) {
		String text = Extractor.getText();
		candidate.addConfidenceScore(candidate.cooccurence(text)/100.0);
	}

	private void proximity(Entity entity, Candidate candidate) {
		String text = Extractor.getText();
		int start = entity.getStartIndex() < 400 ? 0 : entity.getStartIndex() - 400;
		int end = text.length() < start + 400 ? text.length() : start + 400;
		String window = text.substring(start, end);
		
		for (int distance : candidate.proximity(window, entity.getStartIndex() - start)) {
			candidate.addConfidenceScore((10.0/distance));
		}
	}
	
	private void similarity(Entity entity, Candidate candidate) {
		double addition = Similarity.isSimilar(entity.getField("name"), candidate.getField("name"), 90f) ? 0.5 : 0;
		candidate.addConfidenceScore(addition);
	}

	public void fillCandidates(Entity entity) {
		logger.info("Position:"+entity.getStartIndex());
		DataIterator<?> result = knowledgeBase.getData(entity.getCandidatesQuery());
		while (knowledgeBase.hasNext(result)) {
			DataSet<?> resultSet = knowledgeBase.next(result);
			Candidate candidate = Factory.createCandidate("Unresolved candidate of "+entity.getIdentifier());
			candidate.setCandidateResultSet(resultSet);
			entity.addCandidate(candidate);
			candidate.getContext().assignAttributes();
			logger.info(candidate.getConfidenceScore());
		}
		try {
			knowledgeBase.closeResult(result);
		} catch (KnowledgeBaseException e) {
			e.printStackTrace();
		}
	}
	
	private void setEntityContext(Entity entity) {
		
		for (ContextAttribute<?> attr : entity.getContext().getContextAttributes().values()) {
			logger.info(attr.getIdentifier());
			DataIterator<?> result = (DataIterator<?>)knowledgeBase.getData(attr.getQuery()); 
			
			while (knowledgeBase.hasNext(result)) {
				DataSet<?> resultSet = knowledgeBase.next(result);
				entity.getContext().setContextResultSet(attr, resultSet);
			}
			
			try {
				knowledgeBase.closeResult(result);
			} catch (KnowledgeBaseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Entity> getResolved() {
		return resolved;
	}
	
	public List<Entity> getUnresolved() {
		return unresolved;
	}
}
