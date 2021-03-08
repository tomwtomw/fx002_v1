package org.tomw.utils;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test utility to write/read json onbjects to files
 * Created by tomw on 7/22/2017.
 */
public class JsonFileUtilsTest {
    private final static Logger LOGGER = Logger.getLogger(JsonFileUtilsTest.class.getName());

    private static final String TEST_DIRECTORY="test";
    private static final String AAA="aaa";
    private static final String BBB="bbb";
    private static final String CCC="ccc";
    private static final String DDD="ddd";
    private static final String EEE="eee";

    public File testDirectory = new File(TEST_DIRECTORY);

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.mkdirs(testDirectory);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    @SuppressWarnings("unchecked")
    private JSONObject createJson(){
        JSONObject json = new JSONObject();
        json.put(AAA,"aaa");
        json.put(BBB,23.56);
        json.put(CCC,null);
        json.put(DDD,false);
        json.put(EEE,new JSONArray());
        return json;
    }

    @SuppressWarnings("unchecked")
    private JSONArray createJsonArray(){
        JSONArray json = new JSONArray();
        json.add("aaa");
        json.add(23.56);
        json.add(null);
        json.add(false);
        json.add(createJson());
        return json;
    }

    @Test
    public void jsonObject2File() throws Exception {
        LOGGER.info("jsonObject2File");
        writeAndReadJsonObject();
    }

    public void writeAndReadJsonObject() throws IOException, ParseException {

        JSONObject json = createJson();
        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        File tempFile = new File(testDirectory,"testFile1.json");

        jsonFileUtils.jsonObject2File(json,tempFile);

        // read it back
        JsonFileUtils jsonFileUtils2 = new JsonFileUtils();
        JSONObject json2 = jsonFileUtils2.jsonObjectFromFile(tempFile);

        assertEquals(json,json2);
    }

    @Test
    public void jsonObject2File1() throws Exception {
        LOGGER.info("jsonObject2File1");

        JSONObject json = createJson();
        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        File tempFile = new File(testDirectory,"testFile1.json");

        jsonFileUtils.jsonObject2File(json,tempFile.toString());

        // read it back
        JsonFileUtils jsonFileUtils2 = new JsonFileUtils();
        JSONObject json2 = jsonFileUtils2.jsonObjectFromFile(tempFile.toString());

        assertEquals(json,json2);
    }

    @Test
    public void jsonArray2File() throws Exception {
        LOGGER.info("jsonArray2File");
        writeAndReadArrayFromFile();
    }

    public void writeAndReadArrayFromFile() throws IOException, ParseException {

        JSONArray json = createJsonArray();
        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        File tempFile = new File(testDirectory,"testFile1.json");

        jsonFileUtils.jsonArray2File(json,tempFile);

        // read it back
        JsonFileUtils jsonFileUtils2 = new JsonFileUtils();
        JSONArray json2 = jsonFileUtils2.jsonArrayFromFile(tempFile);

        assertEquals(json,json2);
    }

    @Test
    public void jsonArray2File1() throws Exception {
        LOGGER.info("jsonArray2File");
        writeAndReadArrayFromFile1();
    }

    public void writeAndReadArrayFromFile1() throws IOException, ParseException {

        JSONArray json = createJsonArray();
        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        File tempFile = new File(testDirectory,"testFile1.json");

        jsonFileUtils.jsonArray2File(json,tempFile.toString());

        // read it back
        JsonFileUtils jsonFileUtils2 = new JsonFileUtils();
        JSONArray json2 = jsonFileUtils2.jsonArrayFromFile(tempFile.toString());

        assertEquals(json,json2);
    }

    @Test
    public void jsonObjectFromFile() throws Exception {
        LOGGER.info("jsonObjectFromFile");

        JSONObject json = createJson();
        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        File tempFile = new File(testDirectory,"testFile1.json");

        jsonFileUtils.jsonObject2File(json,tempFile);

        // read it back
        JsonFileUtils jsonFileUtils2 = new JsonFileUtils();
        JSONObject json2 = jsonFileUtils2.jsonObjectFromFile(tempFile);

        assertEquals(json,json2);
    }

    @Test
    public void jsonObjectFromFile1() throws Exception {
        LOGGER.info("jsonObjectFromFile1");
        writeAndReadJsonObject();
    }

    @Test
    public void jsonArrayFromFile() throws Exception {
        LOGGER.info("jsonArrayFromFile");
        writeAndReadArrayFromFile();
    }

    @Test
    public void jsonArrayFromFile1() throws Exception {
        LOGGER.info("jsonArrayFromFile1");
        writeAndReadArrayFromFile1();
    }

    @Test
    public void test_jsonObjectFromFileInResources() throws Exception{
        LOGGER.info("test_jsonObjectFromFileInResources");
        String fileName = "/test.json";

        JsonFileUtils jsonUtils = new JsonFileUtils();
        JSONObject json = jsonUtils.jsonObjectFromFileInResources(fileName);
        assertTrue(json.keySet().contains("Transactions"));
    }
}