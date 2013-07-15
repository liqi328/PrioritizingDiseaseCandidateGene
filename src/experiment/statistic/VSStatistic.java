package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class VSStatistic extends AbstractStatistic {

	public VSStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("vs_");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- VS statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- VS statistic finished -------------");
	}
}
