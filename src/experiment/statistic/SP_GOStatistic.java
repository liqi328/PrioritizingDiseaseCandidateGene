package experiment.statistic;

import java.io.File;
import java.io.FileFilter;

public class SP_GOStatistic extends AbstractStatistic {
	public SP_GOStatistic(File[] resultDirs) {
		super(resultDirs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("sp_go_validation_" + a_threshhold);
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- SP_GO_" + a_threshhold + " statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- SP_GO_" + a_threshhold + " statistic finished -------------");
	}

}
