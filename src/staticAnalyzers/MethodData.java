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
	
	String absoluteFilePath = null;
	int complexity = 1;
	
	public MethodData(String name, List<String> list, String absolutePath) {
		
		methodName = name;
		absoluteFilePath = absolutePath;
		
		for(String s: list) {
			
			methodRange.add( Integer.parseInt(s) );
		}
		
		calculateComplexity();
	}
	
	private void calculateComplexity() {
		
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(new File(absoluteFilePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConditionalStatementFetcher complexityVisitor = new ConditionalStatementFetcher(); 
   	 	complexityVisitor.visit(cu, methodRange);
   	 	
   	 	complexity = complexityVisitor.complexity;
	}
	
	
}
