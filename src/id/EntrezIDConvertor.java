package id;

import java.util.Map;
import java.util.Set;

import reader.DiseaseGeneSeedReader;
import util.WriterUtil;

/**
 * 将entrez_Id转换为Hprd_id, omim_id
 * @author Liqi
 *
 */
class EntrezIDConvertor extends IDConvertor{
	@Override
	protected Map<String, HprdIdMapping> getHprdIdMappingMap() {
		return HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
	}

	public static void main(String[] args){
		String filename = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_entrez_id.txt";
		Set<String> entrezIdset = DiseaseGeneSeedReader.read(filename);
		
		IDConvertor convertor = new EntrezIDConvertor();
		
		Set<String> hprdIdSet = convertor.converte2hprdId(entrezIdset);		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_hprd_id.txt", hprdIdSet);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_id_mapping.txt",
				convertor.getHprdIdMappingSet(entrezIdset));
		
		Set<String> omimIdset = convertor.converte2omimId(entrezIdset);
		WriterUtil.write("E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet/Achromatopsia_omim_id.txt", omimIdset);
	}
}