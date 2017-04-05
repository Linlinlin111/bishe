package bishe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fenglin on 2017/4/5.
 */
public class ruleSelectionDP {

    public static void main(String[] args){
        calRules();
//    System.out.println(initDependency());
    }

    public static void calRules(){
        //可以预处理一下，对每个规则使用哪种策略进行运算，确定每个规则的方案之后，就可以确定缓存该规则的代价。
        //计算出选中的规则，比较得出该规则所采用的缓存方法，补全规则集即可完成规则的选择。
        //依赖集和覆盖集的选择，考虑整个链路上的规则。

        int M=4; //背包容量
        int N=6; //物品个数
        //只考虑规则自身时的参数
        int[] weight={0,5,10,35,10,35,40}; //物品价值
        int[] cost={0,1,2,2,2,2,2}; //物品代价



        int[][] matrices=new int[N+1][M+1]; //matrices[i][j]表示前i个物品能装入容量为j的背包中的最大价值
        int[][] path=new int[N+1][M+1];

        //初始化第一列和第一行

        for(int i=0;i<M+1;i++)
            matrices[0][i]=0;
        for(int i=0;i<N+1;i++)
            matrices[i][0]=0;

        for(int i=1;i<N+1;i++){
            for(int j=1;j<M+1;j++){

                if(j<cost[i]){
                    matrices[i][j]=0;//matrices[i-1][j];//0;
                }else{
//                    matrices[i][j]=Math.max(matrices[i-1][j],matrices[i-1][j-cost[i]]+weight[i]);
                    if(matrices[i-1][j]<(matrices[i-1][j-cost[i]]+weight[i])){
                        matrices[i][j]=matrices[i-1][j-cost[i]]+weight[i];
                        path[i][j]=1;
                    }else{
                        matrices[i][j]=matrices[i-1][j];
                    }
                }

            }
        }
        for(int i=0;i<N+1;i++){
            for(int j=0;j<M+1;j++) {
                System.out.print(matrices[i][j] + " ");
            }
            System.out.println("");
        }



        for(int i=0;i<N+1;i++){
            for(int j=0;j<M+1;j++) {
                System.out.print(path[i][j] + " ");
            }
            System.out.println("");
        }


        System.out.println("选出的规则如下：");
        int i=matrices.length-1;
        int j=matrices[0].length-1;
        while(i>0&&j>0){
            if(path[i][j]==1){
                System.out.println("规则 "+i +" 选中");
                j-=cost[i];
            }
            i--;
        }
    }


/*
输出样例：
    {1=[], 2=[1], 3=[2, 1], 4=[2, 1], 5=[3, 2, 1], 6=[4, 2, 1]}
 */
    public static HashMap<Integer, ArrayList<Integer>> initDependency() {


        HashMap<Integer, ArrayList<Integer>> dependency = new HashMap<Integer, ArrayList<Integer>>();

        ArrayList<Integer> arr1 = new ArrayList<Integer>();
        dependency.put(1, arr1);

        ArrayList<Integer> arr2 = new ArrayList<Integer>();
        arr2.add(1);
        dependency.put(2, arr2);

        ArrayList<Integer> arr3 = new ArrayList<Integer>();
        arr3.add(2);
        arr3.add(1);

        dependency.put(3, arr3);

        ArrayList<Integer> arr4 = new ArrayList<Integer>();
        arr4.add(2);
        arr4.add(1);

        dependency.put(4, arr4);

        ArrayList<Integer> arr5 = new ArrayList<Integer>();

        arr5.add(3);
        arr5.add(2);
        arr5.add(1);

        dependency.put(5, arr5);

        ArrayList<Integer> arr6 = new ArrayList<Integer>();
        arr6.add(4);
        arr6.add(2);
        arr6.add(1);



        dependency.put(6, arr6);

        return dependency;
    }

}
