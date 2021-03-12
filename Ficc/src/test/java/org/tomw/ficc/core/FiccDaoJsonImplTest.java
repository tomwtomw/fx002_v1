package org.tomw.ficc.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tomw on 7/22/2017.
 */
public class FiccDaoJsonImplTest {

    public static final String TEST_DIRECTORY = "test";
    public static final String AAA = "aaa";
    public static final String BBB = "bbb";
    public static final String CCC = "ccc";
    public static final String DDD = "ddd";
    public static final String EEE = "eee";

    public File testDirectory = new File(TEST_DIRECTORY);

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.mkdirs(testDirectory);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    private JSONObject createJson1() {
        JSONObject json = new JSONObject();
        json.put(AAA, "aaa");
        json.put(BBB, 23.56);
        json.put(CCC, null);
        json.put(DDD, false);
        json.put(EEE, new JSONArray());
        return json;
    }

    private JSONArray createJsonArray1() {
        JSONArray json = new JSONArray();
        json.add("aaa");
        json.add(23.56);
        json.add(null);
        json.add(false);
        json.add(createJson1());
        return json;
    }

    @Test
    public void commit() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);
        dao1.add(TestEntityFactory.getTransaction1());
        dao1.add(TestEntityFactory.getTransaction2());
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();
        System.out.println("dao1="+dao1.getAllFiccTransactions());

        FiccDao dao2 = new FiccDaoJsonImpl(daoFile);


        System.out.println("dao2="+dao1.getAllFiccTransactions());

        for (FiccTransaction t : dao1.getAllFiccTransactions()) {
            System.out.println(" "+t);
            System.out.println(dao2.getAllFiccTransactions());
            assertTrue(dao2.getAllFiccTransactions().contains(t));
        }
        for (FiccCounterParty c : dao1.getAllFiccCounterParties()) {
            assertTrue(dao2.getAllFiccCounterParties().contains(c));
        }

        for (FiccTransaction t : dao2.getAllFiccTransactions()) {
            assertTrue(dao1.getAllFiccTransactions().contains(t));
        }
        for (FiccCounterParty c : dao2.getAllFiccCounterParties()) {
            assertTrue(dao1.getAllFiccCounterParties().contains(c));
        }
    }

    @Test
    public void getAllFiccTransactions() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();

        List<FiccTransaction> list = dao1.getAllFiccTransactions();
        assertFalse(list.contains(t1));
        assertFalse(list.contains(t2));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        list = dao1.getAllFiccTransactions();
        assertTrue(list.contains(t1));
        assertTrue(list.contains(t2));

    }

    @Test
    public void getAllFiccCounterParties() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c1 = t1.getCounterparty();
        FiccCounterParty c2 = t2.getCounterparty();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        List<FiccCounterParty> list = dao1.getAllFiccCounterParties();
        assertFalse(list.contains(c1));
        assertFalse(list.contains(c2));
        assertFalse(list.contains(c3));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        list = dao1.getAllFiccCounterParties();
        assertTrue(list.contains(c1));
        assertTrue(list.contains(c2));
        assertTrue(list.contains(c3));
    }

    @Test
    public void getFiccTransaction() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();

        assertEquals(null,dao1.getFiccTransaction(t1.getId()));
        assertEquals(null,dao1.getFiccTransaction(t2.getId()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        assertEquals(t1,dao1.getFiccTransaction(t1.getId()));
        assertEquals(t2,dao1.getFiccTransaction(t2.getId()));
    }

    @Test
    public void deleteFiccTransaction() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        assertEquals(t1,dao1.getFiccTransaction(t1.getId()));
        assertEquals(t2,dao1.getFiccTransaction(t2.getId()));

        dao1.deleteFiccTransaction(t1);

        assertEquals(null,dao1.getFiccTransaction(t1.getId()));
        assertEquals(t2,dao1.getFiccTransaction(t2.getId()));

        dao1.deleteFiccTransaction(t2);

        assertEquals(null,dao1.getFiccTransaction(t1.getId()));
        assertEquals(null,dao1.getFiccTransaction(t2.getId()));
    }



    @Test
    public void addFiccTransactions() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        assertEquals(t1,dao1.getFiccTransaction(t1.getId()));
        assertEquals(t2,dao1.getFiccTransaction(t2.getId()));
        assertEquals(t1.getCounterparty(),dao1.getFiccCounterParty(t1.getCounterparty().getId()));
        assertEquals(t2.getCounterparty(),dao1.getFiccCounterParty(t2.getCounterparty().getId()));
    }

    @Test
    public void containsFiccTransaction() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();

        assertFalse(dao1.containsFiccTransaction(t1));
        assertFalse(dao1.containsFiccTransaction(t1.getId()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        assertTrue(dao1.containsFiccTransaction(t1));
        assertTrue(dao1.containsFiccTransaction(t1.getId()));
    }



    @Test
    public void containsFiccTransactionDoNotCompareById() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertFalse(dao1.containsFiccTransactionDoNotCompareById(t1));
        assertFalse(dao1.containsFiccTransactionDoNotCompareById(t2));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        assertTrue(dao1.containsFiccTransactionDoNotCompareById(t1));
        assertTrue(dao1.containsFiccTransactionDoNotCompareById(t2));
    }

    @Test
    public void containsFiccCounterParty() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertFalse(dao1.containsFiccCounterParty(t1.getCounterparty()));
        assertFalse(dao1.containsFiccCounterParty(t1.getCounterparty().getId()));
        assertFalse(dao1.containsFiccCounterParty(t2.getCounterparty()));
        assertFalse(dao1.containsFiccCounterParty(t2.getCounterparty().getId()));
        assertFalse(dao1.containsFiccCounterParty(c3));
        assertFalse(dao1.containsFiccCounterParty(c3.getId()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(TestEntityFactory.getCounterParty3());
        dao1.commit();

        assertTrue(dao1.containsFiccCounterParty(t1.getCounterparty()));
        assertTrue(dao1.containsFiccCounterParty(t1.getCounterparty().getId()));
        assertTrue(dao1.containsFiccCounterParty(t2.getCounterparty()));
        assertTrue(dao1.containsFiccCounterParty(t2.getCounterparty().getId()));
        assertTrue(dao1.containsFiccCounterParty(c3));
        assertTrue(dao1.containsFiccCounterParty(c3.getId()));
    }

    @Test
    public void containsFiccCounterPartyDoNotCompareById() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(t1.getCounterparty()));
        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(t2.getCounterparty()));
        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(c3));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(t1.getCounterparty()));
        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(t2.getCounterparty()));
        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(c3));
    }

    @Test
    public void getFiccCounterParty() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertEquals(null,dao1.getFiccCounterParty(t1.getCounterparty().getId()));
        assertEquals(null,dao1.getFiccCounterParty(t2.getCounterparty().getId()));
        assertEquals(null,dao1.getFiccCounterParty(c3.getId()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        assertEquals(t1.getCounterparty(),dao1.getFiccCounterParty(t1.getCounterparty().getId()));
        assertEquals(t2.getCounterparty(),dao1.getFiccCounterParty(t2.getCounterparty().getId()));
        assertEquals(c3,dao1.getFiccCounterParty(c3.getId()));
    }

    @Test
    public void getFiccCounterPartyByName() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertEquals(null,dao1.getFiccCounterPartyByName(t1.getCounterparty().getName()));
        assertEquals(null,dao1.getFiccCounterPartyByName(t2.getCounterparty().getName()));
        assertEquals(null,dao1.getFiccCounterPartyByName(c3.getName()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        assertEquals(t1.getCounterparty(),dao1.getFiccCounterPartyByName(t1.getCounterparty().getName()));
        assertEquals(t2.getCounterparty(),dao1.getFiccCounterPartyByName(t2.getCounterparty().getName()));
        assertEquals(c3,dao1.getFiccCounterPartyByName(c3.getName()));
    }

    @Test
    public void deleteFiccCounterParty() throws IOException, FiccException, ParseException {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        assertEquals(null,dao1.getFiccCounterPartyByName(t1.getCounterparty().getName()));
        assertEquals(null,dao1.getFiccCounterPartyByName(t2.getCounterparty().getName()));
        assertEquals(null,dao1.getFiccCounterPartyByName(c3.getName()));

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);
        dao1.commit();

        assertEquals(t1.getCounterparty(),dao1.getFiccCounterPartyByName(t1.getCounterparty().getName()));
        assertEquals(t2.getCounterparty(),dao1.getFiccCounterPartyByName(t2.getCounterparty().getName()));
        assertEquals(c3,dao1.getFiccCounterPartyByName(c3.getName()));

        assertTrue(dao1.containsFiccCounterParty(c3));
        dao1.deleteFiccCounterParty(c3);
        assertFalse(dao1.containsFiccCounterParty(c3));

        dao1.add(c3);
        assertTrue(dao1.containsFiccCounterParty(c3.getId()));
        dao1.deleteFiccCounterParty(c3);
        assertFalse(dao1.containsFiccCounterParty(c3.getId()));

        boolean exceptionThrown = false;
        try {
            dao1.deleteFiccCounterParty(t1.getCounterparty());
        } catch (FiccException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            dao1.deleteFiccTransaction(t1);
            dao1.deleteFiccCounterParty(t1.getCounterparty());
        } catch (FiccException e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertFalse(dao1.containsFiccCounterParty(t1.getCounterparty()));

    }

    @Test
    public void containsTransactionForCounterParty() throws Exception {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);

        assertTrue(dao1.containsTransactionForCounterParty(t1.getCounterparty()));
        assertTrue(dao1.containsTransactionForCounterParty(t2.getCounterparty().getId()));
        assertFalse(dao1.containsTransactionForCounterParty(c3));
        assertFalse(dao1.containsTransactionForCounterParty(c3.getId()));
    }



    @Test
    public void addFiccCounterParties() throws Exception {
        List<FiccCounterParty> list = new ArrayList<>();
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();

        list.add(t1.getCounterparty());
        list.add(t2.getCounterparty());
        list.add(c3);

        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(t1.getCounterparty()));
        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(t2.getCounterparty()));
        assertFalse(dao1.containsFiccCounterPartyDoNotCompareById(c3));

        dao1.addFiccCounterParties(list);

        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(t1.getCounterparty()));
        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(t2.getCounterparty()));
        assertTrue(dao1.containsFiccCounterPartyDoNotCompareById(c3));
    }

    @Test
    public void getTransactions() throws Exception {
        List<FiccTransaction> list;
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao1.add(t1);
        dao1.add(t2);
        dao1.add(c3);

        list = dao1.getTransactions(t1.getCounterparty());
        assertEquals(1,list.size());
        assertEquals(t1,list.get(0));

        list = dao1.getTransactions(t2.getCounterparty());
        assertEquals(1,list.size());
        assertEquals(t2,list.get(0));

        list = dao1.getTransactions(c3);
        assertEquals(0,list.size());
    }

    @Test
    public void getTransactionsBefore() throws Exception {
        List<FiccTransaction> list;
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao1.add(t1);
        dao1.add(t2);

        assertEquals(0,dao1.getTransactionsBefore(LocalDate.of(2016,5,1)).size());
        assertEquals(t2,dao1.getTransactionsBefore(LocalDate.of(2016,7,1)).get(0));
        assertEquals(2,dao1.getTransactionsBefore(LocalDate.of(2018,7,1)).size());
    }

    @Test
    public void getTransactionsAfter() throws Exception {
        List<FiccTransaction> list;
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao1.add(t1);
        dao1.add(t2);

        assertEquals(0,dao1.getTransactionsAfter(LocalDate.of(2018,5,1)).size());
        assertEquals(t1,dao1.getTransactionsAfter(LocalDate.of(2016,7,1)).get(0));
        assertEquals(2,dao1.getTransactionsAfter(LocalDate.of(2015,7,1)).size());
    }

    @Test
    public void getTransactionsBetween() throws Exception {
        List<FiccTransaction> list;
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao1 = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao1.add(t1);
        dao1.add(t2);

        assertEquals(0,dao1.getTransactionsBetween(
                LocalDate.of(2016,7,1),
                LocalDate.of(2017,3,1)).size());

        assertEquals(t1,dao1.getTransactionsBetween(
                LocalDate.of(2017,7,1),
                LocalDate.of(2017,7,31)).get(0));

        assertEquals(2,dao1.getTransactionsBetween(
                LocalDate.of(2015,7,1),
                LocalDate.of(2018,7,1)
        ).size());
    }

    @Test
    public void test_getFiccTransactionWithMemo() throws IOException, ParseException {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao.add(t1);
        dao.add(t2);

        assertEquals(t1,dao.getFiccTransactionWithMemo(t1.getMemo()));
        assertEquals(t2,dao.getFiccTransactionWithMemo(t2.getMemo()));
        assertEquals(null,dao.getFiccTransactionWithMemo("DUMMY"));
    }

    @Test
    public void test_containsFiccTransactionWithMemo() throws IOException, ParseException {
        File daoFile = new File(testDirectory, "test.json");

        FiccDao dao = new FiccDaoJsonImpl(daoFile);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccCounterParty c3 = TestEntityFactory.getCounterParty3();
        dao.add(t1);
        dao.add(t2);

        assertTrue(dao.containsFiccTransactionWithMemo(t1.getMemo()));
        assertTrue(dao.containsFiccTransactionWithMemo(t2.getMemo()));
        assertFalse(dao.containsFiccTransactionWithMemo("DUMMY"));
    }

}