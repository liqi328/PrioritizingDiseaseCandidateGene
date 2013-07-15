package experiment.statistic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;

public abstract class AbstractStatistic{
	protected File[] resultDirs;
	
	public AbstractStatistic(File[] resultDirs){
		this.resultDirs = resultDirs;
	}
	
	public final void run(){
		printLogHeader();
		
		List<File> retFileList = parseResultFiles();
		for(File file : retFileList){
			System.out.println(file);
		}
		
		printLogFooter();
	}
	
	protected final List<File> parseResultFiles(){
		List<File> retFileList = new ArrayList<File>();
		for(File dir: resultDirs){
			File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter());
			
			for(File file : files){
				retFileList.add(file);
			}
		}
		return retFileList;
	}
	
	protected abstract void printLogHeader();
	protected abstract void printLogFooter(); 
	
	protected abstract FileFilter createFileFilter();
	
	
	class ResultFileFilter implements FileFilter{
		private String filterString;
		
		public ResultFileFilter(String filterString){
			this.filterString = filterString;
		}
		
		@Override
		public boolean accept(File pathname) {
			if(pathname.isDirectory()){
				return false;
			}
			if(pathname.getName().startsWith(filterString)){
				return true;
			}
			return false;
		}
	}
}