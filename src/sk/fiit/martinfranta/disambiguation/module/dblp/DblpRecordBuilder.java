package sk.fiit.martinfranta.disambiguation.module.dblp;

import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.disambiguation.module.RecordBuilder;
import sk.fiit.martinfranta.tools.learning.Record;

public class DblpRecordBuilder extends RecordBuilder {
	
	public Record build(Entity e) {
		if (e instanceof AuthorEntity) {
			addProxime("coauthors","collegues","years");
			addSimilar("workplace");
			Record r = super.build(e);
			
			return r;
		}
		
		if (e instanceof AuthorCandidate) {
			addProxime("coauthors","collegues","years");
			addSimilar("workplace");
			Record r = super.build(e);
			
			return r;
		}
		
		return null;
	}
}
