package org.tomw.ficc.xls;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FiccExcelReaderTest {
    private final static Logger LOGGER = Logger.getLogger(FiccExcelReaderTest.class.getName());

    public static final String excelPath = "";

    public static final String path2 = "";

    @Test
    public void test_readSpreadsheet() throws IOException, InvalidFormatException {
        boolean exThrown = false;
        List<FiccExcelRecord> listOfRecords = new ArrayList<>();
        try {
            String csvFile = "./test_transactions.csv";
            FiccExcelReader reader = new FiccExcelReader();
            listOfRecords = reader.readSpreadsheet(csvFile);
        } catch (Exception e) {
            exThrown = true;
            System.out.println(e);
        }
        assertFalse(exThrown);
        assertEquals(37,listOfRecords.size());
    }

    @Test
    public void test_getRecordsAfterDate() throws IOException, InvalidFormatException {
        boolean exThrown = false;
        List<FiccExcelRecord> listOfRecords = new ArrayList<>();
        try {
            String csvFile = "./test_transactions.csv";
            LocalDate cutofDate = LocalDate.of(2019,11,21);
            FiccExcelReader reader = new FiccExcelReader();
            listOfRecords = reader.getRecordsAfterDate(csvFile,cutofDate);
        } catch (Exception e) {
            exThrown = true;
            System.out.println(e);
        }
        assertFalse(exThrown);
        assertEquals(26,listOfRecords.size());
    }

    @Test
    public void test_getDifferenceAfterDate() throws IOException, InvalidFormatException {
        boolean exThrown = false;
        List<FiccExcelRecord> listOfRecords = new ArrayList<>();
        FiccExcelReader reader = new FiccExcelReader();
        try {
            String csvFile1 = "./test_transactions.csv";
            String csvFile2 = "./test_transactions2.csv";

            LocalDate cutofDate = LocalDate.of(2019,11,20);

            listOfRecords = reader.getDifferenceAfterDate(csvFile2,csvFile1,cutofDate);

            String report = reader.prepareReport(csvFile2,csvFile1,cutofDate);
            System.out.println("report=\n"+report);
        } catch (Exception e) {
            exThrown = true;
            LOGGER.error("Exception occured",e);
        }
        assertFalse(exThrown);
        assertEquals(3,listOfRecords.size());

        assertEquals(-112.-18.21,reader.getSumOfDebits(listOfRecords),0.001);
        assertEquals(799.11,reader.getSumOfCredits(listOfRecords),0.001);

    }

    @Test
    public void test_getReport() throws IOException, InvalidFormatException {
        boolean exThrown = false;
        List<FiccExcelRecord> listOfRecords = new ArrayList<>();
        FiccExcelReader reader = new FiccExcelReader();
        try {

            String csvFile1 = "./test_transactions.csv";
            String csvFile2 = "./test_transactions2.csv";

            LocalDate cutofDate = LocalDate.of(2019,11,20);

            listOfRecords = reader.getDifferenceAfterDate(csvFile2,csvFile1,cutofDate);
            for(FiccExcelRecord record : listOfRecords){
                System.out.println(record);
            }

            String report = reader.prepareReport(csvFile2,csvFile1,cutofDate);
            System.out.println("report=\n"+report);
        } catch (Exception e) {
            exThrown = true;
            LOGGER.error("Exception occured",e);
        }
        assertFalse(exThrown);

    }

}