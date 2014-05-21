package experiment;

import id.HprdIdMapping;
import id.HprdIdMappingUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneDiseaseAssociation {
	public final Map<String, Gene> geneMap = new HashMap<String, Gene>();
	public final Map<String, Disorder> disorderMap = new HashMap<String, Disorder>();
	
	public void read(){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId_in_ppi.txt";
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		
		read(geneDiseaseAssociationFilepath);
	}
	
	public void read(String geneDiseaseAssociationFilepath){
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = HprdIdMappingUtil.getOmimIdIndexedIdMapping();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(geneDiseaseAssociationFilepath)));
			String line = null;
			HprdIdMapping hprdIdMapping = null;
			String[] cols = null;
			line = in.readLine();
			
			Disorder disorder = null;
			Gene gene = null;
			
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				
				disorder = disorderMap.get(cols[1]);
				if(disorder == null){
					disorder = new Disorder(cols[1], cols[2]);
				}
				String flag = "ENTREZ_ID";
				hprdIdMapping = entrezIdIndexedIdMappingMap.get(cols[0]);
				if(hprdIdMapping == null){
					hprdIdMapping = omimIdIndexedIdMappingMap.get(cols[0]);
					flag = "OMIM_ID";
					
					if(hprdIdMapping == null){
						//System.out.println(line);
						continue;
					}
				}
				
				gene = geneMap.get(cols[0]);
				if(gene == null){
					gene = new Gene(hprdIdMapping.getOmimId(), hprdIdMapping.getGeneSymbol(), hprdIdMapping.getMainName(),
							hprdIdMapping.getHrpdId());
					if("ENTREZ_ID".equals(flag)){
						gene.setGeneNameInPPI(cols[0]);
						//System.out.println("ENTREZ_ID");
					}else if("OMIM_ID".equals(flag)){
						gene.setGeneNameInPPI(gene.getHprdId());
						//System.out.println("OMIM_ID");
					}
					
				}
				
				gene.disorderMap.put(disorder.getOmimId(), disorder);
				disorder.geneMap.put(gene.getGeneNameInPPI(), gene);
				
				geneMap.put(gene.getGeneNameInPPI(), gene);
				disorderMap.put(disorder.getOmimId(), disorder);
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args){
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read();
		
		System.out.println("Gene number: " + associations.geneMap.size());
		System.out.println("Disorder number: " + associations.disorderMap.size());
		
	}
}
