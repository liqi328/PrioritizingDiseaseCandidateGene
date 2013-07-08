package experiment;

import graph.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.VS2SimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentVS2 extends AbstractExperiment {

	public ExperimentVS2(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment VS2 running...");
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		validation.setSimilarityAlgorithm(new VS2SimilarityAlgorithm());
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "vs2_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment VS2 finished.\n");
	}

}
