package staticAnalyzers;

import java.io.File;
import java.util.ArrayList;
import com.google.common.base.Strings;

import main.DirExplorer;

public class StaticAnalyzerStarter {
	//file paths
	public ArrayList<String> filePaths = new ArrayList<String>();
	public ArrayList<LineOfCodes> lineOfCodesStats = new ArrayList<LineOfCodes>();
	
	
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
		
		//loc, comment, method name, cyclomatic complexity
		for(String path: filePaths) {
			
			LineOfCodes lc = new LineOfCodes(projectPath+path);
			lineOfCodesStats.add( lc );
			
			mdf =  new MethodDeclearationFinder(projectPath, path);
			
			//allMethodData.addAll(mdf.methods);
			                        
			                      //last element
			allMethodData.addAll( getCyclomaticComplexity( lc.fileText, mdf.methods) );
		}	
		
	}
	
	private String keyWords[] = {"if", "else if", "for", "do", "while"};
	
	private ArrayList<MethodData> getCyclomaticComplexity(ArrayList<String>lines, ArrayList<MethodData>md) {
		
		
		for(int i=0; i<md.size(); i++) {
			//System.out.print(md.get(i).filePath + " ");
			//System.out.println(md.get(i).methodName);
			int complexity = 1;
			
			
			
			int start = md.get(i).methodRange.get(0);
			int last = md.get(i).methodRange.get(2);
			
			for(int j=start; j<last; j++) {
				
				for(String s: keyWords) {
					if( lines.get(j).contains(s)) {
						complexity++;
						break;
					}
				}
			}
			
			md.get(i).complexity = complexity;
		}
		return md;
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
