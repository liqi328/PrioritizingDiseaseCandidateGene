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

/*
 * 我们对根据SPranker、SPGOranker和ICN、VS的候选基因排名算法的交叉验证结果进行了比较。
 * 这里考察任何两种方法排名第一的基因中，相同基因的数目，实验结果如表1所示。从表1中可以看出SPranker、SPGOranker、VS、ICN排名第一分别有402、407、394、348。由此表明我们的方法比另外两种有一些提高。
 * SPranker与VS、ICN的交集分别是309、268；SPGOranker与VS、ICN的交集分别是311、270；
 * VS与ICN之间的交集有264。
 * */
public class IntersectionStatistic {
	public static void main(String[] args){
		//run_1();
		run_2();
	}
	
	public static void run_1(){
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd0726";
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
	
	public static void run_2(){
		String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd0726";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		List<String> icnTopOneSet = run_1(dirs, "icn_validation");
		List<String> spTopOneSet = run_1(dirs, "sp_validation");
		List<String> spgoTopOneSet = run_1(dirs, "sp_go_validation_0.8");
		List<String> vsTopOneSet = run_1(dirs, "vs_validation_2");
		
		String dirName2 = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/DADA/172OrphanetDisease/dada_output";
		File[] dirs2 = FileUtil.getDirectoryList(dirName2);
		
		List<String> dadaTopOneSet = run_1(dirs2, "dada_validation");
		List<String> rwrTopOneSet = run_1(dirs2, "rwr_validation");
		
		System.out.println("sp - icn: " + intersection(spTopOneSet, icnTopOneSet).size());
		System.out.println("sp - vs: " + intersection(spTopOneSet, vsTopOneSet).size());
		System.out.println("sp - spgo: " + intersection(spTopOneSet, spgoTopOneSet).size());
		System.out.println("sp - dada: " + intersection(spTopOneSet, dadaTopOneSet).size());
		System.out.println("sp - rwr: " + intersection(spTopOneSet, rwrTopOneSet).size());
		
		System.out.println("spgo0.8 - vs: " + intersection(spgoTopOneSet, vsTopOneSet).size());
		System.out.println("spgo0.8 - icn: " + intersection(spgoTopOneSet, icnTopOneSet).size());
		System.out.println("spgo0.8 - dada: " + intersection(spgoTopOneSet, dadaTopOneSet).size());
		System.out.println("spgo0.8 - rwr: " + intersection(spgoTopOneSet, rwrTopOneSet).size());
		
		System.out.println("icn - vs: " + intersection(icnTopOneSet, vsTopOneSet).size());
		System.out.println("icn - dada: " + intersection(icnTopOneSet, dadaTopOneSet).size());
		System.out.println("icn - rwr: " + intersection(icnTopOneSet, rwrTopOneSet).size());
		
		System.out.println("vs - dada: " + intersection(vsTopOneSet, dadaTopOneSet).size());
		System.out.println("vs - rwr: " + intersection(vsTopOneSet, rwrTopOneSet).size());
		
		System.out.println("rwr - dada: " + intersection(rwrTopOneSet, dadaTopOneSet).size());
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
