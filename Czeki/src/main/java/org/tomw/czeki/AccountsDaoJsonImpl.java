/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tomw.czeki.dao.DataFormatException;
import org.tomw.utils.Json2String;

/**
 *
 * @author tomw
 */
public class AccountsDaoJsonImpl implements AccountsDao {

    private final static Logger LOGGER = Logger.getLogger(AccountCrudController.class.getName());
    
    private final String accountsFileName = "accounts.json";
    private final File accountsFile = new File(CzekiRegistry.dataDirectory, accountsFileName);

    private final Map<String, Account> accounts;
    private final ObservableList<Account> accountsData = FXCollections.observableArrayList();

    private final JSONParser jsonParser = new JSONParser();

    public AccountsDaoJsonImpl() {
        this.accounts = new TreeMap<>();
        try {
            this.load();
        } catch (IOException | DataFormatException ex) {            
            LOGGER.error("Failed to start Dao",ex);
        }
    }

    @Override
    public Map<String, Account> getAllAccounts() {
        return this.accounts;
    }

    @Override
    public Account getAccount(String id) {
        return this.accounts.get(id);
    }

    @Override
    public void deleteAccount(String id) {
        this.accounts.remove(id);
        Iterator<Account> iter = this.accountsData.iterator();
        while (iter.hasNext()) {
            Account currentAccount = (Account) iter.next();
            if (currentAccount.getId().equals(id)) {
                iter.remove();
            } else {
            }
        }
    }

    @Override
    public void deleteAccount(Account account) {
        this.accounts.remove(account.getId());
        Iterator<Account> iter = this.accountsData.iterator();
        while (iter.hasNext()) {
            Account currentAccount = (Account) iter.next();
            if (currentAccount.equals(account)) {
                iter.remove();
            } else {
            }
        }
    }
    

    @Override
    public void add(Account a) {
        if (!this.accounts.containsKey(a.getId())) {
            this.accounts.put(a.getId(), a);
        }
        addToListIfNotInList(this.accountsData, a);
    }

    public final void load() throws IOException, DataFormatException {
        createFileIfDoesNotExist();
        String fileContent = this.loadAccountsFileContent();
        List<Account> listOfAccounts = parseJsonString(fileContent);
        for (Account account : listOfAccounts) {
            this.accounts.put(account.getId(), account);
            accountsData.add(account);
        }
    }

    private String loadAccountsFileContent() {
        try {
            String fileContent = FileUtils.readFileToString(accountsFile);
            return fileContent;
        } catch (IOException ex) {
            String message = "Failed to open accounts file " + accountsFile;
            LOGGER.info(message, ex);
            throw new RuntimeException(message);
        }
    }

    @Override
    public void commit() {
        JSONObject json = new JSONObject();
        for (Account account : this.accounts.values()) {
            json.put(account.getId(), account.toJson());
        }
        writeJsonToFile(json);
    }

    private void createFileIfDoesNotExist() {
        if (!accountsFile.exists()) {
            JSONObject json = new JSONObject();
            writeJsonToFile(json);
        }
    }

    @Override
    public void deleteAll() {
        this.accounts.clear();
        this.accountsData.clear();
    }

    @Override
    public ObservableList<Account> getAccountsData() {
        return this.accountsData;
    }

    @Override
    public boolean containsAccount(String id) {
        return this.accounts.containsKey(id);
    }

    @Override
    public boolean containsAccount(Account account) {
        return this.containsAccount(account.getId());
    }
    
    @Override
    public String toString(){
        return "Accounts Dao: Accounts File="+accountsFile.toString();
    }

    public List<Account> parseJsonString(String fileContent) {

        List<Account> result = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) this.jsonParser.parse(fileContent);
            for (Object o : json.keySet()) {
                String id = (String) o;
                JSONObject accountAsJson = (JSONObject) json.get(id);                      
                Account account = Account.fromJson(accountAsJson);
                result.add(account);
            }
            return result;
        } catch (ParseException ex) {
            String message = "Error occured while parsing json string " + fileContent;
            LOGGER.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    public void writeJsonToFile(JSONObject json) {
        try {
            Json2String json2string = new Json2String();
            String prettyString =  json2string.toPrettyString(json.toString());
            LOGGER.info(prettyString);
            FileUtils.writeStringToFile(this.accountsFile, prettyString);
        } catch (IOException ex) {
            String message = "Failed to write json object to file " + accountsFile;
            LOGGER.error(message, ex);
            throw new RuntimeException(message);
        }
    }

    private void addToListIfNotInList(ObservableList<Account> accountsData, Account account) {
         if (!accountInList(accountsData, account)) {
            this.accountsData.add(account);
        }
    }
    
    public static boolean accountInList(ObservableList<Account> accountsData, Account account) {
        for (Account a : accountsData) {
            if (account.getId().equals(a.getId())) {
                return true;
            }
        }
        return false;
    }
}
