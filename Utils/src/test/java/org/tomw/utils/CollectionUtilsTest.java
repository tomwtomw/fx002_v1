package org.tomw.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionUtilsTest {
    @Test
    public void test_Size2String() throws Exception {
        System.out.println("test_Size2String");

        assertEquals("0",CollectionUtils.size2String(null));
        assertEquals("0",CollectionUtils.size2String(new ArrayList<String>()));
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        assertEquals("2",CollectionUtils.size2String(list));
    }

}