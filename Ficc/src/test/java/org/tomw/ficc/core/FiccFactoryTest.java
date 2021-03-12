package org.tomw.ficc.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.tomw.utils.IdGenerator;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by tomw on 7/22/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FiccFactoryTest {
    private String csvString;

    @Mock
    IdGenerator idGenerator;

    @InjectMocks
    private FiccFactory factory = new FiccFactory();

    public static final String TEST_DIRECTORY = "test";
    public File testDirectory = new File(TEST_DIRECTORY);

    private FiccDao dao;

    @Before
    public void setUp() throws Exception {
        csvString = TomwFileUtils.readTextFileFromResources("ficc_test1.txt");

        TomwFileUtils.mkdirs(testDirectory);
        File daoFile = new File(testDirectory, "test.json");

        dao = new FiccDaoJsonImpl(daoFile);
        factory.setDao(dao);

    }

    @After
    public void tearDown() throws Exception {

        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    @Test
    public void fromCsvString() throws Exception {

        Mockito.when(
                idGenerator.generate()
        ).thenReturn(
                "MOCK-ID-XXX-00",
                "MOCK-ID-XXX-01",
                "MOCK-ID-XXX-02",
                "MOCK-ID-XXX-03",
                "MOCK-ID-XXX-04",
                "MOCK-ID-XXX-05",
                "MOCK-ID-XXX-06",
                "MOCK-ID-XXX-07",
                "MOCK-ID-XXX-08",
                "MOCK-ID-XXX-09",
                "MOCK-ID-XXX-10",
                "MOCK-ID-XXX-11",
                "MOCK-ID-XXX-12",
                "MOCK-ID-XXX-13",
                "MOCK-ID-XXX-14",
                "MOCK-ID-XXX-15",
                "MOCK-ID-XXX-16",
                "MOCK-ID-XXX-17",
                "MOCK-ID-XXX-18",
                "MOCK-ID-XXX-19"
                );

        List<FiccTransaction> list = factory.fromCsvString(csvString);
        for(FiccTransaction t : list){
            dao.add(t);
        }

        assertTrue(dao.containsFiccCounterParty(list.get(7).getCounterparty().getId()));

        String s7="{\"Transaction\":\"CREDIT\",\"CounterPartyId\":\"MOCK-ID-XXX-12\",\"Amount\":1050.0,\"Id\":\"MOCK-ID-XXX-11\",\"Date\":\"2017-07-03\",\"Memo\":\"STA 00600292; 00000;\"}";
        FiccTransaction t7reconstructed = factory.fromJsonString(s7);

        assertEquals(list.get(7),t7reconstructed);
    }



    @Test
    public void fromCsvLine() throws Exception {
        System.out.println("test fromCsvLine");

        Mockito.when(
                idGenerator.generate()
        ).thenReturn(
                "MOCK-ID-XXX-00",
                "MOCK-ID-XXX-01",
                "MOCK-ID-XXX-02",
                "MOCK-ID-XXX-03",
                "MOCK-ID-XXX-04",
                "MOCK-ID-XXX-05",
                "MOCK-ID-XXX-06",
                "MOCK-ID-XXX-07",
                "MOCK-ID-XXX-08",
                "MOCK-ID-XXX-09",
                "MOCK-ID-XXX-10",
                "MOCK-ID-XXX-11",
                "MOCK-ID-XXX-12",
                "MOCK-ID-XXX-13",
                "MOCK-ID-XXX-14",
                "MOCK-ID-XXX-15",
                "MOCK-ID-XXX-16",
                "MOCK-ID-XXX-17",
                "MOCK-ID-XXX-18",
                "MOCK-ID-XXX-19"
        );


        String[] csvList = csvString.split(FiccFactory.EOL);
        String line = csvList[2];
        FiccTransaction actual = factory.fromCsvLine(line);
        actual.setId("DUMMY-TRANSACTION-ID");
        dao.add(actual);

        FiccTransaction expected = new FiccTransaction();
        expected.setId("DUMMY-TRANSACTION-ID");
        expected.setDate(LocalDate.of(2017,7,21));
        expected.setTransaction("DEBIT");
        expected.setMemo("123456789098765432111; 55533;");
        FiccCounterParty cpt = dao.getFiccCounterPartyByName("ZATAR NEW YORK");
        expected.setCounterparty(cpt);
        expected.setAmount(-10.06);

        assertEquals(expected,actual);
    }

    @Test
    public void fromJsonString() throws Exception {
        System.out.println("test fromJsonString");
        FiccTransaction expected = TestEntityFactory.getTransaction1();
        dao.add(expected);
        JSONObject json = expected.toJson();
        FiccTransaction tReconstructed = factory.fromJsonString(
                json.toJSONString()
        );
        assertEquals(expected,tReconstructed);
    }

    @Test
    public void fromJsonObject() throws Exception {
        System.out.println("test fromJsonObject");
        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        dao.add(t1);
        JSONObject json = t1.toJson();
        FiccTransaction tReconstructed = factory.fromJsonObject(json);
        assertEquals(tReconstructed,t1);
    }

    @Test
    public void fromJsonArray() throws Exception {
        System.out.println("test fromJsonArray");

        List<FiccTransaction> expected = new ArrayList<>();

        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        dao.add(t1);
        expected.add(t1);
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        dao.add(t2);
        expected.add(t2);

        JSONObject json1 = t1.toJson();
        JSONObject json2 = t2.toJson();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(json1);
        jsonArray.add(json2);

        List<FiccTransaction> actual = factory.fromJsonArray(jsonArray);
        assertEquals(expected,actual);
    }

    @Test
    public void fromCptyName() throws Exception {
        System.out.println("test fromCptyName");
        FiccTransaction t1 = TestEntityFactory.getTransaction1();
        FiccTransaction t2 = TestEntityFactory.getTransaction2();
        dao.add(t1);
        dao.add(t2);

        String name = t1.getCounterparty().getName();
        FiccCounterParty actual = factory.fromCptyName(name);
        FiccCounterParty expected = t1.getCounterparty();
        assertEquals(expected,actual);
    }

}