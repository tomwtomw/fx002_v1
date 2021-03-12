/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author tomw
 */
public class CzekiContextFileImpl implements CzekiContext {

    private final static Logger LOGGER = Logger.getLogger(CzekiContextFileImpl.class.getName());

    private final String contextPropertiesFileName;
    private final Properties properties = new Properties();

    private final String CONTEXT_PROPERTIES = "context.properties";
    
    private final static String ACCOUNT_ID="Account_ID";
    
    private Account mostRecentAccount;
    
    private final String MOST_RECENT_IMAGE_UPLOAD_DIRECTORY="MostRecentImageUploadDirectory";
    public File mostRecentImageUploadDirectory;
    
    public CzekiContextFileImpl() {
        contextPropertiesFileName = getContextLocation();
    }

    @Override
    public void load() {

        InputStream input = null;

        File contextFile = new File(contextPropertiesFileName);
        if (contextFile.exists()) {

            try {
                input = new FileInputStream(contextPropertiesFileName);
                properties.load(input);
                
                String accountId = (String)properties.get(ACCOUNT_ID);
                if(accountId==null){
                    for(String id: CzekiRegistry.accountsDao.getAllAccounts().keySet()){
                        accountId=id;
                    }
                }
                mostRecentAccount = CzekiRegistry.accountsDao.getAccount(accountId);
                if(mostRecentAccount==null){
                    if(CzekiRegistry.accountsDao.getAllAccounts().size()>0){
                        mostRecentAccount = CzekiRegistry.accountsDao.getAllAccounts().values().iterator().next();
                    }else {
                        mostRecentAccount = createUnnamedAccount();
                    }
                }
                CzekiRegistry.currentAccount=mostRecentAccount;
                LOGGER.info("Most recent account="+CzekiRegistry.currentAccount);
                
                String mostRecentImageUploadDirectoryString = (String)properties.get(MOST_RECENT_IMAGE_UPLOAD_DIRECTORY);
                if(mostRecentImageUploadDirectoryString==null){
                    mostRecentImageUploadDirectory = CzekiRegistry.initialDirectory;
                }else{
                    mostRecentImageUploadDirectory=new File(mostRecentImageUploadDirectoryString);
                }
                LOGGER.info("Most recent image upload directory="+mostRecentImageUploadDirectory);
                
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
            mostRecentImageUploadDirectory = CzekiRegistry.initialDirectory;
            LOGGER.info("Most recent image upload directory set to "+mostRecentImageUploadDirectory);
            LOGGER.info("Most recent account set to unnamed");
            CzekiRegistry.currentAccount=createUnnamedAccount();
        }
    }

    private Account createUnnamedAccount() {
        return new Account("unnamed");
    }

    @Override
    public void persist() {
        OutputStream output = null;

        LOGGER.info("Add current account id to properties "+ CzekiRegistry.currentAccount);
        properties.setProperty(ACCOUNT_ID, CzekiRegistry.currentAccount.getId());
        LOGGER.info("Most recent image upload directory="+mostRecentImageUploadDirectory);
        properties.setProperty(MOST_RECENT_IMAGE_UPLOAD_DIRECTORY, mostRecentImageUploadDirectory.toString());
        
        try {             
            LOGGER.info("Store context "+contextPropertiesFileName);
            output = new FileOutputStream(contextPropertiesFileName);
            properties.store(output, null);
        } catch (IOException io) {
            LOGGER.error("Could not write to file " + contextPropertiesFileName, io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOGGER.error("Could not write to file " + contextPropertiesFileName, e);
                }
            }
        }
    }

    /**
     * get file which contains location of context file
     *
     * @return
     */
    private String getContextLocation() {
        return new File(CzekiRegistry.applicationDirectory, CONTEXT_PROPERTIES).toString();
    }
    

    @Override
    public Account getMostRecentAccount() {
        return mostRecentAccount;
    }

    @Override
    public void setMostRecentAccount(Account account) {
        mostRecentAccount=account;
    }

    @Override
    public File getMostRecentImageUploadDirectory() {
        return mostRecentImageUploadDirectory;
    }

    /**
     *
     * @param f
     */
    @Override
    public void setMostRecentImageUploadDirectory(File f) {
        this.mostRecentImageUploadDirectory=f;
    }

}
