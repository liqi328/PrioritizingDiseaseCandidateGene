package id;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public abstract class IDConvertor{
	/**
	 * 将entrez_id集合或者omim_id集合转换成hprd_id集合
	 * @param idSet		entrez_id集合或者omim_id集合
	 * @return			hprd_id集合
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
	 * 将hprd_id集合或者omim_id集合转换成entrez_id集合
	 * @param idSet	hprd_id集合或者omim_id集合
	 * @return		entrez_id集合
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
	 * 将hprd_id集合或者entrez_id集合转换成omim_id集合
	 * @param idSet		hprd_id集合或者entrez_id集合
	 * @return			omim_id集合
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
	 * 将hprd_id集合, omim_id集合或者entrez_id集合转换成Gene Symbol集合
	 * @param idSet		hprd_id集合或者entrez_id集合,omim_id集合
	 * @return			Gene Symbol集合
	 */
	public Set<String> converte2symbol(Set<String> idSet){
		return null;
	}
	
	/**
	 * 利用HprdIdMappingUtil获取
	 * 	entrezIdIndexIdMappingMap,
	 * 	hprdIdIndexedIdMappingMap,
	 *  omimIdIndexedIdMappingMap 三者之一, <br />
	 * 不同的IDConvertor需要不同的idIndexIdMappingMap <br />
	 * @return
	 */
	protected abstract Map<String, HprdIdMapping> getHprdIdMappingMap();
	
	
	/**
	 * 根据给定的id集合，得到其相应的HprdIdMapping
	 * @param idSet		id为entrez_id, omim_id, hprd_id中三者之一
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
