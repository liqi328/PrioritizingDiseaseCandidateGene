package id;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import util.WriterUtil;

public class PPIConventor {
	public static void main(String[] args){
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

}
