package test;

import graph.Graph;
import graph.Path;

import java.util.List;

import util.PathUtil;

import alg.ShortestPathAlgorithm;

public class TestShortPathAlgorithm {
	public static void main(String[] args){
		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/tissue_specific_PPI.txt";
		Graph g = TestMain.getGraphFromFile(filename);
		int start = g.getNodeIndex("1968");
		int end = g.getNodeIndex("1967");
		List<Path<Integer>> shortestPathes = ShortestPathAlgorithm.calculateShortestPath(start, end, g.getAdjMatrix());
		
		for(Path<Integer> p : shortestPathes){
			System.out.println(PathUtil.parsePath(p, g));
		}
	}
}
