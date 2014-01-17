package experiment.statistic;

import java.io.File;
import java.util.List;

import util.FileUtil;
import util.WriterUtil;

public class RankingResultStatistic {
	public static void main(String[] args){
		//String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd";
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/output_hprd";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		AbstractStatistic[] statisticStrategy = new AbstractStatistic[6];
		
		statisticStrategy[0] = new ICNStatistic(dirs);
		statisticStrategy[1] = new VSStatistic(dirs);
		statisticStrategy[2] = new SPStatistic(dirs);
		statisticStrategy[3] = new SP_GOStatistic(dirs);
		statisticStrategy[4] = new VS_GOStatistic(dirs);
		statisticStrategy[5] = new GOStatistic(dirs);
		
		StringBuffer sb = new StringBuffer();
		for(File dir: dirs){
			sb.append(dir.getName()).append("\n");
			for(AbstractStatistic strategy : statisticStrategy){
				sb.append(strategy.run_ranking_statistic(dir, 10)).append("\n");
			}
			sb.append("\n");
		}
		
		//WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd/result.txt",
		//		sb.toString());
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/output_hprd/result.txt",
				sb.toString());
	}

}


