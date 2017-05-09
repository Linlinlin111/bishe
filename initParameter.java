package bishe;

import javax.naming.InitialContext;
import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fenglin on 2017/5/7.
 */
public class initParameter {


    public static int[] initCost(int N){
        int[] cost=new int[N+1];
        cost[0]=0;
        for(int i=1;i<N+1;i++)
            cost[i]=1;
        return cost;
    }

    public static int[] initData(int N){
//        System.out.println(N);
        int[] data=new int[N+1];
        data[0]=0;


        File file = new File("I:\\pyproject\\bishePython\\data\\data.txt");
        BufferedReader reader=null;
        String tempString=null;
        try {
            int line = 1;
            reader = new BufferedReader(new FileReader(file));
            while ((tempString = reader.readLine()) != null) {
               String[] strArray=tempString.split(":");
               int index=Integer.parseInt(strArray[0]);
               int weight=Integer.parseInt(strArray[1]);
               data[index]=weight;
//               System.out.println(tempString);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return data;

    }


    public static HashMap<Integer, ArrayList<Integer>> initDependency(int N){

        HashMap<Integer, ArrayList<Integer>> dependency=new HashMap<Integer, ArrayList<Integer>>();


        File file = new File("I:\\pyproject\\bishePython\\data\\dependency.txt");
        BufferedReader reader=null;
        String tempString=null;
        try {
            int line = 1;
            reader = new BufferedReader(new FileReader(file));
            while ((tempString = reader.readLine()) != null) {
                String[] strArray=tempString.split("=");
                int index=Integer.parseInt(strArray[0]);
                String str=strArray[1];
                str=str.replace("[","");
                str=str.replace("]","");

                ArrayList<Integer> list=new ArrayList<Integer>();
                if(str.equals(""))
                    dependency.put(index,list);
                else {
//                    System.out.println(str);
                    if(!str.contains(",")){
                        list.add(Integer.parseInt(str));
                    }else {
                    String[] array = str.split(",");
                    for(int i=0;i<array.length;i++){
//                        list.add(Integer.parseInt(array[i]));
                        String tp=array[i].trim();
                        int num=Integer.parseInt(tp);
                        list.add(num);

                    }
                    }
                    dependency.put(index,list);
                }


            }

        }catch (IOException e){
            e.printStackTrace();
        }

//        System.out.println(dependency);


        return dependency;

    }

//    public  static  void  main(String[] args){
//        initData(10);
//    }
    public static void main(String[] args){

        System.out.println(initDependency(10));
        System.out.println(initData(10));
        for(int i:initCost(10))
        System.out.println(i);
        for(int i:initData(10))
            System.out.println(i);
    }
}

