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

import similarity.SPSimilarityAlgorithm;
import similarity.VSSimilarityAlgorithm;
import alg.DijkstraAlgorithm;

/**
 * 特定于VS similarity计算方法的 交叉验证方式
 * @author Liqi
 *
 */
public class LeaveOneOutCrossValidationForVS {
	private Graph g;
	private VSSimilarityAlgorithm alg = null;
	
	public void setSimilarityAlgorithm(VSSimilarityAlgorithm alg){
		this.alg = alg;
	}
	
	public LeaveOneOutCrossValidationForVS(Graph g){
		this.g = g;
	}
	
	/**
	 * 
	 * @param diseaseGeneSeed	一种疾病的所有致病基因集合
	 * @param candidateGeneSet	候选基因(测试基因)
	 * @return 致病基因作为target gene的排名
	 */
	public Map<Integer, List<Rank>> run(Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		System.out.println("[LeaveOneOutCrossValidationForVS] VS similarity alg specify cross validation.");
		Map<Integer, List<Rank>> rankResult = new HashMap<Integer, List<Rank>>();
		
		if(g == null || alg == null){
			System.out.println("请设置好结点相似性计算算法和图.");
			return rankResult;
		}
		
		Map<Integer, ShortestPath> shortestPathMap = calculateShortestPath(diseaseGeneSeed, candidateGeneSet);
		
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
			
			rankResult.put(targetGene, run_one_validation_trial(trainingGeneSet, candidateGeneSet, shortestPathMap));
			
			candidateGeneSet.remove(targetGene);
		}
		
		return rankResult;
	}
	
	private Map<Integer, ShortestPath> calculateShortestPath(Set<Integer> diseaseGeneSeed, 
			Set<Integer> candidateGeneSet){
		Map<Integer, ShortestPath> shortestPathMap = new HashMap<Integer, ShortestPath>();
		
		double[][] matrix = g.getAdjMatrix();
		if(this.alg.getClass().getName().equals(SPSimilarityAlgorithm.class.getName())){
			System.out.println("anthor matrix.");
			double similarity = 0.0;
			double[][] newMatrix = new double[matrix.length][matrix.length];
			for(int i = 0; i < matrix.length; ++i){
				for(int j = 0; j < matrix.length; ++j){
					newMatrix[i][j] = matrix[i][j];
					if(newMatrix[i][j] < Graph.INF){
						similarity = this.alg.calculate(i, j, matrix);
						newMatrix[i][j] = similarity < 0.0000001 ? Graph.INF : 1.0 / similarity;
					}
				}
			}
			matrix = newMatrix;
		}
		
		for(Integer seed: diseaseGeneSeed){
        	ShortestPath sp = DijkstraAlgorithm.dijsktra(seed, matrix);
        	shortestPathMap.put(seed, sp);
        }
		
		for(Integer seed: candidateGeneSet){
        	ShortestPath sp = DijkstraAlgorithm.dijsktra(seed, matrix);
        	shortestPathMap.put(seed, sp);
        }
		
		return shortestPathMap;
	}
	
	
	/**
	 * VS 的测试方法
	 * @param trainingGeneSeed	训练集
	 * @param candidateGeneSet	测试集
	 * @return	测试集中的基因与训练集的相似性分数
	 */
	private List<Rank> run_one_validation_trial(Set<Integer> trainingGeneSeed, 
			Set<Integer> candidateGeneSet, Map<Integer, ShortestPath> shortestPathMap){
		List<Rank> rankList = new ArrayList<Rank>();
		
		double score = 0.0;
		Iterator<Integer> candidateItr = candidateGeneSet.iterator();
		while(candidateItr.hasNext()){
			Integer u = candidateItr.next();
			score = 0.0;
			
			Iterator<Integer> trainingItr = trainingGeneSeed.iterator();
			while(trainingItr.hasNext()){
				Integer v = trainingItr.next();
				score += alg.calculateSimilarity(u, v, g.getAdjMatrix(), shortestPathMap.get(u));
			}
			rankList.add(new Rank(u, score));
		}
		
		return rankList;
	}
//	
//	private void print_shortestpath(Map<Integer, ShortestPath> shortestPathMap, String name, Set<Integer> diseaseGeneSeed){
//		StringBuffer sb = new StringBuffer();
//		for(ShortestPath p : shortestPathMap.values()){
//			for(Integer v : diseaseGeneSeed){
//				sb.append(ShortestPathUtil.parseShortestPath(p, g, v)).append("\n");
//			}
//		}
//		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_genealidation_result/"+name+".txt", sb.toString());
//	}

}
