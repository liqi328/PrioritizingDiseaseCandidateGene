package similarity;

import graph.Graph;

/**
 * 计算候选基因与种子基因的邻居之间的相似性
 * @author Liqi
 *
 */
public class NeighborSimilarityAlgorithm extends VSSimilarityAlgorithm {
	/**
	 * 计算两个结点之间的相似性
	 * 若两点有直连边,则计算Sim(u,v)
	 * 没有直连边则返回0
	 * */
	public double calculate(int u, int v, double[][] matrix){
		double vs_similarity = 0.0;
		if(matrix[u][v] < Graph.INF){
			vs_similarity = calculateSimilarity_connected(u, v, matrix);
		}
		return vs_similarity;
	}
}
