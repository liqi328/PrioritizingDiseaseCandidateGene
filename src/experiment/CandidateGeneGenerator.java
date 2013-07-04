package experiment;

import graph.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 随机生成候选基因
 * 
 * @author Liqi
 *
 */
public class CandidateGeneGenerator {
	private static int number = 99;
	
	public static void setNumber(int num){
		number = num;
	}
	
	/**
	 * 根据PPI随机生成number个候选基因，(不在已知致病基因集合中)
	 * @param g					PPI
	 * @param diseaseGeneSet	已知致病基因集合
	 * @return
	 */
	public static Set<Integer> run(Graph g, Set<Integer> diseaseGeneSet){
		Set<Integer> candidateGeneSet = new HashSet<Integer>();
		int nodeNum = g.getNodeNum();
		int index = 0;
		Random rnd = new Random();
		for(int i = 0; i < number;){
			index = rnd.nextInt(nodeNum);
			if(diseaseGeneSet.contains(index) || candidateGeneSet.contains(index)){
				continue;
			}
			candidateGeneSet.add(index);
			++i;
		}
		
		return candidateGeneSet;
	}
}
