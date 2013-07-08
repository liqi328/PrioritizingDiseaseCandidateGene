package alg;

import graph.Graph;
import graph.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


/**
 * 计算图中两点之间的所有最短路径
 * 
 * 基于广度优先算法 求两点之间的所有最短路径
 * @author Liqi
 *
 */
public class ShortestPathAlgorithm {
	/**
	 * 计算图中两点之间的所有最短路径
	 * @param start		源点
	 * @param end		终点
	 * @param matrix	图的邻接矩阵
	 * @return	两点之间的所有最短路径
	 */
	public static List<Path<Integer>> calculateAllShortestPath(int start, int end, double[][] matrix){
		List<Path<Integer>> shortestPathes = new ArrayList<Path<Integer>>();
		
		Queue<Node<Integer>> queue = new LinkedList<Node<Integer>>();
		queue.offer(new Node<Integer>(null, start, 1));
		
		int[] visited = new int[matrix.length];
		Arrays.fill(visited, 0);
		
		int preLevel = 1;
		visited[start] = preLevel;
		while(!queue.isEmpty()){
			Node<Integer> cur = queue.poll();
			if(cur.name == end){
				if(preLevel == 1 || cur.level == preLevel){
					preLevel = cur.level;
					addPath(cur, shortestPathes);
				}else{
					break;
				}
			}else{
				for(int i = 0; i < matrix.length; ++i){
					if(visited[i] > 0 && cur.level + 1 > visited[i]){
						continue;
					}
					if(matrix[cur.name][i] < Graph.INF){
						visited[i] = cur.level + 1;
						queue.offer(new Node<Integer>(cur, i, visited[i]));
					}
				}
			}
		}
		return shortestPathes;
	}
	
	/**
	 * 计算图中两点之间的一条最短路径
	 * @param start		源点
	 * @param end		终点
	 * @param matrix	图的邻接矩阵
	 * @return	两点之间的一条最短路径
	 */
	public static Path<Integer> calculateOneShortestPath(int start, int end, double[][] matrix){
		Path<Integer> resultPath = null;
		
		Queue<Node<Integer>> queue = new LinkedList<Node<Integer>>();
		queue.offer(new Node<Integer>(null, start, 1));
		
		Set<Integer> visited = new HashSet<Integer>();
		visited.add(start);
		
		while(!queue.isEmpty()){
			Node<Integer> cur = queue.poll();
			if(cur.name.equals(end)){
				resultPath = Node.generatePath(cur);
				break;
			}else{
				for(int i = 0; i < matrix.length; ++i){
					if(visited.contains(i)){
						continue;
					}
					if(matrix[cur.name][i] < Graph.INF){
						visited.add(i);
						queue.offer(new Node<Integer>(cur, i, cur.level + 1));
					}
				}
			}
		}
		return resultPath;
	}
	
	private static void addPath(Node<Integer> n, List<Path<Integer>> result){
        Path<Integer> path = new Path<Integer>(Integer.class);
        Node<Integer> p = n;
        while(p != null){
            path.add(p.name);
            p = p.parent; 
        }
        result.add(path);
    }

	
	static class Node<E> {
	    public Node<E> parent;
		public int level;
	    public E name;
	    
	    public Node(Node<E> parent, E value, int level){  
	        this.parent = parent;  
	        this.name = value;
	        this.level = level;
	    }
	    
	    public static Path<Integer> generatePath(Node<Integer> n){
	        Path<Integer> path = new Path<Integer>(Integer.class);
	        Node<Integer> p = n;
	        while(p != null){
	            path.add(p.name);
	            p = p.parent; 
	        }
	        return path;
	    }
	}

}
