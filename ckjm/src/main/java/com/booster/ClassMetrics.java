package com.booster;

import java.util.HashSet;

public class ClassMetrics {
    private int wmc;
    private int dit;
    private int noc;
    private int cbo;
    private int rfc;
    private int lcom;
    private int npm;
    private boolean visited;
    private boolean isPublicClass;
    private HashSet<String> afferentCoupledClasses;

    ClassMetrics() {
        wmc = 0;
        noc = 0;
        cbo = 0;
        npm = 0;
        visited = false;
        afferentCoupledClasses = new HashSet<String>();
    }


    public void incWmc() { wmc++; }
    public int getWmc() { return wmc; }

    public void incNoc() { noc++; }
    public int getNoc() { return noc; }

    public void setRfc(int r) { rfc = r; }
    public int getRfc() { return rfc; }

    public void setDit(int d) { dit = d; }
    public int getDit() { return dit; }

    public void setCbo(int c) { cbo = c; }
    public int getCbo() { return cbo; }

    public int getLcom() { return lcom; }
    public void setLcom(int l) { lcom = l; }

    public int getCa() { return afferentCoupledClasses.size(); }
    public void addAfferentCoupling(String name) { afferentCoupledClasses.add(name); }

    public void incNpm() { npm++; }
    public int getNpm() { return npm; }

    public boolean isPublic() { return isPublicClass; }
    public void setPublic() { isPublicClass = true; }

    public static boolean isJdkClass(String s) {
        return (s.startsWith("java.") ||
                s.startsWith("javax.") ||
                s.startsWith("org.omg.") ||
                s.startsWith("org.w3c.dom.") ||
                s.startsWith("org.xml.sax."));
    }

    public String toString() {
        return (
                wmc +
                        " " + getDit() +
                        " " + noc +
                        " " + cbo +
                        " " + rfc +
                        " " + lcom +
                        " " + getCa()+
                        " " + npm);
    }

    public void setVisited() { visited = true; }
    public boolean isVisited() { return visited; }
}
