package org.tomw.ficc.xls;

import com.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FiccExcelReader {
    private final static Logger LOGGER = Logger.getLogger(FiccExcelReader.class.getName());


    public List<FiccExcelRecord> readSpreadsheet(String csvFile) throws IOException, InvalidFormatException {
        LOGGER.info("Read file " + csvFile);

        File file = new File(
                getClass().getClassLoader().getResource(csvFile).getFile()
        );

        return readSpreadsheet(file);
    }

    public  List<FiccExcelRecord> readSpreadsheet(File file){
        List<FiccExcelRecord> result = new ArrayList<>();

        CSVReader reader = null;

        try {
            reader = new CSVReader(new FileReader(file));
            String[] line;
            boolean firstLine = true;
            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    FiccExcelRecord record = new FiccExcelRecord();
                    LocalDate date = LocalDateUtils.parseUsStyleDate(line[0]);
                    record.setDate(date);

                    record.setDirection(line[1]);
                    record.setName(line[2]);
                    record.setMemo(line[3]);
                    record.setAmount(Double.parseDouble(line[4]));

                    result.add(record);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read file " + file, e);
        }
        return result;
    }

    /**
     * read given file, return list of records for transactions which happen on given date or later
     *
     * @param csvFile
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<FiccExcelRecord> getRecordsAfterDate(String csvFile, LocalDate date) throws IOException, InvalidFormatException {
        List<FiccExcelRecord> filteredRecords = new ArrayList<>();
        for (FiccExcelRecord record : readSpreadsheet(csvFile)) {
            LocalDate recordDate = record.getDate();
            if (date.isBefore(recordDate)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    /**
     *
     * @param file
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<FiccExcelRecord> getRecordsAfterDate(File file, LocalDate date) throws IOException, InvalidFormatException {
        List<FiccExcelRecord> filteredRecords = new ArrayList<>();
        for (FiccExcelRecord record : readSpreadsheet(file)) {
            LocalDate recordDate = record.getDate();
            if (date.isBefore(recordDate)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    /**
     * get list of records which happen after date, which are present in file 1 but not in file 2
     *
     * @param csvFile1
     * @param csvFile2
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<FiccExcelRecord> getDifferenceAfterDate(String csvFile1, String csvFile2, LocalDate date) throws IOException, InvalidFormatException {
        List<FiccExcelRecord> list1 = getRecordsAfterDate(csvFile1, date);
        List<FiccExcelRecord> list2 = getRecordsAfterDate(csvFile2, date);
        List<FiccExcelRecord> result = new ArrayList<>();

        for (FiccExcelRecord record : list1) {
            if (recordNotInList(record, list2)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     *
     * @param file1 more recent transactions
     * @param file2 older transactions
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<FiccExcelRecord> getDifferenceAfterDate(File file1, File file2, LocalDate date) throws IOException, InvalidFormatException {
        List<FiccExcelRecord> list1 = getRecordsAfterDate(file1, date);
        List<FiccExcelRecord> list2 = getRecordsAfterDate(file2, date);
        List<FiccExcelRecord> result = new ArrayList<>();

        for (FiccExcelRecord record : list1) {
            if (recordNotInList(record, list2)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     *
     * @param csvFile1
     * @param csvFile2
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public String prepareReport(String csvFile1, String csvFile2, LocalDate date) throws IOException, InvalidFormatException {
        String result = "";
        List<FiccExcelRecord> newTransactions = getDifferenceAfterDate(csvFile1, csvFile2, date);
        result = result + "New Transactions: " + newTransactions.size() + TomwStringUtils.EOL;
        result = result + "Sum of new credits: " + getSumOfCredits(newTransactions) + TomwStringUtils.EOL;
        result = result + "Sum of new debits: " + getSumOfDebits(newTransactions) + TomwStringUtils.EOL;

        return result;
    }

    /**
     *
     * @param csvFile1
     * @param csvFile2
     * @param date
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public String prepareReport(File csvFile1, File csvFile2, LocalDate date) throws IOException, InvalidFormatException {
        String result = "";
        List<FiccExcelRecord> newTransactions = getDifferenceAfterDate(csvFile1, csvFile2, date);
        result = result + "New Transactions: " + newTransactions.size() + TomwStringUtils.EOL;
        result = result + "Sum of new credits: " + getSumOfCredits(newTransactions) + TomwStringUtils.EOL;
        result = result + "Sum of new debits: " + getSumOfDebits(newTransactions) + TomwStringUtils.EOL;

        return result;
    }

    public double getSumOfDebits(List<FiccExcelRecord> list) {
        double sum = 0.0;
        for (FiccExcelRecord record : list) {
            if (record.getAmount() < 0.) {
                sum = sum + record.getAmount();
            }
        }
        return sum;
    }

    public double getSumOfCredits(List<FiccExcelRecord> list) {
        double sum = 0.0;
        for (FiccExcelRecord record : list) {
            if (record.getAmount() > 0.) {
                sum = sum + record.getAmount();
            }
        }
        return sum;
    }

    private boolean recordNotInList(FiccExcelRecord record, List<FiccExcelRecord> list2) {
        return !list2.contains(record);
    }


}
