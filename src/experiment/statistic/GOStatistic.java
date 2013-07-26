package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

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
}
