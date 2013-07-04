package experiment;

import graph.Graph;

import java.util.Set;

public class ExperimentVS implements Experiment {

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment VS running...");
		System.out.println("Experiment VS finished");
	}

}
