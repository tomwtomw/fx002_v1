package org.tomw.utils;

/**
 * manages system properties
 */
public class RestorableSystemProperty {
    private String key;
    private String originalValue;
    private String newValue;

    /**
     * set new system property
     * @param name
     * @param value
     */
    public RestorableSystemProperty(String name, String value){
        key=name;
        newValue=value;
        String currentvalue = System.getProperty(name);
        if(currentvalue==null){
            originalValue=null;
        }else{
            originalValue=currentvalue;
        }
        System.setProperty(name,value);
    }

    public void restore(){
        if(originalValue!=null){
            System.setProperty(key,originalValue);
        }else{
            System.clearProperty(key);
        }
    }
}
