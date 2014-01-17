package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class SPStatistic extends AbstractStatistic {

	public SPStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("sp_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- SP statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- SP statistic finished -------------");
	}
	
	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("sp_candidate_gene_rank");
	}

	@Override
	protected String run_ranking_statistic(File dir, int top_k) {
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("SP\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}

}
