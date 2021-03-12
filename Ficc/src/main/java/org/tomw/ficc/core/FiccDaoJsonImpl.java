package org.tomw.ficc.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.tomw.utils.JsonFileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * json implementation of Dao class
 * Created by tomw on 7/22/2017.
 */
public class FiccDaoJsonImpl implements FiccDao {
    private final static Logger LOGGER = Logger.getLogger(FiccDaoJsonImpl.class.getName());

    public static final String TRANSACTIONS = "Transactions";
    public static final String COUNTERPARTIES = "Counterparties";

    private Map<String,FiccCounterParty> counterparties = new HashMap<>();
    private Map<String,FiccTransaction> transactions = new HashMap<>();

    private ObservableList<FiccCounterParty> observableCounterParties = FXCollections.observableArrayList();
    private ObservableList<FiccTransaction> observableTransactions = FXCollections.observableArrayList();
    
    private File storageFile;
    private JsonFileUtils jsonFileUtils = new JsonFileUtils();

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Start json dao which keeps data in disk file storageFile
     * @param storageFileIn file where data is stored
     */
    public FiccDaoJsonImpl(File storageFileIn) throws IOException, ParseException {
        LOGGER.info("Dao file="+storageFileIn);
        storageFile=storageFileIn;
        loadData();
    }

    /**
     * load data from file
     */
    private void loadData() throws IOException, ParseException {
        JSONObject jsonDaoContent = jsonFileUtils.jsonObjectFromFile(storageFile);
        JSONArray jsonCounterparties = (JSONArray)jsonDaoContent.get(COUNTERPARTIES);
        JSONArray jsonTransactions   = (JSONArray)jsonDaoContent.get(TRANSACTIONS);

        if(jsonCounterparties!=null) {
            for (Object o : jsonCounterparties) {
                JSONObject cpJson = (JSONObject) o;
                FiccCounterParty cp = ficcCptyfromJsonObject(cpJson);
                add(cp);
            }
        }

        if(jsonTransactions!=null) {
            for (Object o : jsonTransactions) {
                JSONObject cpJson = (JSONObject) o;
                LOGGER.info("Json from file="+cpJson);
                FiccTransaction tr = ficcTransactionfromJsonObject(cpJson);
                add(tr);
            }
        }
    }

    private FiccTransaction ficcTransactionfromJsonObject(JSONObject json) throws IOException {
        LOGGER.info("json="+json);
        String jsonString = json.toString();
        LOGGER.info("jsonString="+jsonString);
        System.out.println("jsonString="+jsonString);
        FiccTransaction tr = mapper.readValue(jsonString,FiccTransaction.class);
        String cptyId = tr.getCounterPartyId();
        FiccCounterParty cpt = getFiccCounterParty(cptyId);
        tr.setCounterparty(cpt);
        return tr;
    }

    public FiccCounterParty ficcCptyfromJsonObject(JSONObject json) throws IOException {
        return mapper.readValue(json.toString(),FiccCounterParty.class);
    }
    
    private void loadCounterparties() {
    }


    @Override
    public List<FiccTransaction> getAllFiccTransactions() {
        List<FiccTransaction> list = new ArrayList<>();
        list.addAll(transactions.values());
        return list;
    }

    @Override
    public List<FiccCounterParty> getAllFiccCounterParties() {
        List<FiccCounterParty> list = new ArrayList<>();
        list.addAll(counterparties.values());
        return list;
    }

    @Override
    public ObservableList<FiccTransaction> getAllFiccTransactionsAsObservableList() {
        return observableTransactions;
    }

    @Override
    public ObservableList<FiccCounterParty> getAllFiccCounterPartiesAsObservableList() {
        return observableCounterParties;
    }

    //TODO add unit test for this
    @Override
    public ObservableList<FiccTransaction> getAllFiccTransactionsForCounterPartyAsObservableList(FiccCounterParty cp) {
        ObservableList<FiccTransaction> result = FXCollections.observableArrayList();
        for(FiccTransaction t : transactions.values()){
            if(t.getCounterparty().equals(cp)){
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public FiccTransaction getFiccTransaction(String id) {
        return transactions.getOrDefault(id, null);
    }

    @Override
    public FiccTransaction getFiccTransactionWithMemo(String memo) {
        for(FiccTransaction t : getAllFiccTransactions()){
            if(t.getMemo().equals(memo)){
                return t;
            }
        }
        return null;
    }

    @Override
    public void deleteFiccTransaction(String id) {
        transactions.remove(id);
        Iterator iter = observableTransactions.iterator();
        while(iter.hasNext()){
            FiccTransaction t= (FiccTransaction)iter.next();
            if(t.getId().equals(id)){
                iter.remove();
            }
        }
    }

    @Override
    public void deleteFiccTransaction(FiccTransaction f) {
        transactions.remove(f.getId(),f);
        Iterator iter = observableTransactions.iterator();
        while(iter.hasNext()){
            FiccTransaction t= (FiccTransaction)iter.next();
            if(t.getId().equals(f.getId())){
                iter.remove();
            }
        }
    }

    @Override
    public void add(FiccTransaction t) {
        transactions.put(t.getId(),t);
        addToListIfNotInList(this.observableTransactions,t);
        add(t.getCounterparty());
    }

    @Override
    public void addFiccTransactions(List<FiccTransaction> list) {
        //TODO implement
        throw new RuntimeException("Not implemented");

    }

    @Override
    public boolean containsFiccTransaction(FiccTransaction t) {
        return transactions.containsKey(t.getId());
    }

    @Override
    public boolean containsFiccTransaction(String id) {
        return transactions.containsKey(id);
    }

    @Override
    public boolean containsFiccTransactionDoNotCompareById(FiccTransaction t) {
        if(t==null){
            return false;
        }
        return transactions.containsValue(t);
    }

    @Override
    public boolean containsFiccTransactionWithMemo(String memo) {
        for(FiccTransaction t : getAllFiccTransactions()){
            if(t.getMemo().equals(memo)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsFiccCounterParty(FiccCounterParty c) {
        if(c==null){
            return false;
        }
        return counterparties.containsKey(c.getId());
    }

    @Override
    public boolean containsFiccCounterParty(String id) {
        if(id==null){
            return false;
        }
        return counterparties.containsKey(id);
    }

    @Override
    public boolean containsFiccCounterPartyDoNotCompareById(FiccCounterParty c) {
        if(c==null){
            return false;
        }
        return counterparties.containsValue(c);
    }

    @Override
    public FiccCounterParty getFiccCounterParty(String cptId) {
        if(counterparties.containsKey(cptId)){
            return counterparties.get(cptId);
        }else{
            return null;
        }
    }

    @Override
    public FiccCounterParty getFiccCounterPartyByName(String cptName ) {
        if(cptName==null){
            return null;
        }
        for(FiccCounterParty c: counterparties.values()){
            if(cptName.equals(c.getName())){
                return c;
            }
        }
        return null;
    }

    @Override
    public void deleteFiccCounterParty(String id) throws FiccException {
        if(containsTransactionForCounterParty(id)){
            throw new FiccException("Cannot delete counterparty id="+id+" it is used by transactions. Delete transactions first ");
        }
        counterparties.remove(id);
    }

    @Override
    public void deleteFiccCounterParty(FiccCounterParty c) throws FiccException {
        if(containsTransactionForCounterParty(c)){
            throw new FiccException("Cannot delete counterparty "+c+" it is used by transactions. Delete transactions first ");
        }
        counterparties.remove(c.getId(),c);
    }

    @Override
    public void add(FiccCounterParty c) {
        counterparties.put(c.getId(),c);
        addCounterPartyToListIfNotInList(this.observableCounterParties,c);
    }

    @Override
    public void addFiccCounterParties(List<FiccCounterParty> list) {
        for(FiccCounterParty c : list){
            add(c);
        }
    }

    @Override
    public boolean containsTransactionForCounterParty(FiccCounterParty c) {
        for(FiccTransaction t : getAllFiccTransactions()){
            if(t.getCounterparty().equals(c)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsTransactionForCounterParty(String cptyId) {
        for(FiccTransaction t : getAllFiccTransactions()){
            if(t.getCounterparty().getId().equals(cptyId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FiccTransaction> getTransactions(FiccCounterParty c) {
        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getCounterparty().equals(c)){
                result.add(t);
            }
        }
        return result;
    }


    @Override
    public List<FiccTransaction> getTransactionsBefore(LocalDate date) {
        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getDate().isBefore(date)){
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public List<FiccTransaction> getTransactionsAfter(LocalDate date) {
        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getDate().isAfter(date)){
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public List<FiccTransaction> getTransactionsBetween(LocalDate dateAfter, LocalDate dateBefore) {
        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getDate().isAfter(dateAfter) && t.getDate().isBefore(dateBefore)){
                result.add(t);
            }
        }
        return result;
    }

    //TODO add unit test for this
    @Override
    public List<FiccTransaction> getTransactionsBetween(FiccCounterParty cp, LocalDate dateAfter, LocalDate dateBefore) {
        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getDate().isAfter(dateAfter) && t.getDate().isBefore(dateBefore)){
                if(t.getCounterparty().equals(cp)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    //TODO add unit test for this
    @Override
    public List<FiccTransaction> getTransactionsBetween(List<FiccCounterParty> listOfCounterParties, LocalDate dateAfter, LocalDate dateBefore) {
        HashSet<FiccCounterParty> setOfCp = new HashSet<>(listOfCounterParties);

        List<FiccTransaction> result = new ArrayList<>();
        for(FiccTransaction t : transactions.values()){
            if(t.getDate().isAfter(dateAfter) && t.getDate().isBefore(dateBefore)){
                if(setOfCp.contains(t.getCounterparty())) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    @Override
    public void commit() throws IOException {
        JSONArray counterpartiesJson = new JSONArray();
        JSONArray transactionsJson = new JSONArray();
        for(FiccTransaction t : getAllFiccTransactions()){
            transactionsJson.add(t.toJson());
        }
        for(FiccCounterParty c : getAllFiccCounterParties()){
            counterpartiesJson.add(c.toJson());
        }
        JSONObject daoAsJson = new JSONObject();
        daoAsJson.put(TRANSACTIONS,transactionsJson);
        daoAsJson.put(COUNTERPARTIES,counterpartiesJson);

        jsonFileUtils.jsonObject2File(daoAsJson,storageFile);
    }

    @Override
    public String toString(){
        return "Dao.fileName="+storageFile.toString();
    }


    /**
     * Add transaction to list, but check if it is already present. Do not add if present.
     * @param transactionsData list of transactions
     * @param transaction to be added
     */
    public static void addToListIfNotInList(ObservableList<FiccTransaction> transactionsData, FiccTransaction transaction) {
        if (!transactionInList(transactionsData, transaction)) {
            transactionsData.add(transaction);
        }
    }

    /**
     *
     * @param transactionsData list of transactions
     * @param transaction to be checked
     * @return true if transaction in list false otherwise
     */
    public static boolean transactionInList(ObservableList<FiccTransaction> transactionsData, FiccTransaction transaction) {
        for (FiccTransaction t : transactionsData) {
            if (transaction.getId().equals(t.getId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * add counterparty to list if it is not already present in list, do nothing if it is present
     * @param counterpartiesData list of counterparties
     * @param counterParty to be added
     */
    public static void addCounterPartyToListIfNotInList(ObservableList<FiccCounterParty> counterpartiesData, FiccCounterParty counterParty) {
        if (!counterpartyInList(counterpartiesData, counterParty)) {
            counterpartiesData.add(counterParty);
        }
    }

    /**
     *
     * @param counterpartiesData list of counterparties
     * @param counterParty to be checked
     * @return true if counterParty in list counterpartiesData false otherwise
     */
    public static boolean counterpartyInList(ObservableList<FiccCounterParty> counterpartiesData, FiccCounterParty counterParty) {
        for (FiccCounterParty t : counterpartiesData) {
            if (counterParty.getId().equals(t.getId())) {
                return true;
            }
        }
        return false;
    }

}
