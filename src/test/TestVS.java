package test;

import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;
import similarity.SimilarityAlgorithm;
import similarity.VSSimilarityAlgorithm;

public class TestVS {
	private static Map<String, Map<String, Double>> similarityMap = new HashMap<String, Map<String, Double>>();
	
	public static void main(String[] args){
		String seedFilename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/fanconi_anemia.txt";
		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/tissue_specific_PPI.txt";
		Graph g = GraphReader.read(filename);
		Set<String> seeds = DiseaseGeneSeedReader.read(seedFilename);
		List<String> seedsList = new ArrayList<String>();
		Collections.addAll(seedsList, seeds.toArray(new String[]{}));
		
		calculateSimilarity(g, seedsList);
		
		System.out.println(print_similarityMap());
	}
	
	private static void initSimilarityMap(List<String> seeds){
		for(String seed : seeds){
			similarityMap.put(seed, new HashMap<String, Double>());
		}
	}
	
	private static void calculateSimilarity(Graph g, List<String> seeds){
		SimilarityAlgorithm vs = new VSSimilarityAlgorithm();
		
		initSimilarityMap(seeds);
		double similarity = 0.0;
		String a = null, b = null;
		
		for(int i = 0; i < seeds.size(); ++i){
			a = seeds.get(i);
			for(int j = i + 1; j < seeds.size(); ++j){
				b = seeds.get(j);
				similarity = vs.calculate(g.getNodeIndex(a), g.getNodeIndex(b), g.getAdjMatrix());
				similarityMap.get(a).put(b, similarity);
				similarityMap.get(b).put(a, similarity);
			}
		}
	}
	
	private static String print_similarityMap(){
		StringBuffer sb = new StringBuffer();
		sb.append("-----------------similarity------------------\n");
		
		Iterator<Map.Entry<String, Map<String, Double>>> itr = similarityMap.entrySet().iterator();
		Map.Entry<String, Map<String, Double>> entry = null;
		while(itr.hasNext()){
			entry = itr.next();
			String key = entry.getKey();
			Map<String, Double> value = entry.getValue();
			
			Iterator<Map.Entry<String, Double>> itr2 = value.entrySet().iterator();
			Map.Entry<String, Double> entry2 = null;
			while(itr2.hasNext()){
				entry2 = itr2.next();
				sb.append(key).append("\t");
				sb.append(entry2.getKey()).append("\t");
				sb.append(entry2.getValue()).append("\n");
			}
			sb.append("-----------------------------------------\n");
		}
		
		return sb.toString();
	}
}
