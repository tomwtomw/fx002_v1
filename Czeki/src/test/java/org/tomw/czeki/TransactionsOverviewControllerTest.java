/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import org.tomw.czeki.transactionoverview.TransactionsOverviewController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;

/**
 *
 * @author tomw
 */
public class TransactionsOverviewControllerTest {

    public TransactionsOverviewControllerTest() {
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
     * Test of calculateSums method, of class TransactionsOverviewController.
     */
    @Test
    public void testCalculateSums_3args() {
        System.out.println("calculateSums");
        List<Transaction> listOfTransactions = new ArrayList<>();

        Transaction transaction1 = new Transaction("id1", LocalDate.parse("2015-09-06"), 1.0, "1234", new CounterParty(), "memo", "comment");
        Transaction transaction2 = new Transaction("id2", LocalDate.parse("2015-09-07"), 2.0, "1234", new CounterParty(), "memo", "comment");
        Transaction transaction3 = new Transaction("id3", LocalDate.parse("2015-09-08"), 3.0, "1234", new CounterParty(), "memo", "comment");
        Transaction transaction4 = new Transaction("id4", LocalDate.parse("2015-09-09"), 4.0, "1234", new CounterParty(), "memo", "comment");

        transaction1.setCleared(true);
        transaction2.setCleared(false);
        transaction3.setCleared(true);
        transaction4.setCleared(false);

        // fill the list of transactions in random order
        listOfTransactions.add(transaction2);
        listOfTransactions.add(transaction4);
        listOfTransactions.add(transaction3);
        listOfTransactions.add(transaction1);

        List<Double> sums = new ArrayList<>();
        List<Integer> nTransactions = new ArrayList<>();

        TransactionsOverviewController.calculateSums(listOfTransactions, sums, nTransactions);

        assertEquals(1., transaction1.getSumAllTransactions(), 0.001);
        assertEquals(3., transaction2.getSumAllTransactions(), 0.001);
        assertEquals(6., transaction3.getSumAllTransactions(), 0.001);
        assertEquals(10., transaction4.getSumAllTransactions(), 0.001);

        assertEquals(1., transaction1.getSumClearedTransactions(), 0.001);
        assertEquals(1., transaction2.getSumClearedTransactions(), 0.001);
        assertEquals(4., transaction3.getSumClearedTransactions(), 0.001);
        assertEquals(4., transaction4.getSumClearedTransactions(), 0.001);

        assertEquals(3, sums.size());
        assertEquals(4., sums.get(0), 0.0001);
        assertEquals(6., sums.get(1), 0.0001);
        assertEquals(10., sums.get(2), 0.0001);

        assertEquals(3, nTransactions.size());

        int numberOfClearedTransactions = nTransactions.get(0);
        int numberOfNonClearedTransactions = nTransactions.get(1);
        int numberOfAllTransactions = nTransactions.get(2);
        assertEquals(2, numberOfClearedTransactions);
        assertEquals(2, numberOfNonClearedTransactions);
        assertEquals(4, numberOfAllTransactions);
    }

}
