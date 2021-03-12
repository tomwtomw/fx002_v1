/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.dao;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.czeki.imageview.CheckImageException;

/**
 *
 * @author tomw
 */
public interface CzekiDao {

    public void init() throws IOException, DataFormatException;

    public void commit() throws IOException;

    public void add(Transaction transaction);

    public void add(CounterParty counterParty);

    public void add(CheckImage checkImage);
    
    public CheckImage addNewImage(File imageFile) throws IOException, CheckImageException;
    
    public CheckImage addNewImage(File imageFile,boolean moveFile) throws IOException,CheckImageException;

    public Transaction getTransaction(String id);

    public CounterParty getCounterParty(String id);
     
    public CheckImage getCheckImage(String id);

    public boolean containsTransaction(String id);

    public boolean containsCounterParty(String id);
    
    public boolean containsCheckImage(String id);

    public CounterParty getCounterPartyByShortName(String shortName);

    public Map<String, Transaction> getTransactions();

    public Map<String, CounterParty> getCounterParties();
    
     public Map<String, CheckImage> getCheckImages();

    public List<CheckImage> getAllImages();

    public ObservableList<Transaction> getTransactionsData();
    
    public ObservableList<Transaction> getTransactionsData(LocalDate startDate, LocalDate endDate);
    
    public ObservableList<Transaction> getTransactionsData(CounterParty cp);
    
    public ObservableList<Transaction> getTransactionsData(List<CounterParty> listOfCounterParties);
    
    public ObservableList<Transaction> getTransactionsData(Set<CounterParty> listOfCounterParties, LocalDate startDate, LocalDate endDate);
    
    public ObservableList<Transaction> getTransactionsData(Set<CounterParty> listOfCounterParties);
    
    public ObservableList<Transaction> getTransactionsData(List<CounterParty> listOfCounterParties, LocalDate startDate, LocalDate endDate);
    
    public boolean transactionsDataContains(String transactionId);
    
    public ObservableList<CounterParty> getCounterPartyData();
    
    public ObservableList<CheckImage> getCheckImageData();

    public void replaceCounterParty(CounterParty cpOld, CounterParty cpNew);

    public void mergeCounterparties(CounterParty cp1, CounterParty cp2, CounterParty newCp);

    public void deleteTransaction(String id);

    public void deleteCounterParty(String id) throws DataIntegrityException;

    public void deleteImage(String id) throws DataIntegrityException;

    public void deleteTransaction(Transaction transaction);

    public void deleteCounterParty(CounterParty cp) throws DataIntegrityException;

    public void deleteImage(CheckImage checkImage) throws DataIntegrityException;

    public void delete(Transaction transaction);
    public void delete(CheckImage checkImage) throws DataIntegrityException;
    public void delete(CounterParty cp) throws DataIntegrityException;

    public void deleteAll()  throws DataIntegrityException;

    public void deleteTransactions();
    public void deleteCounterParties()  throws DataIntegrityException;
    public void deleteCheckImages()  throws DataIntegrityException;

    public void reload();

    public void reloadImages() throws CheckImageException;

    //public List<CheckImage> loadAllImages();

    public Map<String, Transaction> getTransactionsForCounterparty(String counterpartyId);

    public Map<String, Transaction> getTransactionsForCounterparty(CounterParty counterparty);
    
    public Transaction getTransactionForCheckImage(CheckImage checkImage);

    public List<CheckImage> getByCheckNumber(String checkNumber);
    
    public List<CheckImage> getImagesForTransaction(Transaction transaction);

    public List<CheckImage> getByCheckNumberAndSide(String checkNumber, String side);

    public List<CheckImage> getBySubstring(String substring);

    public boolean inStorage(CheckImage checkImage);

    public void exportToFile(File file) throws IOException;

    public void backupToCsvFile() throws IOException;
    
    public String toCsv();

    public File backupToFile() throws IOException;
    
    public void backupToFile(File backupFile) throws IOException;

    public Collection<Transaction> getClearedTransactions();

    public void setMostRecentCheckNumber(String checkNumber);

    public String getMostRecentCheckNumber();

}
