/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;

/**
 *
 * @author tomw
 */
public interface CzekiContext {
    
    /**
     * Load context info
     */
    public  void load();
    
    /**
     * persist context info
     */
    public void persist();
    
    /**
     * get the most recent Account
     * @return 
     */
    public Account getMostRecentAccount();
    
    public void setMostRecentAccount(Account account);
    
    public File getMostRecentImageUploadDirectory();
    
    public void setMostRecentImageUploadDirectory(File f);
    
}
