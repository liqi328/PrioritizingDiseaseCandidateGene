package graph;

import java.util.Arrays;


public class ShortestPath {
	private int source;//Դ��
	
	private double[] dist;
	private int[] path;
	
	public ShortestPath(int source, double[] dist, int[] path){
		this.source = source;
		this.dist = dist;
		this.path = path;
	}
	
	public int getSource(){
		return this.source;
	}
	
	/**
	 * ���� ���յ�dest�����·����Ȩֵ
	 * */
	public double getShortestPathWeight(int dest){
		return this.dist[dest];
	}
	
	/**
	 * ���ص��յ�dest�����·������(·���бߵ���Ŀ)
	 * */
	public int getShortestPathLength(int dest){
		return getShortestPath(dest).length - 1;
	}
	
	/**
	 * ���ص��յ�dest�����·��
	 * */
	public int[] getShortestPath(int dest){
		int[] shortest = new int[this.path.length];
		int k = 0;
		shortest[k] = dest;
		while(path[shortest[k]] != -1 && path[shortest[k]] != this.source){
			++k;
			shortest[k] = path[shortest[k - 1]];
		}
		int[] ret = Arrays.copyOf(shortest, k + 2);
		ret[k + 1] = this.source;
		return ret;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Source node[").append(this.source).append("]\n");
		for(int i = 0; i < dist.length; ++i){
			if(i != this.source){
				sb.append("\t--> Dest node [").append(i).append("]: Min Weight = ").append(dist[i]);
				
				int[] ret = getShortestPath(i);
				sb.append(", path[").append(ret.length - 1).append("]: ");
				for(int k = ret.length - 1; k >=0; --k){
					sb.append(ret[k]);
					if(k > 0){
						sb.append(" - ");
					}
				}
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}
	
}
