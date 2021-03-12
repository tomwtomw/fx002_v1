/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tomw
 */
public class PictureImageParser {

    static JSONParser parser = new JSONParser();

    public static PictureImage fromString(String inputString) throws ParseException {
        JSONObject json = (JSONObject) parser.parse(inputString);
        return PictureImageParser.fromJson(json);
    }

    public static PictureImage fromJson(JSONObject json) {
        String fileName = (String)json.get(PictureImage.FILE_KEY);
        return new PictureImage(fileName);
    }
}
