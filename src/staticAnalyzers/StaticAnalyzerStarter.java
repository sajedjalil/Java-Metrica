package staticAnalyzers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.base.Strings;

import main.DirExplorer;

public class StaticAnalyzerStarter {
	//file paths
	public ArrayList<String> filePaths = new ArrayList<String>();
	
	CompilationUnit cu = null;
	LineOfCodes lc = null;
	CommentFetecher commentVisitor = null;
	ConditionalStatementFetcher complexityVisitor = null;
	MethodDeclearationFinder mdf = null;
	ArrayList<MethodData> allMethodData = new ArrayList<MethodData>();
	
	public StaticAnalyzerStarter(File projectDir) {
		
		//input directory base path
		getFilePaths(projectDir);
		
		//analyze for each file
		for(String path: filePaths) {
			
			String absolutePath = projectDir.toString() + path;
			/* find method name and line range in file
			 * Also Contains method data
			 */ 
			mdf =  new MethodDeclearationFinder(absolutePath);
			
			allMethodData.addAll(mdf.methods);
			
			analyze(absolutePath);
			//printstaticAnalysisResult(path);
			
		}
	}
	
	
	
	
	
	private void analyze(String absoluteFilePath) {
		
		try {
			cu = JavaParser.parse(new File(absoluteFilePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		lc = new LineOfCodes(absoluteFilePath);
		
		commentVisitor = new CommentFetecher();
		commentVisitor.visit(cu, null);
		
		
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(path);
            
        }).explore(projectDir);
	}
	
	
	public void printstaticAnalysisResult(String path) {
		
		System.out.println(path);
		System.out.println(Strings.repeat("=", path.length()));
		System.out.println("LOC: "+ lc.totalStatement);
		System.out.println("Single Comment Lines: "+ commentVisitor.singleCommentLines);
		System.out.println("Multiple Comment Lines: "+ commentVisitor.multipleCommentsLine);
		
		//System.out.println("\n");
		for(MethodData md: allMethodData) {
			System.out.println(md.methodName+" "+md.complexity);
		}
	}
	

	
	
	
}
