package graph;

import reader.GraphReader;

public class GraphAnalysis {
	public static void main(String[] args){
		String[] ppiFilenames = {
				//"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt",
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_symbol.txt",
		};
		
//		for(String name : ppiFilenames){
//			run(name);
//		}
		run(args[0]);
	}
	
	public static void run(String filename){
		Graph g = GraphReader.read(filename);
		StringBuffer sb = new StringBuffer();
		sb.append("----").append(filename).append("----\n");
		sb.append("Nodes num: " + g.getNodeNum() + ", Edges num: " + g.getEdgeNum()).append("\n");
		sb.append("Connected Components: " + GraphHelper.countConnectedComponent(g));
		
		System.out.println(sb.toString());
	}
}
