package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class SPStatistic extends AbstractStatistic {

	public SPStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("sp_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- SP statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- SP statistic finished -------------");
	}

}
