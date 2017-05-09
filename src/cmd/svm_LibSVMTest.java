package cmd;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import read.ReadFile;
import libsvm.*;

public class svm_LibSVMTest {

	/**JAVA test code for LibSVM
	 * @author yangliu
	 * @throws IOException 
	 * @blog http://blog.csdn.net/yangliuy
	 * @mail yang.liu@pku.edu.cn
	 */
	public static void main(String[] args) throws IOException {
		
		
		List<String> choose = new ArrayList<String>();
		choose.add("1");
		choose.add("2");
		ReadFile rf = new ReadFile();
		rf.getFile(choose, "Alltra", "tra");
		rf.getFile(choose, "Alltest", "test");
		
		// TODO Auto-generated method stub
		//Test for svm_train and svm_predict
		//svm_train: 
		//	  param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//	  param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		
		String[] trainArgs = {"tra"};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {"test", modelFile, "result"};//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		Double errorRate = 1-accuracy;
		System.out.println("SVM分类完成! 分类错误率为" + errorRate);
		
		//Test for cross validation
		//String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation
		//modelFile = svm_train.main(crossValidationTrainArgs);
		//System.out.print("Cross validation is done! The modelFile is " + modelFile);
	}

}
