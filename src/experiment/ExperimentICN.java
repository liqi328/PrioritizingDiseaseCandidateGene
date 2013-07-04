package experiment;

import graph.Graph;

import java.util.Map;
import java.util.Set;

import similarity.ICNSimilarityAlgorithm;

public class ExperimentICN implements Experiment {
	public void run(Graph g, Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		System.out.println("Experiment ICN running...");
		LeaveOneOutCrossValidation validation = new LeaveOneOutCrossValidation(g);
		validation.setSimilarityAlgorithm(new ICNSimilarityAlgorithm());
		
		Map<Integer, Map<Integer, Double>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		ValidationResultAnalysis.run(ranksMap);
		
		System.out.println("Experiment ICN finished");
	}
}
