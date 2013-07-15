package id;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import util.WriterUtil;

/**
 * 将omim_id转换为Hprd_id, entrez_Id
 * @author Liqi
 *
 */
public class OmimIDConvertor extends IDConvertor{
	@Override
	protected Map<String, HprdIdMapping> getHprdIdMappingMap() {
		return HprdIdMappingUtil.getOmimIdIndexedIdMapping();
	}
	
	public Set<String> converte2symbol(Set<String> omim_idSet){
		System.out.println("OmimIDConvertor");
		Map<String, String> omimIdIndexedIdMappingMap = readMim2Gene();
		
		Set<String> symbolSet = new HashSet<String>();
		Iterator<String> itr = omim_idSet.iterator();
		while(itr.hasNext()){
			String id = itr.next();
			String symbol = omimIdIndexedIdMappingMap.get(id);
			if(symbol == null || "-".equals(symbol)){
				System.out.println("id: "+ id + " has not find corresponding gene symbol.");
			}else{
				symbolSet.add(symbol);
			}
		}
		return symbolSet;
	}
	
	private Map<String, String> readMim2Gene(){
		String filename = "E:/2013疾病研究/疾病数据/OMIM/mim2gene.txt";
		Map<String, String> ret = new HashMap<String, String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			String line = in.readLine();
			String[] cols = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				ret.put(cols[0].trim(), cols[3].trim());
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private static void run_test(String filename){
		//String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/ad_omimid.txt";
		Set<String> omimIdset = DiseaseGeneSeedReader.read(filename + "omimid.txt");
		
		
		IDConvertor convertor = new OmimIDConvertor();
		
//		Set<String> hprdIdSet = convertor.converte2hprdId(omimIdset);		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/ad_hprdid.txt", hprdIdSet);
//		
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/ad_id_mapping.txt",
//				convertor.getHprdIdMappingSet(omimIdset));
//		
//		Set<String> entrezIdset = convertor.converte2entrezId(omimIdset);
//		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/ad_entrezid.txt", entrezIdset);
		
		Set<String> symbolSet = convertor.converte2symbol(omimIdset);
		WriterUtil.write(filename + "symbol.txt", symbolSet);
		
		WriterUtil.write(filename + "omimid.txt", omimIdset);
	}
	
	public static void main(String[] args){
		String[] filenameArray = new String[]{
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/ad_",
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/pd_",
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/sca_",
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/als_",
				"E:/2013疾病研究/实验数据/prioritizing_candidate_gene/ad_pd_sca/sma_",
		};
		for(String filename : filenameArray){
			run_test(filename);
		}
	}
}