package bishe;

import com.sun.javafx.css.FontUnits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fenglin on 2017/3/8.
 */
public class ruleSelection {


    public static ArrayList<String> selectRuleUsingMixedSetMethod(
            int[] rule_weight, int[] rule_cost, int switch_volume) {

        ArrayList<String> result = new ArrayList<String>();

        int rule_total_number = rule_weight.length;
        int method_number = 2;
        int cover_cost = 2;
        int[] rule_dependent_ratio = new int[rule_total_number];
        boolean[] rule_deal = new boolean[rule_total_number];
        int[] rule_new_cost = new int[rule_total_number];
        int[] rule_cover_ratio = new int[rule_total_number];
        int[][] metric = new int[rule_total_number][method_number];
        HashMap<Integer, ArrayList<Integer>> dependency = initDependency();

        for (int i = 0; i < rule_total_number; i++) {
            rule_new_cost[i] = cover_cost;
            rule_dependent_ratio[i] = rule_weight[i] / rule_cost[i];
            rule_cover_ratio[i] = rule_weight[i] / rule_new_cost[i];
            metric[i][0] = rule_dependent_ratio[i];
            metric[i][1] = rule_cover_ratio[i];
            rule_deal[i] = false;
        }

//        for(int i=0;i<rule_total_number;i++)
//            System.out.println(metric[i][0]+"  "+metric[i][1]);


        int max = -1;
        int pos_x = -1;
        int pos_y = -1;
        int volume = switch_volume;

        while (result.size() < switch_volume) {
            max = -1;
            for (int i = 0; i < rule_total_number; i++) {
                if (rule_deal[i] == false) {
                    if (metric[i][0] >= metric[i][1]) {
                        if (metric[i][0] > max) {
                            pos_x = i;
                            pos_y = 0;
                            max = metric[i][0];
                        }
                    } else {
                        if (metric[i][1] > max) {
                            pos_x = i;
                            pos_y = 1;
                            max = metric[i][1];
                        }
                    }
                }

            }

//            System.out.println("this turn "+pos_x+" "+pos_y);

            if (pos_y == 0) {

                if (rule_cost[pos_x] <= volume) {
                    result.add(pos_x + "");
                    rule_deal[pos_x] = true;
                    int nu = 0;
                    for (Integer i : dependency.get(pos_x)) {
                        if (!result.contains(i)) {
                            result.add(i + "");
                            rule_deal[i] = true;
                            nu++;
                        }

                    }
                    volume = volume - nu - 1;
                } else
                    rule_deal[pos_x] = true;

            } else if (pos_y == 1) {
                if (rule_new_cost[pos_x] <= volume) {
//                                    System.out.println("///  "+pos_x);


//                    System.out.println("///  "+pos_x);

                    boolean flag = false;
                    int distance = 0;
                    int nn = 0;
                    for (String str : result) {
                        if (!str.contains("*")) {
                            if (dependency.get(Integer.parseInt(str)).contains(pos_x)) {
                                distance = dependency.get(Integer.parseInt(str)).indexOf(pos_x);
                                if (distance == 1) {
                                    result.add(pos_x + "");
                                    result.add(dependency.get(Integer.parseInt(str)).get(0) + "");
                                    rule_deal[pos_x] = true;
                                    rule_deal[dependency.get(Integer.parseInt(str)).get(0)] = true;
                                    if (dependency.get(Integer.parseInt(str)).size() == 2) {
                                        result.remove(Integer.parseInt(str) + "*");
                                        volume++;
                                    }
                                    flag = true;
                                    break;
                                }
                            }
                        }

                    }

                    for (String str : result) {
                        if (!str.contains("*")) {
                            if (dependency.get(pos_x).contains(Integer.parseInt(str))) {
                                distance = dependency.get(pos_x).indexOf(Integer.parseInt(str));
                                if (distance == 1) {
                                    result.add(pos_x + "");
                                    result.add(dependency.get(pos_x).get(0) + "");
                                    rule_deal[pos_x] = true;
                                    rule_deal[dependency.get(pos_x).get(0)] = true;
//                                if(dependency.get(Integer.parseInt(str)).size()==2){
//                                    result.remove(Integer.parseInt(str)+"*");
//                                    volume++;
//                                }
                                    flag = true;
                                    break;
                                }
                            }
                        }

                    }


                    if (!flag) {
                        result.add(pos_x + "");
                        result.add(pos_x + "*");
                        rule_deal[pos_x] = true;

                    }

                    volume = volume - rule_new_cost[pos_x];
//                    if(volume==1) break;
//                volume+=nn;
//                System.out.println(volume+ " ----");


//                    result.add(pos_x+"");
//                    result.add(pos_x+"*");
//                    rule_deal[pos_x]=true;
//                    volume=volume-cover_cost;
                } else
                    rule_deal[pos_x] = true;

            }

        }

        return result;
    }

    public static ArrayList<String> selectRulesUsingCoverSetMethod(
            int[] rule_weight, int[] rule_cost, int switch_volume,int N) {




        ArrayList<String> result = new ArrayList<String>();

//        int rule_total_number = rule_cost.length;
        int[] rule_new_cost = new int[N+1];
        int[] rule_ratio = new int[N+1];
        boolean[] rule_deal = new boolean[N+1];


        int[] weight_coverSet = new int[N + 1];
        weight_coverSet[0] = 0;
        int[] cost_coverSet = new int[N + 1];
        cost_coverSet[0] = 0;
        for (int i = 1; i < N + 1; i++) {

            weight_coverSet[i] = rule_weight[i]/2;
            cost_coverSet[i] = 2;
            rule_deal[i]=false;
            rule_ratio[i]=weight_coverSet[i]/cost_coverSet[i];

        }

        rule_weight=weight_coverSet;
        rule_new_cost=cost_coverSet;

        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();



//        for (int i = 0; i < rule_total_number; i++) {
//            System.out.println(i + " " + rule_ratio[i]);
//        }


        int volume = switch_volume;
        int max = rule_ratio[0];
        int pos = -1;
        while (result.size() < switch_volume) {
            max = -1;
            for (int i = 1; i < N+1; i++) {
                if (rule_deal[i] == false && rule_ratio[i] > max) {
                    max = rule_ratio[i];
                    pos = i;
                }
            }
//            System.out.println("this turn choose: "+pos+" "+volume);
            if (rule_new_cost[pos] <= volume) {
//                System.out.println("///  "+pos);


                    result.add(pos + "");
                    result.add(pos + "*");
                    rule_deal[pos] = true;

                volume = volume - rule_new_cost[pos];
                if (volume == 1) break;
//                volume+=nn;
//                System.out.println(volume+ " ----");


            } else {
                rule_deal[pos] = true;
            }


        }


        return result;











    }
    public static ArrayList<String> selectRulesUsingCoverSetMethoddd(
            int[] rule_weight, int[] rule_cost, int switch_volume,int N) {
        ArrayList<String> result = new ArrayList<String>();

        int rule_total_number = rule_cost.length;
        int[] rule_new_cost = new int[rule_total_number];
        int[] rule_ratio = new int[rule_total_number];
        boolean[] rule_deal = new boolean[rule_total_number];

        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();

        for (int i = 0; i < rule_total_number; i++) {
            rule_new_cost[i] = 2;
            rule_ratio[i] = rule_weight[i] / rule_new_cost[i];
            rule_deal[i] = false;
        }

//        for (int i = 0; i < rule_total_number; i++) {
//            System.out.println(i + " " + rule_ratio[i]);
//        }


        int volume = switch_volume;
        int max = rule_ratio[0];
        int pos = -1;
        while (result.size() < switch_volume) {
            max = -1;
            for (int i = 0; i < rule_total_number; i++) {
                if (rule_deal[i] == false && rule_ratio[i] > max) {
                    max = rule_ratio[i];
                    pos = i;
                }
            }
//            System.out.println("this turn choose: "+pos+" "+volume);
            if (rule_new_cost[pos] <= volume) {
//                System.out.println("///  "+pos);

                boolean flag = false;
                int distance = 0;
                int nn = 0;
                for (String str : result) {
                    if (!str.contains("*")) {
                        if (dependency.get(Integer.parseInt(str)).contains(pos)) {
                            distance = dependency.get(Integer.parseInt(str)).indexOf(pos);
                            if (distance == 1) {
                                result.add(pos + "");
                                result.add(dependency.get(Integer.parseInt(str)).get(0) + "");
                                rule_deal[pos] = true;
                                rule_deal[dependency.get(Integer.parseInt(str)).get(0)] = true;
                                if (dependency.get(Integer.parseInt(str)).size() == 2) {
                                    result.remove(Integer.parseInt(str) + "*");
                                    volume++;
                                }
                                flag = true;
                                break;
                            }
                        }
                    }

                }

                for (String str : result) {
                    if (!str.contains("*")) {
                        if (dependency.get(pos).contains(Integer.parseInt(str))) {
                            distance = dependency.get(pos).indexOf(Integer.parseInt(str));
                            if (distance == 1) {
                                result.add(pos + "");
                                result.add(dependency.get(pos).get(0) + "");
                                rule_deal[pos] = true;
                                rule_deal[dependency.get(pos).get(0)] = true;
//                                if(dependency.get(Integer.parseInt(str)).size()==2){
//                                    result.remove(Integer.parseInt(str)+"*");
//                                    volume++;
//                                }
                                flag = true;
                                break;
                            }
                        }
                    }

                }


                if (!flag) {
                    result.add(pos + "");
                    result.add(pos + "*");
                    rule_deal[pos] = true;

                }

                volume = volume - rule_new_cost[pos];
                if (volume == 1) break;
//                volume+=nn;
//                System.out.println(volume+ " ----");


            } else {
                rule_deal[pos] = true;
            }

//            for(String s:result){
//                System.out.print(s + " ");
//            }
//            System.out.println();

        }


        return result;

    }


    public static ArrayList<Integer> selectRulesUsingDependentSetMethod(
            int[] rule_weight, int[] rule_cost, int switch_volume, int N) {

        ArrayList<Integer> result = new ArrayList<Integer>();

//        int rule_total_number = N;
        int[] rule_ratio = new int[N+1];
        boolean[] rule_deal = new boolean[N+1];
        int[] weight_dependency = new int[N + 1];
        weight_dependency[0] = 0;
        int[] cost_dependency = new int[N + 1];
        cost_dependency[0] = 0;
        HashMap<Integer, ArrayList<Integer>> dependency = initParameter.initDependency(N);//initDependency();


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

        }



        for (int i = 1; i < N+1; i++) {
            rule_ratio[i] = weight_dependency[i]/rule_cost[i];//rule_weight[i] / rule_cost[i];
            rule_deal[i] = false;
        }

//        for(int i=0;i<rule_total_number;i++){
//            System.out.println(i+" "+rule_ratio[i]);
//        }

        rule_deal[0]=true;

        int volume = switch_volume;
        int max = rule_ratio[1];
        int pos = -1;
        while (result.size() < switch_volume) {
            max = -1;
            for (int i = 1; i < N+1; i++) {
                if (rule_deal[i] == false && rule_ratio[i] > max) {
                    max = rule_ratio[i];
                    pos = i;
                }
            }
//            System.out.println("this turn choose: "+pos);
            if (dependency.get(pos).size() + 1 <= volume) {
                result.add(pos);
                rule_deal[pos] = true;
                int nu = 0;
                for (Integer i : dependency.get(pos)) {

//                        System.out.println(result.contains(i)+" "+i);
//                    boolean flag = result.contains(i);
//                    if (!result.contains(i)) {
//
                        result.add(i);
                        rule_deal[i] = true;
                        nu++;
//                    }
//                    System.out.println("456");

                }

                volume = volume - nu - 1;
//                System.out.printlmn

            } else {
                rule_deal[pos] = true;
            }

//            System.out.println(pos+" "+dependency.get(pos)+" "+result);
        }

        return result;
    }


    public static HashMap<Integer, ArrayList<Integer>> initDependency() {


        HashMap<Integer, ArrayList<Integer>> dependency = new HashMap<Integer, ArrayList<Integer>>();

        ArrayList<Integer> arr0 = new ArrayList<Integer>();
        dependency.put(0, arr0);

        ArrayList<Integer> arr1 = new ArrayList<Integer>();
        arr1.add(0);
        dependency.put(1, arr1);

        ArrayList<Integer> arr2 = new ArrayList<Integer>();
        arr2.add(1);
        arr2.add(0);
        dependency.put(2, arr2);

        ArrayList<Integer> arr3 = new ArrayList<Integer>();
        dependency.put(3, arr3);

        ArrayList<Integer> arr4 = new ArrayList<Integer>();
        arr4.add(3);
        dependency.put(4, arr4);

        ArrayList<Integer> arr5 = new ArrayList<Integer>();
        arr5.add(4);
        arr5.add(3);
        dependency.put(5, arr5);

        return dependency;
    }


    public static void main(String[] args) {

        int switch_volume = 200;
        int N=65930;
        int[] rule_weight = initParameter.initData(N);//{10, 60, 30, 5, 10, 120};
        int[] rule_cost = initParameter.initCost(N);//{1, 2, 3, 1, 2, 3};

/*
        //覆盖集方法的代码
        ArrayList<String> result=selectRulesUsingCoverSetMethod(rule_weight,rule_cost,switch_volume,N);

        HashSet<String> set=new HashSet<String>();
        int sum = 0;
        for (String i : result) {
//            System.out.print(i + " ");
            set.add(i);
        }
//        System.out.println(result);
//        System.out.println(set);
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

        System.out.println("cover set weight:"+sum2);



*/


      //  依赖集方法的代码
        ArrayList<Integer> result=selectRulesUsingDependentSetMethod(rule_weight,rule_cost,switch_volume,N);

        HashSet<Integer> set=new HashSet<Integer>();
        int sum = 0;
        for (Integer i : result) {
//            System.out.print(i + " ");
            set.add(i);
        }
//        System.out.println(result);
//        System.out.println(set);
        System.out.println(set.size());
//        System.out.println("Mixed Set : " + sum);

        int sum2 = 0;
        for (Integer i : set) {
//            System.out.print(i + " ");
            set.add(i);

                sum2 += rule_weight[i];


        }
        System.out.println("dependency set weight:"+sum2);




    }


}
