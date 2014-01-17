package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class GOStatistic extends AbstractStatistic{
	public GOStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("go_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- GO statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- GO statistic finished -------------");
	}
	
	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("go_candidate_gene_rank");
	}

	@Override
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("GO\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
}
