package experiment.statistic;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;

public class DADAValidationResultStatistic {
	public static void main(String[] args){
		run();
	}
	
	public static void run(){
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/DADA/dada_output";
		
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new DADAStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("DADA", result);
		
		statisticStrategy = new RWRStatistic(dirs);
		result = statisticStrategy.run();
		resultMap.put("RWR", result);
		
		
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "rank_cutoff.txt", resultMap);
	}
}
