package com.example.myapplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class rand_Card_Value {
    int random,value_Limit;
    Integer []value;
    String []pabcd = {"1000","0100","0010","0001"};


    public void generate_Card_Value (int num){

        value = new Integer [4];
        List<Integer> intList;

        try{
            FileWriter fw = new FileWriter("./carddata.txt");

            for(int i = 0 ; i < num ; i++){
                random = (int)(Math.random()*(2-0+1)) + 0;//score
                System.out.print(random);
                fw.write(String.valueOf(random));//w

                random = (int)(Math.random()*(3-0+1)) + 0;//permanent
                System.out.print(pabcd[random]);
                fw.write(pabcd[random]);//w

                value_Limit = (int)(Math.random()*(5-3+1)) + 3;

                int j = 0;
                while(j<4){
                    random = (int)(Math.random()*(3-0+1)) + 0;

                    if(value_Limit-random>=0){
                        value_Limit-=random;
                        value[j]=random;
                        j++;
                    }
                }

                intList = Arrays.asList(value);

                Collections.shuffle(intList);

                ((List) intList).toArray(value);
                for(int a:value){
                    System.out.print(a);     //need
                    fw.write(String.valueOf(a)); //w
                }

                System.out.println();
                fw.write("\r\n");
            }
            fw.flush();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




    }
}
/*
        rand_Card_Value rcv;
        rcv = new rand_Card_Value();
        int num1;
        System.out.println("How many set do you want to generate ?");
        Scanner scanner = new Scanner(System.in);
        num1 = scanner.nextInt();
        rcv.generate_Card_Value(num1);

*/