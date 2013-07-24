package test;

import graph.Graph;
import graph.GraphHelper;
import graph.ShortestPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;

import util.ShortestPathUtil;

import alg.DijkstraAlgorithm;



public class TestMain {
	private static final int INF = Graph.INF;   
	
	public static void main(String[] args){
		//test_dijkstra();
		test_dijkstra_2();
		//test_dijkstra(getGraphMatrixFromConsole());
	}
	
	public static Graph getGraphFromFile(String filename){
		//AdjGraph g = new AdjGraph();
		//g.readGraph(filename);
		Graph g = GraphReader.read(filename);
		System.out.println("Nodes: " + g.getNodeNum() + ", Edges: " + g.getEdgeNum());
		return g;
	}
	
	public static double[][] getGraphMatrixFromConsole(){
		double[][] matrix = {   
				{0,10,INF,30,100},
                {INF,0,50,INF,INF},
                {INF,INF,0,INF,10},
                {INF,INF,20,0,60},
                {INF,INF,INF,INF,0}  
        };  
		return matrix;
	}
	
	public static void test_dijkstra(double[][] matrix){
		System.out.println("\n----------test dijkstra alg--------");
		int start = 0;
		
        ShortestPath sp = DijkstraAlgorithm.dijsktra(start, matrix);
        System.out.println(sp.toString());
	}
	
	public static void test_dijkstra_2(){
		System.out.println("\n----------test dijkstra alg--------");
		
		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/test.txt";
		Graph g = getGraphFromFile(filename);
		
		System.out.println("Is Graph connected: "+GraphHelper.isConnected(g));
		System.out.println("Connected components: " + GraphHelper.countConnectedComponent(g));
		
		int start = g.getNodeIndex("0");
		ShortestPath sp = DijkstraAlgorithm.dijsktra(start, g.getAdjMatrix());
        //System.out.println(sp.toString());
        System.out.println(ShortestPathUtil.parseShortestPath(sp, g));
	}
	
	public static void test_dijkstra(){
		System.out.println("\n----------test dijkstra alg--------");
		
		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/tissue_specific_PPI.txt";
		Graph g = getGraphFromFile(filename);
		int start = g.getNodeIndex("1967");
		
		ShortestPath sp = DijkstraAlgorithm.dijsktra(start, g.getAdjMatrix());
		
		//List<String> seeds = DiseaseGeneSeedReader.read("E:/2013¼²²¡ÑÐ¾¿/À¼Î°/fanconi_anemia.txt");
		Set<String> seeds = DiseaseGeneSeedReader.read("E:/2013¼²²¡ÑÐ¾¿/À¼Î°/fanconi_anemia.txt");
//        Set<String> seeds = new HashSet<String>();
//        seeds.add("1967");
//        seeds.add("1968");
//        seeds.add("2943");
//        seeds.add("4143");
//        seeds.add("4262");
//        seeds.add("4589");
//        seeds.add("5797");
//        seeds.add("6186");
//        seeds.add("6557");
//        seeds.add("6997");
//        seeds.add("7966");
        
        for(String d: seeds){
        	System.out.print(ShortestPathUtil.parseShortestPath(sp, g, g.getNodeIndex(d)));
        }
	}
    
//    private static String getPath(ShortestPath sp, Graph g){
//    	StringBuffer sb = new StringBuffer();
//		sb.append("Source Node[").append(g.getNodeName(sp.getSource())).append("]\n");
//		for(int i = 0; i < g.getNodeNum(); ++i){
//			if(i != sp.getSource()){
//				sb.append("\t to [").append(g.getNodeName(i)).append("]: Weight = ").append(sp.getShortestPathWeight(i));
//				sb.append(", path: ");
//				int[] ret = sp.getShortestPath(i);
//				for(int k = ret.length - 1; k >=0; --k){
//					sb.append(g.getNodeName(ret[k]));
//					if(k > 0){
//						sb.append(" - ");
//					}
//				}
//				sb.append("\n");
//			}
//		}
//		
//		return sb.toString();
//    }
//    
//    private static String getPath(ShortestPath sp, Graph g, int dest){
//    	StringBuffer sb = new StringBuffer();
//    	sb.append("Source Node[").append(g.getNodeName(sp.getSource())).append("]\n");
//    	if(dest != sp.getSource()){
//			sb.append("\t to [").append(g.getNodeName(dest)).append("]: Weight = ").append(sp.getShortestPathWeight(dest));
//			sb.append("\t, path: ");
//			int[] ret = sp.getShortestPath(dest);
//			for(int k = ret.length - 1; k >=0; --k){
//				sb.append(g.getNodeName(ret[k]));
//				if(k > 0){
//					sb.append(" - ");
//				}
//			}
//			sb.append("\n");
//		}
//    	return sb.toString();
//    }
//    
//	public static void test_folyd(){
//		//String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/tissue_specific_PPI.txt";
//		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/test.txt";
//		AdjGraph g = new AdjGraph();
//		g.readGraph(filename);
//		
//		System.out.println("Nodes: " + g.getNodeNum() + ", edges: " + g.getEdgeNum());
//		
//		FloydAlgorithm alg = new FloydAlgorithm(g);  
//		int begin = g.getNodeIndex("0");   
//        int end = g.getNodeIndex("5");   
//          
//        List<Integer> path = alg.findCheapestPath(begin,end, g.getAdjMatrix()); 
//        
//        System.out.println(g.getNodeName(begin) + " to " + g.getNodeName(end) + ",the cheapest path is:");   
//        System.out.println(getPath(path, g));   
//        System.out.println(alg.dist[begin][end]);  
//        
//	}
//	
//	
//	
//    private static List<String> getPath(List<Integer> result, AdjGraph g){
//    	List<String> path = new ArrayList<String>();
//    	Iterator<Integer> itr = result.iterator();
//    	
//    	while(itr.hasNext()){
//    		path.add(g.getNodeName(itr.next()));
//    	}
//    	
//    	return path;
//    }

}
