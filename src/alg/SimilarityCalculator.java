package alg;

import graph.Graph;
import graph.Path;
import graph.ShortestPath;

import java.util.Arrays;
import java.util.List;

public class SimilarityCalculator {
	private static int R_THRESHHOLD = 4000000; // 最短路径长度
	
	public void setR_Theshhold(int r){
		R_THRESHHOLD = r;
	}
	
	public int getR_Threshhold(){
		return R_THRESHHOLD;
	}
	
	/**
	 * 计算两个基因之间的相似性
	 * */
	public static double calculateSimilarity(Graph g, String a, String b){
		double similarity = 0.0;
		if(g.containsEdge(a, b)){
			similarity = calculateSimilarity_connected(g, a, b);
		}else{
			ShortestPath sp = DijkstraAlgorithm.dijsktra(g.getNodeIndex(a), g.getAdjMatrix());
			similarity = calculateSimilarity_unconnected(g, a, b, sp);
		}
		return similarity;
	}
	
	/**
	 * 计算两个基因之间的相似性
	 * */
	public static double calculateSimilarity(Graph g, String a, String b, List<Path<Integer>> sp){
		double similarity = Graph.INF;
		if(g.containsEdge(a, b)){
			similarity = calculateSimilarity_connected(g, a, b);
		}else{
			double tmp = 0.0;
			for(Path<Integer> p : sp){
				tmp = calculateSimilarity_unconnected(g, a, b, p);
				if(tmp < similarity){
					similarity = tmp;
				}
			}
		}
		return similarity;
	}
	
	/**
	 * 计算两个基因之间的相似性
	 * */
	public static double calculateSimilarity(Graph g, String a, String b, ShortestPath sp){
		double similarity = 0.0;
		if(g.containsEdge(a, b)){
			similarity = calculateSimilarity_connected(g, a, b);
		}else{
			similarity = calculateSimilarity_unconnected(g, a, b, sp);
		}
		return similarity;
	}
	
	/**
	 * 计算非直接相连的两个基因之间的相似性
	 * */
	private static double calculateSimilarity_unconnected(Graph g, String a, String b, Path<Integer> p){
		Integer[] ret = p.toArray();
		
		if(ret.length > R_THRESHHOLD){
			return 0.0;
		}
		
		double similarity = 1;
		for(int i = 1; i < ret.length; ++i){
			similarity *= calculateSimilarity_connected(g, g.getNodeName(ret[i]), g.getNodeName(ret[i - 1]));
		}
		
		return similarity;
	}
	
	/**
	 * 计算非直接相连的两个基因之间的相似性
	 * */
	private static double calculateSimilarity_unconnected(Graph g, String a, String b, ShortestPath sp){
		double similarity = 1;
		int bId = g.getNodeIndex(b);

		int[] ret = sp.getShortestPath(bId);
		/* a,b 之间存在最短路径 && 最短路径的长度 <= r*/
		if(sp.getShortestPathWeight(bId) < Graph.INF && ret.length <= R_THRESHHOLD){
			for(int i = ret.length - 1; i > 0; --i){
				similarity *= calculateSimilarity_connected(g, g.getNodeName(ret[i]), g.getNodeName(ret[i - 1]));
			}
		}else{
			similarity = 0.0;
		}
		return similarity;
	}
	
	/**
	 * 计算直接相连两个基因之间的相似性
	 */
	private static double calculateSimilarity_connected(Graph g, String a, String b){
		if(!g.containsEdge(a, b)){
			return 0.0;
		}
		
		double similarity = 0.0;
		double[][] matrix = g.getAdjMatrix();
		int aId = g.getNodeIndex(a);
		int bId = g.getNodeIndex(b);
		byte[] neighbors = new byte[matrix.length];
		Arrays.fill(neighbors, (byte)0);
		
		for(int i = 0; i < matrix.length; ++i){
			if(matrix[aId][i] < Graph.INF){
				neighbors[i]++;
			}
			if(matrix[bId][i] < Graph.INF){
				neighbors[i]++;
			}
		}
		
		double sumAB = 0.0;
		double sumA2 = 0.0, sumB2 = 0.0;
		
		double ai, bi;
		for(int i = 0; i < neighbors.length; ++i){
			if(neighbors[i] > 0){
				ai = matrix[aId][i];
				bi = matrix[bId][i];
				if(i == aId){
					ai = 1;
				}else if(i == bId){
					bi = 1;
				}
				if(ai < Graph.INF && bi < Graph.INF){
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
}
