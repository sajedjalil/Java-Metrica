package objectOrientedAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import main.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;


public class PackageData {
	private final String FILE_PATH = "E:\\JAVA-OOP\\src\\com\\broccolinisoup\\oop\\general\\aboutThisKeyword\\";
	private ArrayList<String> filePaths = new ArrayList<>();
	private ArrayList<String> classNames = new ArrayList<>();
	private HashMap<String,ClassData> classObj = new HashMap<>();
	private HashMap<String,ArrayList<String>> parToChild = new HashMap<>();
	
	public PackageData() throws FileNotFoundException {
		getFilePaths(new File(FILE_PATH));
		getClassPaths();	
		mapParToChild();
		System.out.println("HALUM");
		System.out.println(calculateNoOfChildren());
		System.out.println(calculateInheritanceDepth());
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
			CompilationUnit cu = JavaParser.parse(new File(file));
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				String cname = file+"/"+cli.getNameAsString();
				classNames.add(cname);
				classObj.put(cname,new ClassData(cli,file));
				parToChild.put(cname,new ArrayList<>());
			});
		}
	}
	
	private int countFanOut(ClassOrInterfaceDeclaration classDef) {
		HashSet<String> refs = new HashSet<>();
		classDef.findAll(ObjectCreationExpr.class).forEach(oce -> {	
			System.out.println(oce.getType().getNameAsString());
			if(classNames.contains(oce.getType().getNameAsString())) {
				refs.add(oce.getTypeAsString());
				System.out.println(classDef.getNameAsString()+","+oce.getType().getNameAsString());
			}
		});
		return refs.size();
	}
	
	private void mapParToChild() {
		System.out.println("INSIDE"+parToChild);
		for(String cls:classNames) {
			ClassData obj = classObj.get(cls);
			if(obj.parent==null || !classNames.contains(obj.parent)) continue;
			parToChild.get(obj.parent).add(cls);
		}
	}
	
	
	
}
