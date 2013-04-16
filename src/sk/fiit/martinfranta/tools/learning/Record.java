package sk.fiit.martinfranta.tools.learning;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.tools.Similarity;

public class Record {
	private HashMap<String, ContextAttribute<?>> similar = new HashMap<String, ContextAttribute<?>>();
	private HashMap<String, ContextAttribute<?>> proxime = new HashMap<String, ContextAttribute<?>>();
	private HashMap<String, ContextAttribute<?>> ngrams = new HashMap<String, ContextAttribute<?>>();
	
	public void addSimilarAttribute(ContextAttribute<?> attr) {
		similar.put(attr.getIdentifier(), attr);
	}
	
	public void addProximeAttribute(ContextAttribute<?> attr) {
		proxime.put(attr.getIdentifier(), attr);
	}
	
	public void addNgramsAttribute(ContextAttribute<?> attr) {
		ngrams.put(attr.getIdentifier(), attr);
	}
	
	
	
	public HashMap<String, ContextAttribute<?>> getSimilar() {
		return similar;
	}

	public void setSimilar(HashMap<String, ContextAttribute<?>> similar) {
		this.similar = similar;
	}

	public HashMap<String, ContextAttribute<?>> getProxime() {
		return proxime;
	}

	public void setProxime(HashMap<String, ContextAttribute<?>> proxime) {
		this.proxime = proxime;
	}

	public HashMap<String, ContextAttribute<?>> getNgrams() {
		return ngrams;
	}

	public void setNgrams(HashMap<String, ContextAttribute<?>> ngrams) {
		this.ngrams = ngrams;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Double> computeScore(Record r1, Record r2) {
		HashMap<String, Double> scores = new HashMap<String, Double>();
		for (String s : r1.getProxime().keySet()) {
			ContextAttribute<?> attr1 = r1.getProxime().get(s); 
			ContextAttribute<?> attr2 = r2.getProxime().get(s); 
			if (attr1.getAttribute() instanceof Collection && attr2.getAttribute() instanceof Collection) {
				Collection<Object> r1Collection = (Collection<Object>)attr1.getAttribute();
				Collection<Object> r2Collection = (Collection<Object>)attr2.getAttribute();
				Collection<Object> together = new HashSet<Object>();
				
				together.addAll(r2Collection);
				together.addAll(r1Collection);
				int same = r1Collection.size()+r2Collection.size() - together.size();
				
				scores.put(s, 2.*same/(r1Collection.size()+r2Collection.size()));
			}
		}
		
		for (String s : r1.getSimilar().keySet()) {
			ContextAttribute<?> attr1 = r1.getSimilar().get(s); 
			ContextAttribute<?> attr2 = r2.getSimilar().get(s); 
			
			if (attr2 != null) {
				double similar = (double)Similarity.getMatchLikelyhood(attr1.getAttribute().toString(), attr2.getAttribute().toString());
				scores.put(s, similar/100);
			}	
			
		}
		
		for (String s : r1.getNgrams().keySet()) {
			ContextAttribute<HashMap<String, Integer>> attr1 =  (ContextAttribute<HashMap<String, Integer>>)r1.getNgrams().get(s);
			ContextAttribute<HashMap<String, Integer>> attr2 =  (ContextAttribute<HashMap<String, Integer>>)r2.getNgrams().get(s);
			double ngramScore = .0; 
			for (String ngram : attr1.getAttribute().keySet()) {
				if (attr2.getAttribute().get(ngram) != null) {
					ngramScore += (attr2.getAttribute().get(ngram) + attr1.getAttribute().get(ngram));
				}
			}
			scores.put(s, ngramScore / (attr1.getAttribute().size() + attr2.getAttribute().size()));
		}
		
		for (String key : scores.keySet()) {
			System.out.println(key+": "+scores.get(key));
		}
		
		return scores;
	}
}
