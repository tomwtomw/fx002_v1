/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomw
 */
public class TransactionTest {

    public TransactionTest() {
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
     * Test of getId method, of class Transaction.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction = new Transaction(expected_id, LocalDate.now(), 5.0, "1234", new CounterParty(), "memo", "comment");

        String actual_id = transaction.getId();

        assertEquals(expected_id, actual_id);

    }

    /**
     * Test of getTransactionDateProperty method, of class Transaction.
     */
    @Test
    public void testGetTransactionDateProperty() {
        System.out.println("getTransactionDateProperty");
        LocalDate expected = LocalDate.now();
        Transaction transaction = new Transaction("test_id", expected, 5.0, "1234", new CounterParty(), "memo", "comment");

        LocalDate actual = transaction.getTransactionDate();

        assertEquals(expected, actual);
    }

    /**
     * Test of getTransactionDate method, of class Transaction.
     */
    @Test
    public void testGetTransactionDate() {
        System.out.println("getTransactionDate");
        LocalDate expected = LocalDate.now();
        Transaction transaction = new Transaction("test_id", expected, 5.0, "1234", new CounterParty(), "memo", "comment");

        LocalDate actual = transaction.getTransactionDate();

        assertEquals(expected, actual);
    }

    /**
     * Test of setTransactionDateProperty method, of class Transaction.
     */
    @Test
    public void testSetTransactionDateProperty() {
        System.out.println("setTransactionDateProperty");
        LocalDate expected = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(expected);

        LocalDate actual = transaction.getTransactionDate();

        assertEquals(expected, actual);
    }

    /**
     * Test of setTransactionDate method, of class Transaction.
     */
    @Test
    public void testSetTransactionDate() {
        System.out.println("setTransactionDate");
        LocalDate expected = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(expected);

        LocalDate actual = transaction.getTransactionDate();

        assertEquals(expected, actual);
    }

    /**
     * Test of getCommentProperty method, of class Transaction.
     */
    @Test
    public void testGetCommentProperty() {
        System.out.println("getCommentProperty");

        String expected = "expected comment";
        Transaction transaction = new Transaction();
        transaction.setComment(expected);

        String actual = transaction.getComment();

        assertEquals(expected, actual);

    }

    /**
     * Test of getComment method, of class Transaction.
     */
    @Test
    public void testGetComment() {
        System.out.println("getComment");
        String expected = "expected comment";
        Transaction transaction = new Transaction();
        transaction.setComment(expected);

        String actual = transaction.getComment();

        assertEquals(expected, actual);
    }

    /**
     * Test of setCommentProperty method, of class Transaction.
     */
    @Test
    public void testSetCommentProperty() {
        System.out.println("setCommentProperty");
        String expected = "expected comment";
        Transaction transaction = new Transaction();
        transaction.setComment(expected);

        String actual = transaction.getComment();

        assertEquals(expected, actual);
    }

    /**
     * Test of setComment method, of class Transaction.
     */
    @Test
    public void testSetComment() {
        System.out.println("setComment");

        String expected = "expected comment";
        Transaction transaction = new Transaction();
        transaction.setComment(expected);

        String actual = transaction.getComment();

        assertEquals(expected, actual);
    }

//    /**
//     * Test of getTransactionAmountProperty method, of class Transaction.
//     */
//    @Test
//    public void testGetTransactionAmountProperty() {
//        System.out.println("getTransactionAmountProperty");
//        Transaction instance = new Transaction();
//        DoubleProperty expResult = null;
//        DoubleProperty result = instance.getTransactionAmountProperty();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getTransactionAmount method, of class Transaction.
//     */
//    @Test
//    public void testGetTransactionAmount() {
//        System.out.println("getTransactionAmount");
//        Transaction instance = new Transaction();
//        Double expResult = null;
//        Double result = instance.getTransactionAmount();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setTransactionAmountProperty method, of class Transaction.
//     */
//    @Test
//    public void testSetTransactionAmountProperty() {
//        System.out.println("setTransactionAmountProperty");
//        DoubleProperty transactionAmountProperty = null;
//        Transaction instance = new Transaction();
//        instance.setTransactionAmountProperty(transactionAmountProperty);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setTransactionAmount method, of class Transaction.
//     */
//    @Test
//    public void testSetTransactionAmount() {
//        System.out.println("setTransactionAmount");
//        double amount = 0.0;
//        Transaction instance = new Transaction();
//        instance.setTransactionAmount(amount);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCounterPartyProperty method, of class Transaction.
//     */
//    @Test
//    public void testGetCounterPartyProperty() {
//        System.out.println("getCounterPartyProperty");
//        Transaction instance = new Transaction();
//        ObjectProperty<CounterParty> expResult = null;
//        ObjectProperty<CounterParty> result = instance.getCounterPartyProperty();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCounterParty method, of class Transaction.
//     */
//    @Test
//    public void testGetCounterParty() {
//        System.out.println("getCounterParty");
//        Transaction instance = new Transaction();
//        CounterParty expResult = null;
//        CounterParty result = instance.getCounterParty();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCounterPartyProperty method, of class Transaction.
//     */
//    @Test
//    public void testSetCounterPartyProperty() {
//        System.out.println("setCounterPartyProperty");
//        ObjectProperty<CounterParty> counterPartyProperty = null;
//        Transaction instance = new Transaction();
//        instance.setCounterPartyProperty(counterPartyProperty);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCounterParty method, of class Transaction.
//     */
//    @Test
//    public void testSetCounterParty() {
//        System.out.println("setCounterParty");
//        CounterParty counterParty = null;
//        Transaction instance = new Transaction();
//        instance.setCounterParty(counterParty);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCheckNumberProperty method, of class Transaction.
//     */
//    @Test
//    public void testGetCheckNumberProperty() {
//        System.out.println("getCheckNumberProperty");
//        Transaction instance = new Transaction();
//        StringProperty expResult = null;
//        StringProperty result = instance.getCheckNumberProperty();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCheckNumber method, of class Transaction.
//     */
//    @Test
//    public void testGetCheckNumber() {
//        System.out.println("getCheckNumber");
//        Transaction instance = new Transaction();
//        String expResult = "";
//        String result = instance.getCheckNumber();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCheckNumberProperty method, of class Transaction.
//     */
//    @Test
//    public void testSetCheckNumberProperty() {
//        System.out.println("setCheckNumberProperty");
//        StringProperty checkNumberProperty = null;
//        Transaction instance = new Transaction();
//        instance.setCheckNumberProperty(checkNumberProperty);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCheckNumber method, of class Transaction.
//     */
//    @Test
//    public void testSetCheckNumber() {
//        System.out.println("setCheckNumber");
//        String checkNumber = "";
//        Transaction instance = new Transaction();
//        instance.setCheckNumber(checkNumber);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMemoProperty method, of class Transaction.
//     */
//    @Test
//    public void testGetMemoProperty() {
//        System.out.println("getMemoProperty");
//        Transaction instance = new Transaction();
//        StringProperty expResult = null;
//        StringProperty result = instance.getMemoProperty();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMemo method, of class Transaction.
//     */
//    @Test
//    public void testGetMemo() {
//        System.out.println("getMemo");
//        Transaction instance = new Transaction();
//        String expResult = "";
//        String result = instance.getMemo();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setMemoProperty method, of class Transaction.
//     */
//    @Test
//    public void testSetMemoProperty() {
//        System.out.println("setMemoProperty");
//        StringProperty memoProperty = null;
//        Transaction instance = new Transaction();
//        instance.setMemoProperty(memoProperty);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setMemo method, of class Transaction.
//     */
//    @Test
//    public void testSetMemo() {
//        System.out.println("setMemo");
//        String memo = "";
//        Transaction instance = new Transaction();
//        instance.setMemo(memo);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of toCsv method, of class Transaction.
     */
    @Test
    public void testToCsv() {
        System.out.println("toCsv");

        String id = "64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        LocalDate date = LocalDate.parse("2015-09-06");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction = new Transaction(expected_id, date, 5.0, "1234", cp, "memo", "comment");

        String actual = transaction.toCsv();
        //String expected="2015-09-06	5.0	1234	short name	comment	memo";
        String expected = "2015-09-06	5.0	1234		short name	comment	memo";

        assertEquals(expected, actual);
    }

//    /**
//     * Test of toString method, of class Transaction.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        Transaction instance = new Transaction();
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of toJson method, of class Transaction.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");

        String id = "64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        LocalDate date = LocalDate.parse("2015-09-06");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction = new Transaction(expected_id, date, 5.0, "1234", cp, "memo", "comment");

        //String expected = "{\"Comment\":\"comment\",\"Transaction amount\":5.0,\"Counterparty id\":\"64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\",\"Check Number\":\"1234\",\"Date\":\"2015-09-06\",\"Memo\":\"memo\"}";
        String expected="{\"Comment\":\"comment\",\"Transaction amount\":5.0,\"Counterparty id\":\"64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0\",\"Cleared\":\"\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\",\"Check Number\":\"1234\",\"Date\":\"2015-09-06\",\"Memo\":\"memo\"}";
        String actual = transaction.toJsonString();

        

        assertEquals(expected, actual);
        
        transaction.setCleared("t");
        
        actual = transaction.toJsonString();
        expected="{\"Comment\":\"comment\",\"Transaction amount\":5.0,\"Counterparty id\":\"64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0\",\"Cleared\":\"t\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\",\"Check Number\":\"1234\",\"Date\":\"2015-09-06\",\"Memo\":\"memo\"}" ;
                
        assertEquals(expected, actual);
    }

    /**
     * Test of toJsonString method, of class Transaction.
     */
    @Test
    public void testToJsonString() {
        System.out.println("toJsonString");

        String id = "64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        LocalDate date = LocalDate.parse("2015-09-06");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction = new Transaction(expected_id, date, 5.0, "1234", cp, "memo", "comment");

        String expected = "{\"Comment\":\"comment\",\"Transaction amount\":5.0,\"Counterparty id\":\"64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0\",\"Cleared\":\"\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\",\"Check Number\":\"1234\",\"Date\":\"2015-09-06\",\"Memo\":\"memo\"}";
        String actual = transaction.toJsonString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        String id = "64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        LocalDate date = LocalDate.parse("2015-09-06");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction1 = new Transaction(expected_id, date, 5.0, "1234", cp, "memo", "comment");
        Transaction transaction2 = new Transaction(expected_id, date, 5.0, "1234", cp, "memo", "comment");

        boolean areEqual = transaction1.equals(transaction2);
        assertTrue(areEqual);
    }
    
    @Test
    public void test_dateComparator() {
        String id = "64f3f0a9-565b-43f4-a2b5-64ea93f5b8a0";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        LocalDate date1 = LocalDate.parse("2015-09-06");
        LocalDate date2 = LocalDate.parse("2016-10-21");

        String expected_id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        Transaction transaction1 = new Transaction(expected_id, date2, 5.0, "1234", cp, "memo", "comment");
        Transaction transaction2 = new Transaction(expected_id, date1, 5.0, "1234", cp, "memo", "comment");
        
        List<Transaction> list=new ArrayList<>();
        list.add(transaction1);
        list.add(transaction2);
        
        Collections.sort(list, Transaction.TransactionByDateComparator);
        
        assertEquals(transaction1,list.get(1));
        assertEquals(transaction2,list.get(0));
    }
}
