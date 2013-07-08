package util;

import graph.Graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphUtil {
	
	/**
	 * ���²���������(HPRD_ID) ת��Ϊ ͼ���ڲ���������
	 * @param g						ͼ
	 * @param diseaseGeneSeedSet	�²���������(HPRD_ID)����
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
	 * ��ͼ���ڲ��������� ת��Ϊ �²���������(HPRD_ID)
	 * @param g						ͼ
	 * @param graphNodeIndexSet		�²���������(HPRD_ID)����
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
