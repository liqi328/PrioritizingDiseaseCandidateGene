package test;

import graph.Graph;
import id.EntrezIDConvertor;
import id.IDConvertor;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import reader.GraphReader;
import reader.OrphanetDiseaseGeneReaderAndWriter;
import util.GraphUtil;
import util.WriterUtil;

public class TestIDConvertor {
	public static void main(String[] args){
		//test_2();
		generateConfigFiles();
		//isAllOrphanetDiseaseGeneInThePPI();
	}
	
	public static void test_1(){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/172_ODs.txt";
		String outputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/";
		
		Map<String, List<String>> orphanetDiseaseGeneMap = OrphanetDiseaseGeneReaderAndWriter.read(filename);
		
		OrphanetDiseaseGeneReaderAndWriter.write(outputDir, orphanetDiseaseGeneMap);
	}
	
	public static void test_2(){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/172_ODs.txt";
		String outputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/";
		String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt";
		Graph g = GraphReader.read(ppiFilename);
		
		Map<String, List<String>> orphanetDiseaseGeneMap = OrphanetDiseaseGeneReaderAndWriter.read(filename);
		
		Iterator<Entry<String, List<String>>> itr = orphanetDiseaseGeneMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		
		IDConvertor entrezIdConvertor = new EntrezIDConvertor(); 
		
		StringBuffer sb6 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		StringBuffer tmpSb = null;
		int i = 0;
		int j = 0;
		int tmp = 0;
		File dir = null;
		while(itr.hasNext()){
			entry = itr.next();
			dir = new File(outputDir + entry.getKey());
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			Set<String> entrezIdSet = new HashSet<String>();
			entrezIdSet.addAll(entry.getValue());
			
			Set<String> hprdIdSet = entrezIdConvertor.converte2hprdId(entrezIdSet);
			
			
			WriterUtil.write(dir.getAbsolutePath() + 
					"/" + entry.getKey() + "_hprd_id.txt", hprdIdSet);
			Set<Integer> nodeSet = GraphUtil.transformName2GraphNodeIndex(g, hprdIdSet);
			if(nodeSet.size() < 5){
				tmpSb = sb3;
				tmp = ++i;
				
			}else{
				tmpSb = sb6;
				tmp = ++j;
			}
			
			tmpSb.append(""+ (tmp)+ ": " + entry.getKey()).append("\n");
			tmpSb.append("\t-->entrez_id size: " + entrezIdSet.size()).append("\n");
			tmpSb.append("\t-->hprd_id size: " + hprdIdSet.size()).append("\n");
			
			tmpSb.append("\t-->appear in the ppi_hprd: " + nodeSet.size()).append("\n");
			tmpSb.append("---------------------------------------").append("\n");
			
			WriterUtil.write(dir.getAbsolutePath() + 
					"/" + entry.getKey() + "_omim_id.txt", 
					entrezIdConvertor.converte2omimId(entrezIdSet));
			
			WriterUtil.write(dir.getAbsolutePath() + 
					"/" + entry.getKey() + "_id_mapping.txt", 
					entrezIdConvertor.getHprdIdMappingSet(entrezIdSet));
			
		}
		
		System.out.println(sb6.toString());
		System.out.println(sb3.toString());
	}
	
	private static void generateConfigFiles(){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/172_ODs.txt";
		String outputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd/";
		
		Map<String, List<String>> orphanetDiseaseGeneMap = OrphanetDiseaseGeneReaderAndWriter.read(filename);
		
		Iterator<Entry<String, List<String>>> itr = orphanetDiseaseGeneMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		File dir = null;
		Set<String> configFilenames = new HashSet<String>();
		
		String diseaseName = null;
		while(itr.hasNext()){
			entry = itr.next();
			diseaseName = entry.getKey();
//			if(diseaseName.startsWith("Hypothyroidism due to deficient")){
//				diseaseName = "Hypothyroidism due to deficient";
//			}
			dir = new File(outputDir + diseaseName);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			configFilenames.add(generateConfigFiles(diseaseName, dir));
		}
		
		WriterUtil.write(outputDir + "all_config.txt", configFilenames);
	}
	
	private static String generateConfigFiles(String diseaseName, File dir){
		String ppiFilename = "./input_hprd/ppi_hprd_id.txt";
		String diseaseSeedFilename = "./input_hprd/"+ diseaseName + "/" + diseaseName +"_hprd_id.txt";
		String outputDir = "./output_hprd/"+ diseaseName + "_output/";
		String hprd_id_mappings = "./input_hprd/HPRD_ID_MAPPINGS.txt";
		String a_threshhold_array = "0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1";
		
		StringBuffer sb = new StringBuffer();
		sb.append("ppiFilename = ").append(ppiFilename).append("\n");
		sb.append("diseaseSeedFilename = ").append(diseaseSeedFilename).append("\n");
		sb.append("outputDir = ").append(outputDir).append("\n");
		sb.append("hprd_id_mappings = ").append(hprd_id_mappings).append("\n");
		sb.append("a_threshhold_array = ").append(a_threshhold_array).append("\n");
		
		WriterUtil.write(dir.getAbsolutePath() + "/" + diseaseName + "_config.txt", sb.toString());
		
		return  "./input_hprd/" + diseaseName + "/" + diseaseName + "_config.txt";
	}
	
	private static void isAllOrphanetDiseaseGeneInThePPI(){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_symbol/172_ODs.txt";
		String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_symbol.txt";
		Graph g = GraphReader.read(ppiFilename);
		
		System.out.println("read ppi done.");
		Map<String, List<String>> orphanetDiseaseGeneMap = OrphanetDiseaseGeneReaderAndWriter.read(filename);
		
		int allDiseaseGene = 0;
		int inThePPIGene = 0;
		Iterator<Entry<String, List<String>>> itr = orphanetDiseaseGeneMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			Set<String> symbolSet = new HashSet<String>();
			symbolSet.addAll(entry.getValue());
			
			Set<Integer> nodeSet = GraphUtil.transformName2GraphNodeIndex(g, symbolSet);
			if(symbolSet.size() != nodeSet.size()){
				System.out.println("-->" + entry.getKey());
				System.out.println("-->" + symbolSet.size() + "\t" + nodeSet.size());
			}
			allDiseaseGene += symbolSet.size();
			inThePPIGene += nodeSet.size();
		}
		System.out.println("all done.");
		System.out.println("all Gene = " + allDiseaseGene);
		System.out.println("ppi Gene = " + inThePPIGene);
	}
}
