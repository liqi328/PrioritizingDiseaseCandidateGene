package dada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.FileUtil;
import util.WriterUtil;

/*
 * 抽取罕见疾病的数据，作为为DADA算法输入
 * 文件夹格式：
 * 文件夹名称：疾病的名称
 * 文件夹包含以下3个文件：
 * 	1：ppi.txt
 * 	2: candidates.txt	候选基因集合
 * 	3: seedSet.txt		种子基因集合
 * */
public class ExactData {
	private static String inputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/input_hprd";
	private static String outputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/output_hprd0726/";
	
	public static void main(String[] args){
		File[] dirs = FileUtil.getDirectoryList(inputDir);
		System.out.println(dirs.length);
		
		String outputFilename = "dada_input/";
		FileUtil.makeDir(new File(outputFilename));
		
		for(File d : dirs){
			File dest = new File(outputFilename + d.getName());
			FileUtil.makeDir(dest);
			
			copyPPIFile(dest, d);
			
			copySeedGeneFile(dest, d);
			
			copyCandidateGeneFile(dest, d);
			
		}
		
		genenConfigFile(outputFilename);
	}
	
	public static void copyPPIFile(File dest, File from){
		File destFile = new File(dest.getAbsolutePath()+"/ppi_hprd_id.txt");
		//FileUtil.copy(new File(inputDir + "/ppi_hprd_id.txt"), destFile);
		
		File file = new File(inputDir + "/ppi_hprd_id.txt");
		append1toline(destFile, file);
	}
	
	public static void copySeedGeneFile(File dest, File from){
		File[] files = FileUtil.getFileList(from.getAbsolutePath());
		for(File f: files){
			if(f.getName().contains("_hprd_id2.txt")){
				File destFile = new File(dest.getAbsolutePath()+"/" + from.getName() + "_hprd_id.txt");
				//FileUtil.copy(f, destFile);
				append1toline(destFile, f);
				break;
			}
		}
	}
	
	private static void append1toline(File dest, File from){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dest));
			
			BufferedReader in = new BufferedReader(new FileReader(from));
			String line = null;
			while((line = in.readLine()) != null){
				out.write(line + "    1\n");
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyCandidateGeneFile(File dest, File from){
		File fromFile = new File(outputDir + from.getName() + "_output");
		File[] files = FileUtil.getFileList(fromFile.getAbsolutePath());
		for(File f: files){
			if(f.getName().contains("random_candidate_gene.txt")){
				File destFile = new File(dest.getAbsolutePath()+"/" + f.getName());
				FileUtil.copy(f, destFile);
				break;
			}
		}
	}
	
	public static void genenConfigFile(String dir){
		File[] dirs = FileUtil.getDirectoryList(dir);
		
		StringBuffer configFileList = new StringBuffer();
		for(File d: dirs){
			configFileList.append(generateOneConfigFile(d)).append("\n");
		}
		
		System.out.println(configFileList.toString());
		
		WriterUtil.write("dada_input/config.txt", configFileList.toString());
	}
	
	private static String generateOneConfigFile(File from){
		File configFile = new File(from.getAbsolutePath()+"/" + from.getName() + "_config.txt");
		StringBuffer sb = new StringBuffer();
		sb.append("ppi=").append("dada_input/" + from.getName() + "/ppi_hprd_id.txt").append("\n");
		sb.append("seedset=").append("dada_input/" + from.getName() + "/" + from.getName() + "_hprd_id.txt").append("\n");
		sb.append("candidates=").append("dada_input/" + from.getName() + "/random_candidate_gene.txt").append("\n");
		sb.append("outputdir=").append("dada_output/" + from.getName()).append("\n");
		
		WriterUtil.write(configFile.getAbsolutePath(), sb.toString());
		
		return "dada_input/" + from.getName() + "/" + configFile.getName();
	}
}
