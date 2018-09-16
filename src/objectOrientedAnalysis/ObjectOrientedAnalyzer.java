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
	Map<String, Integer> classMapping = new HashMap<String,  Integer>();
	
	
	public ObjectOrientedAnalyzer(File basePath) {
		
		this.basepath = basePath.toString();
		
		getFilePaths( basePath );
		initiateClassObjects();
		
		mapChildToParent();
		findChildNumber();
		findDepthOfInheritence();
		
		printResult();
	}
	
	private void printResult() {
		for( ClassInfo c: classes) {
			System.out.println(c.filePath + "\t"+c.totalMethods+"\t"+c.totalChilds+"\t"+c.depthOfInheritence);
		}
	}
	
 	private void initiateClassObjects() {
		
		for(String path: filePaths) {
			
			ClassInfo temp = new ClassInfo(basepath , path);
			if(temp.className != null) {
				classes.add( temp );
				classMapping.put( temp.className, classes.size()-1 );
			}
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
	
	private void findDepthOfInheritence() {
		
		for(ClassInfo c: classes) {
			
			c.depthOfInheritence = dfs(c, 0);
		}
		
	}
	
	private int dfs(ClassInfo c, int level) {
		
		//System.out.println(c.filePath);
		// c has a parent class and the parent class is not an library class
		if(!c.parentClass.equals("") &&  classMapping.get( c.parentClass) != null) {
			//System.out.println( );
			level += dfs( classes.get( classMapping.get( c.parentClass) ), level+1);
		}
		
		
		return level;
	}
	
}
