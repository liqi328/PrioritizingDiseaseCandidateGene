package go;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2013-7-5 下午09:18:19 
 * 
 */

public class Main {
	
	private DAGanalysis dag = new DAGanalysis();
	private ProteinAnnotation pa = new ProteinAnnotation();
	private final String root_mf = "GO:0003674";
	private final String root_bp = "GO:0008150";
	private final String root_cc = "GO:0005575";
	
	public Main() throws IOException{
		pa.readGeneOntology("go/human_mf");
		dag.buildDAG("go/GO_ontology_mf");
		dag.rankDAG(root_mf);
	}
	
	/**
	 * 根据给定的两个蛋白质 计算它们之间的相似性
	 * @param p1
	 * @param p2
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
	
	private static Set<String> read(){
		String filename = "./input/debug_id_mapping.txt";
		Set<String> list = new HashSet<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			
			while((line = in.readLine()) != null){
				list.add(line.split("\t")[3].trim());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static void test() throws IOException{
		Main main = new Main();
		Set<String> symbolSet = read();
		for(String symbol : symbolSet){
			for(String sym2 : symbolSet){
				if(!symbol.equals(sym2)){
					System.out.println(symbol + "\t" + sym2 + "\t" + main.ppsimilarity(symbol,sym2));
				}
			}
		}
	}
	
	private static void test2() throws IOException{
		Main main = new Main();
		String[] candidate = {"NOS3", "APP", "APOE", "MPO", "APBB2", "ACE", "PSEN1", "PSEN2",
				"A2M", "SORL1", "PLAU", "PAXIP1", "BLMH"};
		double score = 0.0,tmp;
		for(int i = 0; i < candidate.length; ++i){
			score = 0.0;
			for(int j = 0; j < candidate.length; ++j){
				if(i != j){
					tmp = main.ppsimilarity(candidate[i], candidate[j]);
					//System.out.println(candidate[i] + "\t" + candidate[j] + "\t" + tmp);
					score += tmp;
				}
			}
			System.out.println("-->" + candidate[i] + "\t" + score);
		}
	}
	
	public static void main(String args[]) throws IOException{
		Main main = new Main();
		System.out.println(main.ppsimilarity("PSEN2","PSEN2"));
		System.out.println(main.ppsimilarity("PSEN2","APP"));
		System.out.println(main.ppsimilarity("APP","BLMH"));
		System.out.println(main.ppsimilarity("PIP5KL1","BLMH"));
		System.out.println(main.ppsimilarity("GEM","PIP5KL1"));
		System.out.println("----------------------------------");
		//test();
		test2();
		System.out.println("----------------------------------");
		System.out.println(main.ppsimilarity("GEM","GEM"));
		System.out.println(main.ppsimilarity("PIP5KL1","PIP5KL1"));
		System.out.println(main.ppsimilarity("GEM","-"));
		System.out.println("----------------------------------");
		System.out.println(main.ppsimilarity("APOE","SORL1"));
		System.out.println(main.ppsimilarity("SORL1","APOE"));
		
		System.out.println(main.ppsimilarity("MPO","ACE"));
		System.out.println(main.ppsimilarity("ACE","MPO"));
	}
}
