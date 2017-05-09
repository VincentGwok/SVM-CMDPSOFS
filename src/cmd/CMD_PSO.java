package cmd;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import read.ReadFile;

public class CMD_PSO {  
    /** 
     * ����Ⱥ 
     */  
    CMD_Particle[] swarm;  //����Ⱥ
    CMD_Particle global_best;//ȫ�����Ž�  
    int pcount;//���ӵ�����  
    
    List<CMD_Particle> swarm1 = new ArrayList<CMD_Particle>();
    List<CMD_Particle> swarm2 = new ArrayList<CMD_Particle>();
    List<CMD_Particle> swarm3 = new ArrayList<CMD_Particle>();
    
    List<CMD_Particle> LeaderSet;
    List<CMD_Particle> Archive;
    
    public static String name;
    
    /** 
     * ��ʾ��������� 
     */  
    public void showresult(List<CMD_Particle> list) {
    	INNER:for(int i = 0 ; i<Archive.size() ; i++){
    		int k=0;
    		for(CMD_Particle p:list){
    			if(p.pbest_fitness[0]==Archive.get(i).pbest_fitness[0]){
    				if(p.pbest_fitness[1]<=Archive.get(i).pbest_fitness[1]){
    					continue INNER;
    				}else{
    					k=list.indexOf(p);
    				}
    			}
    		}
    		if(k>0){
    			list.remove(k);
    		}
    		list.add(Archive.get(i));
    	}
    }  
    
    /**
     * ����
     * @param LeaderSet
     * @return
     */
    public List<CMD_Particle> sortSet(List<CMD_Particle> LeaderSet){
    	CMD_Particle p;
        for (int i = 0; i < LeaderSet.size(); i++){
            for (int j = i+1; j < LeaderSet.size(); j++){
            	if(LeaderSet.get(i).pbest_fitness[0] < LeaderSet.get(j).pbest_fitness[0]){
            		p = LeaderSet.get(i);
            		LeaderSet.set(i, LeaderSet.get(j));
            		LeaderSet.set(j, p);
            	}
            }
        }
        return LeaderSet;
    }
    
    /**
     * �����Ӽ�����ӵ����������
     * @param LeaderSet
     * @return
     * @throws IOException
     */
    public List<CMD_Particle> crowdingSort(List<CMD_Particle> LeaderSet) throws IOException{
        List<CMD_Particle> temp = LeaderSet;
        CMD_Particle p;
        for (int i = 0; i < LeaderSet.size()-1; i++){
        	double di;
        	if(i==0){
        		di = Double.POSITIVE_INFINITY;		//��һ�������һ������ӵ������Ϊ�����
        		LeaderSet.get(i).dit = di;
        	}else{
        		di = (temp.get(i+1).pbest_fitness[0]-temp.get(i-1).pbest_fitness[0])/ReadFile.getFeatureNum(name);
        		di+=temp.get(i+1).pbest_fitness[1]-temp.get(i-1).pbest_fitness[1];
        		LeaderSet.get(i).dit = di;
        	}
            for (int j = i+1; j < LeaderSet.size();j++){
            	double dj;
            	if(j==LeaderSet.size()-1){
            		dj = Double.POSITIVE_INFINITY;		//��һ�������һ������ӵ������Ϊ�����
            		LeaderSet.get(j).dit = di;
            	}else{
            		dj = (temp.get(j+1).pbest_fitness[0]-temp.get(j-1).pbest_fitness[0])/ReadFile.getFeatureNum(name);
            		dj+=temp.get(j+1).pbest_fitness[1]-temp.get(j-1).pbest_fitness[1];
            	}
            	if(di > dj){
            		p = LeaderSet.get(i);
            		LeaderSet.set(i, LeaderSet.get(j));
            		LeaderSet.set(j, p);
            	}
            }
        }
        return LeaderSet;
    }
    
    /**
     * ��Ԫ����
     * @param LeaderSet
     * @return
     */
    public CMD_Particle getGbest(List<CMD_Particle> LeaderSet){
    	CMD_Particle g = new CMD_Particle();
    	if(LeaderSet.size()==1){
    		return LeaderSet.get(0);
    	}
    	int a = g.randomNum(0, LeaderSet.size(), 1)[0];
    	for(int i=0;i<1;i++){
    		int b = g.randomNum(0, LeaderSet.size(), 1)[0];
    		if(LeaderSet.get(a).dit>=LeaderSet.get(b).dit){
    			a=b;
    		}
    	}
    	g = LeaderSet.get(a);
		return g;
    }
    
    /** 
     * ����Ⱥ��ʼ�� 
     * @param n ���ӵ����� 
     * @throws IOException 
     */  
    public void init(int n,String name) throws IOException {
    	CMD_PSO.name = name;
    	CMD_Particle.dims = ReadFile.getFeatureNum(CMD_PSO.name); 
    	pcount = n;
        
    	Archive = new ArrayList<CMD_Particle>();
    	LeaderSet = new ArrayList<CMD_Particle>();
        swarm = new CMD_Particle[pcount];  //����Ⱥ
        global_best = new CMD_Particle();
         
        for (int i = 0; i < pcount; ++i) { 
            swarm[i] = new CMD_Particle();
            swarm[i].initial(CMD_Particle.dims);
            swarm[i].evaluate();
            LeaderSet.add(swarm[i]);
        }
        
        
        int sp = swarm.length/3;
        for(int i = 0;i<swarm.length;i++){
        	if(i<sp){
        		swarm1.add(swarm[i]);
        	}else if(i>=sp){
        		if(i<2*sp){
        			swarm2.add(swarm[i]);
        		}else{
        			swarm3.add(swarm[i]);
        		}
        	}
        }
        
        //��LeaderSet�е����Ӱ���λ������
        LeaderSet = sortSet(LeaderSet);
        
        //��ӵ������,��ӵ����������
        LeaderSet = crowdingSort(LeaderSet);
    }  
    
    /** 
     * ����Ⱥ������ 
     * @throws IOException 
     */  
    public void run(int runtimes,int times) throws IOException {
        int index;  
        int count = 1;
        CMD_SortBest sb = new CMD_SortBest();
        List<CMD_Particle> record;
        int T=runtimes;
        while (runtimes > 0) {
            index = -1; 
            //ÿ�����Ӹ���λ�ú���Ӧֵ

            for (int i = 0; i < swarm.length; i++) {
            	CMD_Particle c = getGbest(LeaderSet);
            	swarm[i].gbest = c.pbest;
            	swarm[i].gbest_fitness = c.pbest_fitness;
                swarm[i].updatev(); 
            }
            
            //����
            int sp = swarm.length/3;
            for(int i = 0;i<swarm.length;i++){
            	if(i>=sp){
            		if(i<2*sp){
            			swarm[i].mutation(swarm[i].Mr, swarm);
            		}else{
            			swarm[i].nonUniMutation(runtimes, T, swarm[i].Mr, swarm);
            		}
            	}
            }
            
            //����
            for (int i = 0; i < swarm.length; ++i) {
                swarm[i].evaluate();
            }
	        
	        //update LS
	    	 for (int i = 0; i < pcount;i++){
	        	index = -1; 
	        	for (int j = 0; j < pcount; j++){
	            	if(swarm[j].pbest_fitness[0] <= swarm[i].pbest_fitness[0]&&swarm[j].pbest_fitness[1] < swarm[i].pbest_fitness[1]){
	            		index=1;
	            	}else if(swarm[j].pbest_fitness[0] < swarm[i].pbest_fitness[0]&&swarm[j].pbest_fitness[1] <= swarm[i].pbest_fitness[1]){
	            		index=1;
	            	}
	            }
	        	for(int k = 0;k<LeaderSet.size();k++){
	        		if(LeaderSet.get(k).pbest_fitness[0] == swarm[i].pbest_fitness[0]&&LeaderSet.get(k).pbest_fitness[1] == swarm[i].pbest_fitness[1]){
	        			index=1;
	        		}
	        	}
	            if(index == -1){
	            	double cf = 0;
	            	int cfn = 0;
	            	for(int k=0;k<LeaderSet.size();k++){
            			if(cf<LeaderSet.get(k).dit){
            				cf = LeaderSet.get(k).dit;
            				cfn = k;
            			}
	            	}
            		LeaderSet.add(swarm[i]);
                    
                    LeaderSet = sortSet(LeaderSet);
	                    //��ӵ������,��ӵ����������
	                LeaderSet = crowdingSort(LeaderSet);
                    if(cf>LeaderSet.get(swarm.length).dit){
                    	LeaderSet.remove(cfn);
                    }else{
                    	LeaderSet.remove(swarm.length);
                    }
            	}
	        }
	       	Archive.addAll(LeaderSet);
	       	record = Archive;
	       	if(count%10==0 || count==1){
	       		sb.getCMDResult(CMD_PSO.name, record, count+"-"+times);
	       	}
	       	count++;
            runtimes--;
        }
    }
}  