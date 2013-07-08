package similarity;

import graph.Graph;

public class ICNSimilarityAlgorithm implements SimilarityAlgorithm {
	
	public double calculate(int u, int v, double[][] matrix){
		double icn = 0.0;
		
		double k_u = 0; 	//结点u的度
		double k_v = 0;	//结点v的度
		if(matrix[u][v] < Graph.INF){
			icn = 2 * matrix[u][v];
		}
	
		for(int i = 0; i < matrix.length; ++i){
			icn += matrix[u][i] < Graph.INF && matrix[v][i] < Graph.INF ? matrix[u][i] * matrix[v][i] : 0;
			if(matrix[u][i] < Graph.INF){
				k_u += matrix[u][i];
			}
			if(matrix[v][i] < Graph.INF){
				k_v += matrix[v][i];
			}
		}
		double divisor = k_u * k_v ;
		divisor = divisor <= 0.000000001 ? 1 : Math.sqrt(divisor);
		return icn / divisor;
	}
	
	
	public static void main(String[] args){
		SimilarityAlgorithm icn = new ICNSimilarityAlgorithm();
		double[][] matrix = {
				{Graph.INF, 1, 1, 1, 1, 1, Graph.INF},
				{1, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF, 1},
				{1, 1, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF},
				{1, Graph.INF, 1, Graph.INF, 1, Graph.INF, Graph.INF},
				{1, Graph.INF, Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF},
				{1, Graph.INF, Graph.INF, Graph.INF, Graph.INF, Graph.INF, 1},
				{Graph.INF, 1, Graph.INF, Graph.INF, Graph.INF, 1, Graph.INF}};
		
		System.out.println("ICN(0, 1) = " + icn.calculate(0, 1, matrix));
		System.out.println("ICN(1, 0) = " + icn.calculate(1, 0, matrix));
		System.out.println("ICN(0, 2) = " + icn.calculate(0, 2, matrix));
		System.out.println("ICN(5, 6) = " + icn.calculate(5, 6, matrix));
		System.out.println("ICN(3, 6) = " + icn.calculate(3, 6, matrix));
		System.out.println("ICN(3, 4) = " + icn.calculate(3, 4, matrix));
	}

}
