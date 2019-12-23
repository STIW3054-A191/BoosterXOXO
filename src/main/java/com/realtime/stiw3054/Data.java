package com.realtime.stiw3054;

public class Data {
    private String Column1,Column2,Column3;

    public Data(String Column1, String Column2 , String Column3 ){
        super();
        this.Column1 =Column1;
        this.Column2 =Column2;
        this.Column3 =Column3;
    }

    public String getColumn1() {
        return Column1;
    }

    public String getColumn2() {
        return Column2;
    }

    public String getColumn3() {
        return Column3;
    }
}