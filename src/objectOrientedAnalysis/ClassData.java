package objectOrientedAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.common.base.Optional;

public class ClassData {
	ArrayList<String> fields = new ArrayList<>();
	ArrayList<String> methods = new ArrayList<>();
	HashMap<String,ArrayList<String>> nodes = new HashMap<>();
	ClassOrInterfaceDeclaration cu;
	String parent,file;
	private int inheritanceDepth = 0,noOfChildren = 0;
	
	public ClassData(ClassOrInterfaceDeclaration cu,String file) {
		this.cu = cu;
		this.file = file;
		collectClassData();
		createGraph();
		NodeList<ClassOrInterfaceType> pars = cu.getExtendedTypes();
		if(pars.size()>0) parent = file+"/"+pars.get(0).getNameAsString();
	}
	
	public int getCohesion() {
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
	
	private void collectClassData() {
		cu.findAll(FieldDeclaration.class).forEach(fd ->{
            for(VariableDeclarator v:fd.getVariables()) {
            	fields.add(v.getNameAsString());
            	nodes.put(v.getNameAsString(), new ArrayList<>());
            }
		});
		cu.findAll(MethodDeclaration.class).forEach(m ->{
            methods.add(m.getNameAsString());
            nodes.put(m.getNameAsString(), new ArrayList<>());
		});
	}
	
	private void createGraph(){
		cu.findAll(MethodDeclaration.class).forEach(m ->{
            createMethodEdges(m);
		});
	}
	
	private void createMethodEdges(MethodDeclaration m) {
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
	
	
	private void dfs(String v,HashMap<String,Boolean> vis) {
		vis.put(v,true);
		for(String mate:nodes.get(v)) {
			if(!vis.get(mate)) dfs(mate,vis);
		}
	}
	
	void setInheritanceDepth(int c) {
		this.inheritanceDepth = c;
	}
	
	void setNoChilds(int c) {
		this.noOfChildren = c;
	}
}
