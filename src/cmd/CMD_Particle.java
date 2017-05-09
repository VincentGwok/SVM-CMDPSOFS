package cmd;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import read.ReadFile;

public class CMD_Particle {  
	
	
	public static Random rnd; 
	
	
    public double[] pos;//���ӵ�λ�ã�����������ά���������Ϊ����ά  
    public double[] v;//���ӵ��ٶȣ�ά��ͬλ��  
    public double Mr; //������
    public double dit;
    
    
    public double[] fitness;//���ӵ���Ӧ��  0Ϊλ�� 1Ϊ�ٶ�
    public double[] pbest;//���ӵ���ʷ���λ��  0Ϊλ�� 1Ϊ�ٶ�
    public double[] gbest;//���������ҵ������λ��  0Ϊλ�� 1Ϊ�ٶ�
    
    
    
    public double randnum1;
    public double randnum2;
    
    public static int dims;  //ά��
    public double w;  
    public double c1;  
    public double c2;  
    
    public double[] gbest_fitness;//��ʷ���Ž�  
    public double[] pbest_fitness;//��ʷ���Ž�  
    /** 
     * ����low��uper֮����� (double)
     * @param low ���� 
     * @param uper ���� 
     * @return ����low��uper֮����� 
     */  
    static double rand(double low, double uper) {
        rnd = new Random();
        return rnd.nextDouble() * (uper - low) + low;  
    }
    /**
     * ����min��max֮���n�������
     * @param min
     * @param max
     * @param n
     * @return
     */
    public int[] randomNum(int min, int max, int n){
    	
        if (n > (max - min + 1) || max < min) {  
               return null;  
           }  
        int[] result = new int[n];  
        int count = 0;  
        while(count < n) {  
            int num = (int) (Math.random() * (max - min)) + min;  
            boolean flag = true;  
            for (int j = 0; j < n; j++) {  
                if(num == result[j]){  
                    flag = false;
                    break;
                }
            }  
            if(flag){ 
            	result[count] = num;
                count++;  
            }  
        }  
        return result;  
    }  
    
    
    /** 
     * ��ʼ������ 
     * @param dim ��ʾ���ӵ�ά�� 
     * @throws IOException 
     */  
    public void initial(int dim) throws IOException {  
        pos = new double[dim]; 
        v = new double[dim];
        pbest = new double[dim];  
        dims = dim;
        for(int i=0;i<pos.length;i++){
        	pos[i]=CMD_Particle.rand(0, 1);
        	pbest[i] = pos[i];										//��ʼ�����ӵĸ�������//��ʼ�����ӵ��ٶ�
        	v[i] = rand(-0.6, 0.6); 
        }	                        
        fitness = new double[2];					//��Ӧֵ��������2������һ�������������ڶ����Ǵ�����
        pbest_fitness = new double[2];
        pbest_fitness[0] = ReadFile.getFeatureNum(CMD_PSO.name)+1;
        pbest_fitness[1] = 1;
        gbest_fitness = new double[2];
        gbest = new double[dim];
        
        Mr = 1/pos.length;
        randnum1 = rand(0,1);
        randnum2 = rand(0,1);
        c1 = rand(1.5,2.0);
        c2 = rand(1.5,2.0);
        w = rand(0.1,0.5);
        dit = 0;
    }  
    /** 
     * ��������ֵ,ͬʱ��¼��ʷ����λ�� 
     * @throws IOException 
     */  
    public void evaluate() throws IOException {

    	List<String> choose = new ArrayList<String>();
//    	System.out.print("ѡ���������");
    	int j=1;
    	for(int i=0;i<pos.length;i++){
    		if(pos[i]>0.6){
    			choose.add(String.valueOf(j));
//    			System.out.print(j+"��");
    		}
    		j++;
    	}
//    	System.out.println();
//    	System.out.println("ѡ�������������"+choose.size());
    	
		ReadFile rf = new ReadFile();
		rf.getFile(choose, "dataset\\Alltra--"+CMD_PSO.name, "tra");
		rf.getFile(choose, "dataset\\Alltest--"+CMD_PSO.name, "test");
		
		String[] trainArgs = {"tra"};						//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {"test", modelFile, "result"};		//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		Double errorRate = 1-accuracy;
    	
		fitness[0] = choose.size();
        fitness[1] = errorRate;
        
        //���¸������Ž�
        if (fitness[0] < pbest_fitness[0]&&fitness[1] < pbest_fitness[1]) {
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }else if(fitness[0] == pbest_fitness[0]&&fitness[1] < pbest_fitness[1]){
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }else if(fitness[0] < pbest_fitness[0]&&fitness[1] == pbest_fitness[1]){
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }
    }  
    /** 
     * �����ٶȺ�λ�� 
     * @throws IOException 
     */  
    public void updatev() throws IOException {
        for (int i = 0; i < pos.length; i++) {
            v[i] = w * v[i] + c1 * randnum1 * (pbest[i] - pos[i])  
                    + c2 * randnum2 * (gbest[i] - pos[i]);  
            if (v[i] > 0.6) {  
                v[i] = CMD_Particle.rand(-0.6, 0.6);  
            }  
            if (v[i] < -0.6) {  
                v[i] = CMD_Particle.rand(-0.6, 0.6); 
            }  
            pos[i] = (pos[i] + v[i]);  
            if (pos[i] > 1) {
                pos[i] = CMD_Particle.rand(0, 1);
            }
            if (pos[i] < 0) {  
                pos[i] = CMD_Particle.rand(0, 1);
            }
        }
    }
    
    public void mutation(double Mr,CMD_Particle[] swarm) throws IOException{
    	
    	for(int i=0;i<pos.length;i++){
    		double a = CMD_Particle.rand(0, 1);
    		if (a<=Mr) {
    			pos[i]=CMD_Particle.rand(0, 1);
    		}
    	}
    }
    
    public void nonUniMutation(int t,int T,double Mr,CMD_Particle[] swarm){
    	int b=3;
    	for(int i=0;i<pos.length;i++){
    		double r = CMD_Particle.rand(0, 1);
    		double a = CMD_Particle.rand(0, 1);
    		if (a<=Mr) {
    	    	double posi = CMD_Particle.rand(0, 1);
    	    	double y ;
    	    	int e;
    	    	if (posi>0.5){
    	    		y=1-pos[i];
    	    		e=0;
    	    	}else{
    	    		y=pos[i]-0;
    	    		e=1;
    	    	}
    	    	double k = Math.pow(1-(t/T), b);
    	    	double del =  y*(1-(Math.pow(r, k)));
    	    	
    	    	if (e==0){
    	    		pos[i]=pos[i]+del;
    	    	}else{
    	    		pos[i]=pos[i]-del;
    	    	}
    	    	
    		}
    	}
    }
    
    
    
    
}  
