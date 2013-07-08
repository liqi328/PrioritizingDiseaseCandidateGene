package experiment;

import graph.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ������֤�Ľ������
 * @author Liqi
 *
 */
public class ValidationResultAnalysis {
	private static int top_k = 1;
	
	public static void setTopK(int k){
		top_k = k;
	}
	
	public static Map<Integer, Rank> run(Map<Integer, List<Rank>> rankMap){
		System.out.println("Cross Validation Result Analysis running...");
		
		Map<Integer, Rank> resultMap = new HashMap<Integer, Rank>();
		
		Iterator<Entry<Integer, List<Rank>>> itr = rankMap.entrySet().iterator();
		Entry<Integer, List<Rank>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			Integer targetGene = entry.getKey();
			List<Rank> rankList = entry.getValue();
			
			Collections.sort(rankList);
			Rank rank = null;
			for(int i = 0; i < rankList.size(); ++i){
				rank = rankList.get(i);
				if(rank.getId().equals(targetGene)){
					rank.setRank(i + 1);
					break;
				}
			}
			resultMap.put(targetGene, rank);
		}
		
		System.out.println("Cross Validation Result Analysis finished.");
		
		return resultMap;
	}
	
	public static String ranksMap2String(Graph g, Map<Integer, List<Rank>> ranksMap){
		StringBuffer sb = new StringBuffer();
		
		Iterator<Entry<Integer, List<Rank>>> itr = ranksMap.entrySet().iterator();
		Entry<Integer, List<Rank>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			Integer targetGene = entry.getKey();
			List<Rank> rankList = entry.getValue();
			
			Collections.sort(rankList);
			
			sb.append("targetGene = " + g.getNodeName(targetGene)).append("\n\t");
			Rank rank = null;
			for(int i = 0; i < rankList.size(); ++i){
				rank = rankList.get(i);
				
				sb.append(g.getNodeName(rank.getId())).append("\t").append(rank.getScore()).append("\n");
				if(i != rankList.size() - 1){
					sb.append("\t");
				}
			}
		}
		
		return sb.toString();
	}
	
	public static String map2String(Graph g, Map<Integer, Rank> rankMap){
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<Integer, Rank>> itr = rankMap.entrySet().iterator();
		while(itr.hasNext()){
			Entry<Integer, Rank> entry = itr.next();
			sb.append(g.getNodeName(entry.getKey())).append("\t");
			sb.append(entry.getValue().getRank()).append("\t");
			sb.append(entry.getValue().getScore()).append("\n");
		}
		return sb.toString();
	}
}
