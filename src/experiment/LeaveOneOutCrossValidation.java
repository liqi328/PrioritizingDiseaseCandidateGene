package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import similarity.SimilarityAlgorithm;

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
	public Map<Integer, Map<Integer, Double>> run(Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet){
		Map<Integer, Map<Integer, Double>> rankResult = new HashMap<Integer, Map<Integer, Double>>();
		
		if(g == null || alg == null){
			System.out.println("�����ú��㷨��ͼ.");
			return rankResult;
		}
		
		Set<Integer> trainingGeneSet = new HashSet<Integer>();
		
		Iterator<Integer> seedItr = diseaseGeneSeed.iterator();
		while(seedItr.hasNext()){
			Integer targeGene = seedItr.next();
			
			trainingGeneSet.clear();
			Collections.addAll(trainingGeneSet, diseaseGeneSeed.toArray(new Integer[]{}));
			trainingGeneSet.remove(targeGene);
			
			candidateGeneSet.add(targeGene);
			
			rankResult.put(targeGene, run_one_validation_trial(trainingGeneSet, candidateGeneSet));
			
			candidateGeneSet.remove(targeGene);
		}
		return rankResult;
	}
	
	/**
	 * @param trainingGeneSeed	ѵ����
	 * @param candidateGeneSet	���Լ�
	 * @return	���Լ��еĻ�����ѵ�����������Է���
	 */
	private Map<Integer, Double> run_one_validation_trial(Set<Integer> trainingGeneSeed, 
			Set<Integer> candidateGeneSet){
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		
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
			result.put(v, score);
		}
		
		return result;
	}
}
