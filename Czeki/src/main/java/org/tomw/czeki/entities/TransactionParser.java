/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.dao.CzekiDao;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.czeki.imageview.CheckImageException;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.TomwStringUtils;

/**
 *
 * @author tomw
 */
public class TransactionParser {

    private final static Logger LOGGER = Logger.getLogger(TransactionParser.class.getName());

    public static Transaction buildTransaction(File imageDirectory, JSONObject json, CzekiDao dao) {

        LOGGER.info("json="+json);
        
        String id = (String) json.get(Transaction.ID);
        String dateString = (String) json.get(Transaction.DATE);
        LOGGER.info("Date string = "+dateString);
        LocalDate date = LocalDate.parse(dateString);
        double amount = (Double) json.get(Transaction.TRANSACTION_AMOUNT);
        String checkNumber = (String) json.get(Transaction.CHECK_NO);
        String memo = (String) json.get(Transaction.MEMO);
        String comment = (String) json.get(Transaction.COMMENT);

        String cleared = (String) json.get(Transaction.CLEARED);
        cleared = TomwStringUtils.boolean2BlankY(TomwStringUtils.stringToBoolean(cleared));

        String counterpartyId = (String) json.get(Transaction.COUNTERPARTY_ID);
        CounterParty counterParty = dao.getCounterParties().get(counterpartyId);

        Transaction transaction = new Transaction(id, date, amount, checkNumber, counterParty, memo, comment, cleared);

        JSONObject checkImageFrontJson = (JSONObject) json.get(Transaction.CHECK_IMAGE_FRONT);
        if (checkImageFrontJson != null) {
            CheckImage imageFront = null;
            try {
                LOGGER.info("checkImageFrontJson=" + checkImageFrontJson);
                imageFront = CheckImage.fromJson(imageDirectory, checkImageFrontJson);
            } catch (CheckImageException ex) {
                LOGGER.error("Failed to reconstruct front image for transaction id=" + id, ex);
            }
            transaction.setImageFront(imageFront);
        }
        JSONObject checkImageBackJson = (JSONObject) json.get(Transaction.CHECK_IMAGE_BACK);
        if (checkImageBackJson != null) {
            CheckImage imageBack = null;
            try {
                imageBack = CheckImage.fromJson(imageDirectory, checkImageBackJson);
            } catch (CheckImageException ex) {
                LOGGER.error("Failed to reconstruct back image for transaction id=" + id, ex);
            }
            transaction.setImageBack(imageBack);
        }
        return transaction;
    }

    /**
     *
     * @param transactionAsCsvLine
     * @param dao
     * @return
     * @throws org.tomw.czeki.entities.CsvDataFormatException
     */
    public static Transaction buildTransaction(String transactionAsCsvLine, CzekiDao dao) throws CsvDataFormatException {

        LOGGER.info("Parse line " + transactionAsCsvLine);

        String[] transactionLineSplit = transactionAsCsvLine.split(CzekiRegistry.CSV_SEPARATOR);
        
        if (transactionLineSplit.length < 3) {
            throw new CsvDataFormatException("csv line has bad format: " + transactionAsCsvLine);
        }

        LocalDate date = extractDateFromLine(transactionAsCsvLine);

        double dollars = Double.parseDouble(transactionLineSplit[1]);
        String checkId = transactionLineSplit[2];

        String counterPartyShortName = CzekiRegistry.UNKNOWN_COUNTERPARTY;
        String cleared=CzekiRegistry.BLANK;
        String memo   =CzekiRegistry.BLANK;
        String comment=CzekiRegistry.BLANK;
        // date,amount,check no,cleared, cpty,comment memo
        // 0   ,     1,       2,      3,    4,      5,   6
       
        if(transactionLineSplit.length == 4) {
            cleared=TomwStringUtils.normalizeBooleanToBlankY(transactionLineSplit[3]);
        }
        if(transactionLineSplit.length == 5) {
            cleared=TomwStringUtils.normalizeBooleanToBlankY(transactionLineSplit[3]);
            counterPartyShortName = transactionLineSplit[4];
        }

        if (transactionLineSplit.length == 6) {
            cleared=TomwStringUtils.normalizeBooleanToBlankY(transactionLineSplit[3]);
            counterPartyShortName = transactionLineSplit[4];
            comment=transactionLineSplit[5];
        } 
        
        if (transactionLineSplit.length > 6) {
            cleared= TomwStringUtils.normalizeBooleanToBlankY(transactionLineSplit[3]);
            counterPartyShortName = transactionLineSplit[4];
            comment=transactionLineSplit[5];
            memo=transactionLineSplit[6];
        } 

        // is counterparty known?
        CounterParty counterParty = dao.getCounterPartyByShortName(counterPartyShortName);
        if (counterParty == null) {
            counterParty = new CounterParty(counterPartyShortName);
            dao.add(counterParty);
        }

        Transaction transaction
                = new Transaction(
                        date,
                        dollars,
                        checkId,
                        counterParty,
                        memo,
                        comment,
                        cleared);

        dao.add(transaction);

        LOGGER.info("transaction=" + transaction.toJsonString());

        return transaction;
    }

    /**
     * verify that the input lines are consistent
     * @param sanitizedFileLines
     * @return 
     */
    public static boolean linesAreInconsistent(List<String> sanitizedFileLines) {
        boolean result=false;
        // first, verify that input lines dates are in sorted order
        if(sanitizedFileLines.size()>1){
            for(int i=0;i<sanitizedFileLines.size()-1;i=i+1){
                String currentLine = sanitizedFileLines.get(i);
                String nextLine    = sanitizedFileLines.get(i+1);
                
                LocalDate currentDate = extractDateFromLine(currentLine);
                LocalDate nextDate    = extractDateFromLine(nextLine);
                if(currentDate.isAfter(nextDate)){
                    LOGGER.error("Lines are inconsistent:");
                    LOGGER.error(currentLine);
                    LOGGER.error(nextLine);
                    LOGGER.error(currentDate+" is after "+nextDate);
                    result= true;
                }
            }
        }
        return result;
    }

    /**
     * extract from csv line the first component, convert it to date
     * @param line
     * @return 
     */
    public static LocalDate extractDateFromLine(String line) {
        String[] lineSplit = line.split(CzekiRegistry.CSV_SEPARATOR);
        return LocalDateUtils.fromString(lineSplit[0]);
    }
}
