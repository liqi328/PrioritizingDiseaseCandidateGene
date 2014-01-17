package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.GoSimilarityAlgorithm;
import similarity.SPSimilarityAlgorithm;
import similarity.VSSimilarityAlgorithm;
import similarity.VS_GoSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentVS_GO extends ExperimentGO {
	
	public ExperimentVS_GO(InputArgument input) {
		super(input);
	}
	
	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment VS + GO running...");
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSeedSet, candidateGeneSet);
		
		LeaveOneOutCrossValidationForVS vs_validation = new LeaveOneOutCrossValidationForVS(g);
		vs_validation.setSimilarityAlgorithm(new VSSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> vs_ranksMap = vs_validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		LeaveOneOutCrossValidation go_validation = new LeaveOneOutCrossValidation(g);
		go_validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		Map<Integer, List<Rank>> go_ranksMap = go_validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = null;
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			
			resultMap = ValidationResultAnalysis.run(vs_ranksMap, go_ranksMap, 
					Double.parseDouble(a_threshhold.trim()));
			
			WriterUtil.write(input.getOutputDir() + "vs_go_validation_" + a_threshhold.trim() + ".txt",
					ValidationResultAnalysis.map2String(g, resultMap));
		}
		
		System.out.println("Experiment VS + GO finished.\n");
	}
	
	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Ranking candidate gene using VSGO algorithm. [start]");
		
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSet, candidateGeneSet);
		
		LeaveOneOutCrossValidationForVS vs_validation = new LeaveOneOutCrossValidationForVS(g);
		vs_validation.setSimilarityAlgorithm(new VSSimilarityAlgorithm());
		
		List<Rank> vs_rankList = vs_validation.run_rank(diseaseGeneSet, candidateGeneSet);
		
		LeaveOneOutCrossValidation go_validation = new LeaveOneOutCrossValidation(g);
		go_validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		List<Rank> go_rankList = go_validation.run_rank(diseaseGeneSet, candidateGeneSet);
		
		Normalized normalized = new Normalized();
		
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			normalized.setAthreshhold(Double.parseDouble(a_threshhold.trim()));
			
			List<Rank> new_rankList = normalized.run(vs_rankList, go_rankList);
			Collections.sort(new_rankList);
			
			writeRankList(g, input.getOutputDir() + "vsgo_candidate_gene_rank_"+ a_threshhold.trim() +".txt", new_rankList);
		}
		
		System.out.println("Finished.");
	}
}
