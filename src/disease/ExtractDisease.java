package disease;

import graph.Graph;
import id.HprdIdMapping;
import id.HprdIdMappingUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import reader.GraphReader;
import util.WriterUtil;

public class ExtractDisease {
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/TrustRanker/input/HPRD_ppi.txt";
		String diseaseFilePath = "E:/2013疾病研究/实验数据/TrustRanker/input/19disease/19disease.txt";
		Map<String, List<String>> diseaseMap = readDisease(diseaseFilePath);
		
		Map<String, HprdIdMapping> symbolIndexIdMappingMap = HprdIdMappingUtil.getSymbolIdIndexedIdMapping();
		Graph g = GraphReader.read(ppiFilename);
		
		Iterator<Entry<String, List<String>>> itr = diseaseMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		
		int totalGeneNum = 0;
		int notInPPIGeneNum = 0;
		
		int count = 0;
		
		StringBuffer sb = new StringBuffer();
		while(itr.hasNext()){
			count = 0;
			entry = itr.next();
			sb.append("\n");
			sb.append(entry.getKey()).append("\n");
			StringBuffer buf = new StringBuffer();
			for(String gene : entry.getValue()){
				totalGeneNum++;
				sb.append(gene).append("\t");
				HprdIdMapping idMapping = symbolIndexIdMappingMap.get(gene);
				if(idMapping != null){
					sb.append(idMapping.getHrpdId()).append("\t");
					if(g.containsNode(idMapping.getHrpdId())){
						sb.append("Yes\n");
						count++;
						
						buf.append(gene).append("\t").append(idMapping.getHrpdId()).append("\n");
					}else{
						sb.append("No\n");
						//System.out.println(gene);
						notInPPIGeneNum++;
					}
				}else{
					sb.append("-").append("\tNo\n");
					notInPPIGeneNum++;
					//System.out.println(gene);
				}
			}
			System.out.println(entry.getKey() + "\t" + count);
			WriterUtil.write("E:/2013疾病研究/实验数据/TrustRanker/input/19disease/" + entry.getKey() + ".txt",
					buf.toString());
		}
		System.out.println("19种疾病总基因数：" + totalGeneNum + ", 不在HPRD_PPI中的基因数：" + notInPPIGeneNum);
		System.out.println(sb.toString());
	}
	
	public static Map<String, List<String>> readDisease(String diseaseFilePath){
		Map<String, List<String>> diseaseMap = new LinkedHashMap<String, List<String>>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(diseaseFilePath)));
			String line = in.readLine();
			String[] cols = null;
			int i = 1;
			
			String diseaseName = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				if(!cols[0].equals("")){
					diseaseMap.put(cols[0], new ArrayList<String>());
					diseaseName = cols[0];
					//System.out.println("" + i + " : "+cols[0]);
					++i;
				}
				diseaseMap.get(diseaseName).add(cols[1].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return diseaseMap;
	}
}
