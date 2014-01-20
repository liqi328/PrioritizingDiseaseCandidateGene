package psn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

import util.WriterUtil;

public class HomoloGene {
	
	public static final String HomoSapiensTaxonomyID = "9606";				//人类的物种ID
	public static final String SaccharomycesCerevisiaeTaxonomyID = "4932";	//酵母的物种ID
	
	private static String homoloGeneFilename = "E:/2013疾病研究/疾病数据/HomoloGene/homologene.data";
	
	public static void main(String[] args){
		try {
			//exactHomoloGene();
			Map<Integer, List<HomoloGeneData>> homoGeneDataMap = readHomoloGene();
			processHomoloGeneData(homoGeneDataMap);
			
//			String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/orfCode_geneName.txt";
//			Map<String, String> orfGeneMap = readOrfCodeGeneName(filename);
//			System.out.println(orfGeneMap.size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> readOrfCodeGeneName(String filename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		Map<String, String> orfGeneMap = new HashMap<String, String>();
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			if(cols.length == 1){
				orfGeneMap.put(cols[0], "");
			}else{
				orfGeneMap.put(cols[0], cols[1]);
			}
		}
		in.close();
		return orfGeneMap;
	}
	
	
	/**
	 * 提取人类和酵母的同源基因
	 * @throws IOException 
	 */
	public static void exactHomoloGene() throws IOException{
		String outputFilename = "E:/2013疾病研究/疾病数据/HomoloGene/HomoSapiens_SaccharomycesCerevisiae.txt";
		
		BufferedReader in = new BufferedReader(new FileReader(homoloGeneFilename));
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
		
		int homoCount = 0, saccCount = 0;
		
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			
			if(HomoSapiensTaxonomyID.equals(cols[1])){
				out.write(line + "\n");
				homoCount++;
			}else if(SaccharomycesCerevisiaeTaxonomyID.equals(cols[1])){
				out.write(line + "\n");
				saccCount++;
			}
		}
		
		System.out.println("HomoSapiens:" + homoCount + "\t SaccharomycesCerevisiae:" + saccCount);
		in.close();
		out.close();
	}
	
	public static Map<Integer, List<HomoloGeneData>> readHomoloGene() throws IOException{
		String outputFilename = "E:/2013疾病研究/疾病数据/HomoloGene/HomoSapiens_SaccharomycesCerevisiae.txt";
		
		BufferedReader in = new BufferedReader(new FileReader(homoloGeneFilename));
		//BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
		
		Map<Integer, List<HomoloGeneData>> homoGeneDataMap = new TreeMap<Integer, List<HomoloGeneData>>();
		
		String line = null;
		String[] cols = null;
		HomoloGeneData gene = null;
		List<HomoloGeneData> geneList = null;
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			
			if(HomoSapiensTaxonomyID.equals(cols[1]) || SaccharomycesCerevisiaeTaxonomyID.equals(cols[1])){
				gene = new HomoloGeneData(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5]);
				geneList = homoGeneDataMap.get(Integer.parseInt(cols[0]));
				
				if(geneList == null){
					geneList = new ArrayList<HomoloGeneData>();
					homoGeneDataMap.put(Integer.parseInt(cols[0]), geneList);
				}
				geneList.add(gene);
			}
		}
		in.close();
		//out.close();
		
		return homoGeneDataMap;
	}
	
	public static void processHomoloGeneData(Map<Integer, List<HomoloGeneData>> homoGeneDataMap){
		
		Map<Integer, List<HomoloGeneData>> outMap = new TreeMap<Integer, List<HomoloGeneData>>();
		
		
		
		Set<String> homoGeneSet = new HashSet<String>();
		Set<String> saccGeneSet = new HashSet<String>();
		
		Set<String> tmpHomoGeneSet = new HashSet<String>();
		Set<String> tmpSaccGeneSet = new HashSet<String>();
		
		Iterator<Entry<Integer, List<HomoloGeneData>>> itr = homoGeneDataMap.entrySet().iterator();
		Entry<Integer, List<HomoloGeneData>> entry = null;
		int homoCount = 0, saccCount = 0;
		
		while(itr.hasNext()){
			homoCount = 0;
			saccCount = 0;
			tmpHomoGeneSet.clear();
			tmpSaccGeneSet.clear();
			
			entry = itr.next();
			for(HomoloGeneData gene: entry.getValue()){
				if(SaccharomycesCerevisiaeTaxonomyID.equals(gene.taxonomyID)){
					tmpSaccGeneSet.add(gene.geneSymbol);
					++saccCount;
				}else if(HomoSapiensTaxonomyID.equals(gene.taxonomyID)){
					tmpHomoGeneSet.add(gene.geneSymbol);
					homoCount++;
				}
			}
			
			if(saccCount > 0 && homoCount > 0){
				outMap.put(entry.getKey(), entry.getValue());
				if(saccCount > 0 && homoCount > 1)
					System.out.println(entry.getKey()+ "\t" + saccCount + "\t" + homoCount);
				//System.out.println(entry.getKey() + "\t" +  entry.getValue().size());
				saccGeneSet.addAll(tmpSaccGeneSet);
				homoGeneSet.addAll(tmpHomoGeneSet);
			}
		}
		
		System.out.println("Group size = " + outMap.size());
		System.out.println("SaccharomycesCerevisiae = " + saccGeneSet.size());
		System.out.println("HomoSapiens size = " + homoGeneSet.size());
		
		printHomoloGeneData(outMap);
	}
	
	public static void printHomoloGeneData(Map<Integer, List<HomoloGeneData>> homoGeneDataMap){
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<Integer, List<HomoloGeneData>>> itr = homoGeneDataMap.entrySet().iterator();
		Entry<Integer, List<HomoloGeneData>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			for(HomoloGeneData gene: entry.getValue()){
				sb.append(gene);
			}
		}
		
		WriterUtil.write("E:/2013疾病研究/疾病数据/HomoloGene/HomoSapiens_SaccharomycesCerevisiae2.txt", sb.toString());
	}
	
	
	static class HomoloGeneData{
		public String groupId;
		public String taxonomyID;
		public String geneID;
		public String geneSymbol;
		public String proteinGi;
		public String proteinAccession;
		
		public HomoloGeneData(String groupId, String taxonomyID, 
				String geneID, String geneSymbol,
				String proteinGi, String proteinAccession){
			this.groupId = groupId;
			this.taxonomyID = taxonomyID;
			this.geneID = geneID;
			this.geneSymbol = geneSymbol;
			this.proteinGi = proteinGi;
			this.proteinAccession = proteinAccession;
		}
		
		public String toString(){
			return groupId + "\t" + taxonomyID + "\t" + geneID + 
					"\t" + geneSymbol + "\t" + proteinGi + "\t" + proteinAccession + "\n";
		}
	}

}
