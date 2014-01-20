package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class DADAStatistic extends AbstractStatistic {

	public DADAStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("dada_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- DADA statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- DADA statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("dada_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("DADA\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}

}
