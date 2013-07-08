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
	 * ������������֮���������
	 * ��u, v ��ֱ�������ı�, ��u, v֮��������Լ���ʹ��calculateSimilarity_connected��ʽ  
	 * ��u, v û��ֱ�������ı�, ��������������:
	 * 1: u, v�й�ͬ���ھӽ��, ��u, v֮��������Լ���ʹ��calculateSimilarity_connected��ʽ  
	 * 2: u, vû�й�ͬ���ھӽ��, ��u, v֮��������Լ���ʹ��calculateSimilarity_unconnected��ʽ
	 * */
	public double calculateSimilarity(int u, int v, double[][] matrix, ShortestPath sp){
		double similarity = vsAlg.calculateSimilarity(u, v, matrix, sp);
		similarity += goAlg.calculate(u, v, matrix);
		return similarity;
	}

}
