package experiment.statistic;

import java.io.File;

import util.FileUtil;


public class ValidationResultStatistic {
	
	public static void run(){
		String dirName = "./ppi_symbol/output";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		for(File dir: dirs){
			System.out.println(dir);
			File[] files = FileUtil.getFileList(dir.getPath());
			for(File file : files){
				System.out.println("---" + file);
			}
		}
		
		AbstractStatistic statisticStrategy = new ICNStatistic(dirs);
		statisticStrategy.run();
		
		statisticStrategy = new VSStatistic(dirs);
		statisticStrategy.run();
	}
	
	
	public static void main(String[] args){
		run();
	}
}
