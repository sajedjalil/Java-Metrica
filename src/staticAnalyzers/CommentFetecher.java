package staticAnalyzers;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CommentFetecher extends  VoidVisitorAdapter<Void>{
	
	
	/*
	@Override
	public void visit(BlockComment bc, Void arg ) {
		super.visit(bc, arg); 
		System.out.println(bc.getContent());
		
	}
	*/
	
	@Override
	public void visit(LineComment lc, Void arg ) {
		super.visit(lc, arg); 
		System.out.println(lc.getContent());
		
	}
	
}
