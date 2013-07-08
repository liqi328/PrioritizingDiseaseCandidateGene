package experiment;

import graph.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.GoSimilarityAlgorithm;
import similarity.VSSimilarityAlgorithm;
import similarity.VS_GoSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentVS_GO extends ExperimentGO {
	public ExperimentVS_GO(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment VS + GO running...");
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSeedSet, candidateGeneSet);
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		validation.setSimilarityAlgorithm(new VS_GoSimilarityAlgorithm(new VSSimilarityAlgorithm(),
				new GoSimilarityAlgorithm(geneSymbolMap)));
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "vs_go_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment VS + GO finished.\n");
	}
}
