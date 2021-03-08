/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.timeutils;

/**
 * 
 * @author tomw
 */
public class TimeSleeper {
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TimeSleeper.class.getName());       
    
    public void sleep(long miliseconds){
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException ex) {
            LOGGER.error("Interruption occured");
        }
    }
}
