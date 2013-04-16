package sk.fiit.martinfranta.tools.learning;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Perceptron {
	private double[][] teachingOutput;

	private double[][] patterns;

	
	int numberOfInputNeurons;
	int numberOfOutputNeurons;
	int numberOfPatterns;
	double[][] weights;

	public Perceptron() {
	    
	}
	
	public void setPatterns(List<List<Double>> input) {
		int patternCount = input.size();
		if (patternCount > 0) {
			int patternSize = input.get(0).size();
			
			patterns = new double[patternCount][patternSize];
		}
		int i = 0, j = 0;
		for (List<Double> pattern : input) {
			for (double p : pattern) {
				patterns[i][j++] = p;
			}
			i++;
		}
	}
	
	
	public void setTeachingOutput(List<Double> output) {
		teachingOutput = new double[output.size()][1];
		int i = 0;
		for (double d : output) {
			teachingOutput[i++][0] = d;
		}
	}
	
	public void initialize() {
		numberOfOutputNeurons = teachingOutput[0].length;
		numberOfInputNeurons = patterns[0].length;
		numberOfPatterns = patterns.length;
		weights = new double[numberOfInputNeurons][numberOfOutputNeurons];
	}
	
	public void deltaRule() {
		boolean allCorrect = false;
		boolean error = false;
		double learningFactor = 0.2;
		while (!allCorrect) {
			error = false;
			for (int i = 0; i < numberOfPatterns; i++) {

				double[] output = setOutputValues(i);
				for (int j = 0; j < numberOfOutputNeurons; j++) {
					if (!equals(teachingOutput[i][j], output[j])) {
						for (int k = 0; k < numberOfInputNeurons; k++) {
							weights[k][j] = weights[k][j] + learningFactor
							* patterns[i][k]
							* (teachingOutput[i][j] - output[j]);
						}
						error = true;
					}
				}
			}
			if (!error) {
				allCorrect = true;
			}
		}
	}
	
	double[] setOutputValues(int patternNo) {
		double bias = 0.7;
		double[] result = new double[numberOfOutputNeurons];
		double[] toImpress = patterns[patternNo];
		for (int j = 0; j < result.length; j++) {
		    double net = 0;
		    for (int i = 0; i < toImpress.length; i++) 
		        net += weights[i][j] * toImpress[i];
		    if (net > bias)
		        result[j] = 1;
		    else
		        result[j] = 0;
		}
		return result;
	}
	
	public void printMatrix(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		    	DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
	            decimalFormat.setMaximumFractionDigits(1);
	            decimalFormat.setMinimumFractionDigits(1);
	            System.out.print("(" + decimalFormat.format(matrix[i][j]) + ")");
		    }
		    System.out.println();
		}
	}
	
	@Test
	public void testPerceptron() {
		System.out.println("Weights before training: ");
		printMatrix(weights);
		deltaRule();
		System.out.println("Weights after training: ");
		printMatrix(weights);
		
		
	}
	
	private boolean equals(double a, double b) {
		return Math.abs(a - b) < 0.1;
	}
}
