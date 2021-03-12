package org.tomw.czeki.transactionoverview;

public class MostRecentCheckNumberHandler {
    private String lastCheckNumber="";

    public String getLastCheckNumber() {
        return this.lastCheckNumber;
    }

    public void setLastCheckNumber(String lastCheckNumber) {
        this.lastCheckNumber = lastCheckNumber;
    }

    public String incrementCheckNumber(){
        if(representsInt(lastCheckNumber)){
            int nextCheckNumber = parseToInt(lastCheckNumber)+1;
            if(nextCheckNumber>0) {
                String nextCheckNumberAsString = Integer.toString(nextCheckNumber);
                lastCheckNumber = nextCheckNumberAsString;
            }
        }
        return lastCheckNumber;
    }

    public static int parseToInt(String s){
        int result = Integer.parseInt(s);
        return result;
    }

    public static boolean representsInt(String s){
        boolean isInt=true;
        try{
            Integer.parseInt(s);
        }catch(NumberFormatException e){
            isInt=false;
        }
        return isInt;
    }
}
