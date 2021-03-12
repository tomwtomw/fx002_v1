/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import org.tomw.czeki.transactionoverview.TransactionUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
public class TransactionUtilsTest {
    
    Transaction t1;
    Transaction t2;
    Transaction t3;
    Transaction t4;
    Transaction t5;
    
    public TransactionUtilsTest() {
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
     * Test of calculateSum method, of class TransactionUtils.
     */
    @Test
    public void testCalculateSum() {
        System.out.println("calculateSum");
        
        this.t1 = new Transaction("test_id_1", LocalDate.now(), 1.0, "1111", new CounterParty(), "memo1", "comment1");
        this.t2 = new Transaction("test_id_2", LocalDate.now(), 2.0, "2222", new CounterParty(), "memo2", "comment2");
        this.t3 = new Transaction("test_id_3", LocalDate.now(), 3.0, "3333", new CounterParty(), "memo3", "comment3");
        this.t4 = new Transaction("test_id_4", LocalDate.now(), 4.0, "4444", new CounterParty(), "memo4", "comment4");
        this.t5 = new Transaction("test_id_5", LocalDate.now(), 5.0, "5555", new CounterParty(), "memo5", "comment5");
        
        Collection<Transaction> listOfTransactions = new ArrayList<>();
        listOfTransactions.add(this.t1);
        listOfTransactions.add(this.t2);
        listOfTransactions.add(this.t3);
        listOfTransactions.add(this.t4);
        listOfTransactions.add(this.t5);
                             
        double expResult = 1.+2.+3.+4.+5;
        double result = TransactionUtils.calculateSum(listOfTransactions);
        assertEquals(expResult, result, 0.0);
    }
    
}
