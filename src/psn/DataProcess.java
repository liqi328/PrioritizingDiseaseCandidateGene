package psn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 预处理数据
 * */
public class DataProcess {
	
	public static void main(String[] args){
		processExperimentsData();
	}
	
	private static void processExperimentsData(){
		String inputFilename = "E:/2013疾病研究/疾病数据/PSN/data_expts1-300_meas.txt";
		String outputFilename = "E:/2013疾病研究/疾病数据/PSN/data_expts1-300_meas_pvalue.txt";
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFilename));
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
			String line = null;
			in.readLine();
			in.readLine();
			
			line = in.readLine();
			String[] header = processHeadLine(line);
			writeHeader(out, header);
			
			in.readLine();
			
			StringBuffer sb = new StringBuffer();
			String[] cols = null;
			int i = 0;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				sb.append(cols[0]).append("\t");
				sb.append(cols[1]).append("\t");
				
				for(i = 5;i<cols.length; i+=4){
					sb.append(cols[i]).append("\t");
				}
				sb.append("\n");
			}
			out.write(sb.toString());
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] processHeadLine(String line){
		String[] header = line.split("\t");
		List<String> headerList = new ArrayList<String>();
		for(String s : header){
			if(!s.equals("")){
				headerList.add(s);
			}
		}
		System.out.println(headerList);
		return headerList.toArray(new String[]{});
	}
	
	private static void writeHeader(BufferedWriter out, String[] header) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("ORF_code\t").append("gene_name").append("\t");
		for(String h: header){
			sb.append(h).append("\t");
		}
		sb.append("\n");
		
		out.write(sb.toString());
	}
}
