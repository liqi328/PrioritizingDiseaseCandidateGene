package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class SP_GOStatistic extends AbstractStatistic {
	public SP_GOStatistic(File[] resultDirs) {
		super(resultDirs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("sp_go_validation_" + a_threshhold);
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- SP_GO_" + a_threshhold + " statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- SP_GO_" + a_threshhold + " statistic finished -------------");
	}
	
	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter(filterString);
	}

	@Override
	protected String run_ranking_statistic(File dir, int top_k) {
		StringBuffer sb = new StringBuffer();
		
		String[] a_threshholdArray = new String[]{"1.0", "0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1", "0.0"};
		
		for(String a_threshhold : a_threshholdArray){
			File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter("spgo_candidate_gene_rank_" + a_threshhold));
			sb.append("SPGOranker_").append(a_threshhold).append("\t");
			for(File file : files){
				sb.append(readRankingGene(file, top_k)).append("\n");
			}
		}
		
		return sb.toString();
	}

}
