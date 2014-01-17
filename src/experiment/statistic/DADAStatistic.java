package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

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
		//return new ResultFileFilter("dada_candidate_gene_rank");
		return null;
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		System.out.println("Œ¥ µœ÷DADA");
		return sb.toString();
	}

}
