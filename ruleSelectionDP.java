package bishe;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fenglin on 2017/4/5.
 *
 *
 * 将问题转化为01背包，预处理数据，对每个规则使用哪种策略进行运算，确定每个规则的方案之后，就可以确定缓存该规则的代价和权重。
 * 使用新的代价和权重值利用下面的状态转移方程进行计算。
 *  f[i][v]=max{f[i-1][v],f[i-1][v-c[i]]+w[i]}
 */
public class ruleSelectionDP {

    public static void main(String[] args) {
        calRules();
//    System.out.println(initDependency());
    }

    public static void calRules() {
        //可以预处理一下，对每个规则使用哪种策略进行运算，确定每个规则的方案之后，就可以确定缓存该规则的代价。
        //计算出选中的规则，比较得出该规则所采用的缓存方法，补全规则集即可完成规则的选择。
        //依赖集和覆盖集的选择，考虑整个链路上的规则。

        int M = 4; //背包容量
        int N = 6; //物品个数
        /*只考虑规则自身时的参数
        int[] rule_weight={0,5,10,35,10,35,40}; //物品价值
        int[] rule_cost={0,1,2,2,2,2,2}; //物品代价
    */


        //预处理策略
        int[] rule_weight = {0, 5, 10, 35, 10, 35, 50}; //物品价值
        int[] rule_cost = {0, 1, 1, 1, 1, 1, 1}; //物品代价
        int[] rule_new_weight = new int[N + 1];
        rule_new_weight[0] = 0;
        int[] rule_new_cost = new int[N + 1];
        rule_new_cost[0] = 0;

        int method_number = 2;
        int rule_total_number = rule_weight.length - 1;
        int cover_set_cost = 2;

        int[][] select_matrices = new int[rule_total_number + 1][method_number + 1];

        HashMap<Integer, ArrayList<Integer>> dependency = initDependency();

        boolean[] isUsedCoverSet = new boolean[N + 1];


        //matrices[i][j]表示前i个物品能装入容量为j的背包中的最大价值
//        int[][] path=new int[N+1][M+1];


        ArrayList<Integer> templist;//=new ArrayList<Integer>();

        //初始化方法选择的矩阵
        //方法1：依赖集
        //方法2：覆盖集
        for (int i = 1; i < rule_total_number + 1; i++) {
            templist = dependency.get(i);
            int sum_weight = rule_weight[i];
            int sum_cost = rule_cost[i];
            for (int n : templist) {
                sum_weight += rule_weight[n];
                sum_cost += rule_cost[n];
            }
            select_matrices[i][1] = sum_weight / sum_cost;
            select_matrices[i][2] = rule_weight[i] / cover_set_cost;
            if (select_matrices[i][1] >= select_matrices[i][2]) {
                rule_new_weight[i] = sum_weight;
                rule_new_cost[i] = sum_cost;
                isUsedCoverSet[i] = false;
            } else {
                rule_new_weight[i] = rule_weight[i];
                rule_new_cost[i] = cover_set_cost;
                isUsedCoverSet[i] = true;
            }
        }
/*
        for(int i=1;i<select_matrices.length;i++){
            System.out.println(select_matrices[i][1]+" "+select_matrices[i][2]);
        }
*/

//        dpSelection(M,N,rule_cost,rule_weight);

//        System.out.println("改进版");

        ArrayList<Integer> rule_list = dpSelection(M, N, rule_new_cost, rule_new_weight);

        for (int rule : rule_list) {
            System.out.println("规则 " + rule + " 选中");
            if (isUsedCoverSet[rule]) {

                System.out.println("规则 " + rule + "* 选中");
            } else {

                for (int i : dependency.get(rule)) {
                    System.out.println("规则 " + i + " 选中");
                }
            }
        }

    }


    public static ArrayList<Integer> dpSelection(int M, int N, int[] rule_cost, int[] rule_weight) {


        ArrayList<Integer> rule_list = new ArrayList<Integer>();


        int[][] matrices = new int[N + 1][M + 1];
        int[][] path = new int[N + 1][M + 1];
        //初始化第一列和第一行

        for (int i = 0; i < M + 1; i++)
            matrices[0][i] = 0;
        for (int i = 0; i < N + 1; i++)
            matrices[i][0] = 0;

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {

                if (j < rule_cost[i]) {
                    matrices[i][j] = 0;//matrices[i-1][j];//0;
                } else {
//                    matrices[i][j]=Math.max(matrices[i-1][j],matrices[i-1][j-rule_cost[i]]+rule_weight[i]);
                    if (matrices[i - 1][j] < (matrices[i - 1][j - rule_cost[i]] + rule_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j - rule_cost[i]] + rule_weight[i];
                        path[i][j] = 1;
                    } else {
                        matrices[i][j] = matrices[i - 1][j];
                    }
                }

            }
        }
        /*
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

*/
        System.out.println("选出的规则如下：");
        int i = matrices.length - 1;
        int j = matrices[0].length - 1;
        while (i > 0 && j > 0) {
            if (path[i][j] == 1) {
//                System.out.println("规则 "+i +" 选中");
                rule_list.add(i);
                j -= rule_cost[i];
            }
            i--;
        }


        return rule_list;

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
