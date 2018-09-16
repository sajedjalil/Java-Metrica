package staticAnalyzers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class MethodData {
	
	String methodName = null;
	ArrayList<Integer> methodRange = new ArrayList <Integer>();
	
	String basePath = null;
	String filePath = null;
	int complexity = 1;
	
	public MethodData(String name, List<String> list, String basePath, String filePath) {
		
		methodName = name;
		this.basePath =basePath;
		this.filePath = filePath;
		
		for(String s: list) {
			
			methodRange.add( Integer.parseInt(s) );
		}
		
		
	}
	
	
	
}
