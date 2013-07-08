package similarity;

import graph.Graph;
import graph.ShortestPath;

import java.util.Arrays;

import alg.DijkstraAlgorithm;

public class VSSimilarityAlgorithm implements SimilarityAlgorithm {
	private static int R_THRESHHOLD = 4000000; // ���·������
	
	public void setR_Theshhold(int r){
		R_THRESHHOLD = r;
	}
	
	public int getR_Threshhold(){
		return R_THRESHHOLD;
	}
	
	/**
	 * �����������֮���������
	 * */
	public double calculate(int u, int v, double[][] matrix){
		double vs_similarity = 0.0;
		if(matrix[u][v] < Graph.INF){
			vs_similarity = calculateSimilarity_connected(u, v, matrix);
		}else{
			ShortestPath sp = DijkstraAlgorithm.dijsktra(u, matrix);
			vs_similarity = calculateSimilarity_unconnected(u, v, matrix, sp);
		}
		return vs_similarity;
	}
	
	/**
	 * ������������֮���������
	 * */
	public double calculateSimilarity(int u, int v, double[][] matrix, ShortestPath sp){
		double similarity = 0.0;
		if(matrix[u][v] < Graph.INF){
			similarity = calculateSimilarity_connected(u, v, matrix);
		}else{
			similarity = calculateSimilarity_unconnected(u, v, matrix, sp);
		}
		return similarity;
	}
	
	
	/**
	 * ����ֱ��������������֮���������
	 */
	protected double calculateSimilarity_connected(int u, int v, double[][] matrix){
		double similarity = 0.0;
		
		double sumAB = 0.0;
		double sumA2 = 1.0, sumB2 = 1.0;
		
		if(matrix[u][v] < Graph.INF){
			sumAB = 2 * matrix[u][v];
		}
		
		for(int i = 0; i < matrix.length; ++i){
			if(matrix[u][i] < Graph.INF && matrix[v][i] < Graph.INF){
				sumAB += matrix[u][i] * matrix[v][i];
			}
			if(matrix[u][i] < Graph.INF){
				sumA2 += matrix[u][i] * matrix[u][i];
			}
			if(matrix[v][i] < Graph.INF){
				sumB2 += matrix[v][i] * matrix[v][i];
			}
		}
		
		similarity = sumAB / (Math.sqrt(sumA2) * Math.sqrt(sumB2));
		return similarity;
	}
	
	/**
	 * ����ֱ��������������֮���������
	 */
	private double calculateSimilarity_connected(int u, int v, double[][] matrix, int test){
		double similarity = 0.0;
		
		byte[] neighbors = new byte[matrix.length];
		Arrays.fill(neighbors, (byte)0);
		
		for(int i = 0; i < matrix.length; ++i){
			if(matrix[u][i] < Graph.INF){
				neighbors[i]++;
			}
			if(matrix[v][i] < Graph.INF){
				neighbors[i]++;
			}
		}
		
		double sumAB = 0.0;
		double sumA2 = 0.0, sumB2 = 0.0;
		
		double ai, bi;
		for(int i = 0; i < neighbors.length; ++i){
			if(neighbors[i] > 0){
				ai = matrix[u][i];
				bi = matrix[v][i];
				if(i == u){
					ai = 1;
				}else if(i == v){
					bi = 1;
				}else if(ai < Graph.INF && bi < Graph.INF){
				}else if(ai < Graph.INF){
					bi = 0;
				}else{
					ai = 0;
				}
				sumAB += ai * bi;
				sumA2 += ai * ai;
				sumB2 += bi * bi;
			}
		}
		
		similarity = sumAB / (Math.sqrt(sumA2) * Math.sqrt(sumB2));
		return similarity;
	}
	
	
	/**
	 * �����ֱ����������������֮���������
	 * */
	protected double calculateSimilarity_unconnected(int u, int v, double[][] matrix, ShortestPath sp){
		double similarity = 1;
		
		int[] ret = sp.getShortestPath(v);
		/* a,b ֮��������·�� && ���·���ĳ��� <= r*/
		if(sp.getShortestPathWeight(v) < Graph.INF && ret.length <= R_THRESHHOLD){
			for(int i = ret.length - 1; i > 0; --i){
				similarity *= calculateSimilarity_connected(ret[i], ret[i - 1], matrix);
			}
		}else{
			similarity = 0.0;
		}
		return similarity;
	}
}
