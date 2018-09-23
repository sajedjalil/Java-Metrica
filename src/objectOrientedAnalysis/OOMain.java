package objectOrientedAnalysis;

import java.io.FileNotFoundException;

public class OOMain {

	public static void main(String[] args) {
		try {
			new PackageData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
