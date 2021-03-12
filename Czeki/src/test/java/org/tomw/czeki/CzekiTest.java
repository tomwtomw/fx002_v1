/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tomw.envutils.TomwEnvUtils;

import static org.junit.Assert.*;

/**
 * Testing the Czeki entity class
 * @author tomw
 */
public class CzekiTest {
    
    public CzekiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of selectDataDirectory method, of class Czeki.
     */
    @Test
    public void test_selectDataDirectory(){
        //TODO fix this test on laptop
        if(TomwEnvUtils.isLaptop()){
            return;
        }
        //"C:\\Users\\tomw2\\Documents\\IdeaProjectsData\\Czeki\\data";
        CzekiRegistry.applicationDirectory=new File("C:\\Users\\xxx\\Documents\\NetBeansProjectsDirectory\\dummy");
        File expected1 = new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data");
        assertEquals(expected1, Czeki.selectDataDirectory());
        
        CzekiRegistry.applicationDirectory=new File("C:\\Users\\xxx\\Documents\\qwerty\\dummy");
        File expected2 = new File("C:\\Users\\xxx\\Documents\\qwerty\\dummy\\data");
        assertEquals(expected2, Czeki.selectDataDirectory());
    }    
}
