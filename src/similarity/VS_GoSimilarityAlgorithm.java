package similarity;

import graph.ShortestPath;

public class VS_GoSimilarityAlgorithm extends VSSimilarityAlgorithm {
	private VSSimilarityAlgorithm vsAlg;
	private GoSimilarityAlgorithm goAlg;
	public VS_GoSimilarityAlgorithm(VSSimilarityAlgorithm vsAlg, GoSimilarityAlgorithm goAlg){
		this.vsAlg = vsAlg;
		this.goAlg = goAlg;
	}
	
	/**
	 * 计算两个基因之间的相似性
	 * 若u, v 有直接相连的边, 则u, v之间的相似性计算使用calculateSimilarity_connected方式  
	 * 若u, v 没有直接相连的边, 则分两种情况考虑:
	 * 1: u, v有共同的邻居结点, 则u, v之间的相似性计算使用calculateSimilarity_connected方式  
	 * 2: u, v没有共同的邻居结点, 则u, v之间的相似性计算使用calculateSimilarity_unconnected方式
	 * */
	public double calculateSimilarity(int u, int v, double[][] matrix, ShortestPath sp){
		double similarity = vsAlg.calculateSimilarity(u, v, matrix, sp);
		similarity += goAlg.calculate(u, v, matrix);
		return similarity;
	}

}
