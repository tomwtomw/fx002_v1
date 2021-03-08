package org.tomw.envutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EnvUtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_IsDeploymentInstance() throws Exception {
        System.out.println("test_IsDeploymentInstance");

        assertFalse(TomwEnvUtils.isDeploymentInstance(new File("C:\\Users\\XXX\\IdeaProjectsData\\GasUsageAnalyzer\\data")));
        assertTrue(TomwEnvUtils.isDeploymentInstance(new File("C:\\Users\\XXX\\Documents\\pien\\benzyna olej\\GasUsageAnalyzer")));
    }

}