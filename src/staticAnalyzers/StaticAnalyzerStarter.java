package staticAnalyzers;

import java.io.File;
import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.base.Strings;

import main.DirExplorer;

public class StaticAnalyzerStarter {
	//file paths
	public ArrayList<String> filePaths = new ArrayList<String>();
	public ArrayList<LineOfCodes> lineOfCodesStats = new ArrayList<LineOfCodes>();
	
	CompilationUnit cu = null;
	LineOfCodes lc = null;
	ConditionalStatementFetcher complexityVisitor = null;
	MethodDeclearationFinder mdf = null;
	ArrayList<MethodData> allMethodData = new ArrayList<MethodData>();
	
	public StaticAnalyzerStarter(File projectDir) {
		
		//input directory base path
		getFilePaths(projectDir);
		
		analyze(projectDir.toString());
		//analyze for each file
		
		printstaticAnalysisResult();
		
	}
	
	
	
	
	
	private void analyze(String projectPath) {
		
		
		for(String path: filePaths) {
			
			lineOfCodesStats.add( new LineOfCodes(projectPath+path) );
			
			mdf =  new MethodDeclearationFinder(projectPath, path);
			
			allMethodData.addAll(mdf.methods);
		}
		
		
		
		
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(path);
            
        }).explore(projectDir);
	}
	
	
	public void printstaticAnalysisResult() {
		
		
		int methodSerial = 0;
		
		
		for(int i=0; i<filePaths.size(); i++) {
			System.out.println(Strings.repeat("=", filePaths.get(i).length()));
			System.out.println(filePaths.get(i));
			System.out.println(Strings.repeat("=", filePaths.get(i).length()));
			
			System.out.println("Physical LOC: "+lineOfCodesStats.get(i).physicalLoc);
			System.out.println("Total Statements: "+lineOfCodesStats.get(i).totalStatement);
			System.out.println("Blank LOC: "+lineOfCodesStats.get(i).blankLines);
			System.out.println("Single Comment Lines: "+lineOfCodesStats.get(i).singleCommentLines);
			System.out.println("Multiple Comment Lines: "+lineOfCodesStats.get(i).multipleCommentLines);
			System.out.printf("%s%.2f%s", "Comment Percentage: ", 
					(double)((lineOfCodesStats.get(i).singleCommentLines + lineOfCodesStats.get(i).multipleCommentLines)*100.00) /
					(double)(lineOfCodesStats.get(i).physicalLoc - lineOfCodesStats.get(i).blankLines) , "%\n");
			
			System.out.println(Strings.repeat("-", filePaths.get(i).length()));
			for( ; methodSerial<allMethodData.size(); ) {
				
				if(allMethodData.get(methodSerial).filePath.equals(filePaths.get(i))) {
					System.out.println(allMethodData.get(methodSerial).methodName+"\t"+"--"+"\tComplexity: "+allMethodData.get(methodSerial).complexity);
					methodSerial++;
				}	
				else {
					break;
				}
			}
			
			System.out.println(Strings.repeat("-", filePaths.get(i).length()));
			
		}
	}
	

	
	
	
}
