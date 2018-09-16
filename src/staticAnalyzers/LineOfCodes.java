package staticAnalyzers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LineOfCodes {
	
	ArrayList<String> fileText = new ArrayList<String>();
	
	public int physicalLoc = 0;
	public int totalStatement = 0;
	public int blankLines = 0;
	public int singleCommentLines = 0;
	public int multipleCommentLines = 0;
	
	
	public LineOfCodes(String filePath) {
		
		//loc = getTotalLine(filePath);
		loadFile(filePath);
		
		analyze();
		
		System.out.println(filePath);
		print();
	}
	
	
	private void print() {
		System.out.println(physicalLoc);
		System.out.println(totalStatement);
		System.out.println(blankLines);
		System.out.println(singleCommentLines);
		System.out.println(multipleCommentLines);
		
	}
	
	private void analyze( ) {
		
		setPhysicalLoc();
		setBlankLines();		
		setTotalStatements();
		
		commentLines();
	}
	
	private void setPhysicalLoc() {
		physicalLoc = fileText.size();
	}
	
	private void setTotalStatements() {

		
		for(String s: fileText) {
			
			Boolean flag= ifLineIsUseless(s);
			
			if( flag == true ) {
				for(int i=0; i<s.length(); i++) {
					
					if(s.charAt(i) == ';') totalStatement++;
				}
			}
			
		}
	}
	
	
	private void setBlankLines() {
		
		for(String s: fileText) {
			if( s.trim().equals("") ) blankLines++;
		}
	}
	
	private void commentLines() {
		
		int multipleLineCommentFlag = 0;
		int doubleQuoteFlag = 0;
		int multipleCommentLineStart = 0;
		
		
		for(int line=0; line<fileText.size(); line++) {
			
			String s = fileText.get(line);
			
			for(int i=0; i<s.length()-1; i++) {
				
				if( multipleLineCommentFlag==0 && doubleQuoteFlag==0 ) {
					
					if( s.charAt(i) == '/' && s.charAt(i+1)=='/') {
						singleCommentLines++;
						break;
					}
					else if( s.charAt(i) == '/' && s.charAt(i+1)=='*') {
						multipleCommentLineStart = line;
						multipleLineCommentFlag = 1;
					}
					else if( (int)s.charAt(i) == 34 ) doubleQuoteFlag = 1;
					
				}
				else if( multipleLineCommentFlag == 1) {
					if( s.charAt(i) == '*' && s.charAt(i+1)=='/') {
						multipleLineCommentFlag = 0;
						
						multipleCommentLines += (line-multipleCommentLineStart+1);
					}
				}
				else if( doubleQuoteFlag == 1) {
					if( (int)s.charAt(i) == 34 ) {
						doubleQuoteFlag = 0;
					}
				}
			}
		}
	}
	
	private void loadFile(String filePath) {
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String currentLine;
			while ( (currentLine = br.readLine()) != null ) fileText.add(currentLine);

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
	}
	
	
	
	private static boolean ifLineIsUseless (String line) {
		String vhejals[] = {"package", "import"};
		
		//if line contains unnecessary stuffs
		for(String s:vhejals) {
			if(line.contains(s) == true) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
