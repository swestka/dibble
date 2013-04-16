package sk.fiit.martinfranta.tools.learning;

import java.io.Serializable;
import java.util.HashMap;

import edu.stanford.nlp.util.ArrayUtils;

import sk.fiit.martinfranta.tools.Serializer;

public class ScoringFunction implements Serializable {
	private static final long serialVersionUID = 1L;
	private double weights[];
	
	public ScoringFunction(double weights[]) {
		this.weights = weights;
	}
	
	public void load() {
		ScoringFunction f = (ScoringFunction)Serializer.objectDeserialize("function.srlz");
		this.weights = f.getWeights();
	}
	
	private double[] getWeights() {
		return this.weights;
	}

	public Double eval(double[] variables) {
		if (variables.length != weights.length) {
			return null;
		}
		
		double result = .0;
		
		for (int i = 0; i < variables.length; i++) {
			result += weights[i] + variables[i];
		}
		
		return new Double(result);
	}
	
	public Double eval(HashMap<?, Double> variables) {
		return eval((Double[])variables.values().toArray());
	}
	
	public Double eval(Double[] variables) {
		return eval(ArrayUtils.toPrimitive(variables, .0));
	}
}
