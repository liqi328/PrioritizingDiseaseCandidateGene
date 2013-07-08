package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import similarity.GoSimilarityAlgorithm;
import similarity.SPSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentSP_GO extends ExperimentGO {

	public ExperimentSP_GO(InputArgument input) {
		super(input);
	}

	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment SP + GO running...");
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSeedSet, candidateGeneSet);
		
		LeaveOneOutCrossValidationForVS sp_validation = new LeaveOneOutCrossValidationForVS(g);
		sp_validation.setSimilarityAlgorithm(new SPSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> vs_ranksMap = sp_validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		LeaveOneOutCrossValidation go_validation = new LeaveOneOutCrossValidation(g);
		go_validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		Map<Integer, List<Rank>> go_ranksMap = go_validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(vs_ranksMap, go_ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "sp_go_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment SP + GO finished.\n");
	}

}
