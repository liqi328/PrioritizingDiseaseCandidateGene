package similarity;

import graph.Graph;

/**
 * 边聚集系数
 * @author Liqi
 *
 */
public class ECCSimilarityAlgorithm implements SimilarityAlgorithm {
	public double calculate(int u, int v, double[][] matrix){
		double ecc = 0.0;
		
		int k_u = 0; 	//结点u的度
		int k_v = 0;	//结点v的度
		
		if(matrix[u][v] < Graph.INF){
			for(int i = 0; i < matrix.length; ++i){
				ecc += matrix[u][i] < Graph.INF && matrix[v][i] < Graph.INF ? 1 : 0;
				if(matrix[u][i] < Graph.INF){
					++k_u;
				}
				if(matrix[v][i] < Graph.INF){
					++k_v;
				}
			}
		}
		
		int min_k = Math.min(k_u, k_v);
		min_k = min_k >= 2 ? min_k - 1 : 1; 
		return ecc / min_k;
	}
	
	public static void main(String[] args){
		SimilarityAlgorithm ecc = new ECCSimilarityAlgorithm();
		
		double[][] matrix = {
				{Graph.INF, 1, 1, 1, 1, 1, Graph.INF},
				{1, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF, 1},
				{1, 1, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF},
				{1, Graph.INF, 1, Graph.INF, 1, Graph.INF, Graph.INF},
				{1, Graph.INF, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF},
				{1, Graph.INF, Graph.INF, Graph.INF, Graph.INF, Graph.INF, 1},
				{Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF, 1, Graph.INF}};
		
		System.out.println("ECC(0, 1) = " + ecc.calculate(0, 1, matrix));
		System.out.println("ECC(1, 0) = " + ecc.calculate(1, 0, matrix));
		System.out.println("ECC(0, 2) = " + ecc.calculate(0, 2, matrix));
		System.out.println("ECC(5, 6) = " + ecc.calculate(5, 6, matrix));
		System.out.println("ECC(3, 6) = " + ecc.calculate(3, 6, matrix));
		System.out.println("ECC(4, 5) = " + ecc.calculate(4, 5, matrix));
	}
}
