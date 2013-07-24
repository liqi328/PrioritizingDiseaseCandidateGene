package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class VS_GOStatistic extends AbstractStatistic {

	public VS_GOStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("vs_go_validation_" + a_threshhold);
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- VS_GO_" + a_threshhold + " statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- VS_GO_" + a_threshhold + " statistic finished -------------");
	}
}
