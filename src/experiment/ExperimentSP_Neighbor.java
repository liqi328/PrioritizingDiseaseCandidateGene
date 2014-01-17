package experiment;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
		
		Map<Integer, List<Rank>> sp_nei_ranksMap = null;
		Map<Integer, Rank> resultMap = null;
		for(String a_threshhold : input.getAthreshholdArray()){
			System.out.println("\t--> a_threshhold = " + a_threshhold);
			
			sp_nei_ranksMap = caculateNeighborSimilarity(sp_ranksMap, g, Double.parseDouble(a_threshhold.trim()));
			
			resultMap = ValidationResultAnalysis.run(sp_nei_ranksMap);
			
			WriterUtil.write(input.getOutputDir() + "sp_neighbor_validation_"+ a_threshhold.trim() +".txt",
					ValidationResultAnalysis.map2String(g, resultMap));
		}
		
		System.out.println("Experiment SP + Neighbor finished.\n");
	}
	
	private Map<Integer, List<Rank>> caculateNeighborSimilarity(Map<Integer, List<Rank>> sp_ranksMap,
			 Graph g, double a_threshhold){
		SimilarityAlgorithm neighborAlg = new NeighborSimilarityAlgorithm();
		Map<Integer, List<Rank>> resultMap = new HashMap<Integer, List<Rank>>();
		
		Set<Integer> diseaseGeneSeedSet = new HashSet<Integer>();
		diseaseGeneSeedSet.addAll(sp_ranksMap.keySet());
		
		double score = 0.0;
		Iterator<Entry<Integer, List<Rank>>> ranksItr = sp_ranksMap.entrySet().iterator();
		Entry<Integer, List<Rank>> entry = null;
		while(ranksItr.hasNext()){
			entry = ranksItr.next();
			List<Rank> newRankList = new ArrayList<Rank>();
			resultMap.put(entry.getKey(), newRankList);
			
			//System.out.println("target gene:" + entry.getKey());
			diseaseGeneSeedSet.remove(entry.getKey());
			Set<Integer> neighborSet = GraphUtil.getNeighbors(g, diseaseGeneSeedSet);
			
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
			
			diseaseGeneSeedSet.add(entry.getKey());
		}
		
		return resultMap;
	}

	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		// TODO Auto-generated method stub
		
	}
	
}
