package id;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public abstract class IDConvertor{
	/**
	 * ��entrez_id���ϻ���omim_id����ת����hprd_id����
	 * @param idSet		entrez_id���ϻ���omim_id����
	 * @return			hprd_id����
	 */
	public Set<String> converte2hprdId(Set<String> idSet){
		Map<String, HprdIdMapping> idIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> hprdIdSet = new HashSet<String>();
		Iterator<String> itr = idSet.iterator();
		while(itr.hasNext()){
			String id = itr.next();
			HprdIdMapping idMapping = idIndexedIdMappingMap.get(id);
			
			if(idMapping != null){
				hprdIdSet.add(String.valueOf(Integer.parseInt(idMapping.getHrpdId())));
			}else{
				//System.out.println("id: " + id + " has not find corresponding hprd_id.");
			}
		}
		
		return hprdIdSet;
	}
	
	/**
	 * ��hprd_id���ϻ���omim_id����ת����entrez_id����
	 * @param idSet	hprd_id���ϻ���omim_id����
	 * @return		entrez_id����
	 */
	public Set<String> converte2entrezId(Set<String> idSet){
		Map<String, HprdIdMapping> idIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> entrezIdSet = new HashSet<String>();
		Iterator<String> itr = idSet.iterator();
		while(itr.hasNext()){
			String id = itr.next();
			HprdIdMapping idMapping = idIndexedIdMappingMap.get(id);
			if(idMapping != null){
				entrezIdSet.add(String.valueOf(Integer.parseInt(idMapping.getEntrezGeneId())));
			}else{
				System.out.println("id: " + id + " has not find corresponding entrez_id.");
			}
		}
		return entrezIdSet;
	}
	
	/**
	 * ��hprd_id���ϻ���entrez_id����ת����omim_id����
	 * @param idSet		hprd_id���ϻ���entrez_id����
	 * @return			omim_id����
	 */
	public Set<String> converte2omimId(Set<String> idSet){
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<String> omimIdSet = new HashSet<String>();
		Iterator<String> itr = idSet.iterator();
		while(itr.hasNext()){
			String id = itr.next();
			HprdIdMapping idMapping = entrezIdIndexedIdMappingMap.get(id);
			if(idMapping != null){
				if(null == idMapping.getOmimId() || idMapping.getOmimId().equals("")
						|| idMapping.getOmimId().equals("-")){
					System.out.println("id: " + id + ", corresponding omim_id: " + idMapping.getOmimId());
					continue;
				}
				omimIdSet.add(String.valueOf(Integer.parseInt(idMapping.getOmimId())));
			}else{
				System.out.println("id: "+ id + " has not find corresponding omim_id.");
			}
		}
		
		return omimIdSet;
	}

	
	/**
	 * ��hprd_id����, omim_id���ϻ���entrez_id����ת����Gene Symbol����
	 * @param idSet		hprd_id���ϻ���entrez_id����,omim_id����
	 * @return			Gene Symbol����
	 */
	public Set<String> converte2symbol(Set<String> idSet){
		return null;
	}
	
	/**
	 * ����HprdIdMappingUtil��ȡ
	 * 	entrezIdIndexIdMappingMap,
	 * 	hprdIdIndexedIdMappingMap,
	 *  omimIdIndexedIdMappingMap ����֮һ, <br />
	 * ��ͬ��IDConvertor��Ҫ��ͬ��idIndexIdMappingMap <br />
	 * @return
	 */
	protected abstract Map<String, HprdIdMapping> getHprdIdMappingMap();
	
	
	/**
	 * ���ݸ�����id���ϣ��õ�����Ӧ��HprdIdMapping
	 * @param idSet		idΪentrez_id, omim_id, hprd_id������֮һ
	 * @return
	 */
	public final Set<HprdIdMapping> getHprdIdMappingSet(Set<String> idSet){
		Map<String, HprdIdMapping> idIndexedIdMappingMap = getHprdIdMappingMap();
		
		Set<HprdIdMapping> result = new HashSet<HprdIdMapping>();
		Iterator<String> itr = idSet.iterator();
		while(itr.hasNext()){
			String id = itr.next();
			HprdIdMapping idMapping = idIndexedIdMappingMap.get(id);
			if(idMapping != null){
				result.add(idMapping);
			}else{
				System.out.println("id: " + id + " has not find corresponding id_mapping.");
			}
		}
		return result;
	}
}
