package similarity;

import graph.Graph;
import graph.ShortestPath;
import alg.DijkstraAlgorithm;

public class SPSimilarityAlgorithm extends VSSimilarityAlgorithm {
	/**
	 * 计算两个基因之间的相似性
	 * 若u, v 有直接相连的边, 则u, v之间的相似性计算使用calculateSimilarity_connected方式  
	 * 若u, v 没有直接相连的边, 则分两种情况考虑:
	 * 1: u, v有共同的邻居结点, 则u, v之间的相似性计算使用calculateSimilarity_connected方式  
	 * 2: u, v没有共同的邻居结点, 则u, v之间的相似性计算使用calculateSimilarity_unconnected方式
	 * */
	public double calculateSimilarity(int u, int v, double[][] matrix, ShortestPath sp){
		double similarity = 0.0;
		//if(matrix[u][v] < Graph.INF || hasCommonNeighbor(u, v, matrix)){
		if(matrix[u][v] < Graph.INF){
			similarity = calculateSimilarity_connected(u, v, matrix);
		}else{
			similarity = calculateSimilarity_unconnected(u, v, matrix, sp);
		}
		return similarity;
	}
	
	private boolean hasCommonNeighbor(int u, int v, double[][] matrix){
		for(int i = 0; i < matrix.length; ++i){
			if(matrix[u][i] < Graph.INF && matrix[v][i] < Graph.INF){
				return true;
			}
		}
		return false;
	}
	
}
