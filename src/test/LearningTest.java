package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import sk.fiit.martinfranta.disambiguation.module.ContextAttribute;
import sk.fiit.martinfranta.tools.learning.Perceptron;
import sk.fiit.martinfranta.tools.learning.Record;

public class LearningTest {
	@Test
	public void testString() {
		Assert.assertTrue("abc".toString().contentEquals("abc"));
	}
	
	@Test
	public void testCollection() {
		List<Integer> a = new ArrayList<Integer>();
		Assert.assertTrue(a instanceof Collection);
	}
	
	@Test
	public void testRecordProximeScore() {
		Record r = new Record();
		HashSet<String> coauthors = new HashSet<String>();
		coauthors.add("Jozo Bizon");
		coauthors.add("Slizo Mizo");
		coauthors.add("kato Lato");
		
		r.addProximeAttribute(new ContextAttribute<HashSet<String>>(coauthors, "coauthors"));
		
		Record s = new Record();
		HashSet<String> foundEntities = new HashSet<String>();
		foundEntities.add("Jozo Bizon");
		foundEntities.add("Slizo Mijo");
		foundEntities.add("Mazo Lazo");
		
		s.addProximeAttribute(new ContextAttribute<HashSet<String>>(foundEntities, "coauthors"));
		
		HashMap<String, Double> scores =  Record.computeScore(r ,s);
		Assert.assertTrue(scores.get("coauthors") == 1./3.);
	}
	
	@Test
	public void testRecordSimilarScore() {
		Record r = new Record();
		Record s = new Record();
		r.addSimilarAttribute(new ContextAttribute<String>("michal d. hurban", "name"));
		s.addSimilarAttribute(new ContextAttribute<String>("miloslav hurban", "name"));
		
		r.addSimilarAttribute(new ContextAttribute<String>("Stanford", "workplace"));
		s.addSimilarAttribute(new ContextAttribute<String>("Berkeley Uni CA", "workplace"));
		
		r.addSimilarAttribute(new ContextAttribute<String>("ldis.uci.edu", "mail"));
		s.addSimilarAttribute(new ContextAttribute<String>("ieee.org", "mail"));
		
		HashMap<String, Double> scores =  Record.computeScore(r ,s);
		//Assert.assertTrue(scores.get("coauthors") == 1./3.);
	}
	
	@Test
	public void perceptronTest() {
		Record r = new Record();
		HashSet<String> coauthors = new HashSet<String>();
		coauthors.add("Jozo Bizon");
		coauthors.add("Slizo Mizo");
		coauthors.add("kato Lato");
		
		r.addProximeAttribute(new ContextAttribute<HashSet<String>>(coauthors, "coauthors"));
		
		Record s = new Record();
		HashSet<String> foundEntities = new HashSet<String>();
		foundEntities.add("Jozo Bizon");
		foundEntities.add("Slizo Mijo");
		foundEntities.add("Mazo Lazo");
		
		s.addProximeAttribute(new ContextAttribute<HashSet<String>>(foundEntities, "coauthors"));
		
		r.addSimilarAttribute(new ContextAttribute<String>("michal d. hurban", "name"));
		s.addSimilarAttribute(new ContextAttribute<String>("miloslav hurban", "name"));
		
		r.addSimilarAttribute(new ContextAttribute<String>("Stanford", "workplace"));
		s.addSimilarAttribute(new ContextAttribute<String>("Berkeley Uni CA", "workplace"));
		
		r.addSimilarAttribute(new ContextAttribute<String>("ldis.uci.edu", "mail"));
		s.addSimilarAttribute(new ContextAttribute<String>("ieee.org", "mail"));
		
		Perceptron p = new Perceptron();
		LinkedList<List<Double>> l = new LinkedList<List<Double>>();
		l.add(new LinkedList<Double>(Record.computeScore(r, s).values()));
		p.setPatterns(l);
		
		LinkedList<Double> o = new LinkedList<Double>();
		o.add(1.);
		p.setTeachingOutput(o);
		
		p.initialize();
		
		p.testPerceptron();
		
		
	}
}

