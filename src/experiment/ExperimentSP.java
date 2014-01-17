package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.ICNSimilarityAlgorithm;
import similarity.SPSimilarityAlgorithm;
import util.WriterUtil;

public class ExperimentSP extends AbstractExperiment {

	public ExperimentSP(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeed,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment SP running...");
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		validation.setSimilarityAlgorithm(new SPSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeed, candidateGeneSet);
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "sp_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment SP finished.\n");
	}

	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Ranking candidate gene using SPranker algorithm. [start]");
		
		LeaveOneOutCrossValidationForVS validation = new LeaveOneOutCrossValidationForVS(g);
		validation.setSimilarityAlgorithm(new SPSimilarityAlgorithm());
		
		List<Rank> rankList = validation.run_rank(diseaseGeneSet, candidateGeneSet);
		Collections.sort(rankList);
		
		writeRankList(g, input.getOutputDir() + "sp_candidate_gene_rank.txt", rankList);
		
		System.out.println("Finished.");
	}

}
