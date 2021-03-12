package org.tomw.rachunki.config;

import org.apache.log4j.Logger;
import org.tomw.rachunki.core.RachunkiContext;
import org.tomw.config.ConfigLoaderJsonImpl;
import org.tomw.config.SelfIdentificationService;

import java.io.File;

public class RachunkiConfigurationImpl extends ConfigLoaderJsonImpl implements RachunkiConfiguration{

    private final static Logger LOGGER = Logger.getLogger(RachunkiConfigurationImpl.class.getName());

    private RachunkiContext context=null;

    /**
     * Default constructor. Normally should not be used
     */
    public RachunkiConfigurationImpl() {
    }

    /**
     * Normal constructor. Configuration is created by passing self identification service object
     * @param selfIdentificationService self identification service
     */
    public RachunkiConfigurationImpl(SelfIdentificationService selfIdentificationService) {
        super(selfIdentificationService);

        // load configuration
        this.load();

        //load context
        String contextFileName = this.getValue(CONTEXT_FILE_KEY);
        LOGGER.info("Load context from file: "+contextFileName);
        context = new RachunkiContext(contextFileName);
        LOGGER.info("Context="+context.toString());
    }

    @Override
    public File getDocumentFileDirectory() {
        String documentFileDirectoryName = this.getValue(DOCUMENT_FILE_DIRECTORY_KEY);
        if(documentFileDirectoryName==null){
            return null;
        }else{
            return new File(documentFileDirectoryName);
        }
    }

    @Override
    public void setDocumentFileDirectory(File dir) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public File getApplicationDirectory() {
        String applicationDirectoryName = this.getValue(APPLICATION_DIRECTORY_KEY);
        if(applicationDirectoryName==null){
            return null; //TODO maybe get it from system properties?
        }else{
            return new File(applicationDirectoryName);
        }
    }

    @Override
    public void setApplicationDirectory(File dir) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public RachunkiContext getContext() {
        return context;
    }

    @Override
    public File getFileStoreDirectory() {
        return getDocumentFileDirectory();
    }

    @Override
    public void setFileStoreDirectory(File fileStoreDirectory) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public String toString(){
        String result="";
        result=result+super.toString();
        return result;
    }
}
