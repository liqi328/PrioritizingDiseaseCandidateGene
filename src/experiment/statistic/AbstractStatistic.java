package experiment.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import experiment.Rank;

class StatisticResult{
	public static final int RANK = 100;
	public int totalValidation = 0;
	public int[] rankArray = new int[RANK];
}

public abstract class AbstractStatistic{
	protected String a_threshhold = "0.8";
	protected File[] resultDirs;
	
	public AbstractStatistic(File[] resultDirs){
		this.resultDirs = resultDirs;
	}
	
	public void setAthreshhold(String a_threshhold){
		this.a_threshhold = a_threshhold;
	}
	
	public final StatisticResult run(){
		printLogHeader();
		
		StatisticResult result = run_2();
		
		printLogFooter();
		
		return result;
	}
	
	private StatisticResult run_2(){
		StatisticResult result = new StatisticResult();
		int count = 0;
		List<File> retFileList = parseResultFiles();
		for(File file : retFileList){
			System.out.println(file);
			
			List<Rank> rankList = readRankList(file);
			if(!isCanStatistic(rankList)){
				++count;
				continue;
			}
			
			for(Rank rank : rankList){
				result.rankArray[rank.getRank() - 1]++;
				result.totalValidation++;
			}
		}
		System.out.println("< 4 total = " + count);
		
		return result;
	}
	
	private boolean isCanStatistic(List<Rank> rankList){
		if(rankList.size() < 4){
			return false;
		}
		return true;
	}
	
	private List<Rank> readRankList(File file){
		List<Rank> rankList = new ArrayList<Rank>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			Rank rank = null;
			String[] cols = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				rank = new Rank(-1, Double.parseDouble(cols[2]));
				rank.setName(cols[0]);
				rank.setRank(Integer.parseInt(cols[1]));
				rankList.add(rank);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rankList;
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