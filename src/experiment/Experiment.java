package experiment;

import graph.Graph;

import java.util.Set;

public interface Experiment {
	public void run(Graph g, Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet);
}
