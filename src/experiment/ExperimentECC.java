package experiment;

import graph.Graph;

import java.util.Set;

public class ExperimentECC implements Experiment {

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment ECC running...");
		System.out.println("Experiment ECC finished");

	}

}
