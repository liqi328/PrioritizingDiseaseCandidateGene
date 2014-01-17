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
		
		Map<Integer, Rank> resultMap = null;
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			
			resultMap = ValidationResultAnalysis.run(vs_ranksMap, go_ranksMap,
					Double.parseDouble(a_threshhold.trim()));
			
			WriterUtil.write(input.getOutputDir() + "sp_go_validation_"+ a_threshhold.trim() +".txt",
					ValidationResultAnalysis.map2String(g, resultMap));
		}
		
		System.out.println("Experiment SP + GO finished.\n");
	}

	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Ranking candidate gene using SPGOranker algorithm. [start]");
		
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSet, candidateGeneSet);
		
		LeaveOneOutCrossValidationForVS sp_validation = new LeaveOneOutCrossValidationForVS(g);
		sp_validation.setSimilarityAlgorithm(new SPSimilarityAlgorithm());
		
		List<Rank> sp_rankList = sp_validation.run_rank(diseaseGeneSet, candidateGeneSet);
		
		LeaveOneOutCrossValidation go_validation = new LeaveOneOutCrossValidation(g);
		go_validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		List<Rank> go_rankList = go_validation.run_rank(diseaseGeneSet, candidateGeneSet);
		
		Normalized normalized = new Normalized();
		
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			normalized.setAthreshhold(Double.parseDouble(a_threshhold.trim()));
			
			List<Rank> new_rankList = normalized.run(sp_rankList, go_rankList);
			Collections.sort(new_rankList);
			
			writeRankList(g, input.getOutputDir() + "spgo_candidate_gene_rank_"+ a_threshhold.trim() +".txt", new_rankList);
		}
		
		System.out.println("Finished.");
	}
}
