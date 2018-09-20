package objectOrientedAnalysis;

import java.io.File;

public class OOMain {

	public static void main(String[] args) {
		File inputDirectory = new File(System.getProperty("user.dir")+"/../JAVA-OOP/src/com/broccolinisoup/oop/general/aboutThisKeyword");
		ObjectOrientedAnalyzer oo = new ObjectOrientedAnalyzer(inputDirectory);
	}

}
