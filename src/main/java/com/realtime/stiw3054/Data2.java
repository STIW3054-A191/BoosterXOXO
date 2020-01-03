package com.realtime.stiw3054;

public class Data2 {

    private String matric;
    private String name;

    public Data2() {

    }

    public Data2(String m, String n) {

        this.matric = m;
        this.name = n;
    }

    public void setDataM(String m) {
        this.matric = m;
    }

    public String getDataM() {
        return matric;
    }

    public void setDataN(String n) {
        this.name = n;
    }

    public String getDataN() {
        return name;
    }
}
