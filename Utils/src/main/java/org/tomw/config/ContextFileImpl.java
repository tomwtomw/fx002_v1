package org.tomw.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

import static org.tomw.utils.TomwStringUtils.EOL;

/**
 * Store context information. By default in file contextPropertiesFileName
 */
public class ContextFileImpl implements ApplicationContext{
    private final static Logger LOGGER = Logger.getLogger(ContextFileImpl.class.getName());

    private String contextPropertiesFileName = "context.properties";
    private final Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public ContextFileImpl(){
        //Defult constructor. Should not be used
        //TODO define generic default constructor
    }

    /**
     * Constructor which defines which file is to be used for context
     * @param contextFileName
     */
    public ContextFileImpl(String contextFileName){
        this.contextPropertiesFileName=contextFileName;
        load();
    }


    @Override
    public void load() {

        InputStream input = null;

        File contextFile = new File(contextPropertiesFileName);
        if (contextFile.exists()) {

            try {
                LOGGER.info("Load context from "+contextPropertiesFileName);
                input = new FileInputStream(contextPropertiesFileName);
                properties.load(input);
                LOGGER.info("Context loaded");
            } catch (IOException ex) {
                LOGGER.error("Could not read file  " + contextPropertiesFileName, ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        LOGGER.error("Could not close file " + contextPropertiesFileName, ex);
                    }
                }
            }
        } else {
            LOGGER.warn("Missing context file " + contextPropertiesFileName);
        }

    }

    @Override
    public void persist() {
        OutputStream output = null;

        try {
            LOGGER.info("Save context to "+contextPropertiesFileName);
            // does the output directory exist?
            File contextFileDirectory =(new File((new File(contextPropertiesFileName)).getAbsolutePath())).getParentFile();
            LOGGER.info("Base directory "+contextFileDirectory+" exists: "+contextFileDirectory.exists());
            if(!contextFileDirectory.exists()){
                LOGGER.info("Create context directory "+contextFileDirectory);
                FileUtils.forceMkdir(contextFileDirectory);
            }
            output = new FileOutputStream(contextPropertiesFileName);
            properties.store(output, null);
            LOGGER.info("Context saved");
        } catch (IOException io) {
            LOGGER.error("Could not write to file " + contextPropertiesFileName, io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOGGER.error("Could not close file " + contextPropertiesFileName, e);
                }
            }
        }
    }

    @Override
    public void setContext(String key, String value) {
        properties.setProperty(key,value);
    }

    @Override
    public String getContext(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String toString(){
        String result="Context file="+contextPropertiesFileName+EOL;
        for(String key : properties.stringPropertyNames()) {
            result = result + key+" : "+properties.get(key)+EOL;
        }
        return result;
    }

}
