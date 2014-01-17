package experiment;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;
import util.GraphUtil;
import util.WriterUtil;

public class RankingMain {
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar Prioritizing.jar ./input/config.txt");
			System.exit(-1);
		}
		
		InputArgument input = new InputArgument(args[0]);
		String ppiFilename = input.getPpiFilename();
		String diseaseSeedFilename = input.getDiseaseSeedFilename();
		
		Graph g = GraphReader.read(ppiFilename);
		Set<Integer> diseaseGeneSet = GraphUtil.transformName2GraphNodeIndex(g, 
				DiseaseGeneSeedReader.read(diseaseSeedFilename));
		
		Set<Integer> candidateGeneSet = CandidateGeneGenerator.run_neighbor(g, diseaseGeneSet);
		
		WriterUtil.write(input.getOutputDir() + "candidate_gene.txt",
				GraphUtil.transformGraphNodeIndex2Name(g, candidateGeneSet)); 
		
		List<AbstractExperiment> expList = new ArrayList<AbstractExperiment>();
		expList.add(new ExperimentICN(input));
		expList.add(new ExperimentVS(input));
		expList.add(new ExperimentSP(input));
		expList.add(new ExperimentGO(input));
		
		expList.add(new ExperimentSP_GO(input));
		expList.add(new ExperimentVS_GO(input));
		
		for(AbstractExperiment exp: expList){
			exp.ranking(g, diseaseGeneSet, candidateGeneSet);
		}
	}
}
