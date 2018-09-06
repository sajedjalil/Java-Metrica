package objectOrientedAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import main.DirExplorer;

public class ObjectOrientedAnalyzer {
	
	String basepath;
	public ArrayList<String> filePaths = new ArrayList<String>();
	public ArrayList<ClassInfo> classes = new ArrayList<ClassInfo>();
	
	Map<String, ArrayList<String>> map = new HashMap<String,  ArrayList<String>>();
	
	
	public ObjectOrientedAnalyzer(File basePath) {
		
		this.basepath = basePath.toString();
		
		getFilePaths( basePath );
		initiateClassObjects();
		
		mapChildToParent();
		findChildNumber();
		
		printResult();
	}
	
	private void printResult() {
		for( ClassInfo c: classes) {
			System.out.println(c.filePath + " "+c.totalMethods);
		}
	}
	
 	private void initiateClassObjects() {
		
		for(String path: filePaths) {
			path = basepath + path;
			
			ClassInfo temp = new ClassInfo(path);
			if(temp.className != null) classes.add( temp );
		}
		
		
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			
			filePaths.add(path);
        }).explore(projectDir);
	}
	
	private void mapChildToParent() {
		
		for(ClassInfo c: classes) {
			
			if( !c.parentClass.equals("") ) {

				if (!map.containsKey(c.parentClass )) {
		            map.put( c.parentClass, new ArrayList<String>());
		        }
		        map.get(c.parentClass).add(c.className);
			}
		}
	}
	
	private void findChildNumber() {
		
		
		for(ClassInfo c: classes) {
			
			if ( map.containsKey(c.className )) {
				int totalChilds = map.get(c.className).size();
				c.totalChilds = totalChilds;
				c.childs = map.get(c.className);
			}
			
			
		}
	}
	
	
	
}
