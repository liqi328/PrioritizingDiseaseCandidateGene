package experiment.statistic;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;

public class SPGOrankerOMIMResult {
	public static void run(){
		String dirName = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/output";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		StatisticResult result = null;
		
		String[] a_threshholdArray = new String[]{"1.0", "0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1", "0.0"};
		
		AbstractStatistic statisticStrategy = new SP_GOStatistic(dirs);
		for(String a_threshhold : a_threshholdArray){
			statisticStrategy.setAthreshhold(a_threshhold);
			result = statisticStrategy.run();
			resultMap.put("SP_GO_" + a_threshhold, result);
		}
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic_spgo.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "rank_cutoff_spgo.txt", resultMap);
	}
	
	public static void main(String[] args){
		run();
	}
}
