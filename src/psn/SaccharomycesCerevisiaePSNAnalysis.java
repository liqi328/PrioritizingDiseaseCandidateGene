package psn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;

public class SaccharomycesCerevisiaePSNAnalysis {
	public static void main(String[] args){
		try {
			Set<String> diseaseGeneSet = readDiseaseGene();
			Map<String, List<String>> sacc2humanGeneMap = PSN.readSacc2HumanGeneRelation();
			
			analysisPSN(diseaseGeneSet, sacc2humanGeneMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void analysisPSN(Set<String> diseaseGeneSet, Map<String, List<String>> sacc2humanGeneMap) throws IOException{
		String saccPSNFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/data_expts1-300_"+ DataReader.ErrorModel + "_pvalue_processed_final.txt";
		
		List<MutantPValueData> mutantDataList = new ArrayList<MutantPValueData>();
		
		String[] header = DataReader.readMutantPValueData(saccPSNFilename,mutantDataList);
		System.out.println(header.length);
		
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/orfCode_geneName.txt";
		Map<String, String> orfGeneMap = HomoloGene.readOrfCodeGeneName(filename);
		
		Map<String, String> experimentListMap = DataReader.readExperimentList();
		
		calculateGeneExpressionChanges(mutantDataList, header, diseaseGeneSet, sacc2humanGeneMap, orfGeneMap, experimentListMap);
		
		calculateDeletionMutantsAffectedGeneCount(mutantDataList, header, diseaseGeneSet, sacc2humanGeneMap, orfGeneMap, experimentListMap);
	}
	
	private static void calculateGeneExpressionChanges(List<MutantPValueData> mutantDataList, String[] header, Set<String> diseaseGeneSet,
			Map<String, List<String>> sacc2humanGeneMap, Map<String, String> orfGeneMap, Map<String, String> experimentListMap) throws IOException{
		StringBuffer sb = new StringBuffer();
		
		//sb.append("orfCode\t基因名称\t是否是疾病基因\t被影响的次数\t其中被疾病基因影响的次数\t影响的基因\t其中致病基因\n");
		sb.append("orfCode\t基因名称\t同源基因\t同源基因中的疾病基因\tdeletionMutant基因的个数\tdeletionMutant基因中的疾病基因的个数\tdeletionMutant基因\tdeletionMutant基因中的疾病基因\tdeletionMutant基因的同源基因的个数\tdeletionMutant基因的同源基因中的致病基因的个数\tdeletionMutant基因的同源基因\tdeletionMutant基因的同源基因中的致病基因\n");
		
		for(MutantPValueData pValueData: mutantDataList){
			sb.append(pValueData.orfCode).append("\t");
			if(pValueData.geneName == null || "".equals(pValueData.geneName)){
				pValueData.geneName = orfGeneMap.get(pValueData.orfCode);
				if(pValueData.geneName == null){
					pValueData.geneName = "";
				}
				//System.out.println(pValueData.orfCode + "---" + pValueData.geneName);
			}
			sb.append(pValueData.geneName).append("\t");
			
			if(sacc2humanGeneMap.get(pValueData.geneName) != null){
				for(String s : sacc2humanGeneMap.get(pValueData.geneName)){
					sb.append(s + ", ");
				}
			}
			sb.append("\t");
			
			if(sacc2humanGeneMap.get(pValueData.geneName) != null){
				for(String s : sacc2humanGeneMap.get(pValueData.geneName)){
					if(diseaseGeneSet.contains(s)){
						sb.append(s + ", ");
					}
				}
			}
			sb.append("\t");

			
			int totalCount = 0;
			int diseaseGeneCount = 0;
			
			StringBuffer totalGeneBuffer = new StringBuffer();
			StringBuffer diseaseGeneBuffer = new StringBuffer();
			StringBuffer homologyTotalGeneBuffer = new StringBuffer();//影响的基因的同源基因
			StringBuffer homologyDiseaseGeneBufferGeneBuffer = new StringBuffer();//其中的致病基因的同源基因
			
			int homologyTotalCount = 0;
			int homologyDiseaseGeneCount = 0;
			
			int i = 1;
			for(Double value: pValueData.pValueList){
				++i;
				if(value < PSN.P_VALUE_THRESHOLD){
					++totalCount;
					String geneName = orfGeneMap.get(experimentListMap.get(header[i]).split(",")[0].toUpperCase());
					if(geneName == null || "".equals(geneName)){
						geneName = "";
						//System.out.println(header[i]);
					}else{
						geneName = geneName.toUpperCase();
					}
					totalGeneBuffer.append(geneName).append(",");
					if(diseaseGeneSet.contains(geneName)){
						diseaseGeneCount++;
						diseaseGeneBuffer.append(geneName).append(",");
					}
					
					if(sacc2humanGeneMap.get(geneName) != null){
						++homologyTotalCount;
						for(String s : sacc2humanGeneMap.get(geneName)){
							homologyTotalGeneBuffer.append(s).append(",");
							
							if(diseaseGeneSet.contains(s)){
								homologyDiseaseGeneCount++;
								homologyDiseaseGeneBufferGeneBuffer.append(s).append(",");
							}
						}
					}
				}
			}
			sb.append(totalCount).append("\t");
			sb.append(diseaseGeneCount).append("\t");
			sb.append(totalGeneBuffer.toString()).append("\t");
			sb.append(diseaseGeneBuffer.toString()).append("\t");
			sb.append(homologyTotalCount).append("\t");
			sb.append(homologyDiseaseGeneCount).append("\t");
			sb.append(homologyTotalGeneBuffer.toString()).append("\t");
			sb.append(homologyDiseaseGeneBufferGeneBuffer.toString()).append("\n");
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/SaccharomycesCerevisiae_psn/gene2deletionMutant.txt", sb.toString());
	}
	
	public static void calculateDeletionMutantsAffectedGeneCount(List<MutantPValueData> mutantDataList, String[] header, Set<String> diseaseGeneSet, 
			Map<String, List<String>> sacc2humanGeneMap, Map<String, String> orfGeneMap, Map<String, String> experimentListMap) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("ExperimentName\tORF code\tdeletionMutant基因名称\t同源基因\t同源基因中的疾病基因\t影响的基因个数\t影响的基因中的疾病基因个数\t影响的基因\t影响的基因中的致病基因\t影响的基因的同源基因的个数\t影响的基因的同源基因中的致病基因的个数\t影响的基因的同源基因\t影响的基因的同源基因中的致病基因\n");
		
		for(int i = 2; i < header.length; ++i){
			sb.append(header[i]).append("\t");
			sb.append(experimentListMap.get(header[i])).append("\t");
			String geneName = orfGeneMap.get(experimentListMap.get(header[i]).split(",")[0].toUpperCase());
			if(geneName == null || "".equals(geneName)){
				geneName = "";
				//System.out.println(header[i]);
			}else{
				geneName = geneName.toUpperCase();
			}
			sb.append(geneName).append("\t");
			
			if(sacc2humanGeneMap.get(geneName) != null){
				for(String s : sacc2humanGeneMap.get(geneName)){
					sb.append(s + ", ");
				}
			}
			sb.append("\t");
			
			if(sacc2humanGeneMap.get(geneName) != null){
				for(String s : sacc2humanGeneMap.get(geneName)){
					if(diseaseGeneSet.contains(s)){
						sb.append(s + ", ");
					}
				}
			}
			sb.append("\t");
			
			StringBuffer totalGeneBuffer = new StringBuffer();//影响的基因
			StringBuffer diseaseGeneBuffer = new StringBuffer();//其中的疾病基因
			
			StringBuffer homologyTotalGeneBuffer = new StringBuffer();//影响的基因的同源基因
			StringBuffer homologyDiseaseGeneBufferGeneBuffer = new StringBuffer();//其中的致病基因的同源基因
			
			int totalCount = 0;
			int diseaseGeneCount = 0;
			
			int homologyTotalCount = 0;
			int homologyDiseaseGeneCount = 0;
			
			for(MutantPValueData pValueData : mutantDataList){
				if(pValueData.pValueList.get(i-2) < PSN.P_VALUE_THRESHOLD){
					if(pValueData.geneName == null || "".equals(pValueData.geneName)){
						pValueData.geneName = orfGeneMap.get(pValueData.orfCode);
						if(pValueData.geneName == null){
							pValueData.geneName = "";
						}
						geneName = geneName.toUpperCase();
						//System.out.println(pValueData.orfCode + "---" + pValueData.geneName);
					}
					++totalCount;
					totalGeneBuffer.append(pValueData.geneName).append(",");
					
					if(diseaseGeneSet.contains(pValueData.geneName)){
						diseaseGeneCount++;
						diseaseGeneBuffer.append(pValueData.geneName).append(",");
					}
					
					if(sacc2humanGeneMap.get(pValueData.geneName) != null){
						++homologyTotalCount;
						for(String s : sacc2humanGeneMap.get(pValueData.geneName)){
							homologyTotalGeneBuffer.append(s).append(",");
							
							if(diseaseGeneSet.contains(s)){
								homologyDiseaseGeneCount++;
								homologyDiseaseGeneBufferGeneBuffer.append(s).append(",");
							}
						}
					}
				}
			}
			
			//sb.append(totalCount).append("\t").append(diseaseGeneCount).append("\n");
			sb.append(totalCount).append("\t");
			sb.append(diseaseGeneCount).append("\t");
			sb.append(totalGeneBuffer.toString()).append("\t");
			sb.append(diseaseGeneBuffer.toString()).append("\t");
			sb.append(homologyTotalCount).append("\t");
			sb.append(homologyDiseaseGeneCount).append("\t");
			sb.append(homologyTotalGeneBuffer.toString()).append("\t");
			sb.append(homologyDiseaseGeneBufferGeneBuffer.toString()).append("\n");
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/PSN/SaccharomycesCerevisiae_psn/deletionMutant2gene.txt", sb.toString());
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
