package id;

import java.util.HashSet;
import java.util.Iterator;
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
	/**
	 * ��OMIM_ID����ת��Ϊ��Ӧ��HPRD_ID����
	 * @param omimIdSet	OMIM_ID ����
	 * @return				HPRD_ID����
	 */
	public Set<String> converte2hprdId(Set<String> omimIdSet){
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> hprdIdSet = new HashSet<String>();
		Iterator<String> itr = omimIdSet.iterator();
		while(itr.hasNext()){
			String omimId = itr.next();
			HprdIdMapping idMapping = omimIdIndexedIdMappingMap.get(omimId);
			if(idMapping != null){
				hprdIdSet.add(String.valueOf(Integer.parseInt(idMapping.getHrpdId())));
			}else{
				System.out.println("omim_id: " + omimId + " has not find corresponding hprd_id.");
			}
		}
		
		return hprdIdSet;
	}
	
	public Set<String> converte2entrezId(Set<String> omimIdSet){
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> entrezIdSet = new HashSet<String>();
		Iterator<String> itr = omimIdSet.iterator();
		while(itr.hasNext()){
			String omimId = itr.next();
			HprdIdMapping idMapping = omimIdIndexedIdMappingMap.get(omimId);
			if(idMapping != null){
				entrezIdSet.add(String.valueOf(Integer.parseInt(idMapping.getEntrezGeneId())));
			}else{
				System.out.println("omim_id: " + omimId + " has not find corresponding entrez_id.");
			}
		}
		return entrezIdSet;
	}
	
	@Override
	public Set<String> converte2omimId(Set<String> idSet) {
		// TODO Auto-generated method stub
		return null;
	}
	
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