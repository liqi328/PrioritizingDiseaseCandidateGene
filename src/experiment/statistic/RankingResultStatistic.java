package experiment.statistic;

import java.io.File;

import util.FileUtil;
import util.WriterUtil;

public class RankingResultStatistic {
	public static void main(String[] args){
		run_1();
		run_2();
	}
	
	private static void run_1(){
		//String dirName = "E:/2013�����о�/ʵ������/prioritizing_candidate_gene/orphanet_experiment/VS_SP_ICN/�������/output_hprd";
		String dirName = "E:/2013�����о�/ʵ������/prioritizing_candidate_gene/�������Լ���/VS_SP_ICN/�������/output_hprd";
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
		
//		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/orphanet_experiment/VS_SP_ICN/�������/output_hprd/����top10�Ļ���.txt",
//				sb.toString());
		
		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/�������Լ���/VS_SP_ICN/�������/output_hprd/result.txt",
				sb.toString());
	}
	
	private static void run_2(){
		//String dirName = "E:/2013�����о�/ʵ������/prioritizing_candidate_gene/orphanet_experiment/DADA_RWR/�������/dada_output";
		String dirName = "E:/2013�����о�/ʵ������/prioritizing_candidate_gene/�������Լ���/DADA_RWR/�������/dada_output";
		
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		AbstractStatistic[] statisticStrategy = new AbstractStatistic[2];
		
		statisticStrategy[0] = new DADAStatistic(dirs);
		statisticStrategy[1] = new RWRStatistic(dirs);
		
		StringBuffer sb = new StringBuffer();
		for(File dir: dirs){
			sb.append(dir.getName()).append("\n");
			for(AbstractStatistic strategy : statisticStrategy){
				sb.append(strategy.run_ranking_statistic(dir, 10)).append("\n");
			}
			sb.append("\n");
		}
		
//		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/orphanet_experiment/DADA_RWR/�������/dada_output/����top10�Ļ���.txt",
//				sb.toString());
		
		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/�������Լ���/DADA_RWR/�������/dada_output/����top10�Ļ���.txt",
				sb.toString());
	}

}


