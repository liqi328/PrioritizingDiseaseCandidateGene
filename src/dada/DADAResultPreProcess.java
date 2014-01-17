package dada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.WriterUtil;

public class DADAResultPreProcess {
	private static String dirName = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/DADA/dada_output";
	
	public static void main(String[] args){
		File[] diseasesDir = FileUtil.getDirectoryList(dirName);
		try {
			for(File d : diseasesDir){
				processOneDisease(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(diseasesDir.length);
	}
	
	private static void processOneDisease(File dir) throws IOException{
		//System.out.println(dir.getCanonicalPath());
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		//System.out.println(targetDirArr.length);
		
		StringBuffer rwrRanksBuffer = new StringBuffer();
		StringBuffer dadaRanksBuffer = new StringBuffer();
		
		for(File d: targetDirArr){
			dadaRanksBuffer.append(processOneTargetGeneValidationFile(d, "dada_rank.txt"));
			rwrRanksBuffer.append(processOneTargetGeneValidationFile(d, "rwr_rank.txt"));
		}
		
		WriterUtil.write(dir.getAbsolutePath() + "/dada_validation.txt", dadaRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/rwr_validation.txt", rwrRanksBuffer.toString());
	}
	
	private static String processOneTargetGeneValidationFile(File dir, String filename){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		List<String> rankList = readRankFile(rankFile);
		
		for(int i = 0; i <rankList.size(); ++i){
			if(rankList.get(i).equals(dir.getName())){
				rankStr.append("\t").append(i + 1).append("\t0.0\n");
				break;
			}
		}
		
		return rankStr.toString();
	}
	
	private static List<String> readRankFile(File rankFile){
		List<String> rankList = new ArrayList<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(rankFile));
			String line = null;
			while((line = in.readLine()) != null){
				rankList.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rankList;
	}
}
