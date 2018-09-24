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
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.common.base.Optional;

public class ClassData {
	ArrayList<String> fields = new ArrayList<>();
	ArrayList<String> methods = new ArrayList<>();
	HashMap<String,ArrayList<String>> nodes = new HashMap<>();
	ClassOrInterfaceDeclaration clsDec;
	String parent,file;
	private int inheritanceDepth = 0,noOfChildren = 0,rfc = 0,cmntSize = 0,cycComplex = 0;
	
	
	public ClassData(ClassOrInterfaceDeclaration cu,String file) {
		this.clsDec = cu;
		this.file = file;
		collectClassData();
		createGraph();
		NodeList<ClassOrInterfaceType> pars = cu.getExtendedTypes();
		if(pars.size()>0) parent = pars.get(0).getNameAsString();
	}
	
	public int cmntLoc() {
		cmntSize = clsDec.findAll(LineComment.class).size();
		clsDec.findAll(MethodDeclaration.class).forEach(md->{
			cmntSize += md.findAll(LineComment.class).size();
		});
		int block = clsDec.findAll(JavadocComment.class).size();
		//System.out.println(block);
		return  cmntSize;
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
	
	public int getRFC() {
		clsDec.findAll(MethodCallExpr.class).forEach(mce -> {
			rfc++;
		});
		return rfc + methods.size();
	}
	
	int cyc() {
		cycComplex += 1;
		clsDec.findAll(MethodDeclaration.class).forEach(md->{
			cycComplex += md.findAll(IfStmt.class).size();
			cycComplex += md.findAll(ForeachStmt.class).size();
			cycComplex += md.findAll(ForStmt.class).size();
			cycComplex += md.findAll(WhileStmt.class).size();
			cycComplex += md.findAll(DoStmt.class).size();
		});
		return cycComplex;
	}
	
	int weightedCyc(){
		if(cycComplex==0) cyc();
		return methods.size()==0?1: cycComplex/methods.size();
	}
	
	private void collectClassData() {
		clsDec.findAll(FieldDeclaration.class).forEach(fd ->{
            for(VariableDeclarator v:fd.getVariables()) {
            	fields.add(v.getNameAsString());
            	nodes.put(v.getNameAsString(), new ArrayList<>());
            }
		});
		clsDec.findAll(MethodDeclaration.class).forEach(m ->{
            methods.add(m.getNameAsString());
            nodes.put(m.getNameAsString(), new ArrayList<>());
		});
	}
	
	private void createGraph(){
		clsDec.findAll(MethodDeclaration.class).forEach(m ->{
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
			 if(nodes.containsKey(fae.getNameAsString())) {
				 String[] tmp = fae.toString().split("\\.");
				 if(tmp[0].equals("this")) {
					 nodes.get(m.getNameAsString()).add(fae.getNameAsString());
					 nodes.get(fae.getNameAsString()).add(m.getNameAsString());
				 }
			 }
		 });
	}
	
	
	private void dfs(String v,HashMap<String,Boolean> vis) {
		vis.put(v,true);
		for(String mate:nodes.get(v)) {
			if(!vis.get(mate)) dfs(mate,vis);
		}
	}
	

}
