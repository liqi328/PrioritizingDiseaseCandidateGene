package experiment;

import graph.Graph;
import graph.GraphHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import util.GraphUtil;

public class ExperimentSD extends AbstractExperiment {
	public ExperimentSD(InputArgument input) {
		super(input);
	}
	
	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment SD running...");
		
		Set<Integer> extendedSet = extendSeedSet(g, diseaseGeneSeedSet);
		Iterator<Integer> itr = extendedSet.iterator();
		while(itr.hasNext()){
			System.out.println(itr.next());
		}
		
		System.out.println("Experiment SD finished.\n");
	}
	
	private Set<Integer> extendSeedSet(Graph g, Set<Integer> diseaseGeneSeedSet){
		Set<Integer> extendedSet = new HashSet<Integer>();
		Set<Integer> curGraphNodeSet = new HashSet<Integer>();
		curGraphNodeSet.addAll(diseaseGeneSeedSet);
		
		Graph newGraph = GraphHelper.createSubGraph(g, curGraphNodeSet);
		
		Set<Integer> neighborSet = null;
		
		while(!GraphHelper.isConnected(newGraph)){
			System.out.println("Is Graph connected: "+GraphHelper.isConnected(newGraph));
			System.out.println("Connected components: " + GraphHelper.countConnectedComponent(newGraph));
			System.out.println("extend Set: " + extendedSet.size());
			
			neighborSet = GraphUtil.getNeighbors(g, curGraphNodeSet);
			Integer nodeId = maxDegreeNode(neighborSet, g, curGraphNodeSet);
			curGraphNodeSet.add(nodeId);
			extendedSet.add(nodeId);
			
			newGraph = GraphHelper.createSubGraph(g, curGraphNodeSet);
		}
		
		return extendedSet;
	}
	
	private Integer maxDegreeNode(Set<Integer> neighborSet, Graph g, Set<Integer> curGraphNodeSet){
		Iterator<Integer> itr = neighborSet.iterator();
		int max = 0;
		int count = 0;
		Integer maxNodeId = null;
		double[][] matrix = g.getAdjMatrix();
		while(itr.hasNext()){
			Integer nodeId = itr.next();
			count = 0;
			for(int i = 0; i < matrix.length; ++i){
				if(matrix[nodeId][i] < Graph.INF && curGraphNodeSet.contains(i)){
					++count;
				}
			}
			if(count > max){
				max = count;
				maxNodeId = nodeId;
			}
		}
		return maxNodeId;
	}

}
