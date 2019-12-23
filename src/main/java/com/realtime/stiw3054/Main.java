package com.realtime.stiw3054;


public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("____________________________________________________________________");
        System.out.printf("| %-5s| %-17s| %-50s\n","No.","Matric","Name");
        System.out.println("____________________________________________________________________");
        StudentList list = new StudentList();
        list.showList();
        System.out.println(" ");
    }

}