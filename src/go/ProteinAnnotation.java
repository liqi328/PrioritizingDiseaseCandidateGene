package go;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ProteinAnnotation {
//	public Map<String,Set<String>> funsToProteins = new HashMap<String,Set<String>>();
	//all the protein to the functions
	//��ǰ������������ÿ�������ʶ�Ӧ�Ĺ�����Ϣ
	public Map<String,Set<String>> proteinsToFunctionsMap = new HashMap<String,Set<String>>();
	
	//��¼�����������б���Ϣ
//	public Map<String,Integer> edgeMap = new HashMap<String,Integer>();
	/**
	 * ��ȡ�����ʹ���ע����Ϣ
	 * read the gene ontology file
	 * @param filename
	 * @throws IOException
	 */
	public void readGeneOntology(String filename) throws IOException{
		proteinsToFunctionsMap.clear();
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		Scanner s = null;
		String protein = null;
		while(str!=null){
			s = new Scanner(str);
			protein = s.next();
			Set<String> funs = new HashSet<String>();
			proteinsToFunctionsMap.put(protein, funs);
			while(s.hasNext()){
				String fun = s.next();
				funs.add(fun);
			}
			str = br.readLine();
		}
		br.close();
	}
	
}
