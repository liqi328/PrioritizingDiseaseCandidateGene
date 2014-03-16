package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class PageRankAvageStatistic extends AbstractStatistic{
	public PageRankAvageStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("pageRankAvage_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- pageRankAvage statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- pageRankAvage statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("pageRankAvage_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("pageRankAvage\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
}
