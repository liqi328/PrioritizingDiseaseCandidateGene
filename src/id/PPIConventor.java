package id;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;

public class PPIConventor {
	public static void entrezGeneId2hprdId(String hprdPPIFile, String outputFile){
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(hprdPPIFile)));
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			String[] cols = null;
			String a, b;
			HprdIdMapping hprdMapping = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdMapping = entrezIdIndexedIdMappingMap.get(cols[0]);
				if(hprdMapping == null){
					System.out.println(line);
					continue;
				}
				a = hprdMapping.getHrpdId();
				if(a == null || a.equals("-")){
					System.out.println(line);
					continue;
				}
				
				hprdMapping = entrezIdIndexedIdMappingMap.get(cols[1]);
				if(hprdMapping == null){
					System.out.println(line);
					continue;
				}
				b = hprdMapping.getHrpdId();
				if(b == null || b.equals("-")){
					System.out.println(line);
					continue;
				}
				
				if(cols.length == 2){
					sb.append(a).append("\t").append(b).append("\n");
				}else if(cols.length == 3){
					sb.append(a).append("\t").append(b).append("\t").append(cols[2]).append("\n");
				}else{
					System.out.println("PPI file format Error.");
				}
				
			}			
			in.close();			
			WriterUtil.write(outputFile, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void hprdId2entrezGeneId(String hprdPPIFile, String outputFile){
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(hprdPPIFile)));
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			String[] cols = null;
			String a, b;
			HprdIdMapping hprdMapping = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdMapping = hprdIdIndexedIdMappingMap.get(cols[0]);
				if(hprdMapping == null){
					System.out.println("null0: " + line);
					continue;
				}
				a = hprdMapping.getEntrezGeneId();
				if(a == null || a.equals("-")){
					System.out.println("col0: " + line);
					continue;
				}
				
				hprdMapping = hprdIdIndexedIdMappingMap.get(cols[1]);
				if(hprdMapping == null){
					System.out.println("null1: " + line);
					continue;
				}
				b = hprdMapping.getEntrezGeneId();
				if(b == null || b.equals("-")){
					System.out.println("col1: " + line);
					continue;
				}
				
				if(cols.length == 2){
					sb.append(a).append("\t").append(b).append("\n");
				}else if(cols.length == 3){
					sb.append(a).append("\t").append(b).append("\t").append(cols[2]).append("\n");
				}else{
					System.out.println("PPI file format Error.");
				}
				
			}			
			in.close();			
			WriterUtil.write(outputFile, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void hprdId2symbol(String hprdPPIFile){
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
		String ppiFilename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_id.txt";
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(ppiFilename)));
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			String[] cols = null;
			String a, b;
			HprdIdMapping hprdMapping = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdMapping = hprdIdIndexedIdMappingMap.get(cols[0]);
				if(hprdMapping == null){
					System.out.println(line);
					continue;
				}
				a = hprdMapping.getGeneSymbol();
				if(a == null || a.equals("-")){
					System.out.println(line);
					continue;
				}
				
				hprdMapping = hprdIdIndexedIdMappingMap.get(cols[1]);
				if(hprdMapping == null){
					System.out.println(line);
					continue;
				}
				b = hprdMapping.getGeneSymbol();
				if(b == null || b.equals("-")){
					System.out.println(line);
					continue;
				}
				sb.append(a).append("\t").append(b).append("\n");
			}			
			in.close();			
			WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ppi_hprd_symbol.txt", sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/HPRD_ppi_weighted.txt";
		
		String outFilename = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/entrezid_ppi_weighted.txt";
		hprdId2entrezGeneId(ppiFilename, outFilename);
	}

}
