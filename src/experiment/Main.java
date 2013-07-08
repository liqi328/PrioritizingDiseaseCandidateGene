package experiment;

import graph.Graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;
import util.GraphUtil;
import util.WriterUtil;


class InputArgument{
	private Properties p = new Properties();
	
	public InputArgument(){
		try {
			//FileInputStream is = new FileInputStream("./input/config.txt");
			InputStreamReader is = new InputStreamReader(new FileInputStream("./input/config.txt"), "UTF-8");
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPpiFilename(){
		return p.getProperty("ppiFilename");
	}
	
	public String getDiseaseSeedFilename(){
		return p.getProperty("diseaseSeedFilename");
	}
	
	public String getHprdIdMappingsFileName(){
		return p.getProperty("hprd_id_mappings");
	}
	
	public String getOutputDir(){
		File outputDir = new File(p.getProperty("outputDir"));
		if(!outputDir.exists()){
			outputDir.mkdir();
		}
		return p.getProperty("outputDir");
	}
}

public class Main {
	public static void main(String[] args){
		//String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt";
		//String diseaseSeedFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_hprdid.txt";
		
		InputArgument input = new InputArgument();
		String ppiFilename = input.getPpiFilename();
		String diseaseSeedFilename = input.getDiseaseSeedFilename();
		
		Graph g = GraphReader.read(ppiFilename);
		Set<Integer> diseaseGeneSeedSet = GraphUtil.transformName2GraphNodeIndex(g, 
				DiseaseGeneSeedReader.read(diseaseSeedFilename));
		Set<Integer> candidateGeneSet = CandidateGeneGenerator.run(g, diseaseGeneSeedSet);
		
		WriterUtil.write(input.getOutputDir() + "debug.txt",
				print_set(GraphUtil.transformGraphNodeIndex2Name(g, diseaseGeneSeedSet)) + 
				"candidate_gene\n" +
				print_set(GraphUtil.transformGraphNodeIndex2Name(g, candidateGeneSet)));
		
		List<AbstractExperiment> expList = new ArrayList<AbstractExperiment>();
		expList.add(new ExperimentICN(input));
		expList.add(new ExperimentECC(input));
		expList.add(new ExperimentSP(input));
		expList.add(new ExperimentVS(input));
		expList.add(new ExperimentGO(input));
		expList.add(new ExperimentSP_GO(input));
		expList.add(new ExperimentVS_GO(input));
		
		for(AbstractExperiment exp: expList){
			exp.run(g, diseaseGeneSeedSet, candidateGeneSet);
		}
	}
	
	private static String print_set(Set<?> set){
		StringBuffer sb = new StringBuffer();
		Iterator<?> itr = set.iterator();
		while(itr.hasNext()){
			sb.append(itr.next()).append("\n");
		}
		return sb.toString();
	}
}
