package experiment;

import graph.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.ECCSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentECC extends AbstractExperiment {

	public ExperimentECC(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment ECC running...");
		
		LeaveOneOutCrossValidation validation = new LeaveOneOutCrossValidation(g);
		validation.setSimilarityAlgorithm(new ECCSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "ecc_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment ECC finished.\n");
	}

}
