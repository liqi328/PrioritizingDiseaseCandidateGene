package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class RWRStatistic  extends AbstractStatistic {

	public RWRStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("rwr_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- RWR statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- RWR statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		//return new ResultFileFilter("dada_candidate_gene_rank");
		return null;
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		System.out.println("Œ¥ µœ÷RWR");
		return sb.toString();
	}

}
