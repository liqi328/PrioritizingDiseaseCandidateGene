package graph;

import java.util.HashMap;
import java.util.Map;

public class AdjGraph implements Graph {
	private Map<String, Integer> nodes = new HashMap<String, Integer>();
	private Map<Integer, String> idIndexNodes = new HashMap<Integer, String>();
	
	private double[][] matrix;	//邻接矩阵
	
	private int nodeIndex = 0;
	
	public double[][] getAdjMatrix(){
		return matrix;
	}
	
	/**
	 * 返回图的邻接矩阵，若withWeighed = false, 则将边的权值置为1
	 * @param withWeighed	是否需要权值
	 * @return
	 */
	public double[][] getAdjMatrix(boolean withWeighed){
		if(withWeighed){
			return matrix;
		}
		int len = matrix.length;
		double[][] matrix_ret = new double[len][len];
		for(int i = 0; i < len; ++i){
			for(int j = 0; j < len; ++j){
				matrix_ret[i][j] = matrix[i][j] < INF ? 1 : INF;
			}
		}
		return matrix_ret;
	}
	
	public int getNodeNum(){
		return nodes.size();
	}
	
	public int getEdgeNum(){
		if(matrix == null){
			return 0;
		}
		
		int len = nodes.size();
		int edgeNum = 0;
		
		for(int i = 0; i < len; ++i){
			for(int j = 0; j < len; ++j){
				if(matrix[i][j] < INF){
					++edgeNum;
				}
			}
		}
		return edgeNum >> 1; 
	}
	
	public void addNode(String node){
		if(!nodes.containsKey(node)){
			nodes.put(node, nodeIndex);
			idIndexNodes.put(nodeIndex, node);
			++nodeIndex;
		}
	}
	public void addEdge(String from, String to){
		this.addEdge(from, to, 1.0);
	}
	
	public void addEdge(String from, String to, double w){
		allocMemory();
		
		int fromId = nodes.get(from);
		int toId = nodes.get(to);

		matrix[fromId][toId] = w;
		matrix[toId][fromId] = w;
	}
	
	public boolean containsEdge(String a, String b){
		if(matrix[nodes.get(a)][nodes.get(b)] < INF){
			return true;
		}
		return false;
	}
	
	@Override
	public String getNodeName(int index) {
		return idIndexNodes.get(index);
		
	}

	@Override
	public int getNodeIndex(String name) {
		return nodes.get(name);
	}
	
	private void allocMemory(){
		if(matrix == null){
			int len = nodes.size();
			matrix = new double[len][len];
			
			for(int i = 0; i < len; ++i){
				for(int j = 0; j < len; ++j){
					matrix[i][j] = INF;
				}
			}
		}
	}
	
//	
//	public void readGraph(String filename){
//		try {
//			readNodes(filename);
//			allocMemory();
//			readEdges(filename);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void readNodes(String filename) throws IOException{
//		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
//		String line = null;
//		String[] cols = null;
//		int id = 0;
//		while((line = in.readLine()) != null){
//			cols = line.split(" |\t");
//			if(!nodes.containsKey(cols[0])){
//				nodes.put(cols[0], id);
//				idIndexNodes.put(id, cols[0]);
//				++id;
//			}
//			if(!nodes.containsKey(cols[1])){
//				nodes.put(cols[1], id);
//				idIndexNodes.put(id, cols[1]);
//				++id;
//			}
//		}
//		
//		in.close();
//	}
//	
//	private void readEdges(String filename) throws IOException{
//		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
//		String line = null;
//		String[] cols = null;
//		int idOne = 0;
//		int idTwo = 0;
//		double w = 0;
//		while((line = in.readLine()) != null){
//			cols = line.split(" |\t");
//			idOne = nodes.get(cols[0]);
//			idTwo = nodes.get(cols[1]);
////			if(idOne == idTwo){
////				System.out.println(cols[0]);
////			}
//			w = Double.parseDouble(cols[2]);
//			matrix[idOne][idTwo] = w;
//			matrix[idTwo][idOne] = w;
//		}
//		
//		in.close();
//	}

	
}
