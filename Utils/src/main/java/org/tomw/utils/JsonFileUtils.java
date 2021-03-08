package org.tomw.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for reading and writing json objects into text file
 * Created by tomw on 7/22/2017.
 */
public class JsonFileUtils {
    private final static Logger LOGGER = Logger.getLogger(JsonFileUtils.class.getName());
    private static final String ENCODING = "ISO-8859-1";

    private  Json2String jsopn2StringConverter = new Json2String();

    private final JSONParser jsonParser = new JSONParser();

    public JsonFileUtils(){}

    public void jsonObject2File(JSONObject json, File f) throws IOException {
        String s = jsopn2StringConverter.toPrettyString(json);
        FileUtils.writeStringToFile(f,s,ENCODING);
    }
    public void jsonObject2File(JSONObject json, String fileName) throws IOException {
        File f = new File(fileName);
        jsonObject2File(json,f);
    }

    public void jsonArray2File(JSONArray json, File f) throws IOException {
        String s = jsopn2StringConverter.toPrettyString(json);
        FileUtils.writeStringToFile(f,s,ENCODING);
    }
    public void jsonArray2File(JSONArray json, String fileName) throws IOException {
        File f = new File(fileName);
        jsonArray2File(json,f);
    }

    /**
     * read json object from file
     * @param f file to be read
     * @return json object
     * @throws IOException if file cannot be read
     * @throws ParseException if its content cannot be parsed
     */
    public JSONObject jsonObjectFromFile( File f) throws IOException, ParseException {
        if(f!=null && f.exists()){
            String s = FileUtils.readFileToString(f, ENCODING);
            return (JSONObject) jsonParser.parse(s);
        }else{
            return new JSONObject();
        }
    }

    /**
     * read json object from file
     * @param fileName file name as string
     * @return json object
     * @throws IOException if file cannot be read
     * @throws ParseException if its content cannot be parsed
     */
    public JSONObject jsonObjectFromFile(String fileName) throws IOException, ParseException {
        File f = new File(fileName);
        return jsonObjectFromFile(f);
    }

    /**
     * read json file located in resources directory
     * @param fileName name of the file in resources
     * @return json object
     * @throws ParseException if it cannot be parsed
     */
    public JSONObject jsonObjectFromFileInResources(String fileName) throws ParseException, IOException {
        String s = TomwFileUtils.readTextFileFromResourcesAsStream(fileName);
        return (JSONObject)jsonParser.parse(s);
    }

    /**
     * read jsonarray from file
     * @param f file
     * @return json arrat
     * @throws IOException if file cannot be read
     * @throws ParseException if its content cannot be parsed
     */
    public JSONArray jsonArrayFromFile(File f) throws IOException, ParseException {
        String s = FileUtils.readFileToString(f,ENCODING);
        return (JSONArray)jsonParser.parse(s);
    }

    /**
     * get json array from file with given name
     * @param fileName name of the file
     * @return json array
     * @throws IOException if file cannot be read
     * @throws ParseException if its content cannot be parsed
     */
    public JSONArray jsonArrayFromFile( String fileName) throws IOException, ParseException {
        File f = new File(fileName);
        return jsonArrayFromFile(f);
    }
}
