package org.tomw.czeki.imageview;

import java.io.File;

public class Temp {
    public static void main(String[] args){
        String fileName="EA5DA3CC123412610009F";
        System.out.println(fileName.matches("^\\w{8}\\d{4}\\w{8}"));
        String a="EA5DA3CC123412610009G";
        System.out.println(a.matches("^\\w{8}\\d{4}\\w{8}[FB]"));

        String dir="a/b/c/d/e.txt";
        File f = new File(dir);
        System.out.println(f.getPath());
        System.out.println(f.getParentFile());
    }
}
