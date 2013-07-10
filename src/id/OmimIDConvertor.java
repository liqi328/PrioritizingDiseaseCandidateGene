package id;

import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import util.WriterUtil;

/**
 * ��omim_idת��ΪHprd_id, entrez_Id
 * @author Liqi
 *
 */
class OmimIDConvertor extends IDConvertor{
	@Override
	protected Map<String, HprdIdMapping> getHprdIdMappingMap() {
		return HprdIdMappingUtil.getOmimIdIndexedIdMapping();
	}
	
	public static void main(String[] args){
		String filename = "E:/2013�����о�/ʵ������/prioritizing_candidate_gene/ad_omimid.txt";
		Set<String> omimIdset = DiseaseGeneSeedReader.read(filename);
		
		IDConvertor convertor = new OmimIDConvertor();
		
		Set<String> hprdIdSet = convertor.converte2hprdId(omimIdset);		
		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/ad_hprdid.txt", hprdIdSet);
		
		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/ad_id_mapping.txt",
				convertor.getHprdIdMappingSet(omimIdset));
		
		Set<String> entrezIdset = convertor.converte2entrezId(omimIdset);
		WriterUtil.write("E:/2013�����о�/ʵ������/prioritizing_candidate_gene/ad_entrezid.txt", entrezIdset);
	}
}