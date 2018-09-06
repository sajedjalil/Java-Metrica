package main;

import java.io.File;

import staticAnalyzers.StaticAnalyzerStarter;

public class Startup {
	
	File inputDirectory;
	
	
	public static void main(String[] args) {
		
		new Startup();
	}
	
	
	public Startup() {
		
		try {
			initializer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializer () throws Exception {
		
		inputDirectory = new File(System.getProperty("user.dir"));
		
		new StaticAnalyzerStarter(inputDirectory);
   	 
   	 	
   	 	
	}
}
