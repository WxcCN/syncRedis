package com.company;
import java.util.*;

/**
 * Created by Nova on 16/10/9.
 */
public class intToString {
    Random random = new Random();
    String data = "Nova";
    public void testInt(String data){
        try {
            System.out.println(Integer.parseInt(data));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void testInt(String data,int radix) {
        try {
            System.out.println(Integer.parseInt(data,radix));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    int[] aArray = null;
    int i = 0;//数组下标

    public int[] randomArray(int start,int end){
        if(start>end){
            System.out.println("起大于终");
            return null;
        }
        int length = end - start;
        aArray = new int[length+1];
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间"+startTime);
        randomKey(start-1,end+1);
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间"+ endTime);
        System.out.println("生成"+length+"需要时间为:"+ (endTime-startTime));


//        int count = 0;
//        for(Integer integer:aArray){
//            System.out.println("第"+ count++ +"个随机数为:" +integer);
//        }
        return aArray;
    }

    public void randomKey(int astart,int aend) {
        if ((astart + 1) == aend) return;
        //生成开区间内随机数
        int middle = random.nextInt(aend - astart - 1) + astart + 1;
        aArray[i++] = middle;
        //递归随机化
        int flag = random.nextInt(2);
        if (flag == 0) {
            randomKey(astart, middle);
            randomKey(middle, aend);
        }else{
            randomKey(middle, aend);
            randomKey(astart, middle);
        }
    }



    public void randomTest(){
        int[] testArray = randomArray(1333,1888);
        int[] array2 = new int[testArray.length];
        int count = 0;

        for (int i=0;i<=testArray.length;i++
             ) {
            System.out.println("第"+i+"次测试");
            for (int j = 0; j < count; j++) {
                if(array2[j]==aArray[i]){
                    System.out.println(i);
                    break;
                }else {
                    array2[count++]=i;
                }
            }

        }
    }

    public static void shuffle(int start ,int end){
        int length = end - start;
        //int[] randonArr = new int[length+1];

        List list = new ArrayList();
        for(int i = 0;i < length;i++){
           // System.out.print(i+", ");
            list.add(i);
        }
        System.out.println();
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间2:"+startTime);
        Collections.shuffle(list);
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间2:"+ endTime);
        System.out.println("生成"+length+"需要时间为:"+ (endTime-startTime));



        Iterator ite = list.iterator();
        while(ite.hasNext()){
            //System.out.print(ite.next().toString()+", ");
        }
    }

    int stringToInt(String s) {
        int[] num = new int[10];
        int j = 0;
        String tmp = null;
        for (int i = 0; i < s.length(); i++) {


        }
        int NUM = (int) Math.pow(10, j);
        int a = 0;
        for (int i = 0; i < num.length; i++) {
            NUM = NUM / 10;
            a += num[i] * NUM;
        }
        if (tmp != null) {
            return -a;
        }

        return -1;
    }

    // 将大写字母转换成数字 26进制
    public int letterToNum(String input) {
        int returnNum = 0;
        int index = (int) Math.pow(26,input.length()-1);
        int count = 0;
        for (byte b : input.getBytes()) {
            returnNum += (b - 65)*index;
            index/=26;
        }
        return returnNum;
    }

    // 将数字转换成字母
    public String numToLetter(Integer input,int length) {
        String rS = "";
        int index = 1;
        int count = 0;
        do{
            index = input/(int)Math.pow(26,count);
            count++;
        }while (index>0);
        int remainder = input;
        for(int i = count-2;i>=0;i--){
            int pow = (int)Math.pow(26,i);//种子
            index = remainder/pow;//商
            remainder%=pow;//余数
            rS += (char)((byte)(index)+65);
        }
        for(;count<=length;count++)//补位
        {
            rS = 'A'+rS;
        }
        return rS;
    }

    public String numToLetter2(Integer input,int length) {
            String ntl = "";
            String tmp = numToLetter(input);
            for(;tmp.length()<length;length--){
                tmp+='A';
            }

        return  tmp;
    }
    public String numToLetter(Integer input) {
        String rS = "";
        int index = 1;
        int remainder = input;
        while(index>0){
            int pow = (int)Math.pow(26,i);//种子
            index = remainder/pow;//商
            remainder%=pow;//余数
            rS += (char)((byte)(index)+65);
        }
        return rS;
    }




}
