package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphHelper {
	
	/**
	 * 判断图是否连通
	 * @param g
	 * @return
	 */
	public static boolean isConnected(Graph g){
		Set<Integer> visited = new HashSet<Integer>();
		dfs(g, 0, visited);
		
		if(visited.size() < g.getNodeNum()){
			return false;
		}
		return true;
	}
	
	/**
	 * 计算图的连通分量的数目
	 * @param g
	 * @return
	 */
	public static int countConnectedComponent(Graph g){
		Set<Integer> visited = new HashSet<Integer>();
		int count = 0;
		for(int i = 0; i < g.getNodeNum(); ++i){
			if(visited.contains(i))continue;
			dfs(g, i, visited);
			++count;
		}
		
		return count;
	}
	
	private static void dfs(Graph g, Integer v, Set<Integer> visited){
		visited.add(v);
		double[][] matrix = g.getAdjMatrix();
		for(int i = 0; i < matrix.length; ++ i){
			if(matrix[v][i] < Graph.INF && !visited.contains(i)){
				dfs(g, i, visited);
			}
		}
	}
	
	public static Graph createSubGraph(Graph g, Set<Integer> nodeIdSet){
		Graph newGraph = new AdjGraph();
		Iterator<Integer> itr = nodeIdSet.iterator();
		while(itr.hasNext()){
			newGraph.addNode(g.getNodeName(itr.next()));
		}
		
		double[][] matrix = g.getAdjMatrix();
		Integer nodeId = null;
		itr = nodeIdSet.iterator();
		while(itr.hasNext()){
			nodeId = itr.next();
			for(int i = 0; i < matrix.length; ++i){
				if(matrix[nodeId][i] < Graph.INF && nodeIdSet.contains(i)){
					newGraph.addEdge(g.getNodeName(nodeId), g.getNodeName(i), matrix[nodeId][i]);
				}
			}
		}
		
		return newGraph;
	}
}
