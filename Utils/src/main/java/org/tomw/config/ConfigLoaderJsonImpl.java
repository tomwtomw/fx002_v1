package org.tomw.config;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.tomw.utils.ConfigUtils;
import org.tomw.utils.Json2String;

import java.io.File;
import java.io.IOException;

import static org.tomw.utils.TomwStringUtils.EOL;

/**
 * manages finding and loading the configuration file
 * once found it loads its content and stores it internally in json object
 */
public class ConfigLoaderJsonImpl implements ApplicationConfigLoader {
    private final static Logger LOGGER = Logger.getLogger(ConfigLoaderJsonImpl.class.getName());

    public static final String DOCUMENT_FILE_DIRECTORY_KEY = "documentFileDirectory";
    public static final String APPLICATION_DIRECTORY_KEY = "applicationDirectory";

    public static final String CONTEXT_FILE_KEY = "contextFile";

    public static final String CONFIG_FILE_VM_PARAMETER="configFile";

    private SelfIdentificationService selfIdentificationService;

    // application name
    private String applicationName;
    // instance name
    private String instanceName;

    // default configuration file name
    private String configurationFileName = "config.json";
    private JSONObject configuration = new JSONObject();

    /**
     * Default constructor. Normally should never be used
     */
    public ConfigLoaderJsonImpl(){
        this.applicationName="default";
    }

    /**
     * Load the self identification service object
     * @param selfIdentificationService
     */
    public ConfigLoaderJsonImpl(SelfIdentificationService selfIdentificationService){
        this.selfIdentificationService = selfIdentificationService;
        this.applicationName = this.selfIdentificationService.getApplicationName();
        LOGGER.info("Application name="+this.applicationName);
        this.instanceName = this.selfIdentificationService.instanceName();
        LOGGER.info("Instance name="+this.instanceName);
    }

    @Override
    public void load() {
        configurationFileName = findConfigFile();
        LOGGER.info("Load config from file "+configurationFileName);
        try {
            JSONObject allConfiguration = ConfigUtils.readConfigFiles(configurationFileName);
            // we are interested only in configuration for this particular instance
            configuration = (JSONObject)allConfiguration.get(instanceName);
        } catch (IOException e) {
            LOGGER.error("Failed to read content of file "+configurationFileName,e);
        } catch (ParseException e) {
            LOGGER.error("Failed to parse content of file "+configurationFileName,e);
        }
    }

    @Override
    public String getValue(String key) {
        return (String)configuration.get(key);
    }

    @Override
    public SelfIdentificationService getSelfIdentificationService() {
        return selfIdentificationService;
    }

    /**
     * Find location of config file
     * First look in the VM argument, if undefined then look for
     * file [application name].json in resources directory
     * @return configuration file object
     */
    public String findConfigFile(){
        // first, try to get the config file from VM arguments
        String configFileName = getConfigFromVmArguments();

        if(configFileName!=null && (new File(configFileName)).exists()){
            return configFileName;
        }else{
            configFileName =  defaultConfigFile();
            return configFileName;
        }
    }

    /**
     * get location of config file as defined by VM arguments
     * @return config file defined in vm arguments or null if undefined
     */
    public String getConfigFromVmArguments(){
        return System.getProperty(CONFIG_FILE_VM_PARAMETER);
    }

    /**
     * get default configuration file, to be found in resources directory
     * @return default config, normally in resources
     */
    private String defaultConfigFile() {
        return "/"+this.applicationName+".json";
    }

    /**
     * return pretty format of json configuration
     */
    public String toString(){
        String result = "";
        result=result+"Instance: "+selfIdentificationService.toString()+EOL;
        String configFileNameString = configurationFileName.toString();
        result=result+"Config file name: "+configFileNameString+EOL;
        Json2String converter = new Json2String();
        result = result+converter.toPrettyString(configuration);
        return result;
    }
}
