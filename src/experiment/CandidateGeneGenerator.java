package experiment;

import graph.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * ������ɺ�ѡ����
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
	 * ����PPI�������number����ѡ����(������֪�²����򼯺���)
	 * @param g					PPI
	 * @param diseaseGeneSet	��֪�²����򼯺�
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
