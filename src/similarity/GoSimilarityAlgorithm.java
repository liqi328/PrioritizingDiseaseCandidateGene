package similarity;

import go.DAGanalysis;
import go.ProteinAnnotation;
import graph.Graph;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GoSimilarityAlgorithm implements SimilarityAlgorithm {
	private DAGanalysis dag = new DAGanalysis();
	private ProteinAnnotation pa = new ProteinAnnotation();
	private final String root_mf = "GO:0003674";
	private final String root_bp = "GO:0008150";
	private final String root_cc = "GO:0005575";
	
	private Map<Integer, String> geneSymbolMap;
	
	public GoSimilarityAlgorithm(Map<Integer, String> geneSymbolMap){
		this.geneSymbolMap = geneSymbolMap;
		init();
	}
	
	private void init(){
		try {
			pa.readGeneOntology("go/human_mf");
			dag.buildDAG("go/GO_ontology_mf");
			dag.rankDAG(root_mf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据给定的两个基因计算它们之间的相似性
	 * @param p1	基因名称(Symbol)
	 * @param p2	基因名称(Symbol)
	 * @return
	 */
	public double ppsimilarity(String p1, String p2){
		double sim = 0f;
		Set<String> a1 = pa.proteinsToFunctionsMap.get(p1);
		Set<String> a2 = pa.proteinsToFunctionsMap.get(p2);
		if(a1 == null || a2 == null) return sim;
		Iterator<String> at1 = a1.iterator();
		Iterator<String> at2;
		String s1 , s2;
		while(at1.hasNext()){
			s1 = at1.next();
			at2 = a2.iterator();
			while(at2.hasNext()){
				s2 = at2.next();
				double d = dag.getSimilarityMinusEp(s1, s2);
				if(d>sim)sim = d;
			}
		}
		return sim;
	}
	
	@Override
	public double calculate(int u, int v, double[][] matrix) {
		return ppsimilarity(geneSymbolMap.get(u), geneSymbolMap.get(v));
	}

}
