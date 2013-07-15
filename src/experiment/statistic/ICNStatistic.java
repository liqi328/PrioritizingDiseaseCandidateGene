package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class ICNStatistic extends AbstractStatistic{

	public ICNStatistic(File[] resultDirs) {
		super(resultDirs);
	}
	
	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("icn_");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- ICN statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- ICN statistic finished -------------");
	}
	
}