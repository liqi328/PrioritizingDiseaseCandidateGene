package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class VSStatistic extends AbstractStatistic {

	public VSStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("vs_validation_" + a_threshhold);
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- VS statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- VS statistic finished -------------");
	}
	
	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter(filterString);
	}

	@Override
	protected String run_ranking_statistic(File dir, int top_k) {
		StringBuffer sb = new StringBuffer();
		String[] r_threshholdArray = new String[]{"2", "3", "4", "1000000"};
		for(String r_threshhold : r_threshholdArray){
			File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter("vs_candidate_gene_rank_" + r_threshhold));
			sb.append("VS_").append(r_threshhold).append("\t");
			for(File file : files){
				sb.append(readRankingGene(file, top_k)).append("\n");
			}
		}
		
		return sb.toString();
	}


}
