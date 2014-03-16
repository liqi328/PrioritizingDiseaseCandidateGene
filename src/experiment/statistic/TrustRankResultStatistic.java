package experiment.statistic;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;

public class TrustRankResultStatistic {
	public static void main(String[] args){
		run();
	}
	
	public static void run(){
		String dirName = "E:/2013疾病研究/实验数据/TrustRanker/19disease/output";
		
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy3 = new PageRankAvageStatistic(dirs);
		StatisticResult result3 = statisticStrategy3.run();
		resultMap.put("PageRankAvage", result3);
		
		AbstractStatistic statisticStrategy4 = new PageRankPriorStatistic(dirs);
		StatisticResult result4 = statisticStrategy4.run();
		resultMap.put("PageRankPrior", result4);
		
		AbstractStatistic statisticStrategy = new TrustRanksHighAvageStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("TrustRanksHighAvage", result);
		
		AbstractStatistic statisticStrategy2 = new TrustRanksHighPriorStatistic(dirs);
		StatisticResult result2 = statisticStrategy2.run();
		resultMap.put("TrustRanksHighPrior", result2);

		AbstractStatistic statisticStrategy5 = new TrustRanksInverseAvageStatistic(dirs);
		StatisticResult result5 = statisticStrategy5.run();
		resultMap.put("TrustRanksInverseAvage", result5);
		
		AbstractStatistic statisticStrategy6 = new TrustRanksInversePriorStatistic(dirs);
		StatisticResult result6 = statisticStrategy6.run();
		resultMap.put("TrustRanksInversePrior", result6);
					
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "rank_cutoff.txt", resultMap);
	}
}
