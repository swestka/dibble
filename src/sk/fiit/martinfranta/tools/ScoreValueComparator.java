package sk.fiit.martinfranta.tools;

import java.util.Comparator;
import java.util.Map;

import sk.fiit.martinfranta.disambiguation.module.Candidate;

public class ScoreValueComparator implements Comparator<Candidate> {

    Map<Candidate, Double> base;
    public ScoreValueComparator(Map<Candidate, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(Candidate a, Candidate b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } 
    }
}
