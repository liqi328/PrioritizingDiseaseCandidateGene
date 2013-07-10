package id;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;

import util.WriterUtil;

/**
 * 将entrez_Id转换为Hprd_id, omim_id
 * @author Liqi
 *
 */
class EntrezIDConvertor extends IDConvertor{

	@Override
	public Set<String> converte2hprdId(Set<String> entrezIdSet) {
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> hprdIdSet = new HashSet<String>();
		Iterator<String> itr = entrezIdSet.iterator();
		while(itr.hasNext()){
			String entrezId = itr.next();
			HprdIdMapping idMapping = entrezIdIndexedIdMappingMap.get(entrezId);
			addOneHprdId(hprdIdSet, entrezId, idMapping);
		}
		
		return hprdIdSet;
	}

	private void addOneHprdId(Set<String> hprdIdSet, String entrezId,
			HprdIdMapping idMapping) {
		if(idMapping != null){
			hprdIdSet.add(String.valueOf(Integer.parseInt(idMapping.getHrpdId())));
		}else{
			System.out.println("entrez_id: "+ entrezId + " has not find corresponding hprd_id.");
		}
	}
	
	@Override
	public Set<String> converte2omimId(Set<String> entrezIdSet) {
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> hprdIdSet = new HashSet<String>();
		Iterator<String> itr = entrezIdSet.iterator();
		while(itr.hasNext()){
			String entrezId = itr.next();
			HprdIdMapping idMapping = entrezIdIndexedIdMappingMap.get(entrezId);
			if(idMapping != null){
				hprdIdSet.add(String.valueOf(Integer.parseInt(idMapping.getOmimId())));
			}else{
				System.out.println("entrez_id: "+ entrezId + " has not find corresponding omim_id.");
			}
		}
		
		return hprdIdSet;
	}
	

	@Override
	public Set<String> converte2entrezId(Set<String> entrezIdSet) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Map<String, HprdIdMapping> getHprdIdMappingMap() {
		return HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
	}


	public Set<HprdIdMapping> getHprdIdMappingSet2(Set<String> entrezIdSet) {
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
		
		Set<HprdIdMapping> result = new HashSet<HprdIdMapping>();
		Iterator<String> itr = entrezIdSet.iterator();
		while(itr.hasNext()){
			result.add(entrezIdIndexedIdMappingMap.get(itr.next()));
		}
		return result;
	}
	
	public static void main(String[] args){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_entrez_id.txt";
		Set<String> entrezIdset = DiseaseGeneSeedReader.read(filename);
		
		IDConvertor convertor = new EntrezIDConvertor();
		
		Set<String> hprdIdSet = convertor.converte2hprdId(entrezIdset);		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_hprd_id.txt", hprdIdSet);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_id_mapping.txt",
				convertor.getHprdIdMappingSet(entrezIdset));
		
		Set<String> omimIdset = convertor.converte2omimId(entrezIdset);
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_omim_id.txt", omimIdset);
	}
}