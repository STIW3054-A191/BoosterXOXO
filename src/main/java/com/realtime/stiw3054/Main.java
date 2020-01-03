package com.realtime.stiw3054;

import jxl.read.biff.BiffException;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws InterruptedException, IOException, BiffException {


        CloneRepo repo = new CloneRepo();
        repo.Clone();
        Ckjm sc = new Ckjm();
        sc.runckjm();
        ConvertPDF convert = new ConvertPDF();
        convert.mkpdf();


    }

}




