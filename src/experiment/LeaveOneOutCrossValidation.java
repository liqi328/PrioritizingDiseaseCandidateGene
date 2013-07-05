package experiment;

import graph.Graph;
import graph.Path;
import graph.ShortestPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alg.DijkstraAlgorithm;
import alg.ShortestPathAlgorithm;
import alg.SimilarityCalculator;

import similarity.SimilarityAlgorithm;
import similarity.VSSimilarityAlgorithm;
import util.PathUtil;
import util.ShortestPathUtil;

/**
 * ��һ������֤
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
	 * @param diseaseGeneSeed	һ�ּ����������²����򼯺�
	 * @param candidateGeneSet	��ѡ����(���Ի���)
	 * @return �²�������Ϊtarget gene������
	 */
	public Map<Integer, List<Rank>> run(Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		Map<Integer, List<Rank>> rankResult = new HashMap<Integer, List<Rank>>();
		
		if(g == null || alg == null){
			System.out.println("�����ú��㷨��ͼ.");
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
			
			System.out.println("Target Gene = " + targetGene + ", Training = " + trainingGeneSet.size() + 
					", Candidate = " + candidateGeneSet.size());
			
			if(this.alg.getClass().getName().equals(VSSimilarityAlgorithm.class.getName())){
				rankResult.put(targetGene, run_one_validation_trial_vs(trainingGeneSet, candidateGeneSet));
			}else{
				rankResult.put(targetGene, run_one_validation_trial(trainingGeneSet, candidateGeneSet));
			}
			
			
			candidateGeneSet.remove(targetGene);
		}
		return rankResult;
	}
	
	/**
	 * @param trainingGeneSeed	ѵ����
	 * @param candidateGeneSet	���Լ�
	 * @return	���Լ��еĻ�����ѵ�����������Է���
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
	
	/**
	 * VS �Ĳ��Է���
	 * @param trainingGeneSeed	ѵ����
	 * @param candidateGeneSet	���Լ�
	 * @return	���Լ��еĻ�����ѵ�����������Է���
	 */
	private List<Rank> run_one_validation_trial_vs(Set<Integer> trainingGeneSeed, 
			Set<Integer> candidateGeneSet){
		System.out.println("VS similarity alg specify cross validation.");
		List<Rank> rankList = new ArrayList<Rank>();
		
		Map<Integer, ShortestPath> shortestPathMap = new HashMap<Integer, ShortestPath>();
		for(Integer candidate: candidateGeneSet){
        	ShortestPath sp = DijkstraAlgorithm.dijsktra(candidate, g.getAdjMatrix());
        	shortestPathMap.put(candidate, sp);
        }
		
		VSSimilarityAlgorithm vsAlg = (VSSimilarityAlgorithm)this.alg;
		
		double score = 0.0;
		Iterator<Integer> candidateItr = candidateGeneSet.iterator();
		while(candidateItr.hasNext()){
			Integer u = candidateItr.next();
			score = 0.0;
			
			Iterator<Integer> trainingItr = trainingGeneSeed.iterator();
			while(trainingItr.hasNext()){
				Integer v = trainingItr.next();
				score += vsAlg.calculateSimilarity(u, v, g.getAdjMatrix(), shortestPathMap.get(u));
			}
			rankList.add(new Rank(u, score));
		}
		
		return rankList;
	}
}
