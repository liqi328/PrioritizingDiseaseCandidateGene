package experiment;

import graph.Graph;

import java.util.HashSet;
import java.util.Iterator;
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
	
	/**
	 * ���²������ֱ���ھ���Ϊ��ѡ����
	 * @param g
	 * @param diseaseGeneSet
	 * @return
	 */
	public static Set<Integer> run_neighbor(Graph g, Set<Integer> diseaseGeneSet){
		Set<Integer> candidateGeneSet = new HashSet<Integer>();
		double[][] adjMatrix = g.getAdjMatrix();
		
		Iterator<Integer> itr = diseaseGeneSet.iterator();
		while(itr.hasNext()){
			Integer nodeIndex = itr.next();
			for(int j = 0; j < g.getNodeNum(); ++j){
				if(adjMatrix[nodeIndex][j] < Graph.INF){
					candidateGeneSet.add(j);
				}
			}
		}
		
		itr = candidateGeneSet.iterator();
		while(itr.hasNext()){
			Integer nodeIdex = itr.next();
			if(diseaseGeneSet.contains(nodeIdex)){
				itr.remove();
			}
		}
		
		return candidateGeneSet;
	}
}
