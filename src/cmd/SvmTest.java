package cmd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;





public class SvmTest {
	
	
	
	
	

	public static void main(String[] args) throws IOException {
//		String[] trainArgs = {"Alltra"};						//directory of training file
//		String modelFile = svm_train.main(trainArgs);
//		System.out.println(modelFile);
//		String[] testArgs = {"Alltest", modelFile, "result"};		//directory of test file, model file, result file
//		Double accuracy = svm_predict.main(testArgs);
//		Double errorRate = 1-accuracy;

//		CMD_Particle c = new CMD_Particle();
		
//		System.out.println(c.rand(0, 1));
//		ReadFile.getFeatureNum();
//		System.out.println(ReadFile.getFeatureNum());
		
		
		String[] trainArgs = {"dataset/Alltra--Hill_Valley"};						//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {"dataset/Alltest--Hill_Valley", modelFile, "dataset/result"};		//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		Double errorRate = 1-accuracy;
		System.out.println(errorRate);
		
		String name = "";
		for(int i=0;i<trainArgs.length;i++){
			name+=trainArgs[i];
		}
		
		File f = new File("dataset/AllFeatures.dat");
		if(!f.isFile()){
			f.createNewFile();
		}
		PrintWriter pw = new PrintWriter(new FileOutputStream("dataset/AllFeatures.dat",true));
		
		pw.write(name.split("--")[1]+" "+errorRate);
		pw.write("\n");
		pw.close();
		
	}

}
