/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.dao;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.czeki.entities.TransactionParser;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.czeki.imageview.CheckImageException;
import org.tomw.czeki.imageview.CheckImageNameDecoder04;
import org.tomw.czeki.imageview.CzekImageUtils;
import org.tomw.utils.Json2String;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.TomwStringUtils;

/**
 *
 * @author tomw
 */
public final class CzekiDaoJsonImpl implements CzekiDao {

    private final static Logger LOGGER = Logger.getLogger(CzekiDaoJsonImpl.class.getName());

    public static final String COUNTERPARTIES = "Counterparties";
    public static final String TRANSACTIONS = "Transactions";
    public static final String IMAGES = "Images";
    public static final String MOST_RECENT_CHECK_NUMBER = "MostRecentCheckNumber";

    public static final String CONFIGURATION = "Configuration";

    private final JSONParser jsonParser = new JSONParser();

    private final Map<String, Transaction> transactions = new TreeMap<>();
    private final Map<String, CounterParty> counterparties = new TreeMap<>();
    private final Map<String, CheckImage> images = new TreeMap<>();

    private String mostRecentCheckNumber=null;

    private final ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();
    private final ObservableList<CounterParty> counterpartiesData = FXCollections.observableArrayList();
    private final ObservableList<CheckImage> imagesObservableData = FXCollections.observableArrayList();

    private final File imageDirectory;
    private final File dataFile;

    public CzekiDaoJsonImpl(File dataFile, File imageDirectory) {
        LOGGER.info("dao image storage = " + imageDirectory);
        LOGGER.info("dao data file = " + dataFile);
        this.dataFile = dataFile;
        this.imageDirectory = imageDirectory;
        init();
    }

    @Override
    public void init() {
        clear();
        try {
            load();
        } catch (IOException | CheckImageException ex) {
            LOGGER.error("Failed to load DAO " + this, ex);
        }
    }

    @Override
    public void commit() throws IOException {
        JSONObject json = toJson();
        writeJsonToFile(this.dataFile, json);
    }

    /**
     *
     * @param transaction to be added to storage
     */
    @Override
    public void add(Transaction transaction) {
        CounterParty counterParty = transaction.getCounterParty();
        if (counterParty != null) {
            if (!this.counterparties.containsValue(counterParty)) {
                this.add(transaction.getCounterParty());
            }
        }

        CheckImage imageFront = transaction.getImageFront();
        if (imageFront != null) {
            if (!containsCheckImage(imageFront.getId())) {
                add(imageFront);
            }
        }
        CheckImage imageBack = transaction.getImageBack();
        if (imageBack != null) {
            if (!containsCheckImage(imageBack.getId())) {
                add(imageBack);
            }
        }

        this.transactions.put(transaction.getId(), transaction);

        addToListIfNotInList(transactionsData, transaction);
    }

    /**
     *
     * @param counterParty to be added to storage
     */
    @Override
    public void add(CounterParty counterParty) {
        this.counterparties.put(counterParty.getId(), counterParty);
        addCounterPartyToListIfNotInList(this.counterpartiesData, counterParty);
    }

    @Override
    public void add(CheckImage checkImage) {
        this.images.put(checkImage.getId(), checkImage);
        addCheckImageToListIfNotInList(this.imagesObservableData, checkImage);
    }

    /**
     * Create new Check image from image file
     *
     * @param imageFile image file to be used, file is left in place
     * @return check image object which has been added to storage
     * @throws java.io.IOException if IO error happened
     * @throws org.tomw.czeki.imageview.CheckImageException if there is a problem with check image
     */
    @Override
    public CheckImage addNewImage(File imageFile) throws IOException, CheckImageException {
        return this.addNewImage(imageFile, false);
    }

    /**
     * Create new Check image from image file
     *
     * @param imageFile image file
     * @param moveFile true if source file should be deleted after successfull add
     * @return image added
     * @throws java.io.IOException if IO error happened
     * @throws org.tomw.czeki.imageview.CheckImageException if there is a problem with check image
     */
    @Override
    public CheckImage addNewImage(File imageFile, boolean moveFile) throws IOException, CheckImageException {
        if (!imageFile.exists()) {
            return null;
        } else {
            File dst = new File(imageDirectory, imageFile.getName());
            if (moveFile) {
                FileUtils.moveFile(imageFile, dst);
            } else {
                FileUtils.copyFile(imageFile, dst);
            }
            CheckImage checkImage = new CheckImage(imageDirectory, imageFile.getName());
            add(checkImage);
            return checkImage;
        }
    }

    @Override
    public Transaction getTransaction(String id) {
        return this.transactions.get(id);
    }

    @Override
    public CounterParty getCounterParty(String id) {
        return this.counterparties.get(id);
    }

    @Override
    public CheckImage getCheckImage(String id) {
        return this.images.get(id);
    }

    @Override
    public boolean containsTransaction(String id) {
        return this.transactions.containsKey(id);
    }

    @Override
    public boolean containsCounterParty(String id) {
        return this.counterparties.containsKey(id);
    }

    @Override
    public boolean containsCheckImage(String id) {
        return this.images.containsKey(id);
    }

    @Override
    public CounterParty getCounterPartyByShortName(String shortName) {
        for (CounterParty counterParty : this.counterparties.values()) {
            if (counterParty.getShortName().equals(shortName)) {
                return counterParty;
            }
        }
        // if we are here then we have not found the counterparty 
        // look among other names
        for (CounterParty counterParty : this.counterparties.values()) {
            if (counterParty.getOtherNames().contains(shortName)) {
                return counterParty;
            }
        }
        // if we are here then we have not found the counterparty
        return null;
    }

    @Override
    public Map<String, Transaction> getTransactions() {
        return this.transactions;
    }

    @Override
    public Map<String, CounterParty> getCounterParties() {
        return this.counterparties;
    }

    @Override
    public Map<String, CheckImage> getCheckImages() {
        return this.images;
    }

    @Override
    public List<CheckImage> getAllImages() {
        List<CheckImage> result = new ArrayList<>();
        for (CheckImage checkImage : this.images.values()) {
            result.add(checkImage);
        }
        return result;
    }

    @Override
    public ObservableList<Transaction> getTransactionsData() {
        return transactionsData;
    }
    
    @Override
    public ObservableList<Transaction> getTransactionsData(LocalDate startDate, LocalDate endDate) {
       ObservableList<Transaction> selectedTransactionsData = FXCollections.observableArrayList();
       for(Transaction t : transactionsData){
           LOGGER.info("t="+t);
           boolean startDatePassed = true;
           if(t.getTransactionDate().isBefore(startDate)){
               startDatePassed=false;
           }
           boolean endDatePassed = true;
           if(t.getTransactionDate().isAfter(endDate)){
               endDatePassed=false;
           }
           if(startDatePassed && endDatePassed){
               selectedTransactionsData.add(t);
           }
       }
       return selectedTransactionsData;
    }

    @Override
    public ObservableList<Transaction> getTransactionsData(List<CounterParty> listOfCounterParties) {
        Set<CounterParty> setOfCounterParties = new HashSet<>(listOfCounterParties);
        return getTransactionsData(setOfCounterParties);
    }

    @Override
    public ObservableList<Transaction> getTransactionsData(List<CounterParty> listOfCounterParties, LocalDate startDate, LocalDate endDate) {
        Set<CounterParty> setOfCounterParties = new HashSet<>(listOfCounterParties);
        return getTransactionsData(setOfCounterParties, startDate, endDate);
    }

    @Override
    public ObservableList<Transaction> getTransactionsData(Set<CounterParty> setOfCounterParties, LocalDate startDate, LocalDate endDate) {
        ObservableList<Transaction> selectedTransactionsData = FXCollections.observableArrayList();
        for (Transaction t : getTransactionsData()) {
            if (setOfCounterParties.contains(t.getCounterParty())) {
                boolean startDatePassed = true;
                if (startDate != null) {
                    if (t.getTransactionDate().isBefore(startDate)) {
                        startDatePassed = false;
                    }
                }
                boolean endDatePassed = true;
                if (endDate != null) {
                    if (t.getTransactionDate().isAfter(endDate)) {
                        endDatePassed = false;
                    }
                }
                if (startDatePassed && endDatePassed) {
                    selectedTransactionsData.add(t);
                }
            }
        }
        return selectedTransactionsData;
    }

    @Override
    public ObservableList<Transaction> getTransactionsData(Set<CounterParty> setOfCounterParties) {
        ObservableList<Transaction> selectedTransactionsData = FXCollections.observableArrayList();
        for (Transaction t : getTransactionsData()) {
            if (setOfCounterParties.contains(t.getCounterParty())) {
                selectedTransactionsData.add(t);
            }
        }
        return selectedTransactionsData;
    }

    @Override
    public ObservableList<CounterParty> getCounterPartyData() {
        return this.counterpartiesData;
    }

    @Override
    public ObservableList<CheckImage> getCheckImageData() {
        return imagesObservableData;
    }

    @Override
    public void replaceCounterParty(CounterParty cpOld, CounterParty cpNew) {
        this.add(cpNew);
        for (Transaction transaction : this.getTransactions().values()) {
            if (transaction.getCounterParty().equals(cpOld)) {
                transaction.setCounterParty(cpNew);
            }
        }
        try {
            this.deleteCounterParty(cpOld);
        } catch (DataIntegrityException ex) {
            LOGGER.info("Error occured while deleting old counterparty " + cpOld + ", ingnore it, it should not be there anyway.");
        }
    }

    @Override
    public void mergeCounterparties(CounterParty cp1, CounterParty cp2, CounterParty newCp) {
        replaceCounterParty(cp1, newCp);
        replaceCounterParty(cp2, newCp);
    }

    @Override
    public void deleteTransaction(String id) {
        this.transactions.remove(id);
        //TODO add check of transactionsData in delete(Transaction) tests
        Iterator<Transaction> iter = this.transactionsData.iterator();
        while (iter.hasNext()) {
            Transaction currentTransaction = (Transaction) iter.next();
            if (currentTransaction.getId().equals(id)) {
                iter.remove();
            } else {
            }
        }
    }

    @Override
    public void deleteCounterParty(String id) throws DataIntegrityException {
        if (!getTransactionsForCounterparty(id).isEmpty()) {
            throw new DataIntegrityException("Counterparty id=" + id + " is used by transactions");
        } else {
            //TODO add check of counterpartiesData in delete(Counterparty) tests
            this.counterparties.remove(id);
            Iterator<CounterParty> iter = this.counterpartiesData.iterator();
            while (iter.hasNext()) {
                CounterParty cp = (CounterParty) iter.next();
                if (cp.getId().equals(id)) {
                    iter.remove();
                } else {
                }
            }
        }
    }

    @Override
    public void deleteImage(String id) throws DataIntegrityException {
        CheckImage image = getCheckImage(id);
        deleteImage(image);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        deleteTransaction(transaction.getId());
    }

    @Override
    public void deleteCounterParty(CounterParty cp) throws DataIntegrityException {
        deleteCounterParty(cp.getId());
    }

    @Override
    public void deleteImage(CheckImage image) throws DataIntegrityException {
        if (image != null) {
            if (this.getTransactionForCheckImage(image) != null) {
                throw new DataIntegrityException("cannot delete image, it is used by transaction");
            }
            this.images.remove(image.getId());
            Iterator<CheckImage> iter = this.imagesObservableData.iterator();
            while (iter.hasNext()) {
                CheckImage ci = (CheckImage) iter.next();
                if (ci.equals(image)) {
                    iter.remove();
                }
            }
            image.getFile().delete();
        }
    }

    @Override
    public void delete(Transaction transaction) {
        deleteTransaction(transaction);
    }

    @Override
    public void delete(CheckImage checkImage) throws DataIntegrityException {
        deleteImage(checkImage);
    }

    @Override
    public void delete(CounterParty cp) throws DataIntegrityException {
        deleteCounterParty(cp);
    }

    @Override
    public void deleteAll() throws DataIntegrityException {
        this.deleteTransactions();
        this.deleteCounterParties();
        this.deleteCheckImages();
    }

    @Override
    public void deleteTransactions() {
        List<Transaction> listOfTransactions = new ArrayList<>();
        for (Transaction t : getTransactions().values()) {
            listOfTransactions.add(t);
        }
        for (Transaction t : listOfTransactions) {
            delete(t);
        }
    }

    /**
     *
     * @throws DataIntegrityException
     */
    @Override
    public void deleteCounterParties() throws DataIntegrityException {
        List<CounterParty> list = new ArrayList<>();
        for (CounterParty c : getCounterParties().values()) {
            if (!getTransactionsForCounterparty(c.getId()).isEmpty()) {
                throw new DataIntegrityException("Counterparty id=" + c.getId() + " is used by transactions");
            }
            list.add(c);
        }
        for (CounterParty c : list) {
            delete(c);
        }
    }

    @Override
    public void deleteCheckImages() throws DataIntegrityException {
        List<CheckImage> list = new ArrayList<>();
        for (CheckImage c : getCheckImages().values()) {
            Transaction t = getTransactionForCheckImage(c);
            if (t != null) {
                throw new DataIntegrityException("Transaction uses image " + t.toString());
            }
            list.add(c);
        }
        for (CheckImage c : list) {
            delete(c);
        }
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Reload images. Loop over images in images directory, check which files
     * correspond to known images. Create image objects for those which do not
     * have known images and add them to dao
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Override
    public void reloadImages() throws CheckImageException {
        renameSimpleFormatFiles(imageDirectory,LocalDate.now());
        for (File f : imageDirectory.listFiles()) {
            LOGGER.info("Image file="+f.getName());
            
            if (CzekImageUtils.getDecoder(f)!=null) {
                CheckImage c = new CheckImage(imageDirectory,f.getName());
                LOGGER.info("c="+c.getCheckNumber()+" side="+c.getSide());
                if (!containsCheckImage(c.getId())) {
                    add(c);
                }
            }
        }
    }

    /**
     * The objective is to find image files with names in format
     * NNNNF or NNNNB
     * and rename them into format YYYY-MM-DD-NNNN-F or YYYY-MM-DD-NNNN-B
     */
    public static void renameSimpleFormatFiles(File imageDirectory,LocalDate date) {

        CheckImageNameDecoder04 decoder = new CheckImageNameDecoder04();

        for (File f : imageDirectory.listFiles()) {
            if(decoder.isValid(f.getAbsoluteFile())) {
                LOGGER.info(f.getAbsolutePath());
                String extension = FilenameUtils.getExtension(f.toString());
                String checkNumber = decoder.getCheckNumber(f);
                String side = decoder.getSide(f);
                String dateAsString = LocalDateUtils.toYYYYMMDD(date);
                String destinationName = dateAsString+"-"+checkNumber+"-"+side+"."+extension;

                File destination = new File(imageDirectory,destinationName);
                LOGGER.info("move "+f);
                LOGGER.info("to="+destination);
                try {
                    FileUtils.moveFile(f,destination);
                } catch (IOException e) {
                    LOGGER.error("Failed to move file "+f+" to "+destination,e);
                }
            }
        }
    }

    @Override
    public Map<String, Transaction> getTransactionsForCounterparty(String counterpartyId) {
        Map<String, Transaction> selectedTransactions = new TreeMap<>();
        for (Transaction transaction : this.transactions.values()) {
            if (transaction.getCounterParty().getId().equals(counterpartyId)) {
                selectedTransactions.put(transaction.getId(), transaction);
            }
        }
        return selectedTransactions;
    }

    @Override
    public Map<String, Transaction> getTransactionsForCounterparty(CounterParty counterparty) {
        return getTransactionsForCounterparty(counterparty.getId());
    }

    @Override
    public List<CheckImage> getByCheckNumber(String checkNumber) {
        List<CheckImage> result = new ArrayList<>();
        for (CheckImage c : getAllImages()) {
            if (c.getCheckNumber().equals(checkNumber)) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<CheckImage> getImagesForTransaction(Transaction transaction) {
        List<CheckImage> result = new ArrayList<>();
        if (transaction.getImageFront() != null) {
            result.add(transaction.getImageFront());
        }
        if (transaction.getImageBack() != null) {
            result.add(transaction.getImageBack());
        }
        return result;
    }

    @Override
    public List<CheckImage> getByCheckNumberAndSide(String checkNumber, String side) {
        LOGGER.info("Find chec number and side " + checkNumber + " " + side);
        List<CheckImage> result = new ArrayList<>();
        List<CheckImage> allImages =  getAllImages();
        
        Collections.sort(allImages,CheckImage.compareByImageNumber);
        
        for (CheckImage c : allImages) {
            LOGGER.info("Check image=" + c.getId()+" number="+c.getCheckNumber()+" side="+c.getSide());            
            if (c.getCheckNumber().equals(checkNumber)) {
                LOGGER.info("Check number agrees");
                LOGGER.info("Side is front, back" + c.isFront() + " " + c.isBack());
                if (CzekImageUtils.FRONT.equals(side) && c.isFront()
                        || CzekImageUtils.BACK.equals(side) && c.isBack()) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    @Override
    public List<CheckImage> getBySubstring(String substring) {
        List<CheckImage> result = new ArrayList<>();
        for (CheckImage c : getAllImages()) {
            if (c.getFile().getName().contains(substring)) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public boolean inStorage(CheckImage checkImage) {
        //TODO redefile inStorage. It should check if image file is present in image directory
        return checkImage.getFile().exists();
    }

    @Override
    public void exportToFile(File file) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void backupToCsvFile() throws IOException {

        //throw new RuntimeException("Not implemented");
        String fileName = "backup.csv";
        String csv = toCsv();
        FileUtils.writeStringToFile(new File(fileName), csv);
    }

    /**
     *
     * @return
     */
    @Override
    public String toCsv() {
        String result = Transaction.fullHeader() + Transaction.EOL;
        List<Transaction> allTransactions = new ArrayList<>();
        for (Transaction t : getTransactions().values()) {
            allTransactions.add(t);
        }
        Collections.sort(allTransactions, new Transaction.CompareByDate());

        for (Transaction t : allTransactions) {
            result = result + t.toFullCsv() + Transaction.EOL;
        }
        return result;
    }

    public void load() throws IOException, CheckImageException {
        JSONObject json = loadDataJson(this.dataFile);
        loadCheckImages(json);
        loadCounterParties(json);
        loadTransactions(json);
        loadMostRecentCheckNumber(json);
    }

    public void loadMostRecentCheckNumber(JSONObject json){
        if(json.containsKey(MOST_RECENT_CHECK_NUMBER)){
            mostRecentCheckNumber = (String)json.get(MOST_RECENT_CHECK_NUMBER);
        }else{
            mostRecentCheckNumber=null;
        }
    }

    public void loadCheckImages(JSONObject json) throws IOException, CheckImageException {
        if (json.containsKey(IMAGES)) {
            JSONArray checkImagesJson = (JSONArray) json.get(IMAGES);
            for (Object o : checkImagesJson) {
                JSONObject checkImageJson = (JSONObject) o;
                CheckImage checkImage = CheckImage.fromJson(this.imageDirectory,checkImageJson);
                add(checkImage);
            }
        }
    }

    public void loadCounterParties(JSONObject json) throws CheckImageException {
        if (json.containsKey(COUNTERPARTIES)) {
            JSONArray counterpartiesJson = (JSONArray) json.get(COUNTERPARTIES);
            for (Object o : counterpartiesJson) {
                JSONObject counterpartyJson = (JSONObject) o;
                CounterParty counterParty = CounterParty.fromJson(counterpartyJson);
                add(counterParty);
            }
        }
    }

    public void loadTransactions(JSONObject json) {
        if (json.containsKey(TRANSACTIONS)) {
            JSONArray traqnsactionsJson = (JSONArray) json.get(TRANSACTIONS);
            for (Object o : traqnsactionsJson) {
                JSONObject transactionJson = (JSONObject) o;
                Transaction transaction = TransactionParser.buildTransaction(imageDirectory, transactionJson, this);
                if(transaction.getCounterParty()==null){
                    LOGGER.error("Transaction has null counterparty, skip. "+transaction);
                }else {
                    add(transaction);
                }
            }
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public JSONObject loadDataJson(File file) throws IOException {
        JSONObject json;
        if (file == null) {
            String message = "Cannot load data, file is null";
            LOGGER.error(message);
            throw new IOException(message);
        } else {
            if (!file.exists()) {
                String message = "Cannot load data, missing file " + file;
                LOGGER.warn(message);
                return new JSONObject();
            } else {
                String fileContent = TomwStringUtils.filterCommentLines(
                        FileUtils.readFileToString(file)
                );

                try {
                    json = (JSONObject) this.jsonParser.parse(fileContent);
                } catch (ParseException ex) {
                    String message = "Cannot parse string " + fileContent;
                    LOGGER.error(message);
                    throw new IOException(message);
                }
            }
        }
        return json;
    }

    /**
     *
     * @param transactionsData transaction data
     * @param transaction transaction
     */
    public static void addToListIfNotInList(ObservableList<Transaction> transactionsData, Transaction transaction) {
        if (!transactionInList(transactionsData, transaction)) {
            transactionsData.add(transaction);
        }
    }

    /**
     *
     * @param transactionsData transaction data
     * @param transaction transaction
     * @return
     */
    public static boolean transactionInList(ObservableList<Transaction> transactionsData, Transaction transaction) {
        for (Transaction t : transactionsData) {
            if (transaction.getId().equals(t.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param counterpartiesData counterparties data
     * @param counterParty counterparty
     */
    public static void addCounterPartyToListIfNotInList(ObservableList<CounterParty> counterpartiesData, CounterParty counterParty) {
        if (!counterpartyInList(counterpartiesData, counterParty)) {
            counterpartiesData.add(counterParty);
        }
    }

    /**
     *
     * @param list
     * @param checkImage
     */
    public static void addCheckImageToListIfNotInList(ObservableList<CheckImage> list, CheckImage checkImage) {
        if (!checkImageInListInList(list, checkImage)) {
            list.add(checkImage);
        }
    }

    /**
     *
     * @param list
     * @param checkImage
     * @return
     */
    public static boolean checkImageInListInList(ObservableList<CheckImage> list, CheckImage checkImage) {
        for (CheckImage t : list) {
            if (checkImage.getId().equals(t.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param counterpartiesData  counterparties data
     * @param counterParty counterparty
     * @return
     */
    public static boolean counterpartyInList(ObservableList<CounterParty> counterpartiesData, CounterParty counterParty) {
        for (CounterParty t : counterpartiesData) {
            if (counterParty.getId().equals(t.getId())) {
                return true;
            }
        }
        return false;
    }

    private void clear() {
        this.transactions.clear();
        this.transactionsData.clear();
        this.counterparties.clear();
        this.images.clear();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray transactionsArray = new JSONArray();
        JSONArray counterpartiesArray = new JSONArray();
        JSONArray imagesArray = new JSONArray();

        for (Transaction t : this.transactions.values()) {
            transactionsArray.add(t.toJson());
        }
        for (CounterParty c : this.counterparties.values()) {
            counterpartiesArray.add(c.toJson());
        }
        for (CheckImage i : this.images.values()) {
            imagesArray.add(i.toJson());
        }
        json.put(TRANSACTIONS, transactionsArray);
        json.put(COUNTERPARTIES, counterpartiesArray);
        json.put(IMAGES, imagesArray);
        json.put(MOST_RECENT_CHECK_NUMBER,mostRecentCheckNumber);

        return json;
    }

    /**
     * write json object to data file
     *
     * @param dataFile data file
     * @param json json
     * @throws IOException if something ges wrong
     */
    public static void writeJsonToFile(File dataFile, JSONObject json) throws IOException {
        LOGGER.info("Write data to file " + dataFile.getAbsolutePath());
        Json2String json2string = new Json2String();
        FileUtils.writeStringToFile(dataFile,json2string.toPrettyString(json));
    }

    /**
     *
     * @param checkImage
     * @return
     */
    @Override
    public Transaction getTransactionForCheckImage(CheckImage checkImage) {
        for (Transaction transaction : this.transactions.values()) {
            if (transaction.hasImageFront()) {
                if (transaction.getImageFront().equals(checkImage)) {
                    return transaction;
                }
            }
            if (transaction.hasImageBack()) {
                if (transaction.getImageBack().equals(checkImage)) {
                    return transaction;
                }
            }
        }
        return null;
    }

    /**
     * tell if dao contains transaction of given id in transactions data
     *
     * @param transactionId transaction id
     * @return if dao contains transaction
     */
    @Override
    public boolean transactionsDataContains(String transactionId) {
        for (Transaction t : this.transactionsData) {
            if (t.getId().equals(transactionId)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     * @throws IOException if something is wronfg
     */
    @Override
    public File backupToFile() throws IOException {
        LocalDateTime today = LocalDateTime.now();
        File backupFile = new File(dataFile.toString() + "_" + LocalDateUtils.toYYYYMMDDHHMMSS(today));
        backupToFile(backupFile);
        return backupFile;
    }

    /**
     *
     * @param backupFile
     * @throws IOException
     */
    @Override
    public void backupToFile(File backupFile) throws IOException {
        JSONObject json = toJson();
        writeJsonToFile(backupFile, json);
    }

    /**
     *
     * @return
     */
    @Override
    public List<Transaction> getClearedTransactions() {
        List<Transaction> clearedTransactions = new ArrayList<>();
        for (Transaction t : getTransactions().values()) {
            if (t.isCleared()) {
                clearedTransactions.add(t);
            }
        }
        return clearedTransactions;
    }

    @Override
    public void setMostRecentCheckNumber(String checkNumber) {
        this.mostRecentCheckNumber=checkNumber;

    }

    @Override
    public String getMostRecentCheckNumber() {
        return this.mostRecentCheckNumber;
    }

    @Override
    public ObservableList<Transaction> getTransactionsData(CounterParty cp) {
        Set<CounterParty> setOfCounterParties = new HashSet<>();
        setOfCounterParties.add(cp);
        return getTransactionsData(setOfCounterParties);
    }
}
