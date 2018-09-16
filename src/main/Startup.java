package main;

import java.io.File;

import objectOrientedAnalysis.ObjectOrientedAnalyzer;
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
		
		//inputDirectory = new File(System.getProperty("user.dir")+"/../JAVA-OOP");
		//inputDirectory = new File(System.getProperty("user.dir")+"/../proguard6.0.3/core/src/proguard/io");
		inputDirectory = new File(System.getProperty("user.dir")+"/../proguard6.0.3");
		
		new StaticAnalyzerStarter(inputDirectory);
   	 	new ObjectOrientedAnalyzer( inputDirectory );
   	 	
   	 	
	}
}
