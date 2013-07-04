package alg;

import graph.AdjGraph;

import java.util.ArrayList;
import java.util.List;
  
  
public class FloydAlgorithm {   
  
    private static int INF = Integer.MAX_VALUE;   
         //dist[i][j]=INF<==>顶点i和j之间没有边   
    public double[][] dist;   
         //顶点i 到 j的最短路径长度，初值是i到j的边的权重     
    private int[][] path;
    
    boolean isDirty = true;
    
    public List<Integer> result = new ArrayList<Integer>();   
    
    protected FloydAlgorithm(int size){  
        this.path = new int[size][size];   
        this.dist = new double[size][size];   
    }
    
    public FloydAlgorithm(AdjGraph g){
    	int size = g.getNodeNum(); 
    	this.path = new int[size][size];   
        this.dist = new double[size][size];   
    } 
    
    public List<Integer> findCheapestPath(int begin,int end,double[][] matrix){
    	if(isDirty){
    		floyd(matrix);
    		isDirty = false;
    	} 
    	result.clear();
        result.add(begin);   
        findPath(begin,end);   
        result.add(end);
        return result;
    }
       
    public void findPath(int i,int j){   
        int k = path[i][j];   
        if(k == -1)return;   
        findPath(i,k);   //递归
        result.add(k);   
        findPath(k,j);   
    }   
    
    public void floyd(double[][] matrix){   
        int size = matrix.length;   
        //initialize dist and path   
        for(int i=0;i<size;i++){   
            for(int j=0;j<size;j++){   
                path[i][j]=-1;   
                dist[i][j]= matrix[i][j];   
            }   
        }   
        for(int k=0;k<size;k++){   
            for(int i=0;i<size;i++){   
                for(int j=0;j<size;j++){   
                    if(dist[i][k]!=INF&&   
                        dist[k][j]!=INF&&   
                        dist[i][k]+dist[k][j]<dist[i][j]){   
                        dist[i][j]=dist[i][k]+dist[k][j];   
                        path[i][j]=k;   
                    }   
                }   
            }   
        }   
           
    }   
       
    public static void main(String[] args) {   
        FloydAlgorithm graph=new FloydAlgorithm(5);   
        double[][] matrix={   
                {INF,30,INF,10,50},   
                {INF,INF,60,INF,INF},   
                {INF,INF,INF,INF,INF},   
                {INF,INF,INF,INF,30},   
                {50,INF,40,INF,INF},   
        };   
        int begin=0;   
        int end=4;   
        graph.findCheapestPath(begin,end,matrix);   
        List<Integer> list=graph.result;   
        System.out.println(begin+" to "+end+",the cheapest path is:");   
        System.out.println(list.toString());   
        System.out.println(graph.dist[begin][end]);   
    }   
}  
