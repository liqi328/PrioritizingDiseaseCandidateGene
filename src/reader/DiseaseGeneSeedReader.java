package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DiseaseGeneSeedReader {
	public static Set<String> read(String filename){
		Set<String> ret = new HashSet<String>();
		String line = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			while((line = in.readLine()) != null){
				ret.add(line.trim());
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
