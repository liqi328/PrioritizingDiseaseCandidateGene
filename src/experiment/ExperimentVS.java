package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.SPSimilarityAlgorithm;
import similarity.SimilarityAlgorithm;
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
		VSSimilarityAlgorithm alg = new VSSimilarityAlgorithm();
		validation.setSimilarityAlgorithm(alg);
		
		String[] r_threshhold = {"2", "3", "4", "1000000"};
		
		for(String r : r_threshhold){
			alg.setR_Theshhold(Integer.parseInt(r));
			
			Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
			
			Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
			
			WriterUtil.write(input.getOutputDir() + "vs_validation_"+ r +".txt",
					ValidationResultAnalysis.map2String(g, resultMap));
		}
		
		
		System.out.println("Experiment VS finished.\n");
	}

	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Ranking candidate gene using VS algorithm. [start]");
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		VSSimilarityAlgorithm alg = new VSSimilarityAlgorithm();
		validation.setSimilarityAlgorithm(alg);
		
		String[] r_threshhold = {"2", "3", "4", "1000000"};
		
		for(String r : r_threshhold){
			alg.setR_Theshhold(Integer.parseInt(r));
			
			List<Rank> rankList = validation.run_rank(diseaseGeneSet, candidateGeneSet);
			Collections.sort(rankList);
			
			writeRankList(g, input.getOutputDir() + "vs_candidate_gene_rank_"+ r +".txt", rankList);
		}
		
		System.out.println("Finished.");
	}

}
