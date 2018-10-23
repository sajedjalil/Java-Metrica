package objectOrientedAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

//import main.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.FileNotFoundException;
import java.util.HashSet;

import com.github.javaparser.ast.expr.ObjectCreationExpr;


public class PackageData {
	private final String FILE_PATH;
	private ArrayList<String> filePaths = new ArrayList<>();
	public ArrayList<String> classNames = new ArrayList<>();
	private HashMap<String,ClassData> classObj = new HashMap<>();
	private HashMap<String,ArrayList<String>> parToChild = new HashMap<>();
	
	public PackageData(String filePath) throws FileNotFoundException {
		FILE_PATH = filePath;
		
		getFilePaths(new File(FILE_PATH));
		
		getClassPaths();	
		//System.out.println(classNames.size()+"lllll");
		mapParToChild();
		/*System.out.println("HALUM");
		System.out.println(calculateNoOfChildren());
		System.out.println(calculateInheritanceDepth());*/
	}
	
	public HashMap<String,Integer> calculateCoupling() throws FileNotFoundException {
		HashMap<String,Integer> fanouts = new HashMap<String,Integer>();
		for(String cls:classObj.keySet()) {
			ClassData obj = classObj.get(cls);
			fanouts.put(cls,countFanOut(obj.clsDec));
		}
		return fanouts;
	}
	
	public HashMap<String,Integer> calculateCohesion(){
		HashMap<String,Integer> cohs = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			cohs.put(cls,obj.getCohesion());
		}
		return cohs;
	}
	
	public HashMap<String,Integer> calculateCyc(){
		HashMap<String,Integer> cc = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			cc.put(cls,obj.cyc());
		}
		return cc;
	}
	
	public HashMap<String,Integer> calculateWeightedCyc(){
		HashMap<String,Integer> cc = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			cc.put(cls,obj.weightedCyc());
		}
		return cc;
	}
	
	public HashMap<String,Integer> calculateRFC(){
		HashMap<String,Integer> rfcs = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			rfcs.put(cls,obj.getRFC());
		}
		return rfcs;
	}
	
	public HashMap<String, Integer> calculateNoOfChildren(){
		HashMap<String,Integer> inhDepth = new HashMap<String,Integer>();
		for(String cls:parToChild.keySet()) {
			inhDepth.put(cls,parToChild.get(cls).size());
		}
		return inhDepth;
	}
	
	public HashMap<String, Integer> calculateLOC(){
		HashMap<String,Integer> loc = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			int x = obj.clsDec.getRange().get().end.line-obj.clsDec.getRange().get().begin.line;
			loc.put(cls,x);
		}
		return loc;
	}
	
	public HashMap<String, Integer> calculateCmntLOC(){
		//System.out.println("JKBHKVBK");
		HashMap<String,Integer> loc = new HashMap<String,Integer>();
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			loc.put(cls,obj.cmntLoc());
		}
		//System.out.println(loc);
		return loc;
	}
	

	
	public HashMap<String,Integer> calculateInheritanceDepth(){
		HashMap<String,Integer> inhDepth = new HashMap<String,Integer>();
		for(String cls:classNames) {
			int depth = 0;
			ClassData obj = classObj.get(cls);
			while(obj.parent!=null && classNames.contains(obj.parent)) {
				depth++;
				obj = classObj.get(obj.parent);
			}
			inhDepth.put(cls,depth);
		}
		return inhDepth;
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(projectDir.getAbsoluteFile()+ path);
            
        }).explore(projectDir);
	}
	
	private void getClassPaths() throws FileNotFoundException {
		for(String file : filePaths) {
			//System.out.println(file);
			CompilationUnit cu = JavaParser.parse(new File(file));
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				//System.out.println("PAISI");
				String cname = cli.getNameAsString();
				classNames.add(cname);
				classObj.put(cname,new ClassData(cli,file));
				parToChild.put(cname,new ArrayList<>());
			});
		}
		
		
	}
	
	private int countFanOut(ClassOrInterfaceDeclaration classDef) {
		HashSet<String> refs = new HashSet<>();
		classDef.findAll(ObjectCreationExpr.class).forEach(oce -> {	
			//System.out.println(oce.getType().getNameAsString());
			if(classNames.contains(oce.getType().getNameAsString())) {
				refs.add(oce.getTypeAsString());
				//System.out.println(classDef.getNameAsString()+","+oce.getType().getNameAsString());
			}
		});
		return refs.size();
	}
	
	private void mapParToChild() {
		//System.out.println("JJJJJJ:"+classNames);
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			//System.out.println(cls+","+obj.parent);
			if(obj.parent==null || !classNames.contains(obj.parent)) continue;
			parToChild.get(obj.parent).add(cls);
		}
		//System.out.println("Par to child:"+parToChild);
	}
	
	
	
}
