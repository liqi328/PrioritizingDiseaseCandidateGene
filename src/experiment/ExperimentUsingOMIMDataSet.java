package experiment;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import reader.GraphReader;
import util.GraphUtil;
import util.WriterUtil;

/**
 * @author Liqi
 *
 * 使用OMIM数据库的疾病-基因数据
 */
public class ExperimentUsingOMIMDataSet {
	//private static Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = HprdIdMappingUtil.getOmimIdIndexedIdMapping();
	
	public static void main(String[] args){
		String ppiFilename = "./input/ppi_hprd_id.txt";
		Graph g = GraphReader.read(ppiFilename);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read("./input/geneOmimId_diseaseOmimId_in_ppi.txt");
		
		for(Disorder disorder: associations.disorderMap.values()){
			if(disorder.geneMap.size() < 2){
				continue;
			}
			run_one_disease(disorder, g);
			
			//System.out.println(disorder.getName() + " -->completed.");
		}
		System.out.println(associations.disorderMap.size());

	}
	
	private static void run_one_disease(Disorder disorder, Graph g){
		InputArgument inputArg = generateInputArgument(disorder.getOmimId());
		
		Set<Integer> diseaseGeneSeedSet = GraphUtil.transformName2GraphNodeIndex(g, 
				transfer2HprdIdSet(disorder));
		Set<Integer> candidateGeneSet = CandidateGeneGenerator.run(g, diseaseGeneSeedSet);
		
		WriterUtil.write(inputArg.getOutputDir() + "random_candidate_gene.txt",
				GraphUtil.transformGraphNodeIndex2Name(g, candidateGeneSet)); 
		
		List<AbstractExperiment> expList = new ArrayList<AbstractExperiment>();
		expList.add(new ExperimentSP_GO(inputArg));
		
		for(AbstractExperiment exp: expList){
			exp.run(g, diseaseGeneSeedSet, candidateGeneSet);
		}
	}
	
	private static Set<String> transfer2HprdIdSet(Disorder disorder){
		Set<String> hprdIdDiseaseGeneSeedSet = new HashSet<String>();
		hprdIdDiseaseGeneSeedSet.addAll(disorder.geneMap.keySet());
		return hprdIdDiseaseGeneSeedSet;
	}
	
	private static InputArgument generateInputArgument(String diseaseOmimId){
		InputArgument inputArg = new InputArgument();
		inputArg.setHprdIdMappingsFileName("./input/HPRD_ID_MAPPINGS.txt");
		inputArg.setAthreshholdArray("1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0");
		inputArg.setOutputDir("./output/"+ diseaseOmimId + "/");
		return inputArg;
	}
}
