package dada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;

/*
 * 产生排名的输入数据：
 * 使用的候选基因是种子基因(已知的致病基因)在PPI网络中的邻居节点。
 * */

public class GeneRankingDataForDADA {
	private static String inputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/DADA/172OrphanetDisease/交叉验证/";
	private static String outDirPath = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/DADA/172OrphanetDisease/排名结果/dada_input/";
	
	public static void main(String[] args){
		String configFilename = inputDir + "dada_input/config.txt";
		
		StringBuffer configFilePathBuffer = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(configFilename));
			int count = 0;
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = in.readLine()) != null){
				Map<String, String> configMap = GenerateValidationDataForDADA.readConfigFile(inputDir + line);
				List<String> seedList = GenerateValidationDataForDADA.readSeedList(inputDir + configMap.get("seedset"));
				
				if(seedList.size() > 1){
					configFilePathBuffer.append(generateRankingData(configMap, new File(inputDir + line).getParentFile().getName())).append("\n");
					++count;
				}else{
					sb.append(new File(configMap.get("outputdir")).getName() + "\t" + seedList.size() + "\n");
				}
			}
//			System.out.println(sb.toString());
//			System.out.println("Count = " + count);
//			
//			System.out.println(configFilePathBuffer.toString());
			
			WriterUtil.write(outDirPath + "all_config.txt", configFilePathBuffer.toString());
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateRankingData(Map<String, String> configMap, String diseaseName){
		File destDir = new File(outDirPath + diseaseName);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		
		copyCandidateFile(destDir, diseaseName);
		copySeedSetFile(destDir, new File(inputDir + "dada_input/"+ diseaseName));
		
		
		String configFilePath = "dada_input/" + diseaseName + "/config.txt";
		/* 生成configFile
		 * */
		StringBuffer configFileContent = new StringBuffer();
		configFileContent.append("ppi=").append(configMap.get("ppi")).append("\n");
		configFileContent.append("seedset=").append(configMap.get("seedset")).append("\n");
		configFileContent.append("candidates=").append("dada_input/"+ diseaseName + "/candidate_gene.txt").append("\n");
		configFileContent.append("outputdir=").append(configMap.get("outputdir")).append("\n");
		
		WriterUtil.write(outDirPath + diseaseName + "/config.txt", configFileContent.toString());
		
		return configFilePath;
	}
	
	private static boolean copyCandidateFile(File dest, String diseaseName){
		String sourceDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd/";
		File fromFile = new File(sourceDir + diseaseName + "_output");
		File[] files = FileUtil.getFileList(fromFile.getAbsolutePath());
		
		boolean flag = false;
		
		for(File f: files){
			if(f.getName().contains("candidate_gene.txt")){
				try {
					Set<String> candidatesSet = GenerateValidationDataForDADA.readCandidatesSet(f.getAbsolutePath());
					
					if(candidatesSet.size() > 0){
						File destFile = new File(dest.getAbsolutePath()+"/" + f.getName());
						FileUtil.copy(f, destFile);
						flag = true;
					}else{
						System.out.println(diseaseName + "\t" + candidatesSet.size());
					}
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
		}
		return flag;
	}
	
	private static void copySeedSetFile(File dest, File from){
		File[] files = FileUtil.getFileList(from.getAbsolutePath());
		for(File f: files){
			if(f.getName().contains("_hprd_id.txt") && !f.getName().startsWith("ppi")){
				File destFile = new File(dest.getAbsolutePath()+"/" + f.getName());
				FileUtil.copy(f, destFile);
				break;
			}
		}
	}
}
