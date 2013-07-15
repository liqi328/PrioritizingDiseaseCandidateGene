package experiment;

import graph.Graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;
import util.GraphUtil;
import util.WriterUtil;


class InputArgument{
	private Properties p = new Properties();
	
	public InputArgument(String configFilepath){
		try {
			//FileInputStream is = new FileInputStream("./input/config.txt");
			//InputStreamReader is = new InputStreamReader(new FileInputStream("./input/config.txt"), "UTF-8");
			InputStreamReader is = new InputStreamReader(new FileInputStream(configFilepath), "UTF-8");
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
			outputDir.mkdirs();
		}
		return p.getProperty("outputDir");
	}
	
	/**
	 * 归一化, a_threshhold参数
	 * @return
	 */
	public String[] getAthreshholdArray(){
		//System.out.println("a_threshhold_array = " + Arrays.toString(p.getProperty("a_threshhold_array").split(",")));
		return p.getProperty("a_threshhold_array").split(",");
	}
}

public class Main {
	public static void main(String[] args){
		//String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt";
		//String diseaseSeedFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_hprdid.txt";
		
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar Prioritizing.jar ./input/config.txt");
			System.exit(-1);
		}
		
		InputArgument input = new InputArgument(args[0]);
		String ppiFilename = input.getPpiFilename();
		String diseaseSeedFilename = input.getDiseaseSeedFilename();
		
		Graph g = GraphReader.read(ppiFilename);
		Set<Integer> diseaseGeneSeedSet = GraphUtil.transformName2GraphNodeIndex(g, 
				DiseaseGeneSeedReader.read(diseaseSeedFilename));
		Set<Integer> candidateGeneSet = CandidateGeneGenerator.run(g, diseaseGeneSeedSet);
		
//		WriterUtil.write(input.getOutputDir() + "random_candidate_gene.txt",
//				GraphUtil.transformGraphNodeIndex2Name(g, candidateGeneSet)); 
		
		run_single_method(input, g, diseaseGeneSeedSet, candidateGeneSet);
		
		run_combined_method(input, g, diseaseGeneSeedSet, candidateGeneSet);
	}
	
	public static void run_single_method(InputArgument input, Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet){
		List<AbstractExperiment> expList = new ArrayList<AbstractExperiment>();
		expList.add(new ExperimentICN(input));
		expList.add(new ExperimentECC(input));
		expList.add(new ExperimentGO(input));
		expList.add(new ExperimentSP(input));
		expList.add(new ExperimentVS(input));
		
		for(AbstractExperiment exp: expList){
			exp.run(g, diseaseGeneSeedSet, candidateGeneSet);
		}
	}
	
	public static void run_combined_method(InputArgument input, Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet){
		List<AbstractExperiment> expList = new ArrayList<AbstractExperiment>();
		expList.add(new ExperimentSP_GO(input));
		expList.add(new ExperimentVS_GO(input));
		expList.add(new ExperimentSP_Neighbor(input));
		
		for(AbstractExperiment exp: expList){
			exp.run(g, diseaseGeneSeedSet, candidateGeneSet);
		}
	}
}
