package experiment.statistic;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;


public class ValidationResultStatistic {
	
	public static void run(){
		//String dirName = "./ppi_symbol/output";
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd";
		//String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/output_hprd";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		//print_files(dirs);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new ICNStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("ICN", result);
		
		String[] r_threshholdArray = new String[]{"2", "3", "4", "1000000"};
		statisticStrategy = new VSStatistic(dirs);
		for(String r_threshhold : r_threshholdArray){
			statisticStrategy.setAthreshhold(r_threshhold);
			result = statisticStrategy.run();
			resultMap.put("VS_" + r_threshhold, result);
		}
		
		
		statisticStrategy = new SPStatistic(dirs);
		result = statisticStrategy.run();
		resultMap.put("SP", result);
		
		statisticStrategy = new GOStatistic(dirs);
		result = statisticStrategy.run();
		resultMap.put("GO", result);
		
		String[] a_threshholdArray = new String[]{"0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1"};
		
		//String[] a_threshholdArray = new String[]{"0.8", " 0.7", " 0.3", " 0.2"};
		
		statisticStrategy = new VS_GOStatistic(dirs);
		for(String a_threshhold : a_threshholdArray){
			statisticStrategy.setAthreshhold(a_threshhold);
			result = statisticStrategy.run();
			resultMap.put("VS_GO_" + a_threshhold, result);
		}
		
		statisticStrategy = new SP_GOStatistic(dirs);
		for(String a_threshhold : a_threshholdArray){
			statisticStrategy.setAthreshhold(a_threshhold);
			result = statisticStrategy.run();
			resultMap.put("SP_GO_" + a_threshhold, result);
		}
		
/*		statisticStrategy = new SP_NeighborStatistic(dirs);
		for(String a_threshhold : a_threshholdArray){
			statisticStrategy.setAthreshhold(a_threshhold);
			result = statisticStrategy.run();
			resultMap.put("SP_Neighbor_" + a_threshhold, result);
		}*/
		
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "rank_cutoff.txt", resultMap);
	}
	
	private static void print_files(File[] dirs){
		for(File dir: dirs){
			System.out.println(dir);
			File[] files = FileUtil.getFileList(dir.getPath());
			for(File file : files){
				System.out.println("---" + file);
			}
		}
	}
	
	
	public static void main(String[] args){
		run();
	}
}
