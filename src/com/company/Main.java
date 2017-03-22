package com.company;

public class Main {



    class Alpha {
        String getType() {
            return "alpha";
        }
    }
    class Beta extends Alpha {
        String getType() {
            return "beta";
        }
    }
    public class Gamma extends Beta {
        String getType() {
            return "gamma";
        }
        public static void main(String[] args) {
            //Gamma g1 = new Alpha();
            Gamma g2 = new Gamma();
            System.out.println();
            System.out.println(  " "
                    + g2.getType());
        }
    }
}
