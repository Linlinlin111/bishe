package bishe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fenglin on 2017/5/8.
 */
public class ruleSectionInPaper {



    public static void main(String[] args){
        int capacity=200;
        int rule_number=65930;
        int[] weight= initParameter.initData(rule_number);
        int[] cost=initParameter.initCost(rule_number);//{0, 1, 1, 1, 1, 1, 1,1,1,1,1};
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
        int rule_total_number = rule_weight.length - 1;
        int cover_set_cost = 2;
        boolean[] rule_deal = new boolean[rule_number+1];

        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();
//        System.out.println(dependency);

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
            weight_dependency[i] = sum_weight;
            cost_dependency[i] = sum_cost;

            weight_coverSet[i] = rule_weight[i];
            cost_coverSet[i] = cover_set_cost;

        }

        double[][] matrices = new double[N+1][method_number];
        for(int i=1;i<N+1;i++){
            matrices[i][0]=weight_dependency[i]*1.0/cost_dependency[i];
            matrices[i][1]=weight_coverSet[i]*1.0/cost_coverSet[i];
            rule_deal[i]=false;
        }

        ArrayList<String> result=selectRuleUsingMixedSetMethod(N,M,matrices,cost_dependency,cost_coverSet,weight_dependency,weight_coverSet);
        HashSet<String> set=new HashSet<String>();
        int sum = 0;
        for (String i : result) {
//            System.out.print(i + " ");
            set.add(i);
            if (!i.contains("*")) {
                sum += rule_weight[Integer.parseInt(i)];

            }
        }
//        System.out.println(result);
        System.out.println(set);
        System.out.println(set.size());
//        System.out.println("Mixed Set : " + sum);

        int sum2 = 0;
        for (String i : set) {
//            System.out.print(i + " ");
            set.add(i);
            if (!i.contains("*")) {
                sum2 += rule_weight[Integer.parseInt(i)];

            }
        }
        System.out.println("set weight:"+sum2);


    }




    public static ArrayList<String> selectRuleUsingMixedSetMethod(
            int N,int M,double[][] matrices ,int[] c_d,int[] c_c,int[] w_d,int[] w_c ) {


        ArrayList<String> result = new ArrayList<String>();
        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);
        double max = -1;
        int pos_x = -1;
        int pos_y = -1;
        int volume = M;

        boolean[] rule_deal=new boolean[N+1];
        boolean alldealed=false;
        Arrays.fill(rule_deal,false);
        rule_deal[0]=true;


        while (result.size() < M && !alldealed) {
            max = -1;
            for (int i = 0; i < N; i++) {
                if (rule_deal[i] == false) {
                    if (matrices[i][0] >= matrices[i][1]) {
                        if (matrices[i][0] > max) {
                            pos_x = i;
                            pos_y = 0;
                            max = matrices[i][0];
                        }
                    } else {
                        if (matrices[i][1] > max) {
                            pos_x = i;
                            pos_y = 1;
                            max = matrices[i][1];
                        }
                    }
                }

            }


            if (pos_y == 0) {

                if (c_d[pos_x] <= volume) {
                    result.add(pos_x + "");
                    rule_deal[pos_x] = true;
                    int nu = 0;
//                    System.out.println(pos_x+"**"+dependency.get(pos_x));
                   if(dependency.get(pos_x).size()!=0){
                       for (Integer i : dependency.get(pos_x)) {
                           if (!result.contains(i)) {
                               result.add(i + "");
                               rule_deal[i] = true;
                               nu++;
                           }

                       }
                   }
                    volume = volume - nu - 1;
                } else
                    rule_deal[pos_x] = true;

            } else if (pos_y == 1) {
                if (c_c[pos_x] <= volume) {

                        result.add(pos_x + "");
                        result.add(pos_x + "*");
                        rule_deal[pos_x] = true;

                    volume = volume - c_c[pos_x];
                } else
                    rule_deal[pos_x] = true;

            }
            alldealed=true;
            for(boolean bl:rule_deal){
                if(bl==false) {
                    alldealed = false;
                    break;
                }
            }
        }


//        System.out.println(result);
        return result;
    }
}
