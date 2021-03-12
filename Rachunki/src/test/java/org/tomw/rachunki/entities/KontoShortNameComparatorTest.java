package org.tomw.rachunki.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class KontoShortNameComparatorTest {
    @Test
    public void test_Compare() throws Exception {

        KontoShortNameComparator comparator = new KontoShortNameComparator();

        Konto k1 = new Konto();
        k1.setShortName(null);
        Konto k2 = new Konto();
        k2.setShortName(null);

        assertEquals(0,comparator.compare(k1,k2));

        k1.setShortName("a");
        assertTrue(comparator.compare(k1,k2)>0);
        assertTrue(comparator.compare(k2,k1)<0);

        k2.setShortName("a");
        assertEquals(0,comparator.compare(k1,k2));


        k2.setShortName("z");
        assertTrue(comparator.compare(k1,k2)<0);
    }

}