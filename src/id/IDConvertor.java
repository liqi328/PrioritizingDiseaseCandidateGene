package id;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public abstract class IDConvertor{
	public abstract Set<String> converte2hprdId(Set<String> idSet);
	public abstract Set<String> converte2entrezId(Set<String> idSet);
	public abstract Set<String> converte2omimId(Set<String> idSet);
	
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
	
	protected abstract Map<String, HprdIdMapping> getHprdIdMappingMap();
}
