package staticAnalyzers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LineOfCodes {
	
	public int loc = 0;
	
	public LineOfCodes(String filePath) {
		
		loc = getTotalLine(filePath);
	}
	
	public int getTotalLine(String filePath) {
		
		BufferedReader br = null;
		FileReader fr = null;
		int counter = 0;
		
		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String sCurrentLine;

			
			
			while ( (sCurrentLine = br.readLine()) != null ) {
				
				
				if( ifLineIsUseless(sCurrentLine) == true ) counter++;
				
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		return counter;
	}
	
	
	private static boolean ifLineIsUseless (String line) {
		String vhejals[] = {"package", "import"};
		
		//if line contains unnecessary stuffs
		for(String s:vhejals) {
			if(line.contains(s) == true) {
				return false;
			}
		}
		
		//if line is blank
		if( line.trim().equals("") ) return false;
		
		return true;
	}
	
	
}
