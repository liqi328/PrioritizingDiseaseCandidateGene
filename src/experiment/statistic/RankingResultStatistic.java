package experiment.statistic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.WriterUtil;

public class RankingResultStatistic {
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -jar SPranker_TopPrediction.jar ./output");
			System.exit(-1);
		}
		
		run_1(args[0]);
		//run_2();
	}
	
	private static void run_1(String dirName){
		//String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/VS_SP_ICN/排名结果/output_hprd";
		//String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/VS_SP_ICN/排名结果/output_hprd";
		
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		List<AbstractStatistic> statisticStrategyList = new ArrayList<AbstractStatistic>();
//		statisticStrategyList.add(new ICNStatistic(dirs));
//		statisticStrategyList.add(new VSStatistic(dirs));
		//statisticStrategyList.add(new SPStatistic(dirs));
		statisticStrategyList.add(new SP_GOStatistic(dirs));
//		statisticStrategyList.add(new VS_GOStatistic(dirs));
//		statisticStrategyList.add(new GOStatistic(dirs));
		
		StringBuffer sb = new StringBuffer();
		for(File dir: dirs){
			sb.append(dir.getName()).append("\n");
			for(AbstractStatistic strategy : statisticStrategyList){
				sb.append(strategy.run_ranking_statistic(dir, 20)).append("\n");
			}
			sb.append("\n");
		}
		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/VS_SP_ICN/排名结果/output_hprd/排名top10的基因.txt",
//				sb.toString());
//		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/VS_SP_ICN/排名结果/output_hprd/result.txt",
//				sb.toString());
		
		WriterUtil.write(dirName + "/SPGOranker_Top20.txt", sb.toString());
	}
	
	private static void run_2(){
		//String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/DADA_RWR/排名结果/dada_output";
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/DADA_RWR/排名结果/dada_output";
		
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
		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/DADA_RWR/排名结果/dada_output/排名top10的基因.txt",
//				sb.toString());
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/神经退行性疾病/DADA_RWR/排名结果/dada_output/排名top10的基因.txt",
				sb.toString());
	}

}


