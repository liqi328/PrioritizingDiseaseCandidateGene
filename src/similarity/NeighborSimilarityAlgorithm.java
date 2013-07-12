package similarity;

import graph.Graph;

/**
 * �����ѡ���������ӻ�����ھ�֮���������
 * @author Liqi
 *
 */
public class NeighborSimilarityAlgorithm extends VSSimilarityAlgorithm {
	/**
	 * �����������֮���������
	 * ��������ֱ����,�����Sim(u,v)
	 * û��ֱ�����򷵻�0
	 * */
	public double calculate(int u, int v, double[][] matrix){
		double vs_similarity = 0.0;
		if(matrix[u][v] < Graph.INF){
			vs_similarity = calculateSimilarity_connected(u, v, matrix);
		}
		return vs_similarity;
	}
}
