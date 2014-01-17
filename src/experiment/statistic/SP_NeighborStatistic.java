package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class SP_NeighborStatistic extends AbstractStatistic {
	public SP_NeighborStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("sp_neighbor_validation_0.8");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- SP_Neighbor statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- SP_Neighbor statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String run_ranking_statistic(File dir, int top_k) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
