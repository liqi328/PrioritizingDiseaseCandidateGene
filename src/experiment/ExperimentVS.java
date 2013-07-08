package experiment;

import graph.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.VSSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentVS extends AbstractExperiment {

	public ExperimentVS(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment VS running...");
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		validation.setSimilarityAlgorithm(new VSSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "vs_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment VS finished.\n");
	}

}
