package com.liqi.shortest;

import graph.Graph;
import graph.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import reader.GraphReader;
import util.PathUtil;
import alg.ShortestPathAlgorithm;
import alg.SimilarityCalculator;

/**
 * 
 * */
public class ExperimentMain2 {
	private static Map<String, Map<String, List<Path<Integer>>>> shortestPathMap = new HashMap<String, Map<String, List<Path<Integer>>>>();
	
	private static Map<String, Map<String, Double>> similarityMap = new HashMap<String, Map<String, Double>>();
	
	public static void main(String[] args){
		String seedFilename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/fanconi_anemia.txt";
		String filename = "E:/2013¼²²¡ÑÐ¾¿/À¼Î°/tissue_specific_PPI.txt";
		Graph g = GraphReader.read(filename);
		Set<String> seeds = DiseaseGeneSeedReader.read(seedFilename);
		List<String> seedsList = new ArrayList<String>();
		Collections.addAll(seedsList, seeds.toArray(new String[]{}));
		
		calculateShortestPath(g, seedsList);
		calculateSimilarity(g, seedsList);
		
		System.out.println(print_similarityMap());
	}
	
	private static void calculateShortestPath(Graph g, List<String> seeds){
		initShortestPathMap(seeds);
		
		for(int i = 0; i < seeds.size(); ++i){
			for(int j = i + 1; j < seeds.size(); ++j){
				List<Path<Integer>> sp = ShortestPathAlgorithm.calculateAllShortestPath(g.getNodeIndex(seeds.get(i)),
						g.getNodeIndex(seeds.get(j)), g.getAdjMatrix());
	        	shortestPathMap.get(seeds.get(i)).put(seeds.get(j), sp);
	        	shortestPathMap.get(seeds.get(j)).put(seeds.get(i), sp);
	        	
	        	for(Path<Integer> p : sp){
	    			System.out.println(PathUtil.parsePath(p, g));
	    		}
	        	System.out.println("----------------------------------------------");
			}
		}
	}
	
	private static void initShortestPathMap(List<String> seeds){
		for(int i = 0; i < seeds.size(); ++i){
			Map<String, List<Path<Integer>>> spMap = new HashMap<String, List<Path<Integer>>>();
			shortestPathMap.put(seeds.get(i), spMap);
		}
	}
	
	private static void initSimilarityMap(List<String> seeds){
		for(String seed : seeds){
			similarityMap.put(seed, new HashMap<String, Double>());
		}
	}
	
	private static void calculateSimilarity(Graph g, List<String> seeds){
		initSimilarityMap(seeds);
		double similarity = 0.0;
		String a = null, b = null;
		
		for(int i = 0; i < seeds.size(); ++i){
			a = seeds.get(i);
			for(int j = i + 1; j < seeds.size(); ++j){
				b = seeds.get(j);
				similarity = SimilarityCalculator.calculateSimilarity(g, a, b, shortestPathMap.get(a).get(b));
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
