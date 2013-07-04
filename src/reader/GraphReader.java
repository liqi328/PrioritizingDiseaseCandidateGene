package reader;

import graph.AdjGraph;
import graph.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphReader {
	
	public static Graph read(String filename){
		Graph g = new AdjGraph();
		try {
			readNodes(filename, g);
			readEdges(filename, g);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}
	
	private static void readNodes(String filename, Graph g) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split(" |\t|,");
			g.addNode(cols[0]);
			g.addNode(cols[1]);
		}
		
		in.close();
	}
	
	private static void readEdges(String filename, Graph g) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split(" |\t|,");
			if(cols.length > 2){
				g.addEdge(cols[0], cols[1], Double.parseDouble(cols[2]));
			}else{
				g.addEdge(cols[0], cols[1], 1);
			}
			//g.addEdge(cols[0], cols[1], Double.parseDouble(cols[2]));
		}
		
		in.close();
	}

}
