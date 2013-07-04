package util;

import graph.Graph;
import graph.Path;


public class PathUtil {
	public static String parsePath(Path<Integer> path, Graph g){
		StringBuffer sb = new StringBuffer();
		for(Integer p : path){
			sb.append(g.getNodeName(p)).append(" - ");
		}
		return sb.toString();
	}
}
