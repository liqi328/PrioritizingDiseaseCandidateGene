package util;

import graph.Graph;
import graph.ShortestPath;

import java.util.List;

public class ShortestPathUtil {
	
	public static String parseShortestPath(ShortestPath sp, Graph g){
		StringBuffer sb = new StringBuffer();
		//sb.append("Source Node[").append(g.getNodeName(sp.getSource())).append("]\n");
		for(int i = 0; i < g.getNodeNum(); ++i){
			sb.append(parseShortestPath(sp, g, i));
		}
		
		return sb.toString();
	}
	
	public static String parseShortestPath(ShortestPath sp, Graph g, List<String> seeds){
		StringBuffer sb = new StringBuffer();
		//sb.append("Source Node[").append(g.getNodeName(sp.getSource())).append("]\n");
		for(String seed : seeds){
			sb.append(parseShortestPath(sp, g, g.getNodeIndex(seed)));
		}
		
		return sb.toString();
	}
	
	
	public static String parseShortestPath(ShortestPath sp, Graph g, int dest){
		StringBuffer sb = new StringBuffer();
    	if(dest != sp.getSource()){
    		sb.append("Source node[").append(g.getNodeName(sp.getSource())).append("] ");
			sb.append("--> Dest node [").append(g.getNodeName(dest)).append("]: Min Weight = ").append(sp.getShortestPathWeight(dest));
			int[] ret = sp.getShortestPath(dest);
			sb.append(", path[").append(ret.length - 1).append("]: ");
			for(int k = ret.length - 1; k >=0; --k){
				sb.append(g.getNodeName(ret[k]));
				if(k > 0){
					sb.append(" - ");
				}
			}
			sb.append("\n");
		}
    	return sb.toString();
	}
}
