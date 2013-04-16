package sk.fiit.martinfranta.tools.learning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sk.fiit.martinfranta.disambiguation.module.Candidate;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.disambiguation.module.RecordBuilder;
import sk.fiit.martinfranta.tools.Factory;

public class PairwiseClassifier {

	private static ScoringFunction function;
	private static double treshold = .6;
	static {
		function = new ScoringFunction(null);
		function.load();
	}
	
	public static boolean classify(Record r1, Record r2) {
		return function.eval((Double[])Record.computeScore(r1, r2).values().toArray()) > treshold;
	}

	public static double getTreshold() {
		return treshold;
	}

	public static void setTreshold(double treshold) {
		PairwiseClassifier.treshold = treshold;
	}
	
	public static Record buildRecord(Entity e) {
		RecordBuilder rb = Factory.createRecordBuilder(e);
		return rb.build(e);
	}
	
	public static Record buildRecord(Candidate c) {
		RecordBuilder rb = Factory.createRecordBuilder(c);
		return rb.build(c);
	}

	public static void learnTrue(List<Entity> resolved) {
		List<List<Double>> list = new LinkedList<List<Double>>();
		
		for (Entity entity : resolved) {
			Record r1 = PairwiseClassifier.buildRecord(entity);
			Candidate c = entity.getLinkedEntity();
				
			Record r2 = PairwiseClassifier.buildRecord(c);
			list.add((ArrayList)Record.computeScore(r1, r2).values());
		}
		
		Perceptron p = new Perceptron();
		p.setPatterns(list);
		p.testPerceptron();
	}
}
