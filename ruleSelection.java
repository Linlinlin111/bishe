package bishe;

import com.sun.javafx.css.FontUnits;

import java.util.ArrayList;
import java.util.HashMap;

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
            int[] rule_weight, int[] rule_cost, int switch_volume) {
        ArrayList<String> result = new ArrayList<String>();

        int rule_total_number = rule_cost.length;
        int[] rule_new_cost = new int[rule_total_number];
        int[] rule_ratio = new int[rule_total_number];
        boolean[] rule_deal = new boolean[rule_total_number];

        HashMap<Integer, ArrayList<Integer>> dependency = initDependency();

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
            int[] rule_weight, int[] rule_cost, int switch_volume) {

        ArrayList<Integer> result = new ArrayList<Integer>();

        int rule_total_number = rule_weight.length;
        int[] rule_ratio = new int[rule_total_number];
        boolean[] rule_deal = new boolean[rule_total_number];
        HashMap<Integer, ArrayList<Integer>> dependency = initDependency();


        for (int i = 0; i < rule_total_number; i++) {
            rule_ratio[i] = rule_weight[i] / rule_cost[i];
            rule_deal[i] = false;
        }

//        for(int i=0;i<rule_total_number;i++){
//            System.out.println(i+" "+rule_ratio[i]);
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
//            System.out.println("this turn choose: "+pos);
            if (dependency.get(pos).size() + 1 <= volume) {
                result.add(pos);
                rule_deal[pos] = true;
                int nu = 0;
                for (Integer i : dependency.get(pos)) {

//                        System.out.println(result.contains(i)+" "+i);
                    boolean flag = result.contains(i);
                    if (!result.contains(i)) {
//
                        result.add(i);
                        rule_deal[i] = true;
                        nu++;
                    }
//                    System.out.println("456");

                }

                volume = volume - nu - 1;
//                System.out.printlmn

            } else {
                rule_deal[pos] = true;
            }


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


        int[] rule_weight = {10, 60, 30, 5, 10, 120};
        int[] rule_cost = {1, 2, 3, 1, 2, 3};
        int switch_volume = 4;

        ArrayList<Integer> rule1 = selectRulesUsingDependentSetMethod(rule_weight, rule_cost, switch_volume);

        int sum = 0;
        for (int i : rule1) {
            System.out.print(i + " ");
            sum += rule_weight[i];
        }

        System.out.println("Dependecy Set : " + sum);

        sum = 0;

        ArrayList<String> rule2 = selectRulesUsingCoverSetMethod(rule_weight, rule_cost, switch_volume);

        for (String i : rule2) {
            System.out.print(i + " ");
            if (!i.contains("*")) {
                sum += rule_weight[Integer.parseInt(i)];

            }
        }

        System.out.println("Cover Set : " + sum);

        ArrayList<String> rule3 = selectRuleUsingMixedSetMethod(rule_weight, rule_cost, switch_volume);
        sum = 0;
        for (String i : rule3) {
            System.out.print(i + " ");
            if (!i.contains("*")) {
                sum += rule_weight[Integer.parseInt(i)];
            }
        }

        System.out.println("Mixed Set : " + sum);


    }


}
