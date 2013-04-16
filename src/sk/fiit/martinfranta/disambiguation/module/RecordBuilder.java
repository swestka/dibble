package sk.fiit.martinfranta.disambiguation.module;

import java.util.LinkedList;
import java.util.List;

import sk.fiit.martinfranta.tools.learning.Record;

public abstract class RecordBuilder {
	
	List<String> proximeAttrIds = new LinkedList<String>();
	List<String> similarAttrIds = new LinkedList<String>();
	List<String> ngramsAttrIds = new LinkedList<String>();
	
	
	public Record build(Entity e) {
		Record r = new Record();
		for (String id : proximeAttrIds) {
			r.addSimilarAttribute(e.getContext().getContextAttributes().get(id));
		}
		for (String id : similarAttrIds) {
			r.addSimilarAttribute(e.getContext().getContextAttributes().get(id));
		}
		for (String id : ngramsAttrIds) {
			r.addSimilarAttribute(e.getContext().getContextAttributes().get(id));
		}
		
		return r;
	}
	
	public void addProxime(String...strings) {
		for (String id : strings) {
			proximeAttrIds.add(id);
		}
	}
	
	public void addSimilar(String...strings) {
		for (String id : strings) {
			similarAttrIds.add(id);
		}
	}
	
	public void addNGrams(String...strings) {
		for (String id : strings) {
			ngramsAttrIds.add(id);
		}
	}
	
	
}
