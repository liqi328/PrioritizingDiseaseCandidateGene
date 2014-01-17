package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class ICNStatistic extends AbstractStatistic{

	public ICNStatistic(File[] resultDirs) {
		super(resultDirs);
	}
	
	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("icn_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- ICN statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- ICN statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("icn_candidate_gene_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("ICN\t" + files.length);
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
	
}