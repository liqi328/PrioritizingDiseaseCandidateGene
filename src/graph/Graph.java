package graph;

public interface Graph {
	public static final int INF = Integer.MAX_VALUE;
	
	public double[][] getAdjMatrix();
	public double[][] getAdjMatrix(boolean withWeighted);
	public int getNodeNum();
	public int getEdgeNum();
	
	public String getNodeName(int index);
	public Integer getNodeIndex(String name);
	
	public void addNode(String node);
	public void addEdge(String from, String to);
	public void addEdge(String from, String to, double w);
	
	public boolean containsEdge(String a, String b);
	public boolean containsNode(String node);
}
