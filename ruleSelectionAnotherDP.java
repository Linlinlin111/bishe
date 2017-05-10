package bishe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by fenglin on 2017/4/11.
 * 将问题转化为01背包，预处理数据，确定每个规则采用不同方案后缓存该规则的代价和权重。
 * 使用依赖集的代价和权重表示为：
 * cost_dependency[i]  // cd
 * weight_dependency[i]    // wd
 * 使用覆盖集的代价和权重表示为：
 * cost_coverSet[i]    //cc
 * weight_coverSet[i]  //wc
 * 状态转移方程如下：
 * f[i][v]=max{f[i-1][v],f[i-1][v-cd[i]]+wd[i],f[i-1][v-cost_cc[i]]+wc[i]}
 */
public class ruleSelectionAnotherDP {

    public static void main(String[] args) {
//        calRules();
        //calRules();
//    System.out.println(initDependency());
        int capacity=200;
        int rule_number=65930;
        int[] weight= initParameter.initData(rule_number);
        int[] cost=initParameter.initCost(rule_number);//{0, 1, 1, 1, 1, 1, 1,1,1,1,1};
//        for(int w:weight)
//            System.out.println(w);
        calRulesByParams(capacity,rule_number,weight,cost);
    }


    public static void calRulesByParams(int capacity, int rule_number, int[] weight,int[] cost){
        //可以预处理一下，对每个规则使用哪种策略进行运算，确定每个规则的方案之后，就可以确定缓存该规则的代价。
        //计算出选中的规则，比较得出该规则所采用的缓存方法，补全规则集即可完成规则的选择。
        //依赖集和覆盖集的选择，考虑整个链路上的规则。

        int M = capacity; //背包容量
        int N = rule_number; //物品个数
        /*只考虑规则自身时的参数
        int[] rule_weight={0,5,10,35,10,35,40}; //物品价值
        int[] rule_cost={0,1,2,2,2,2,2}; //物品代价
    */


        //预处理策略,初始值
        int[] rule_weight = weight;//{0, 5, 10, 35, 10, 35, 40}; //物品价值
        int[] rule_cost = cost;//{0, 1, 1, 1, 1, 1, 1}; //物品代价

        //计算使用依赖集各规则的权重和代价
        int[] weight_dependency = new int[N + 1];
        weight_dependency[0] = 0;
        int[] cost_dependency = new int[N + 1];
        cost_dependency[0] = 0;

        //计算使用覆盖集各规则的权重和代价
        int[] weight_coverSet = new int[N + 1];
        weight_coverSet[0] = 0;
        int[] cost_coverSet = new int[N + 1];
        cost_coverSet[0] = 0;


        int method_number = 2;
        int rule_total_number = N;
        int cover_set_cost = 2;


        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();
//        System.out.println(dependency);

        boolean[] isUsedCoverSet = new boolean[N + 1];


        ArrayList<Integer> templist;//=new ArrayList<Integer>();

        //初始化方法选择的矩阵
        //方法1：依赖集
        //方法2：覆盖集
        for (int i = 1; i < N + 1; i++) {
            templist = dependency.get(i);
            int sum_weight = rule_weight[i];
            int sum_cost = rule_cost[i];
            for (int n : templist) {
                sum_weight += rule_weight[n];
                sum_cost += rule_cost[n];
            }
            weight_dependency[i] = sum_weight/sum_cost;
            cost_dependency[i] = sum_cost;

            weight_coverSet[i] = rule_weight[i]/2;
            cost_coverSet[i] = cover_set_cost;

        }

//        System.out.println("依赖集代价 和权重");
//        for(int i=0;i<N+1;i++)
//            System.out.println(cost_dependency[i]+"  "+weight_dependency[i]);
//        System.out.println("覆盖集代价 和权重");
//        for(int i=0;i<N+1;i++)
//            System.out.println(cost_coverSet[i]+"  "+weight_coverSet[i]);



        ArrayList<Integer> rule_list = dpSelection(M, N, cost_dependency, weight_dependency, cost_coverSet, weight_coverSet,isUsedCoverSet);



        int sum_weight=0;
        HashSet<String> set=new HashSet<String>();
        for (int rule : rule_list) {
//            System.out.println("规则 " + rule + " 选中");
            set.add(rule+"");
            if (isUsedCoverSet[rule]) {
                sum_weight+=weight_coverSet[rule];
                set.add(rule+"*");

//                System.out.println("规则 " + rule + "* 选中");
            } else {
                sum_weight+=weight_dependency[rule];

                for (int i : dependency.get(rule)) {
//                    System.out.println("规则 " + i + " 选中");
                    set.add(i+"");
                }
            }
        }
        System.out.println(set);
        System.out.println(set.size());

//        System.out.println("总权重："+sum_weight);
        int sum2 = 0;
        for (String i : set) {
//            System.out.print(i + " ");
            set.add(i);
            if (!i.contains("*")) {
                sum2 += rule_weight[Integer.parseInt(i)];

            }
        }
        System.out.println("set weight:"+sum2);

        int sum3=0;
        for(int i:weight){
            sum3+=i;
        }
        System.out.println(sum3);
    }

    public static void calRules() {
        //可以预处理一下，对每个规则使用哪种策略进行运算，确定每个规则的方案之后，就可以确定缓存该规则的代价。
        //计算出选中的规则，比较得出该规则所采用的缓存方法，补全规则集即可完成规则的选择。
        //依赖集和覆盖集的选择，考虑整个链路上的规则。

        int M = 5; //背包容量
        int N = 10; //物品个数
        /*只考虑规则自身时的参数
        int[] rule_weight={0,5,10,35,10,35,40}; //物品价值
        int[] rule_cost={0,1,2,2,2,2,2}; //物品代价
    */


        //预处理策略,初始值
        int[] rule_weight =initParameter.initData(N);// {0, 5, 10, 35, 10, 35, 40}; //物品价值
        int[] rule_cost = initParameter.initCost(N);//{0, 1, 1, 1, 1, 1, 1}; //物品代价

        //计算使用依赖集各规则的权重和代价
        int[] weight_dependency = new int[N + 1];
        weight_dependency[0] = 0;
        int[] cost_dependency = new int[N + 1];
        cost_dependency[0] = 0;

        //计算使用覆盖集各规则的权重和代价
        int[] weight_coverSet = new int[N + 1];
        weight_coverSet[0] = 0;
        int[] cost_coverSet = new int[N + 1];
        cost_coverSet[0] = 0;


        int method_number = 2;
        int rule_total_number = rule_weight.length - 1;
        int cover_set_cost = 2;


        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();
        System.out.println(dependency);

        boolean[] isUsedCoverSet = new boolean[N + 1];


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
            weight_dependency[i] = sum_weight/sum_cost;
            cost_dependency[i] = sum_cost;

            weight_coverSet[i] = rule_weight[i]/2;
            cost_coverSet[i] = cover_set_cost;

        }
        System.out.println("依赖集代价 和权重");
        for(int i=0;i<N+1;i++)
            System.out.println(cost_dependency[i]+"  "+weight_dependency[i]);
        System.out.println("覆盖集代价 和权重");
        for(int i=0;i<N+1;i++)
            System.out.println(cost_coverSet[i]+"  "+weight_coverSet[i]);

        ArrayList<Integer> rule_list = dpSelection(M, N, cost_dependency, weight_dependency, cost_coverSet, weight_coverSet,isUsedCoverSet);


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







    public static ArrayList<Integer> dpSelection(int M, int N, int[] dependency_cost, int[] dependency_weight,
                                                 int[] coverSet_cost, int[] coverSet_weight,boolean[] isUsedCoverSet) {


        ArrayList<Integer> rule_list = new ArrayList<Integer>();


        int[][] matrices = new int[N + 1][M + 1];
        int[][] path = new int[N + 1][M + 1];
//        boolean[] isUsedCoverSet = new boolean[N + 1];
        //初始化第一列和第一行


        for (int i = 0; i < M + 1; i++)
            matrices[0][i] = 0;
        for (int i = 0; i < N + 1; i++) {
            matrices[i][0] = 0;
            isUsedCoverSet[i] = false;
        }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                if (j < dependency_cost[i] && j < coverSet_cost[i]) {
                    matrices[i][j] = 0;
                }
                if (j >= dependency_cost[i] && j < coverSet_cost[i]) {
                    if (matrices[i - 1][j] < (matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i];
                        path[i][j] = 1;
                    } else
                        matrices[i][j] = matrices[i - 1][j];

//                    matrices[i][j]=Math.max(matrices[i-1][j],matrices[i-1][j-dependency_cost[i]]+dependency_weight[i]);

                } else if (j < dependency_cost[i] && j >= coverSet_cost[i]) {
                    if (matrices[i - 1][j] < (matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i];
                        path[i][j] = 1;
                        isUsedCoverSet[i] = true;
                    } else
                        matrices[i][j] = matrices[i - 1][j];
//                    matrices[i][j]=Math.max(matrices[i-1][j],matrices[i-1][j-coverSet_cost[i]]+coverSet_weight[i]);

                } else if (j >= dependency_cost[i] && j >= coverSet_cost[i]) {

                    if (matrices[i - 1][j] >= (matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i])
                            && matrices[i - 1][j] >= (matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j];
                    } else if (matrices[i - 1][j] < (matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i])
                            && (matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i]) > (matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i];
                        path[i][j] = 1;
                        isUsedCoverSet[i] = true;
                    } else if (matrices[i - 1][j] < (matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i])
                            && (matrices[i - 1][j - coverSet_cost[i]] + coverSet_weight[i]) <= (matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i])) {
                        matrices[i][j] = matrices[i - 1][j - dependency_cost[i]] + dependency_weight[i];
                        path[i][j] = 1;
                    }
//                    int mtemp=Math.max(matrices[i-1][j],matrices[i-1][j-coverSet_cost[i]]+coverSet_weight[i]);
//                    matrices[i][j]=Math.max(mtemp,matrices[i-1][j-dependency_cost[i]]+dependency_weight[i]);
                }

            }
        }

//        for (int i = 0; i < N + 1; i++) {
//            for (int j = 0; j < M + 1; j++) {
//                System.out.print(matrices[i][j] + " ");
//            }
//            System.out.println("");
//        }
//        System.out.println("选出的规则如下：");




        int i = matrices.length - 1;
        int j = matrices[0].length - 1;
        while (i > 0 && j > 0) {
            if (path[i][j] == 1) {
//                System.out.println("规则 " + i + " 选中  依赖集？"+isUsedCoverSet[i]);
                rule_list.add(i);
                if (isUsedCoverSet[i])
                    j -= coverSet_cost[i];
                else
                    j -= dependency_cost[i];
            }
            i--;
        }
        System.out.println("总权重："+matrices[N][M]);
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
