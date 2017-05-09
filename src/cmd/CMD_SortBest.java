package cmd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class CMD_SortBest {
	public void getCMDResult(String name,List<CMD_Particle> list,String type) throws IOException {
		String path1 = "testfile\\"+name;
		File f = new File(path1);
		if(!f.exists()){
			f.mkdir();
		}
		String path = path1+"\\"+type+name+"-CMD-result.dat";
		File file = new File(path);
		if(file.isFile()){
			file.delete();
		}
		file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(path, true));
		
		for(int i = 0;i<list.size();i++){
			for(int j = list.size()-1;j>i;j--){
				if(list.get(i).pbest_fitness[1]==list.get(j).pbest_fitness[1]){
					if(list.get(i).pbest_fitness[0]<=list.get(j).pbest_fitness[0]){
						list.remove(j);
					}else{
						list.set(i, list.get(j));
						list.remove(j);
					}
				}
			}
		}
		
		List<CMD_Particle> nonDomS = new ArrayList<CMD_Particle>();
	   	 for (int i = 0; i < list.size();i++){
	     	int index = -1; 
	     	for (int j = 0; j < list.size(); j++){
	         	if(list.get(j).pbest_fitness[0] <= list.get(i).pbest_fitness[0]&&list.get(j).pbest_fitness[1] <list.get(i).pbest_fitness[1]){
	         		index=1;
	         	}else if(list.get(j).pbest_fitness[0] < list.get(i).pbest_fitness[0]&&list.get(j).pbest_fitness[1] <=list.get(i).pbest_fitness[1]){
	         		index=1;
	         	}
	         }
	     	
	     	for(int k = 0;k<nonDomS.size();k++){
	     		if(nonDomS.get(k).pbest_fitness[0] == list.get(i).pbest_fitness[0]&&nonDomS.get(k).pbest_fitness[1] == list.get(i).pbest_fitness[1]){
	     			index=1;
	     		}
	     	}
	     	
	         if(index == -1){
	         	nonDomS.add(list.get(i));
	         }
	     }
		for(CMD_Particle p:nonDomS){
			System.out.println(p.pbest_fitness[0]);
			
	    	List<String> choose = new ArrayList<String>();
	    	for(int i=0;i<p.pbest.length;i++){
	    		if(p.pbest[i]>0.6){
	    			choose.add(String.valueOf(i+1));
	    		}
	    	}
			
			for(int i=0;i<choose.size();i++){
				if(i==choose.size()-1){
					pw.write(String.valueOf(choose.get(i)));
					continue;
				}
				pw.write(String.valueOf(choose.get(i))+" ");
			}
			pw.write("*"+p.pbest_fitness[0]+" "+p.pbest_fitness[1]);
			pw.write("\n");
			pw.flush();
		}
		pw.close();
		System.out.println("CMDdone!");
	}
}
