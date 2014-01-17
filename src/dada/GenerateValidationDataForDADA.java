package dada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;

public class GenerateValidationDataForDADA {
	private static String dir = "dada_input/";
	
	public static void main(String[] args){
		String configFilename = "dada_input/config.txt";
		
		StringBuffer configFilePathBuffer = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(configFilename));
			int count = 0;
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = in.readLine()) != null){
				Map<String, String> configMap = readConfigFile(line);
				List<String> seedList = readSeedList(configMap.get("seedset"));
				Set<String> candidatesSet = readCandidatesSet(configMap.get("candidates"));
				
				if(seedList.size() > 1){
					configFilePathBuffer.append(generateValidationData(configMap, seedList, candidatesSet));
					++count;
				}else{
					sb.append(new File(configMap.get("outputdir")).getName() + "\t" + seedList.size() + "\n");
				}
			}
			System.out.println(sb.toString());
			System.out.println("Count = " + count);
			
			System.out.println(configFilePathBuffer.toString());
			//System.out.println(configFilePathBuffer.toString().split("\n").length);
			
			WriterUtil.write("dada_input/all_config.txt", configFilePathBuffer.toString());
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateValidationData(Map<String, String> configMap, List<String> seedList, Set<String> candidatesSet){
		//System.out.println(new File(configMap.get("outputdir")).getName());
		StringBuffer configFilePathBuffer = new StringBuffer();
		String diseaseDir = dir + new File(configMap.get("outputdir")).getName();
		String targetDir = null;
		Set<String> seedSet = new HashSet<String>(seedList);
		for(String seed: seedList){
			seedSet.remove(seed);
			candidatesSet.add(seed);
			
			targetDir = diseaseDir + "/" + seed;
			FileUtil.makeDir(new File(targetDir));
			//System.out.println(targetDir);
			
			/* Éú³ÉconfigFile
			 * */
			StringBuffer configFileContent = new StringBuffer();
			configFileContent.append("ppi=").append(configMap.get("ppi")).append("\n");
			configFileContent.append("seedset=").append(targetDir + "/seedSet.txt").append("\n");
			configFileContent.append("candidates=").append(targetDir + "/candidates.txt").append("\n");
			configFileContent.append("outputdir=").append(configMap.get("outputdir") + "/" + seed).append("\n");
			
			WriterUtil.write(targetDir + "/config.txt", configFileContent.toString());
			//WriterUtil.write(targetDir + "/seedSet.txt", seedSet);
			writeSeedSet(targetDir + "/seedSet.txt", seedSet);
			WriterUtil.write(targetDir + "/candidates.txt", candidatesSet);
			
			configFilePathBuffer.append(targetDir + "/config.txt\n");
			
			seedSet.add(seed);
			candidatesSet.remove(seed);
		}
		
		return configFilePathBuffer.toString();
	}
	
	private static void writeSeedSet(String filename, Set<String> seedSet){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			for(String seed : seedSet){
				out.write(seed + "  1\n");
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> readConfigFile(String configFilename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(configFilename));
		
		Map<String, String> configMap = new HashMap<String, String>();
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split("=");
			configMap.put(cols[0], cols[1]);
		}
		in.close();
		return configMap;
	}
	
	public static List<String> readSeedList(String seedFilename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(seedFilename));
		
		List<String> seedList = new ArrayList<String>();
		String line = null;
		while((line = in.readLine()) != null){
			seedList.add(line.split(" |\t")[0]);
		}
		in.close();
		return seedList;
	}
	
	public static Set<String> readCandidatesSet(String candidatesFilename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(candidatesFilename));
		
		Set<String> candidatesSet = new HashSet<String>();
		String line = null;
		while((line = in.readLine()) != null){
			candidatesSet.add(line);
		}
		in.close();
		return candidatesSet;
	}
	
	
}
