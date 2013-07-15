package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.WriterUtil;

/**
 * Orphanet Disease Gene association Reader
 * @author Liqi
 *
 */
public class OrphanetDiseaseGeneReaderAndWriter {
	public static Map<String, List<String>> read(String filename){
		/* String = 疾病名称, List<String> = 此疾病的所有基因entrez_id */
		Map<String, List<String>> orphanetDiseaseGeneMap = new HashMap<String, List<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line = reader.readLine();
			String[] cols = null;
			while((line = reader.readLine()) != null){
				cols = line.split("\t");
				if(orphanetDiseaseGeneMap.get(cols[1]) == null){
					orphanetDiseaseGeneMap.put(cols[1], new ArrayList<String>());
				}
				orphanetDiseaseGeneMap.get(cols[1]).add(cols[3]);
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orphanetDiseaseGeneMap;
	}
	
	public static void write(String outputDir, Map<String, List<String>> orphanetDiseaseGeneMap){
		Iterator<Entry<String, List<String>>> itr = orphanetDiseaseGeneMap.entrySet().iterator();
		Entry<String, List<String>> entry = null;
		
		File dir = null;
		while(itr.hasNext()){
			entry = itr.next();
			dir = new File(outputDir + entry.getKey());
			if(!dir.exists()){
				dir.mkdirs();
			}
			StringBuffer sb = new StringBuffer();
			
			for(String entrez_id : entry.getValue()){
				sb.append(entrez_id).append("\n");
			}
			
			WriterUtil.write(dir.getAbsolutePath() + 
					"/" + entry.getKey() + "_symbol.txt", sb.toString());
		}
	}
	
	public static void main(String[] args){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/orphanet_input/172_ODs.txt";
		Map<String, List<String>> orphanetDiseaseGeneMap = OrphanetDiseaseGeneReaderAndWriter.read(filename);
		
		String outputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/orphanet_input/";
		OrphanetDiseaseGeneReaderAndWriter.write(outputDir, orphanetDiseaseGeneMap);
	}
}
