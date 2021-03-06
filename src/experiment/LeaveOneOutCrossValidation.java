package experiment;

import graph.Graph;
import graph.ShortestPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import similarity.SimilarityAlgorithm;

/**
 * 留一交叉验证
 * 
 * @author Liqi
 *
 */
public class LeaveOneOutCrossValidation {
	private SimilarityAlgorithm alg;
	private Graph g;
	
	public LeaveOneOutCrossValidation(Graph g){
		this.g = g;
	}
	
	public void setSimilarityAlgorithm(SimilarityAlgorithm alg){
		this.alg = alg;
	}
	
	/**
	 * 
	 * @param diseaseGeneSeed	一种疾病的所有致病基因集合
	 * @param candidateGeneSet	候选基因(测试基因)
	 * @return 致病基因作为target gene的排名
	 */
	public Map<Integer, List<Rank>> run(Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		Map<Integer, List<Rank>> rankResult = new HashMap<Integer, List<Rank>>();
		
		if(g == null || alg == null){
			System.out.println("请设置好算法和图.");
			return rankResult;
		}
		
		Set<Integer> trainingGeneSet = new HashSet<Integer>();
		
		Iterator<Integer> seedItr = diseaseGeneSeed.iterator();
		while(seedItr.hasNext()){
			Integer targetGene = seedItr.next();
			
			trainingGeneSet.clear();
			Collections.addAll(trainingGeneSet, diseaseGeneSeed.toArray(new Integer[]{}));
			trainingGeneSet.remove(targetGene);
			
			candidateGeneSet.add(targetGene);
			
//			System.out.println("Target Gene = " + targetGene + ", Training = " + trainingGeneSet.size() + 
//					", Candidate = " + candidateGeneSet.size());
		
			rankResult.put(targetGene, run_one_validation_trial(trainingGeneSet, candidateGeneSet));
			
			candidateGeneSet.remove(targetGene);
		}
		return rankResult;
	}
	
	/**
	 * @param trainingGeneSeed	训练集
	 * @param candidateGeneSet	测试集
	 * @return	测试集中的基因与训练集的相似性分数
	 */
	private List<Rank> run_one_validation_trial(Set<Integer> trainingGeneSeed, 
			Set<Integer> candidateGeneSet){
		List<Rank> rankList = new ArrayList<Rank>();
		double score = 0.0;
		Iterator<Integer> candidateItr = candidateGeneSet.iterator();
		while(candidateItr.hasNext()){
			Integer v = candidateItr.next();
			score = 0.0;
			
			Iterator<Integer> trainingItr = trainingGeneSeed.iterator();
			while(trainingItr.hasNext()){
				Integer u = trainingItr.next();
				score += this.alg.calculate(u, v, this.g.getAdjMatrix());
			}
			rankList.add(new Rank(v, score));
		}
		
		return rankList;
	}
	
	
	public List<Rank> run_rank(Set<Integer> diseaseGeneSet, Set<Integer> candidateGeneSet){
		List<Rank> rankList = run_one_validation_trial(diseaseGeneSet, candidateGeneSet);
		return rankList;
	}
}
