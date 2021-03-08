package org.tomw.utils;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigUtilsTest {
    private File config1 = new File("/test.json");
    private File config2 = new File("/test2.json");

    private File testDir = new File("test_dir");

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(testDir);
        TomwFileUtils.mkdirs(testDir);
        TomwFileUtils.copyFileFromResources(config2,new File(testDir,config2.getName()));
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(testDir);
    }

    @Test
    public void test_readConfigFiles() throws Exception {
        System.out.println("test_readConfigFiles");

        String[] args = new String[1];
        args[0]=(new File(testDir,config2.getName())).getAbsolutePath();
        JSONObject json = ConfigUtils.readConfigFiles("/"+config1.getName(), args);

        assertTrue(json.containsKey("Transactions"));
        assertTrue(json.containsKey("Property"));
    }

    @Test
    public void test_readConfigFiles_File_File() throws Exception {
        System.out.println("test_readConfigFiles_File_File");

        String customConfigFileName=(new File(testDir,config2.getName())).getAbsolutePath();
        JSONObject json = ConfigUtils.readConfigFiles("/"+config1.getName(), customConfigFileName);

        assertTrue(json.containsKey("Transactions"));
        assertTrue(json.containsKey("Property"));
    }

}