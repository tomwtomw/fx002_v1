/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomw
 */
public class AccountsDaoJsonImplTest {
    private Account account1;
    private Account account2;
    private Account account3;
    private AccountsDao dao;
    
    public AccountsDaoJsonImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        CzekiRegistry.applicationDirectory = new File(System.getProperty("user.dir"));
        CzekiRegistry.dataDirectory=CzekiRegistry.applicationDirectory ;
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        File accountsFile = new File("accounts.json");
        if(accountsFile.exists()){
            accountsFile.delete();
        }
    }
    
    @Before
    public void setUp() {
        account1 = new Account();
        account2 = new Account();
        account3 = new Account();
        dao = new AccountsDaoJsonImpl();
        
    }
    
    @After
    public void tearDown() {
        File accountsFile = new File("accounts.json");
        if(accountsFile.exists()){
            accountsFile.delete();
        }
    }

    /**
     * Test of getAllAccounts method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testGetAllAccounts() {
        System.out.println("getAllAccounts");
        
        dao.add(account1);
        dao.add(account2);
        
        Map<String,Account> all = dao.getAllAccounts();
        
        assertTrue(all.containsValue(account1));
        assertTrue(all.containsValue(account2));
        assertFalse(all.containsValue(account3));
        
        assertTrue(all.containsKey(account1.getId()));
        assertTrue(all.containsKey(account2.getId()));
        assertFalse(all.containsKey(account3.getId()));
        
        assertEquals(all.keySet().size(),2);
    }

    /**
     * Test of getAccount method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testGetAccount() {
        System.out.println("getAccount");
        
        dao.add(account1);
        dao.add(account2);
        
        Account actual1 = dao.getAccount(account1.getId());
        assertEquals(actual1,account1);
        
        Account actual3 = dao.getAccount(account3.getId());
        assertEquals(actual3,null);
    }

    /**
     * Test of deleteAccount method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testDeleteAccount_String() {
        System.out.println("deleteAccount");
        dao.add(account1);
        dao.add(account2);
        
        assertEquals(account1,dao.getAccount(account1.getId()));
        
        dao.deleteAccount(account1.getId());
        
        assertEquals(null,dao.getAccount(account1.getId()));
    }

    /**
     * Test of deleteAccount method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testDeleteAccount_Account() {
        System.out.println("deleteAccount");
        dao.add(account1);
        dao.add(account2);
        
        assertEquals(account1,dao.getAccount(account1.getId()));
        
        dao.deleteAccount(account1);
        
        assertEquals(null,dao.getAccount(account1.getId()));
    }

    /**
     * Test of add method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        dao.add(account1);
        dao.add(account2);
        
        assertEquals(null,dao.getAccount(account3.getId()));
        
        dao.add(account3);
        
        assertEquals(account3,dao.getAccount(account3.getId()));
    }

    /**
     * Test of load method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testLoad() {
        System.out.println("load");
        
        // same as testWriteJsonToFile
        AccountsDaoJsonImpl dao2 = new AccountsDaoJsonImpl();
        JSONObject json=new JSONObject();
        json.put(account1.getId(),account1);
        json.put(account2.getId(),account2);
        
        dao2.writeJsonToFile(json);
        
        AccountsDao dao3 = new AccountsDaoJsonImpl();
        
        assertTrue(dao3.containsAccount(account1));
        assertTrue(dao3.containsAccount(account2));
        assertFalse(dao3.containsAccount(account3));
    }

    /**
     * Test of commit method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testCommit() {
        System.out.println("commit");
        
        assertEquals(null,dao.getAccount(account1.getId()));
        assertEquals(null,dao.getAccount(account2.getId()));
        assertEquals(null,dao.getAccount(account3.getId()));
        
        dao.add(account1);
        dao.add(account2);
        
        assertEquals(account1,dao.getAccount(account1.getId()));
        assertEquals(account2,dao.getAccount(account2.getId()));
        assertEquals(null,dao.getAccount(account3.getId()));
        
        dao.add(account1);
        dao.add(account2);
        dao.commit();
        
        AccountsDao dao2 = new AccountsDaoJsonImpl();
        
        assertEquals(account1,dao2.getAccount(account1.getId()));
        assertEquals(account2,dao2.getAccount(account2.getId()));
        assertEquals(null,dao2.getAccount(account3.getId()));
        assertEquals(2,dao2.getAllAccounts().entrySet().size());
        
    }

    /**
     * Test of deleteAll method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testDeleteAll() {
        System.out.println("deleteAll");
        
        dao.add(account1);
        dao.add(account2);
        
        assertEquals(account1,dao.getAccount(account1.getId()));
        assertEquals(account2,dao.getAccount(account2.getId()));
        assertEquals(null,dao.getAccount(account3.getId()));
        
        assertEquals(2,dao.getAllAccounts().entrySet().size());
        
        dao.deleteAll();
        
        assertEquals(null,dao.getAccount(account1.getId()));
        assertEquals(null,dao.getAccount(account2.getId()));
        assertEquals(0,dao.getAllAccounts().entrySet().size());
    }

    /**
     * Test of getAccountsData method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testGetAccountsData() {
        System.out.println("getAccountsData");
        
        ObservableList<Account> accountsData =  dao.getAccountsData();
        assertFalse(AccountsDaoJsonImpl.accountInList(accountsData, account1));
        assertFalse(AccountsDaoJsonImpl.accountInList(accountsData, account2));
        assertFalse(AccountsDaoJsonImpl.accountInList(accountsData, account3));
        
        accountsData.add(account1);
        accountsData.add(account2);
        
        assertTrue(AccountsDaoJsonImpl.accountInList(accountsData, account1));
        assertTrue(AccountsDaoJsonImpl.accountInList(accountsData, account2));
        assertFalse(AccountsDaoJsonImpl.accountInList(accountsData, account3));
    }

    /**
     * Test of containsAccount method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testContainsAccount_String() {
        System.out.println("containsAccount");
        
        dao.add(account1);
        dao.add(account2);
        assertTrue(dao.containsAccount(account1.getId()));
        assertTrue(dao.containsAccount(account2.getId()));
        assertFalse(dao.containsAccount(account3.getId()));
    }

    /**
     * Test of containsAccount method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testContainsAccount_Account() {
        System.out.println("containsAccount");
        dao.add(account1);
        dao.add(account2);
        assertTrue(dao.containsAccount(account1));
        assertTrue(dao.containsAccount(account2));
        assertFalse(dao.containsAccount(account3));
    }

    /**
     * Test of parseJsonString method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testParseJsonString() {
        System.out.println("parseJsonString");
        JSONObject json=new JSONObject();
        json.put(account1.getId(),account1);
        json.put(account2.getId(),account2);
        String fileContent = json.toString();
        
        AccountsDaoJsonImpl daoJson = new AccountsDaoJsonImpl();
        
        List<Account> result = daoJson.parseJsonString(fileContent);
        
        assertTrue(result.contains(account1));
        assertTrue(result.contains(account2));
        assertFalse(result.contains(account3));
    }

    /**
     * Test of writeJsonToFile method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testWriteJsonToFile() {
        System.out.println("writeJsonToFile");
        
        AccountsDaoJsonImpl dao2 = new AccountsDaoJsonImpl();
        JSONObject json=new JSONObject();
        json.put(account1.getId(),account1);
        json.put(account2.getId(),account2);
        
        dao2.writeJsonToFile(json);
        
        AccountsDao dao3 = new AccountsDaoJsonImpl();
        
        assertTrue(dao3.containsAccount(account1));
        assertTrue(dao3.containsAccount(account2));
        assertFalse(dao3.containsAccount(account3));
    }

    /**
     * Test of accountInList method, of class AccountsDaoJsonImpl.
     */
    @Test
    public void testAccountInList() {
        System.out.println("accountInList");
        ObservableList<Account> accountsData =  FXCollections.observableArrayList();
        accountsData.add(account1);
        accountsData.add(account2);
        
        assertTrue(AccountsDaoJsonImpl.accountInList(accountsData, account1));
        assertTrue(AccountsDaoJsonImpl.accountInList(accountsData, account2));
        assertFalse(AccountsDaoJsonImpl.accountInList(accountsData, account3));
    }
    
    
    
}
