package experiment;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import similarity.NeighborSimilarityAlgorithm;
import similarity.SPSimilarityAlgorithm;
import similarity.SimilarityAlgorithm;
import util.GraphUtil;
import util.WriterUtil;

public class ExperimentSP_Neighbor extends AbstractExperiment{

	public ExperimentSP_Neighbor(InputArgument input) {
		super(input);
	}

	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment SP + Neighbor running...");
		
		LeaveOneOutCrossValidationForVS sp_validation = new LeaveOneOutCrossValidationForVS(g);
		sp_validation.setSimilarityAlgorithm(new SPSimilarityAlgorithm());
		
		Map<Integer, List<Rank>> sp_ranksMap = sp_validation.run(diseaseGeneSeedSet, candidateGeneSet);
		
		
		Set<Integer> neighborSet = GraphUtil.getNeighbors(g, diseaseGeneSeedSet);
		
		caculateNeighborSimilarity(sp_ranksMap, neighborSet, g, 0.5);
		
		Map<Integer, List<Rank>> sp_nei_ranksMap = null;
		Map<Integer, Rank> resultMap = null;
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			
			sp_nei_ranksMap = caculateNeighborSimilarity(sp_ranksMap, neighborSet, g, Double.parseDouble(a_threshhold));
			
			resultMap = ValidationResultAnalysis.run(sp_nei_ranksMap);
			
			WriterUtil.write(input.getOutputDir() + "sp_neighbor_validation_"+ a_threshhold +".txt",
					ValidationResultAnalysis.map2String(g, resultMap));
		}
		
		System.out.println("Experiment SP + Neighbor finished.\n");
	}
	
	private Map<Integer, List<Rank>> caculateNeighborSimilarity(Map<Integer, List<Rank>> sp_ranksMap,
			Set<Integer> neighborSet, Graph g, double a_threshhold){
		SimilarityAlgorithm neighborAlg = new NeighborSimilarityAlgorithm();
		Map<Integer, List<Rank>> resultMap = new HashMap<Integer, List<Rank>>();
		
		double score = 0.0;
		Iterator<Entry<Integer, List<Rank>>> ranksItr = sp_ranksMap.entrySet().iterator();
		Entry<Integer, List<Rank>> entry = null;
		while(ranksItr.hasNext()){
			entry = ranksItr.next();
			List<Rank> newRankList = new ArrayList<Rank>();
			resultMap.put(entry.getKey(), newRankList);
			
			Iterator<Rank> itr = entry.getValue().iterator();
			while(itr.hasNext()){
				Rank rank = itr.next();
				Integer u = rank.getId();
				
				score = 0.0;
				Iterator<Integer> neighborItr = neighborSet.iterator();
				while(neighborItr.hasNext()){
					Integer v = neighborItr.next();
					score += neighborAlg.calculate(u, v, g.getAdjMatrix());
				}
				
				score = rank.getScore() + a_threshhold * score;
				newRankList.add(new Rank(u, score));
			}
		}
		
		return resultMap;
	}
	
		
		
}
