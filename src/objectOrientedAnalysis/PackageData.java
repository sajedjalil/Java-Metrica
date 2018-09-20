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
	
	public PackageData() throws FileNotFoundException {
		getFilePaths(new File(FILE_PATH));
		getClassPaths();	
		System.out.println(classNames);
	}
	
	public HashMap<String,Integer> calculateCoupling() throws FileNotFoundException {
		HashMap<String,Integer> fanouts = new HashMap<String,Integer>();
		for(String file : filePaths) {
			CompilationUnit cu = JavaParser.parse(new File(file));
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				String clsName = file+"/"+cli.getNameAsString();
				fanouts.put(clsName,countFanOut(cli));
			});
		}
		return fanouts;
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
				classObj.put(cname,new ClassData(cli));
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
}
