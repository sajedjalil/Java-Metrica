package main;

import java.io.File;

import staticAnalyzers.LinesOfCode;

public class Startup {

	public static void main(String[] args) {
		
		System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
    	 File projectDir = new File(System.getProperty("user.dir"));
    	 
    	 new LinesOfCode(projectDir).print();
	}

}
