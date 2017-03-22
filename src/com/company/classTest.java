package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nova on 16/12/19.
 */
public class classTest implements Cloneable {
    public int a1;
    public int a2;

    public int getA1() {
        return a1;
    }

    public void setA1(int a1) {
        this.a1 = a1;
    }

    public int getA2() {
        return a2;
    }

    public void setA2(int a2) {
        this.a2 = a2;
    }


    @Override
    public Object clone(){

        classTest cl1 = null;
        try {
            cl1 = ((classTest) super.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cl1;
    }

    public void listTest(){

        classTest c1 = new classTest();
        c1.setA1(1);
        List<classTest> l1 = new ArrayList<classTest>();
        for (int i = 0; i < 10; i++) {
            classTest c2 = new classTest();
            //TODO
            c2 = ((classTest) c1.clone());
            System.out.println(c1==c2);
            c2.setA1(i);
            l1.add(c2);
        }
        c1.setA1(222);
        for (int i = 0; i < 10; i++) {
            System.out.println("获得a1:" +
                    l1.get(i).getA1());
        }
    }
}
