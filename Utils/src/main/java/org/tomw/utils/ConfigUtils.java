package org.tomw.utils;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

/**
 * Tools for managing config files
 */
public class ConfigUtils {
    private final static Logger LOGGER = Logger.getLogger(ConfigUtils.class.getName());
    /**
     * read configuration files. first the default ones from resources, then optionally from args
     * @param args optional, config file
     * @throws ParseException if config file cannot be parsed.
     */
    public static JSONObject readConfigFiles(String defaultConfig, String[] args) throws ParseException, IOException {
        LOGGER.info("Load default config "+defaultConfig+" from resources");
        JsonFileUtils jsonUtils = new JsonFileUtils();
        JSONObject json = jsonUtils.jsonObjectFromFileInResources(defaultConfig);

        if(args!=null && args.length>0){
            LOGGER.info("Load custom config from "+args[0]);
            try {
                JSONObject json2 = jsonUtils.jsonObjectFromFile(new File(args[0]));
                for(Object o : json2.keySet()){
                    String key = (String)o;
                    json.put(key,json2.get(key));
                }
            }catch(IOException e){
                String message = "cannot read file "+args[0];
                LOGGER.error(message,e);
                throw e;
            }
        }
        return json;
    }

    public static JSONObject readConfigFiles(String defaultConfig, String customConfig) throws ParseException, IOException {
        LOGGER.info("Load default config "+defaultConfig+" from resources");
        JsonFileUtils jsonUtils = new JsonFileUtils();
        JSONObject json = jsonUtils.jsonObjectFromFileInResources(defaultConfig);

        if(customConfig!=null){
            LOGGER.info("Load custom config from "+customConfig);
            try {
                JSONObject json2 = jsonUtils.jsonObjectFromFile(new File(customConfig));
                for(Object o : json2.keySet()){
                    String key = (String)o;
                    json.put(key,json2.get(key));
                }
            }catch(IOException e){
                String message = "Cannot read file "+customConfig;
                LOGGER.error(message,e);
                throw e;
            }
        }
        return json;
    }

    public static JSONObject readConfigFiles(String defaultConfig) throws IOException, ParseException {
        LOGGER.info("Load default config "+defaultConfig+" from resources");
        JsonFileUtils jsonUtils = new JsonFileUtils();
        JSONObject json = jsonUtils.jsonObjectFromFileInResources(defaultConfig);
        return json;
    }
}
