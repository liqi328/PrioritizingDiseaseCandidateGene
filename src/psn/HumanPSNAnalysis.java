package psn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.WriterUtil;

public class HumanPSNAnalysis {
	public static void main(String[] args){
		try {
			Set<String> diseaseGeneSet = readDiseaseGene();
			
			//WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/huamn_psn/diseasegene.txt", diseaseGeneSet);
			
			analysisPSN(diseaseGeneSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void analysisPSN(Set<String> diseaseGeneSet) throws IOException{
		String huamPSNFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/huamn_psn/humanPSN.txt";
		
		List<MutantPValueData> mutantDataList = new ArrayList<MutantPValueData>();
		
		String[] header = DataReader.readMutantPValueData(huamPSNFilename,mutantDataList);
		System.out.println(header.length);
		
		calculateGeneExpressionChanges(mutantDataList, header, diseaseGeneSet);
		
		calculateDeletionMutantsAffectedGeneCount(mutantDataList, header, diseaseGeneSet);
	}
	
	private static void calculateGeneExpressionChanges(List<MutantPValueData> mutantDataList, String[] header, Set<String> diseaseGeneSet) throws IOException{
		StringBuffer sb = new StringBuffer();
		
		sb.append("orfCode\t基因名称\t是否是疾病基因\tdeletionMutant基因的个数\tdeletionMutant基因中的疾病基因的个数\tdeletionMutant基因\tdeletionMutant基因中的疾病基因\n");
		
		for(MutantPValueData pValueData: mutantDataList){
			sb.append(pValueData.orfCode).append("\t");
			sb.append(pValueData.geneName).append("\t");
			
			if(diseaseGeneSet.contains(pValueData.geneName)){
				sb.append("Yes").append("\t");
			}else{
				sb.append("No").append("\t");
			}
			
			int totalCount = 0;
			int diseaseGeneCount = 0;
			
			StringBuffer totalGeneBuffer = new StringBuffer();
			StringBuffer diseaseGeneBuffer = new StringBuffer();
			
			int i = 1;
			for(Double value: pValueData.pValueList){
				++i;
				if(value < PSN.P_VALUE_THRESHOLD){
					++totalCount;
					totalGeneBuffer.append(header[i]).append(",");
					if(diseaseGeneSet.contains(header[i])){
						diseaseGeneCount++;
						diseaseGeneBuffer.append(header[i]).append(",");
					}
				}
			}
			sb.append(totalCount).append("\t");
			sb.append(diseaseGeneCount).append("\t");
			sb.append(totalGeneBuffer.toString()).append("\t");
			sb.append(diseaseGeneBuffer.toString()).append("\n");
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/huamn_psn/gene2deletionMutant.txt", sb.toString());
	}
	
	public static void calculateDeletionMutantsAffectedGeneCount(List<MutantPValueData> mutantDataList, String[] header, Set<String> diseaseGeneSet) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("deletionMutant基因名称\t是否是疾病基因\t影响的基因个数\t影响的基因中疾病基因个数\t影响的基因\t影响的基因中的致病基因\n");
		
		for(int i = 2; i < header.length; ++i){
			int totalCount = 0;
			int diseaseGeneCount = 0;
			
			StringBuffer totalGeneBuffer = new StringBuffer();
			StringBuffer diseaseGeneBuffer = new StringBuffer();
			
			sb.append(header[i]).append("\t");
			if(diseaseGeneSet.contains(header[i])){
				sb.append("Yes").append("\t");
			}else{
				sb.append("No").append("\t");
			}
			
			for(MutantPValueData data : mutantDataList){
				if(data.pValueList.get(i-2) < PSN.P_VALUE_THRESHOLD){
					++totalCount;
					totalGeneBuffer.append(data.geneName).append(",");
					if(diseaseGeneSet.contains(data.geneName)){
						diseaseGeneCount++;
						diseaseGeneBuffer.append(data.geneName).append(",");
					}
				}
			}
			
			//sb.append(totalCount).append("\t").append(diseaseGeneCount).append("\n");
			sb.append(totalCount).append("\t");
			sb.append(diseaseGeneCount).append("\t");
			sb.append(totalGeneBuffer.toString()).append("\t");
			sb.append(diseaseGeneBuffer.toString()).append("\n");
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/huamn_psn/deletionMutant2gene.txt", sb.toString());
	}
	
	private static Set<String> readDiseaseGene() throws IOException{
		String filename = "E:/2013疾病研究/疾病数据/OMIM/morbidmap_gene.txt";
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		Set<String> diseaseGeneSet = new HashSet<String>();
		
		String line = null;
		String[] cols = null;
		
		while((line = in.readLine()) != null){
			cols = line.split("\t");
			for(String name : cols[1].split(",")){
				if(name.trim().equals("")){
					System.out.println(line);
					continue;
				}
				diseaseGeneSet.add(name.trim());
			}
		}
		in.close();
		
		return diseaseGeneSet;
	}
}
