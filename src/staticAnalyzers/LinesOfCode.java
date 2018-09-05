package staticAnalyzers;

import java.io.File;
import java.util.ArrayList;


import main.DirExplorer;

public class LinesOfCode {
	
	ArrayList<String> filePaths = new ArrayList<String>();
	
	public LinesOfCode(File projectDir) {
		// TODO Auto-generated constructor stub
		getFilePaths(projectDir);
	}
	
	
	private void getFilePaths( File projectDir ) {
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(path);
            
        }).explore(projectDir);
	}
	
	public void print() {
		for(String s: filePaths) {
			System.out.println(s);
		}
	}
	
}
