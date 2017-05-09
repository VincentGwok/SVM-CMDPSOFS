package read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DatasetFile {
	
	private static BufferedReader bf = null;
	private static PrintWriter pw = null;
	
	

	public static void main(String[] args) throws IOException {
		
		String path = "Hill_Valley_with_noise_Training.data";//从UCI上下载的数据集，不同的数据集格式不同，以下代码根据数据集改变
		String Newpath = "Alltra";
		
		File file = new File(Newpath);
		file.delete();
		file.createNewFile();
		
		bf=new BufferedReader(new FileReader(path));
		pw=new PrintWriter(new FileOutputStream(Newpath, true));
		String date = bf.readLine();
		while(true){
			if(date!=null){
				String[] dates = date.split(",");
				
				dates[dates.length-1] = dates[dates.length-1].trim();
				
				pw.write(dates[dates.length-1]);
				pw.write(" ");
				for(int i = 0;i<dates.length;i++){
					dates[i] = dates[i].trim();
					if(i==dates.length-1){
						
					}else{
						pw.write((i+1)+":"+dates[i]);
						pw.write(" ");
					}
				}
				pw.write("\n");
				date = bf.readLine();
			}
			else{
				System.out.println("done");
				break;
			}
		}
		pw.flush();
		pw.close();
		bf.close();
	}

}
