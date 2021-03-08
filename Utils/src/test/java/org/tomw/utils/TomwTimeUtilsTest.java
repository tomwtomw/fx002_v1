package org.tomw.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.tomw.timeutils.TomwTimeUtils;

import static org.junit.Assert.*;

/**
 * Created by tomw on 7/8/2017.
 */
public class TomwTimeUtilsTest {
    private final static Logger LOGGER = Logger.getLogger(TomwTimeUtilsTest.class.getName());
    @Test
    public void test_seconds2String() throws Exception {
        LOGGER.info("test_seconds2String");
        long seconds = (50*3600L+4321);
        assertEquals("2d3h12m1s", TomwTimeUtils.seconds2String(seconds));
    }

}