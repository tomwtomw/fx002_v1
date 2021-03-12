/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author tomw
 */
public interface AccountsDao {
    public Map<String,Account> getAllAccounts();
    public ObservableList<Account> getAccountsData();
    
    public boolean containsAccount(String id);
    public boolean containsAccount(Account account);
        
    public Account getAccount(String id);
    
    public void deleteAccount(String id);
    
    public void deleteAccount(Account account);
    
    public void add(Account a);
    
    public void deleteAll();
    
    public void commit();
    
    @Override
    public String toString();
}
