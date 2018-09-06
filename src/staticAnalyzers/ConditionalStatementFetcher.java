package staticAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConditionalStatementFetcher extends  VoidVisitorAdapter<ArrayList<Integer>>{
	public int complexity = 1;
	
	public ConditionalStatementFetcher() {
		
	}
	
	@Override
	public void visit(IfStmt bc, ArrayList<Integer> range ) {
		super.visit(bc, range); 
		
		List<String> range1 = Arrays.asList( bc.getRange().toString().replaceAll("[^0-9]+", " ").trim().split(" ") );
		checkIfInRange( range1, range );
		
	}
	
	@Override
	public void visit(DoStmt bc, ArrayList<Integer> range ) {
		super.visit(bc, range); 
		
		List<String> range1 = Arrays.asList( bc.getRange().toString().replaceAll("[^0-9]+", " ").trim().split(" ") );
		checkIfInRange( range1, range );
	}
	
	@Override
	public void visit(WhileStmt bc, ArrayList<Integer> range ) {
		super.visit(bc, range); 
		
		List<String> range1 = Arrays.asList( bc.getRange().toString().replaceAll("[^0-9]+", " ").trim().split(" ") );
		checkIfInRange( range1, range );
	}
	
	@Override
	public void visit(ForeachStmt bc, ArrayList<Integer> range ) {
		super.visit(bc, range); 
		
		List<String> range1 = Arrays.asList( bc.getRange().toString().replaceAll("[^0-9]+", " ").trim().split(" ") );
		checkIfInRange( range1, range );
	}
	
	
	@Override
	public void visit(ForStmt bc, ArrayList<Integer> range ) {
		super.visit(bc, range); 
		
		List<String> range1 = Arrays.asList( bc.getRange().toString().replaceAll("[^0-9]+", " ").trim().split(" ") );
		checkIfInRange( range1, range );
	}
	
	
	private void checkIfInRange(List<String> range1, ArrayList<Integer> range2) {
		
		//System.out.println(range1.get(0)+" "+range1.get(2));
		if( Integer.parseInt( range1.get(0) ) >= range2.get(0) && Integer.parseInt( range1.get(2)) <= range2.get(2) ) complexity++;
	}
	
	public void print() {
		System.out.println("Cyclomatic Complexity: "+ complexity);
	}
}
