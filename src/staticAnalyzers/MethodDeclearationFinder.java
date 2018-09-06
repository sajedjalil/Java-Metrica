package staticAnalyzers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class MethodDeclearationFinder {
	
	ArrayList<MethodData> methods = new ArrayList<MethodData>();
	
	public MethodDeclearationFinder(String filePath) {
		
		listClasses(filePath);
	}
	
	
	private void listClasses(String absoluteFilePath) {
        	
		//System.out.println(absoluteFilePath);
        try {
            new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        
                        methods.add( makeMethodData(n , absoluteFilePath) );
                    }
                }.visit(JavaParser.parse(new File(absoluteFilePath) ), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }

    }
 
	private MethodData makeMethodData(MethodDeclaration n, String absoluteFilePath) {
		
		String name = n.getNameAsString();
		String range = n.getRange().toString();
		
		range = range.replaceAll("[^0-9]+", " "); 
	    List<String> list = Arrays.asList(range.trim().split(" "));

	    return new MethodData(name, list, absoluteFilePath);
	}
}
