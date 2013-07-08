package experiment;

import graph.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.ICNSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentICN extends AbstractExperiment {
	public ExperimentICN(InputArgument input) {
		super(input);
	}

	public void run(Graph g, Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		System.out.println("Experiment ICN running...");
		
		LeaveOneOutCrossValidation validation = new LeaveOneOutCrossValidation(g);
		validation.setSimilarityAlgorithm(new ICNSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "/icn_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment ICN finished.\n");
	}
}
