package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class TrustRanksInversePriorStatistic extends AbstractStatistic{
	public TrustRanksInversePriorStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("trustRanksInversePrior_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- trustRanksInversePrior statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- trustRanksInversePrior statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("trustRanksInversePrior_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("trustRanksInversePrior\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
}
