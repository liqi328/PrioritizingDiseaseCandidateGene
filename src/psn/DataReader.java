package psn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;


class MutantPValueData{
	public String orfCode;
	public String geneName;
	public List<Double> pValueList = new ArrayList<Double>();
	
	public MutantPValueData(String code, String name){
		this.orfCode = code;
		this.geneName = name;
	}
	
}

public class DataReader {
	//ErrorModel = "meas", "geneerr";
	public static String ErrorModel = "geneerr";
	
	public static void main(String[] args){
		try {
			String inputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ ErrorModel + "_pvalue.txt";
			List<MutantPValueData> mutantDataList = new ArrayList<MutantPValueData>();
			String[] header = readMutantPValueData(inputFilename,mutantDataList);
			
			inputFilename = deleteColWhichNotInExperimentList(header, mutantDataList);
			
			mutantDataList.clear();
			header = readMutantPValueData(inputFilename,mutantDataList);
			inputFilename = deleteColWhichNotAffectedGene(header, mutantDataList);
			
			inputFilename = deleteRowWhichNotAffectedByAnyMutant(inputFilename);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] readMutantPValueData(String filename, List<MutantPValueData> mutantDataList) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		//Set<String> orfSet = new HashSet<String>();
		
		String line = in.readLine();
		String[] header = line.split("\t");
		String[] cols = null;
		MutantPValueData pValueData = null;
		
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			pValueData = new MutantPValueData(cols[0], cols[1]);
			
			for(int i = 2; i < cols.length; ++i){
				pValueData.pValueList.add(Double.parseDouble(cols[i]));
			}
			
			mutantDataList.add(pValueData);
			
//			if(orfSet.contains(cols[0])){
//				System.out.println(cols[0]);
//			}
//			orfSet.add(cols[0]);
		}
		in.close();
		
		//System.out.println("ORFs size = " + orfSet.size());
		
		return header;
	}
	
	/**
	 * 删除不在experiments_list.txt中的列
	 * @param header
	 * @param mutantDataList
	 * @throws IOException
	 */
	private static String deleteColWhichNotInExperimentList(String[] header, List<MutantPValueData> mutantDataList) throws IOException{
		Map<String, String> expList = readExperimentList();
		
		Set<Integer> containedColsSet = new HashSet<Integer>();
		
		for(int i = 2; i < header.length; ++i){
			for(String key : expList.keySet()){
				if(header[i].contains(key)){
					containedColsSet.add(i - 2);
					break;
				}
			}
		}
		
		String outputString = transferExperimentData2String(header, mutantDataList, containedColsSet);
		String outputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ ErrorModel + "_pvalue_processed_1.txt";
		
		WriterUtil.write(outputFilename, outputString);
		
		System.out.println("列数：" + (header.length - 2));
		System.out.println("删除不在experiment_list.txt中的列，剩下：" + containedColsSet.size());
		return outputFilename;
	}
	
	
	
	/**
	 * 删除没有(影响基因)的列mutant
	 * @param header
	 * @param mutantDataList
	 */
	private static String deleteColWhichNotAffectedGene(String[] header, List<MutantPValueData> mutantDataList){
		Set<Integer> containedColsSet = new HashSet<Integer>();
		
		double P_VALUE = 0.01;
		
		for(int i = 2; i < header.length; ++i){
			for(MutantPValueData data : mutantDataList){
				if(data.pValueList.get(i-2) < P_VALUE){
					containedColsSet.add(i - 2);
					break;
				}
			}
		}
		
		String outputString = transferExperimentData2String(header, mutantDataList, containedColsSet);
		String outputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ ErrorModel + "_pvalue_processed_2.txt";
		
		WriterUtil.write(outputFilename, outputString);
		
		System.out.println("列数：：" + (header.length - 2));
		System.out.println("删除没有(影响基因)的列mutant，剩下：" + containedColsSet.size());
		return outputFilename;
	}
	
	
	/**
	 * 删除没有被mutant影响的的行（ORFs）,同时删除被合并和删除的ORFs
	 * @throws IOException
	 */
	public static String deleteRowWhichNotAffectedByAnyMutant(String inputFilename) throws IOException{
		//String inputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ ErrorModel + "_pvalue_processed_2.txt";
		String outputFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ ErrorModel + "_pvalue_processed_3.txt";
		
		BufferedReader in = new BufferedReader(new FileReader(inputFilename));
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
		
		Set<String> deletedOrfSet = readMergedAndDeletedORFs();
		
		int preRowCount = 0, afterRowCount = 0;
		
		String line = null;
		String[] cols = null;
		out.write(in.readLine() + "\n");
		double P_VALUE = 0.01;
		while((line = in.readLine()) != null){
			preRowCount++;
			cols = line.split("\t");
			
//			if(deletedOrfSet.contains(cols[0])){
//				continue;
//			}
			for(int i = 2; i < cols.length; ++i){
				if(Double.parseDouble(cols[i]) < P_VALUE){
					out.write(line + "\n");
					afterRowCount++;
					
//					if(cols[0].equals("YLR162W")){
//						System.out.println(cols[i] + "\t" + i);
//					}
					break;
				}
			}
		}
		in.close();
		out.close();
		
		System.out.println("行数：" + preRowCount);
		System.out.println("删除没有被mutant影响的的行（ORFs）,同时删除被合并和删除的ORFs。剩下：" + afterRowCount);
		
		return outputFilename;
	}
	
	private static String transferExperimentData2String(String[] header, List<MutantPValueData> mutantDataList, Set<Integer> containedColsSet){
		StringBuffer sb = new StringBuffer();
		sb.append(header[0]).append("\t").append(header[1]).append("\t");
		
		for(int i = 2; i < header.length; ++i){
			if(containedColsSet.contains(i - 2)){
				sb.append(header[i]).append("\t");
			}
		}
		sb.append("\n");
		//System.out.println(sb.toString());
		
		for(MutantPValueData data : mutantDataList){
			sb.append(data.orfCode).append("\t");
			sb.append(data.geneName).append("\t");
			
			for(int i = 0; i < data.pValueList.size(); ++i){
				if(containedColsSet.contains(i)){
					sb.append(data.pValueList.get(i)).append("\t");
				}
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static Set<String> readMergedAndDeletedORFs() throws IOException{
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/deleted_merged_features.tab";
		Set<String> orfSet = new HashSet<String>();
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		String line = null;
		while((line = in.readLine()) != null){
			orfSet.add(line.split("\t")[0]);
		}
		in.close();
		
		return orfSet;
	}
	
	public static Map<String, String> readExperimentList() throws IOException{
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/experiment_list.txt";
		Map<String, String> expList = new HashMap<String, String>();
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		String line = null;
		String[] cols = null;
		in.readLine();
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			expList.put(cols[0], cols[1]);
		}
		in.close();
		
		//System.out.println(expList);
		//System.out.println(expList.size());
		
		return expList;
	}
}
