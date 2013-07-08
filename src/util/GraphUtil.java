package util;

import graph.Graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphUtil {
	
	/**
	 * 将致病基因名称(HPRD_ID) 转换为 图的内部索引集合
	 * @param g						图
	 * @param diseaseGeneSeedSet	致病基因名称(HPRD_ID)集合
	 * @return
	 */
	public static Set<Integer> transformName2GraphNodeIndex(Graph g, Set<String> diseaseGeneSeedSet){
		Set<Integer> result = new HashSet<Integer>();
		Iterator<String> itr = diseaseGeneSeedSet.iterator();
		String name = null;
		while(itr.hasNext()){
			name = itr.next();
			if(g.getNodeIndex(name) == null){
				System.out.println("Gene Hprd_id is not int the ppi_hprd_id.txt: " + name);
				continue;
			}
			result.add(g.getNodeIndex(name));
		}
		return result;
	}
	
	/**
	 * 将图的内部索引集合 转换为 致病基因名称(HPRD_ID)
	 * @param g						图
	 * @param graphNodeIndexSet		致病基因名称(HPRD_ID)集合
	 * @return
	 */
	public static Set<String> transformGraphNodeIndex2Name(Graph g, Set<Integer> graphNodeIndexSet){
		Set<String> result = new HashSet<String>();
		Iterator<Integer> itr = graphNodeIndexSet.iterator();
		while(itr.hasNext()){
			result.add(g.getNodeName(itr.next()));
		}
		return result;
	}
}
