package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class TrustRanksHighPriorStatistic extends AbstractStatistic{
	public TrustRanksHighPriorStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("trustRanksHighPrior_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- trustRanksHighPrior statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- trustRanksHighPrior statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("trustRanksHighPrior_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("trustRanksHighPrior\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
}
