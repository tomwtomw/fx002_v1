package org.tomw.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import static org.junit.Assert.*;

/**
 * Created by tomw on 7/15/2017.
 */
public class Json2StringTest {
    @Test
    public void toPrettyString() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AAA","aaa");
        jsonObject.put("BBBB",2);
        jsonObject.put("CCCC",false);
        jsonObject.put("DDDD",null);

        String result = jsonFormatter.toPrettyString(jsonObject);
        System.out.println(result);
        System.out.println(jsonObject.toJSONString());

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("AA");
        jsonArray.add(null);
        jsonArray.add(3.14);
        jsonArray.add(5);
        jsonArray.add(false);
        String result2 = jsonFormatter.toPrettyString(jsonArray);
        System.out.println(result2);
        System.out.println(jsonArray.toJSONString());

        JSONObject json = new JSONObject();
        json.put("Integer",1);
        json.put("Float",-2.71);
        json.put("Boolean",false);
        json.put("NULL",null);
        json.put("String","AGFJDJTDDJFDJFKDFJDDJDJD");
        json.put("Array",jsonArray);
        json.put("json",jsonObject);
        String result3 = jsonFormatter.toPrettyString(json);
        System.out.println(result3);
        System.out.println(json.toJSONString());

        String jsonStringInput = TomwFileUtils.readTextFileFromResources("test.json");
        System.out.println(jsonStringInput);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonStringInput);
        JSONObject primaryObject = (JSONObject)obj;

        String s = jsonFormatter.toPrettyString(primaryObject);
        System.out.println(s);

        JSONObject reconstructedObject = (JSONObject)parser.parse(s);
        assertEquals(primaryObject,reconstructedObject);
    }

    @Test
    public void test_isJsonObject() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        assertTrue(jsonFormatter.isJsonObject(jsonObject));

        JSONArray jsonArray = new JSONArray();
        assertFalse(jsonFormatter.isJsonObject(jsonArray));

        String string = "dummy";
        assertFalse(jsonFormatter.isJsonObject(string));
    }

    @Test
    public void test_isJsonArray() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        assertFalse(jsonFormatter.isJsonArray(jsonObject));

        JSONArray jsonArray = new JSONArray();
        assertTrue(jsonFormatter.isJsonArray(jsonArray));

        String string = "dummy";
        assertFalse(jsonFormatter.isJsonArray(string));
    }

    @Test
    public void test_isNonJson() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        assertFalse(jsonFormatter.isNonJson(jsonObject));

        JSONArray jsonArray = new JSONArray();
        assertFalse(jsonFormatter.isNonJson(jsonArray));

        String string = "dummy";
        assertTrue(jsonFormatter.isNonJson(string));
    }

    @Test
    public void test_isSimpleJsonObject() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AAA","aaa");
        jsonObject.put("BBBB",2);
        jsonObject.put("CCCC",false);
        jsonObject.put("DDDD",null);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("AA");
        jsonArray.add(null);
        jsonArray.add(3.14);
        jsonArray.add(5);
        jsonArray.add(false);

        JSONObject json = new JSONObject();
        json.put("Integer",1);
        json.put("Float",-2.71);
        json.put("Boolean",false);
        json.put("NULL",null);
        json.put("String","AGFJDJTDDJFDJFKDFJDDJDJD");
        json.put("Array",jsonArray);
        json.put("json",jsonObject);

        assertTrue(jsonFormatter.isSimpleJsonObject(jsonObject));
        assertFalse(jsonFormatter.isSimpleJsonObject(json));

    }


    @Test
    public void test_isSimpleJsonArray() throws Exception {
        Json2String jsonFormatter = new Json2String();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AAA", "aaa");
        jsonObject.put("BBBB", 2);
        jsonObject.put("CCCC", false);
        jsonObject.put("DDDD", null);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("AA");
        jsonArray.add(null);
        jsonArray.add(3.14);
        jsonArray.add(5);
        jsonArray.add(false);

        assertTrue(jsonFormatter.isSimpleJsonArray(jsonArray));

        JSONObject json = new JSONObject();
        json.put("Integer", 1);
        json.put("Float", -2.71);
        json.put("Boolean", false);
        json.put("NULL", null);
        json.put("String", "AGFJDJTDDJFDJFKDFJDDJDJD");
        json.put("Array", jsonArray);
        json.put("json", jsonObject);

        jsonArray.add(jsonObject);

        assertFalse(jsonFormatter.isSimpleJsonArray(jsonArray));

    }
}