package read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReadFile {
	private static BufferedReader bf = null;
	private PrintWriter pw = null;
	
	//得到数据集的特征数
	public static int getFeatureNum(String name) throws IOException{
		bf=new BufferedReader(new FileReader("dataset\\Alltra--"+name));
		String[] num = bf.readLine().split(" ");
		String LastFeature = num[num.length-1].split(":")[0];
		return Integer.valueOf(LastFeature);
		
	}
	
	
	//按照输入的选择特征号将指定路径的文件数据拷贝到另一个测试文件
	public void getFile(List<?> choose,String path,String type) throws IOException{
		File f = new File(type);
		if(f.isFile()){
			f.delete();
		}
		f.createNewFile();
		bf=new BufferedReader(new FileReader(path));
		pw=new PrintWriter(new FileOutputStream(type, true));
		
		
		while(true){
			String data = bf.readLine();
			if(data==null||data.equals("")){
				break;//如果行数据为空则结束
			}
			String[] dataSet = data.split(" ");//将行数据按空格分为数组
			
			pw.write(dataSet[0]+" ");
			for(int i=1;i<dataSet.length;i++){
				String[] ele = dataSet[i].split(":");
				for(int j=0;j<choose.size();j++){
					if(ele[0].equals(choose.get(j))){
						pw.write(dataSet[i]+" ");
					}
				}
			}
			pw.write("\r\n");
			pw.flush();
			
		}
//		System.out.println(path+"的"+choose.size()+"组特征数据转移到"+type+"成功！");
		bf.close();
		pw.close();
	}
}
