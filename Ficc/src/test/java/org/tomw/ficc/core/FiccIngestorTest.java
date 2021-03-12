package org.tomw.ficc.core;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests of the ingestor class
 * Created by tomw on 7/29/2017.
 */
public class FiccIngestorTest {
    public static final String TEST_DIRECTORY = "test_FiccIngestorTest";
    public File testDirectory = new File(TEST_DIRECTORY);

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.mkdirs(testDirectory);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    @Test
    public void test_ingestFromMultipleCsvFiles() throws IOException, ParseException {
        System.out.println("test_ingestFromMultipleCsvFiles");

        File daoFile = new File(testDirectory, "test.json");
        FiccDao dao = new FiccDaoJsonImpl(daoFile);

        FiccIngestor ingestor = new FiccIngestor(dao);

        List<File> listOfFiles = new ArrayList<>();
        File csvFile1 = new File("ingestor_test1.txt");
        File csvFile2 = new File("ingestor_test2.txt");
        listOfFiles.add(csvFile1);
        listOfFiles.add(csvFile2);

        assertEquals(0,dao.getAllFiccCounterParties().size());
        assertEquals(0,dao.getAllFiccTransactions().size());

        ingestor.ingestFromMultipleCsvFiles(listOfFiles);

        assertEquals(6,dao.getAllFiccCounterParties().size());
        assertEquals(9,dao.getAllFiccTransactions().size());

    }


    @Test
    public void test_IngestFromCsvFile() throws Exception {
        System.out.println("test_IngestFromCsvFile");
        File daoFile = new File(testDirectory, "test.json");
        FiccDao dao = new FiccDaoJsonImpl(daoFile);

        FiccIngestor ingestor = new FiccIngestor(dao);

        File csvFile1 = new File("ingestor_test1.txt");
        File csvFile2 = new File("ingestor_test2.txt");

        assertEquals(0,dao.getAllFiccCounterParties().size());
        assertEquals(0,dao.getAllFiccTransactions().size());

        ingestor.ingestFromCsvFile(csvFile1);
        assertEquals(4,dao.getAllFiccCounterParties().size());
        assertEquals(7,dao.getAllFiccTransactions().size());

        ingestor.ingestFromCsvFile(csvFile2);
        assertEquals(6,dao.getAllFiccCounterParties().size());
        assertEquals(9,dao.getAllFiccTransactions().size());

    }

    @Test
    public void test_IngestList() throws Exception {
        System.out.println("test_IngestFromCsvFile");

        File daoFile = new File(testDirectory, "test.json");
        FiccDao dao = new FiccDaoJsonImpl(daoFile);

        FiccIngestor ingestor = new FiccIngestor(dao);

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        FiccTransaction t3 = TestEntityFactory.getTransaction3();

        List<FiccTransaction> list1 = new ArrayList<>();
        list1.add(t1);
        list1.add(t2);

        assertFalse(dao.containsFiccTransaction(t1));
        assertFalse(dao.containsFiccTransaction(t2));
        assertFalse(dao.containsFiccCounterParty(t1.getCounterparty()));
        assertFalse(dao.containsFiccCounterParty(t2.getCounterparty()));

        ingestor.ingestList(list1);

        assertTrue(dao.containsFiccTransaction(t1));
        assertTrue(dao.containsFiccTransaction(t2));
        assertTrue(dao.containsFiccCounterParty(t1.getCounterparty()));
        assertTrue(dao.containsFiccCounterParty(t2.getCounterparty()));

        List<FiccTransaction> list2 = new ArrayList<>();
        list2.add(t2);
        list2.add(t3);

        ingestor.ingestList(list2);

        assertTrue(dao.containsFiccTransaction(t1));
        assertTrue(dao.containsFiccTransaction(t2));
        assertTrue(dao.containsFiccTransaction(t3));
        assertTrue(dao.containsFiccCounterParty(t1.getCounterparty()));
        assertTrue(dao.containsFiccCounterParty(t2.getCounterparty()));
        assertTrue(dao.containsFiccCounterParty(t3.getCounterparty()));

        ingestor.ingestList(list1);
        ingestor.ingestList(list2);

        assertEquals(3,dao.getAllFiccCounterParties().size());
        assertEquals(3,dao.getAllFiccTransactions().size());

    }

}