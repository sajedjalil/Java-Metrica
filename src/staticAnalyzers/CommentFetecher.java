package staticAnalyzers;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CommentFetecher extends VoidVisitorAdapter<Void>{
	
	int singleCommentLines = 0;
	int multipleCommentsLine = 0;
	
	@Override
	public void visit(BlockComment bc, Void arg ) {
		super.visit(bc, arg); 
		multipleCommentsLine +=  bc.getContent().toString().split(System.getProperty("line.separator")).length;
	}
	
	
	@Override
	public void visit(LineComment lc, Void arg ) {
		super.visit(lc, arg); 
		singleCommentLines++;
		
	}
}
