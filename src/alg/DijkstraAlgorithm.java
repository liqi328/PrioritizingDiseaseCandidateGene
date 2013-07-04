package alg;

import graph.Graph;
import graph.ShortestPath;

public class DijkstraAlgorithm {
	
    /**
     * �Ͻ�˹�����㷨����Դ�㵽����������·��
     * @param start		Դ��
     * @param matrix	�ڽӾ���
     * @return
     */
    public static ShortestPath dijsktra(int start, double[][] matrix){
    	int n = matrix.length;
    	boolean[] flag = new boolean[n];
    	double[] dist = new double[n];
    	int[] path = new int[n];
    	
    	for(int i = 0; i < n; ++i){
    		dist[i] = matrix[start][i];
    		flag[i] = false;
    		if(i != start && dist[i] < Graph.INF){
    			path[i] = start;
    		}else{
    			path[i] = -1;
    		}
    	}
    	
    	//����start���뵽���㼯��flag
    	flag[start] = true;
    	dist[start] = 0;
    	
    	double min = Graph.INF;
    	int u = -1;
    	for(int i = 0; i < n -1; ++i){
    		min = Graph.INF;
    		u = start;
    		for(int j = 0; j < n; ++j){
    			if(!flag[j] && dist[j] < min){
    				u = j;
    				min = dist[j];
    			}
    		}
    		
//    		if(u == start){
//    			System.out.println(u);
//    		}
    		flag[u] = true;
        	for(int k = 0; k < n; ++k){
        		if(!flag[k] && matrix[u][k] < Graph.INF && dist[u] + matrix[u][k] < dist[k]){
        			dist[k] = dist[u] + matrix[u][k];
        			path[k] = u;
        		}
        	}
    	}
    	
    	return new ShortestPath(start, dist, path);
    }
}

