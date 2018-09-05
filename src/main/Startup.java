package main;

import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import staticAnalyzers.CommentFetecher;
import staticAnalyzers.LinesOfCode;

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
		
		LinesOfCode lc = new LinesOfCode(inputDirectory);
   	 
   	 	System.out.println("Total LOC: "+ lc.getTotalLine(inputDirectory +  lc.filePaths.get(0)) );
   	 	
   	 	
   	 	CompilationUnit cu = JavaParser.parse(new File(inputDirectory +  lc.filePaths.get(0))); 
   	 	VoidVisitor<?> commentVisitor = new CommentFetecher(); 
   	 	
   	 	commentVisitor.visit(cu, null);
   	 	((CommentFetecher) commentVisitor).print();
   	 	
	}
}
