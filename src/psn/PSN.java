package psn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import psn.HomoloGene.HomoloGeneData;
import util.WriterUtil;

public class PSN {
	public static double P_VALUE_THRESHOLD = 0.01;
	
	public static void main(String[] args){
		try {
			transferSaccPSN2HumanPSN();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将酵母的PSN转换成人类的PSN
	 * @throws IOException 
	 */
	public static void transferSaccPSN2HumanPSN() throws IOException{
		Map<String, List<String>> sacc2humanGeneMap = readSacc2HumanGeneRelation();
		String inputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ DataReader.ErrorModel + "_pvalue_processed_final.txt";
		List<MutantPValueData> mutantDataList = new ArrayList<MutantPValueData>();
		
		String[] header = DataReader.readMutantPValueData(inputFilename,mutantDataList);
		
		Map<String, String> experimentListMap = DataReader.readExperimentList();
		
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/orfCode_geneName.txt";
		Map<String, String> orfGeneMap = HomoloGene.readOrfCodeGeneName(filename);
		
		Set<Integer> containedHeaderSet = new HashSet<Integer>();
		StringBuffer sb = new StringBuffer();
		sb.append("gene_name").append("\t").append("gene_name").append("\t");
		
		for(int i = 2; i< header.length; ++i){
			for(String key: experimentListMap.keySet()){
				if(header[i].contains(key)){
					String geneName = orfGeneMap.get(experimentListMap.get(key).split(",")[0].toUpperCase());
					
					if( !(geneName == null || "".equals(geneName))){
						if(sacc2humanGeneMap.get(geneName) != null){
							containedHeaderSet.add(i - 2);
							if(sacc2humanGeneMap.get(geneName).size() > 1){
								System.out.println(sacc2humanGeneMap.get(geneName));
							}
							sb.append(sacc2humanGeneMap.get(geneName).get(0)).append("\t");
							
						}
					}
					break;
				}
			}
		}
		sb.append("\n");
		System.out.println(containedHeaderSet.size());
		System.out.println(sb.toString());
		int count = 0;
		for(MutantPValueData pValueData: mutantDataList){
			String geneName = orfGeneMap.get(pValueData.orfCode.toUpperCase());
			if(sacc2humanGeneMap.get(geneName) != null){
				String str = "";
				for(int i = 2; i < header.length; ++i){
					if(containedHeaderSet.contains(i - 2)){
						str += pValueData.pValueList.get(i - 2) + "\t";
					}
				}
				
				for(String name: sacc2humanGeneMap.get(geneName)){
					sb.append(name).append("\t").append(name).append("\t").append(str).append("\n");
					
					if(sacc2humanGeneMap.get(geneName).size() > 1){
						System.out.println(name + "\t" + str + "\n");
					}
				}
				
			}else{
				++count;
			}
		}
		
		System.out.println(count);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/humanPSN.txt",
				sb.toString());
	}
	
	public static Map<String, List<String>> readSacc2HumanGeneRelation() throws IOException{
		String inputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/HomoSapiens_SaccharomycesCerevisiae_homologene.txt";
		
		BufferedReader in = new BufferedReader(new FileReader(inputFilename));
		
		Map<Integer, Map<String, List<HomoloGeneData>>> homoGeneDataMap = new TreeMap<Integer, Map<String, List<HomoloGeneData>>>();
		
		String line = null;
		String[] cols = null;
		HomoloGeneData gene = null;
		Map<String, List<HomoloGeneData>> homoGeneMap = null;
		List<HomoloGeneData> geneList = null;
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			gene = new HomoloGeneData(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5]);
			
			homoGeneMap = homoGeneDataMap.get(Integer.parseInt(cols[0]));
			
			if(homoGeneMap == null){
				homoGeneMap = new HashMap<String, List<HomoloGeneData>>();
				homoGeneDataMap.put(Integer.parseInt(cols[0]), homoGeneMap);
			}
			
			geneList = homoGeneMap.get(cols[1]);
			if(geneList == null){
				geneList = new ArrayList<HomoloGeneData>();
				homoGeneMap.put(cols[1], geneList);
			}
			geneList.add(gene);
		}
		in.close();
		
		/* String:酵母基因
		 * List<String>: 人类基因List
		 * */
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		
		for(Map<String, List<HomoloGeneData>> map : homoGeneDataMap.values()){
			List<HomoloGeneData> saccGeneList = map.get(HomoloGene.SaccharomycesCerevisiaeTaxonomyID);
			List<HomoloGeneData> homoGeneList = map.get(HomoloGene.HomoSapiensTaxonomyID);
			
			for(HomoloGeneData saccGene: saccGeneList){
				List<String> list = resultMap.get(saccGene.geneSymbol);
				if(list == null){
					list = new ArrayList<String>();
					resultMap.put(saccGene.geneSymbol, list);
				}else{
					System.out.println(saccGene.geneSymbol);
				}
				for(HomoloGeneData homoGene: homoGeneList){
					list.add(homoGene.geneSymbol);
				}
			}
		}
		
		//writeResultMap(resultMap);
		
		return resultMap;
	}
	
	private static void writeResultMap(Map<String, List<String>> resultMap){
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, List<String>>> itr = resultMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			for(String name: entry.getValue()){
				sb.append(entry.getKey()).append("\t");
				sb.append(name).append("\n");
			}
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/HomoSapiens_SaccharomycesCerevisiae_relation.txt",
				sb.toString());
	}
	
}
