package objectOrientedAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;


public class Maain {
	private static final String FILE_PATH = "E:\\JAVA-OOP\\src\\com\\broccolinisoup\\oop\\general\\aboutThisKeyword\\TEST.java";
	static ArrayList<String> fields = new ArrayList<>();
	static ArrayList<String> methods = new ArrayList<>();
	static HashMap<String,ArrayList<String>> nodes = new HashMap<>();

	public static void main(String[] args) throws FileNotFoundException {
		CompilationUnit cu = JavaParser.parse(new File(FILE_PATH));
		cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cl->{
			collectClassData(cl);
		});
		
		for(String a:fields) nodes.put(a,new ArrayList<>());
		for(String a:methods) nodes.put(a,new ArrayList<>());
		System.out.println(fields);
		System.out.println(methods);

		cu.findAll(MethodDeclaration.class).forEach(m->createMethodEdges(m));
		System.out.println("Printing Graph....");
		for(String k:nodes.keySet()) {
			System.out.println(k+":"+nodes.get(k));
		}
		System.out.println(CC());

	}
	
	static void collectClassData(ClassOrInterfaceDeclaration cu) {
		cu.findAll(FieldDeclaration.class).forEach(fd ->{
            for(VariableDeclarator v:fd.getVariables()) {
            	fields.add(v.getNameAsString());
            }
		});
		cu.findAll(MethodDeclaration.class).forEach(m ->{
            methods.add(m.getNameAsString());
		});
	}
	static void createMethodEdges(MethodDeclaration m) {
		 ArrayList<String> locals = new ArrayList<>();
		 NodeList<Parameter> params = m.getParameters();
		 for(Parameter p:params) locals.add(p.getNameAsString());
	
		 locals.add("HALI");
		 m.findAll(VariableDeclarator.class).forEach(v->{
			 locals.add(v.getNameAsString());
		 });
		 
		 //connect with other methods in this class
		 m.findAll(MethodCallExpr.class).forEach(ne->{
			 if(methods.contains(ne.getNameAsString())){
				 if(ne.getNameAsString().equals(m.getNameAsString())) {} //self-loop
				 else {
					 nodes.get(m.getNameAsString()).add(ne.getNameAsString());
					 nodes.get(ne.getNameAsString()).add(m.getNameAsString());
				 }
			 }
		 });
		 //connect with class vars
		 m.findAll(NameExpr.class).forEach(ne->{
			 if(fields.contains(ne.getNameAsString())) { //if this matches with any class var
				 if(!locals.contains(ne.getNameAsString())) { //but not with local var
					 nodes.get(m.getNameAsString()).add(ne.getNameAsString());
					 nodes.get(ne.getNameAsString()).add(m.getNameAsString());
				 }
			 }
		 });
		 
		 //all this.x kind of stmt can only refer to class vars,can't be caught by NameExpr
		 m.findAll(FieldAccessExpr.class).forEach(fae->{
			 String[] tmp = fae.toString().split("\\.");
			 //Below,no need to check if tmp[1] -in fields
			 if(tmp[0].equals("this")) {
				 nodes.get(m.getNameAsString()).add(fae.getNameAsString());
				 nodes.get(fae.getNameAsString()).add(m.getNameAsString());
			 }
		 });
	}
	
	static int CC() {
		HashMap<String,Boolean> vis = new HashMap<>();
		for(String k:nodes.keySet()) vis.put(k,false);
		int cc = 0;
		for(String k:nodes.keySet()) {
			if(!vis.get(k)) {
				dfs(k,vis);
				cc++;
			}
		}
		return cc;
	}
	static void dfs(String v,HashMap<String,Boolean> vis) {
		vis.put(v,true);
		for(String mate:nodes.get(v)) {
			if(!vis.get(mate)) dfs(mate,vis);
		}
	}
}
