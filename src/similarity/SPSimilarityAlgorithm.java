package similarity;

import graph.Graph;
import graph.ShortestPath;
import alg.DijkstraAlgorithm;

public class SPSimilarityAlgorithm extends VSSimilarityAlgorithm {
	/**
	 * ������������֮���������
	 * ��u, v ��ֱ�������ı�, ��u, v֮��������Լ���ʹ��calculateSimilarity_connected��ʽ  
	 * ��u, v û��ֱ�������ı�, ��������������:
	 * 1: u, v�й�ͬ���ھӽ��, ��u, v֮��������Լ���ʹ��calculateSimilarity_connected��ʽ  
	 * 2: u, vû�й�ͬ���ھӽ��, ��u, v֮��������Լ���ʹ��calculateSimilarity_unconnected��ʽ
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
