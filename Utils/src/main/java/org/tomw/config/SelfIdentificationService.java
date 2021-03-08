package org.tomw.config;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class SelfIdentificationService {
    private final static Logger LOGGER = Logger.getLogger(SelfIdentificationService.class.getName());

    public static final String LAPTOP1="Laptop1";
    public static final String OFFICE1="Office1";

    public static final String TEST="Test";
    public static final String DEVEL="Devel";
    public static final String PREPROD="Preprod";
    public static final String PROD="Prod";

    public String DEVEL_DIR_OFFICE1;
    public   String TEST_DIR_OFFICE1;

    public  String DEVEL_DIR_LAPTOP1 ;
    public String TEST_DIR_LAPTOP1 ;

    private Map<String,Map<String,String>> map = new HashMap<>();

    public Map<String, Map<String, String>> getMap() {
        return map;
    }

    public boolean isKnownInstance(String instance){
        if(LAPTOP1.equals(instance) || OFFICE1.equals(instance)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Get name of the system. null if unknown
     * @return system name or null if unknown
     */
    public String  getSystemName(){
        if(isLaptop1()){
            return LAPTOP1;
        }
        if(isOffice1()){
            return OFFICE1;
        }
        throw new RuntimeException("Unknown system");
    }

    /**
     * get system type: TEST, DEVEL etc
     * @return type
     */
    public String getSystemType(){
        if(isTest())return TEST;
        if(isDevel())return DEVEL;
        if(isPreprod())return PREPROD;
        if(isProd())return PROD;
        throw new RuntimeException("Unknown system type!");
    }

    public static String currentDir(){
        return System.getProperty("user.dir");
    }

    /**
     * is this LAPTOP1?
     * @return true if yes
     */
    public boolean isLaptop1(){
        return currentDir().contains("D:\\tomw2");
    }

    /**
     * Is this office Pc
     * @return true if yes
     */
    public boolean isOffice1(){
        return currentDir().contains("C:\\Users\\tomw\\");
    }

    public abstract String getApplicationName();

    public boolean isTest() {
        return SelfIdentificationService.currentDir().contains(
                getMap().get(getSystemName()).get(TEST)
        );
    }

    public boolean isDevel() {
        return SelfIdentificationService.currentDir().contains(
                getMap().get(getSystemName()).get(DEVEL)
        ) && !isTest();
    }

    public boolean isPreprod() {
        return  SelfIdentificationService.currentDir().contains(PREPROD);
    }

    public boolean isProd() {
        return  SelfIdentificationService.currentDir().contains(PROD) && !isPreprod();
    }

    public String instanceName() {
        return getApplicationName()+"@"+instanceType()+"@"+getSystemName();
    }
    /**
     * return type of instance
     * @return TEST, DEVEL, PREPROD or PROD
     */
    public String instanceType(){
        if(isTest())return TEST;
        if(isDevel())return DEVEL;
        if(isPreprod())return PREPROD;
        if(isProd())return PROD;
        LOGGER.error("Unknown instance type");
        return null;
    }

    public String toString(){
        return instanceName();
    }

}
