package experiment;

import graph.Graph;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import util.WriterUtil;

public abstract class AbstractExperiment {
	protected InputArgument input;
	
	public AbstractExperiment(InputArgument input){
		this.input = input;
	}
	
	protected void writeRankList(Graph g, String filename, List<Rank> rankList){
		StringBuffer sb = new StringBuffer();
		
		Iterator<Rank> itr = rankList.iterator();
		while(itr.hasNext()){
			Rank rank = itr.next();
			sb.append(g.getNodeName(rank.getId())).append("\t");
			//sb.append(rank.getRank()).append("\t");
			sb.append(rank.getScore()).append("\n");
		}
		
		WriterUtil.write(filename, sb.toString());
	}
	
	public abstract void run(Graph g, Set<Integer> diseaseGeneSeed, Set<Integer> candidateGeneSet);
	
	public abstract void ranking(Graph g, Set<Integer> diseaseGeneSet, 
			Set<Integer> candidateGeneSet);
}
