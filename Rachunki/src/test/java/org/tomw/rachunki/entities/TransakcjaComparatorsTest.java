package org.tomw.rachunki.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TransakcjaComparatorsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTransakcjaComparatorByDate(){
        Transakcja t1 = new Transakcja(LocalDate.of(2018,4,1));
        Transakcja t2 = null;
        Transakcja t3 = new Transakcja(LocalDate.of(2019,5,15));
        Transakcja t4 = null;
        Transakcja t5 = new Transakcja(LocalDate.of(2017,2,3));
        Transakcja t6 = new Transakcja(null);

        List<Transakcja> listOfTransactions = new ArrayList<>();
        listOfTransactions.add(t1);
        listOfTransactions.add(t2);
        listOfTransactions.add(t3);
        listOfTransactions.add(t4);
        listOfTransactions.add(t5);
        listOfTransactions.add(t6);

        Collections.sort(listOfTransactions,TransakcjaComparators.dateComparator);

        assertEquals(t5,listOfTransactions.get(0));
        assertEquals(t1,listOfTransactions.get(1));
        assertEquals(t3,listOfTransactions.get(2));
        assertEquals(t6,listOfTransactions.get(3));
        assertNull(listOfTransactions.get(4));
        assertNull(listOfTransactions.get(5));
    }

}