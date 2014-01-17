package experiment;

import graph.Graph;

import id.HprdIdMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import reader.HprdIdMappingReader;
import similarity.GoSimilarityAlgorithm;
import similarity.SPSimilarityAlgorithm;
import util.GraphUtil;
import util.WriterUtil;

public class ExperimentGO extends AbstractExperiment {
	
	public ExperimentGO(InputArgument input) {
		super(input);
	}
	
	@Override
	public void run(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Experiment GO running...");
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSeedSet, candidateGeneSet);
		
		//printMap(g, geneSymbolMap);
		
		LeaveOneOutCrossValidation validation = new LeaveOneOutCrossValidation(g);
		validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		Map<Integer, List<Rank>> ranksMap = validation.run(diseaseGeneSeedSet, candidateGeneSet);
//		WriterUtil.write(input.getOutputDir() + "go_validation_rank.txt", 
//				ValidationResultAnalysis.ranksMap2String(g, ranksMap));
		
		Map<Integer, Rank> resultMap = ValidationResultAnalysis.run(ranksMap);
		
		WriterUtil.write(input.getOutputDir() + "go_validation.txt",
				ValidationResultAnalysis.map2String(g, resultMap));
		
		System.out.println("Experiment GO finished.\n");
	}
	
	/**
	 * PPI为hprd
	 * 返回基因Symbol的map, 其中key=基因的图的内部ID, value = 基因的symbol
	 * @param g						图
	 * @param diseaseGeneSeedSet	致病基因集合(图的内部ID)
	 * @param candidateGeneSet		候选基因集合(图的内部ID)
	 * @return
	 */
	protected Map<Integer, String> getGeneSymbolMap(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet){
		//String filename = "E:/2013疾病研究/疾病数据/HumanPPI/HPRD_Release9_062910/FLAT_FILES_072010/HPRD_ID_MAPPINGS.txt";
		/* HPRD 数据库中的HPRD_ID_MAPPINGS.txt
		 * String:hprdId
		 * */
		String filename = input.getHprdIdMappingsFileName();
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = new HprdIdMappingReader(filename).read();
		
		Set<Integer> graphNodeIdSet = new HashSet<Integer>();
		graphNodeIdSet.addAll(diseaseGeneSeedSet);
		graphNodeIdSet.addAll(candidateGeneSet);
		
		Map<Integer, String> resultMap = new HashMap<Integer, String>();
		Iterator<Integer> itr = graphNodeIdSet.iterator();
		while(itr.hasNext()){
			Integer nodeId = itr.next();
			resultMap.put(nodeId, hprdIdIndexedIdMappingMap.get(g.getNodeName(nodeId)).getGeneSymbol());
		}
		return resultMap;
	}
	
	
	/**
	 * PPI为symbol
	 * 返回基因Symbol的map, 其中key=基因的图的内部ID, value = 基因的symbol
	 * @param g						图
	 * @param diseaseGeneSeedSet	致病基因集合(图的内部ID)
	 * @param candidateGeneSet		候选基因集合(图的内部ID)
	 * @return
	 */
	protected Map<Integer, String> getGeneSymbolMap2(Graph g, Set<Integer> diseaseGeneSeedSet,
			Set<Integer> candidateGeneSet){
		Set<Integer> graphNodeIdSet = new HashSet<Integer>();
		graphNodeIdSet.addAll(diseaseGeneSeedSet);
		graphNodeIdSet.addAll(candidateGeneSet);
		
		Map<Integer, String> resultMap = new HashMap<Integer, String>();
		Iterator<Integer> itr = graphNodeIdSet.iterator();
		while(itr.hasNext()){
			Integer nodeId = itr.next();
			resultMap.put(nodeId, g.getNodeName(nodeId));
		}
		return resultMap;
	}
	
	private void printMap(Graph g, Map<Integer, String> geneSymbolMap){
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<Integer, String>> itr = geneSymbolMap.entrySet().iterator();
		while(itr.hasNext()){
			Entry<Integer, String> entry = itr.next();
			sb.append(g.getNodeName(entry.getKey())).append("\t");
			sb.append(entry.getValue()).append("\n");
		}
		WriterUtil.write(input.getOutputDir() + "go_validation.txt",
				sb.toString());
	}

	@Override
	public void ranking(Graph g, Set<Integer> diseaseGeneSet,
			Set<Integer> candidateGeneSet) {
		System.out.println("Ranking candidate gene using GO algorithm. [start]");
		Map<Integer, String> geneSymbolMap = getGeneSymbolMap(g, diseaseGeneSet, candidateGeneSet);
		
		LeaveOneOutCrossValidation validation = new LeaveOneOutCrossValidation(g);
		validation.setSimilarityAlgorithm(new GoSimilarityAlgorithm(geneSymbolMap));
		
		List<Rank> rankList = validation.run_rank(diseaseGeneSet, candidateGeneSet);
		Collections.sort(rankList);
		
		writeRankList(g, input.getOutputDir() + "go_candidate_gene_rank.txt", rankList);
		
		System.out.println("Finished.");		
	}
}
