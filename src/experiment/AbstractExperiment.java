package experiment;

import graph.Graph;

import java.util.Set;

public abstract class AbstractExperiment {
	protected InputArgument input;
	
	public AbstractExperiment(InputArgument input){
		this.input = input;
	}
	
	public abstract void run(Graph g, Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet);
}
