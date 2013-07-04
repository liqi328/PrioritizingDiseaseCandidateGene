package experiment;

import graph.Graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;

public class Main {
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt";
		Graph g = GraphReader.read(ppiFilename);
		
		Set<Integer> diseaseGeneSeedSet = getDiseaseGeneSeedSet(g);
		Set<Integer> candidateGeneSet = CandidateGeneGenerator.run(g, diseaseGeneSeedSet);
		
		Experiment[] exps = new Experiment[3];
		exps[0] = new ExperimentICN();
		exps[1] = new ExperimentVS();
		exps[2] = new ExperimentECC();
		
		for(Experiment exp: exps){
			exp.run(g, diseaseGeneSeedSet, candidateGeneSet);
		}
	}
	
	/**
	 * 将名称转换为图中结点的id
	 * @param g		PPI
	 * @return
	 */
	private static Set<Integer> getDiseaseGeneSeedSet(Graph g){
		String diseaseSeedFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_hprdid.txt";
		Set<String> diseaseGeneSeedSet = DiseaseGeneSeedReader.read(diseaseSeedFilename);
		
		return transform2GraphId(g, diseaseGeneSeedSet);
	}
	
	private static Set<Integer> transform2GraphId(Graph g, Set<String> diseaseGeneSeedSet){
		Set<Integer> result = new HashSet<Integer>();
		Iterator<String> itr = diseaseGeneSeedSet.iterator();
		while(itr.hasNext()){
			result.add(g.getNodeIndex(itr.next()));
		}
		return result;
	}
}
