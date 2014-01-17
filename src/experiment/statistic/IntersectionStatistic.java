package experiment.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;
import experiment.Rank;

public class IntersectionStatistic {
	public static void main(String[] args){
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		List<String> icnTopOneSet = run_1(dirs, "icn_validation");
		List<String> spTopOneSet = run_1(dirs, "sp_validation");
		List<String> spgoTopOneSet = run_1(dirs, "sp_go_validation_0.9");
		List<String> vsTopOneSet = run_1(dirs, "vs_validation_2");
		
		System.out.println("icn - sp: " + intersection(icnTopOneSet, spTopOneSet).size());
		System.out.println("icn - spgo: " + intersection(icnTopOneSet, spgoTopOneSet).size());
		System.out.println("icn - vs: " + intersection(icnTopOneSet, vsTopOneSet).size());
		System.out.println("sp - icn: " + intersection(spTopOneSet, icnTopOneSet).size());
		System.out.println("sp - vs: " + intersection(spTopOneSet, vsTopOneSet).size());
		System.out.println("sp - spgo: " + intersection(spTopOneSet, spgoTopOneSet).size());
		System.out.println("spgo - vs: " + intersection(spgoTopOneSet, vsTopOneSet).size());
		System.out.println("spgo - icn: " + intersection(spgoTopOneSet, icnTopOneSet).size());
		System.out.println("spgo - sp: " + intersection(spgoTopOneSet, spTopOneSet).size());
	}
	
	public static Set<String> intersection(List<String> A, List<String> B){
		List<String> BTmp = new ArrayList<String>();
		BTmp.addAll(B);
		
		Set<String> commonSet = new HashSet<String>();
		for(String a : A){
			for(int i = 0; i < BTmp.size(); ++i){
				if(a.equals(BTmp.get(i))){
					commonSet.add(a);
					BTmp.remove(i);
				}
			}
		}
		return commonSet;
	}
	
	public static List<String> run_1(File[] dirs, String methodName){
		List<File> retFileList = parseResultFiles(dirs, methodName);
		
		List<String> topOneSet = new ArrayList<String>();
		for(File file : retFileList){
			//System.out.println(file);
			
			List<Rank> rankList = readRankList(file);
			if(!isCanStatistic(rankList)){
				continue;
			}
			
			for(Rank rank : rankList){
				if(rank.getRank() == 1){
					topOneSet.add(rank.getName());
				}
			}
		}
		System.out.println(methodName + "\t" + topOneSet.size());
		return topOneSet;
	}
	
	private static boolean isCanStatistic(List<Rank> rankList){
		if(rankList.size() < 4){
			return false;
		}
		return true;
	}
	
	protected final static List<File> parseResultFiles(File[] resultDirs, String methodName){
		List<File> retFileList = new ArrayList<File>();
		for(File dir: resultDirs){
			File[] files = FileUtil.getFileList(dir.getPath(), new ResultFileFilter(methodName));
			
			for(File file : files){
				retFileList.add(file);
			}
		}
		return retFileList;
	}
	
	
	private static List<Rank> readRankList(File file){
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
}
