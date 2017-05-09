package cmd;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class CMD_test {
	public static void main(String[] args) throws IOException{
		CMD_PSO pso = new CMD_PSO();
		List<CMD_Particle> list = new ArrayList<CMD_Particle>();
		CMD_SortBest sb = new CMD_SortBest();
		for(int i=0;i<30;i++){
			pso.init(30,"zoo");
			pso.run(500,i);
			pso.showresult(list);
			System.out.println("CMDPSOFS:"+CMD_PSO.name+"第"+(i+1)+"次测试完成");
		}
		sb.getCMDResult(CMD_PSO.name,list,"All");
	}
}
