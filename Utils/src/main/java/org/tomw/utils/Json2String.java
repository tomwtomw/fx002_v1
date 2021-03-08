package org.tomw.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Converting json into human readable strings
 * Created by tomw on 7/15/2017.
 */
public class Json2String {
    private static final String EOL="\n";
    private String defaultIndent="  ";
    private int indentation = 0;

    /**
     * Convert json into nicely formatte string
     * @param o json object or array
     * @return json as nicely formatted string
     */
    public String toPrettyString(Object o){
       if(isNonJson(o)){
           return o.toString();
       }else{
            if(isJsonObject(o)){
                return jsonObjectToPrettyString((JSONObject)o);
            }
           if(isJsonArray(o)){
               return jsonArrayToPrettyString((JSONArray)o);
           }
       }
        throw new RuntimeException("We should never reach this place in code");
    }

    /**
     * convert json array to nice string
     * @param jsonArray json array
     * @return string
     */
    private String jsonArrayToPrettyString(JSONArray jsonArray) {
        String result;
        increaseIndent();
        StringBuilder resultBuilder = new StringBuilder("[" + EOL);
        for(Object o : jsonArray){
            resultBuilder.append(getIndentation()).append(objectToString(o)).append(",").append(EOL);
        }
        result = resultBuilder.toString();
        result = TomwStringUtils.removeLastComa(result);
        decreaseIndent();
        return result+getIndentation()+"]";
    }

    /**
     * Convert json object to nicely formatted string
     * @param jsonObject json object
     * @return json object as nicely formatted string
     */
    private String jsonObjectToPrettyString(JSONObject jsonObject) {
        String result;
        increaseIndent();
        StringBuilder resultBuilder = new StringBuilder("{" + EOL);
        for(Object o : jsonObject.keySet()){
            String key = (String)o;
            resultBuilder.append(getIndentation()).append(wrapInQuotes(key)).append(":").append(objectToString(jsonObject.get(key))).append(",").append(EOL);
        }
        result = resultBuilder.toString();
        result = TomwStringUtils.removeLastComa(result);
        decreaseIndent();
        return result+getIndentation()+"}";
    }

    /**
     * Check if object is JSONObject
     * @param o object
     * @return true if this is JSONObject
     */
    public boolean isJsonObject(Object o){
        try {
            JSONObject json = (JSONObject) o;
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Check if object is JSONArray
     * @param o object
     * @return true if this is JSONArray
     */
    public boolean isJsonArray(Object o){
        try {
            JSONArray json = (JSONArray) o;
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * check if this is simple json object
     * @param o object to check
     * @return true if it is neither jsonobject nor json array, also true for null
     */
    public boolean isNonJson(Object o){
        if(o==null){
            return true;
        }
        if(isJsonArray(o) || isJsonObject(o)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * test if json is json object
     * @param json object to test
     * @return thue if it is jsonobject
     */
    public boolean isSimpleJsonObject(JSONObject json){
        for(Object o : json.keySet()){
            String key = (String)o;
            Object obj = json.get(key);
            if(!isNonJson(obj)){
                return false;
            }
        }
        return true;
    }

    /**
     * check if this is jsonarray
     * @param json object to be tested
     * @return true if this is jsonarray
     */
    public boolean isSimpleJsonArray(JSONArray json){
        for(Object o : json){
            if(!isNonJson(o)){
                return false;
            }
        }
        return true;
    }

    private void increaseIndent(){
        indentation=indentation+1;
    }
    private void decreaseIndent(){
        indentation=indentation-1;
    }
    private String getIndentation(){
        StringBuilder sb = new StringBuilder("");
        for(int i=0;i<indentation;i++){
            sb.append(defaultIndent);
        }
        return sb.toString();
    }

    private String objectToString(Object o){
        if(isNonJson(o)){
            if (o == null) {
                return null;
            } else {
                if (o.getClass() == String.class) {
                    return wrapInQuotes((String) o);
                } else {
                    return o.toString();
                }
            }
        }else{
            if(isJsonObject(o)){
                if(isSimpleJsonObject((JSONObject)o)){
                    return ((JSONObject)o).toJSONString();
                }else{
                    return toPrettyString(o);
                }
            }
            if(isJsonArray(o)){
                if(isSimpleJsonArray((JSONArray)o)){
                    return ((JSONArray)o).toJSONString();
                }else{
                    return toPrettyString(o);
                }
            }
            throw new RuntimeException("Somewthing is wrong, the object is neither JSONArray nor JSONObject: "+o);
        }
    }

    private String wrapInQuotes(String s){
        return "\""+s+"\"";
    }
}
