package objectOrientedAnalysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class OOMain {

	public static void main(String[] args) throws FileNotFoundException {
		
		final String FILE_PATH = "E:\\proguard";
		
		final long startTime = System.currentTimeMillis();
		
		PackageData pck = new PackageData(FILE_PATH);
		HashMap<String,Integer> coupling = pck.calculateCoupling();
		HashMap<String,Integer> cohesion = pck.calculateCohesion();
		HashMap<String,Integer> rfc = pck.calculateRFC();
		HashMap<String,Integer> noOfChilds = pck.calculateNoOfChildren();
		HashMap<String,Integer> depthOfInh = pck.calculateInheritanceDepth();
		HashMap<String,Integer> loc = pck.calculateLOC();	
		HashMap<String,Integer> cmntLoc = pck.calculateCmntLOC();
		HashMap<String,Integer> cc = pck.calculateCyc();
		HashMap<String,Integer> cwc = pck.calculateWeightedCyc();
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (double)(endTime - startTime)/1000.0 + " seconds" );
		
		BufferedWriter bw = null;
		FileWriter fw = null;

			try {
				fw = new FileWriter("e:\\result.csv");
				bw = new BufferedWriter(fw);
				
				
				String header = "Class Name" +","+"LOC"+","+ "Comment"+","+
						"Cyclomatic Complexity"+","+ "WMC"+
						","+"Coupling"+","+"Lack of Cohesion"+","+
						"RFC"+","+"No of Child"+","+"Depth of Inheritence";
				
				bw.write(header);
				bw.newLine();
				
				for(String cls:pck.classNames) {
					String out = cls +","+loc.get(cls)+","+ cmntLoc.get(cls)+","+
							cc.get(cls)+","+cwc.get(cls)+
							","+coupling.get(cls)+","+cohesion.get(cls)+","+
							rfc.get(cls)+","+noOfChilds.get(cls)+","+depthOfInh.get(cls);
					
					bw.write(out);
					bw.newLine();
				}

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
	
	
			}
	}

}
