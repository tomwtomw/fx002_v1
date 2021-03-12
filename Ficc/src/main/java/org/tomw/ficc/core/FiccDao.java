package org.tomw.ficc.core;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by tomw on 7/22/2017.
 */
public interface FiccDao {

    public List<FiccTransaction> getAllFiccTransactions();
    public List<FiccCounterParty> getAllFiccCounterParties();

    public ObservableList<FiccTransaction> getAllFiccTransactionsAsObservableList();
    public ObservableList<FiccCounterParty> getAllFiccCounterPartiesAsObservableList();

    public  ObservableList<FiccTransaction> getAllFiccTransactionsForCounterPartyAsObservableList(FiccCounterParty cp);

    public FiccTransaction getFiccTransaction(String id);
    public FiccTransaction getFiccTransactionWithMemo(String memo);
    public void deleteFiccTransaction(String id);
    public void deleteFiccTransaction(FiccTransaction f);
    public void add(FiccTransaction f);
    public void addFiccTransactions(List<FiccTransaction> list);

    public boolean containsFiccTransaction(FiccTransaction t);
    public boolean containsFiccTransaction(String id);
    public boolean containsFiccTransactionDoNotCompareById(FiccTransaction t);
    public boolean containsFiccTransactionWithMemo(String memo);

    public boolean containsFiccCounterParty(FiccCounterParty c);
    public boolean containsFiccCounterParty(String id);
    public boolean containsFiccCounterPartyDoNotCompareById(FiccCounterParty c);

    public FiccCounterParty getFiccCounterParty(String id);
    public FiccCounterParty getFiccCounterPartyByName(String name);
    public void deleteFiccCounterParty(String id) throws FiccException;
    public void deleteFiccCounterParty(FiccCounterParty c) throws FiccException;
    public void add(FiccCounterParty c);
    public void addFiccCounterParties(List<FiccCounterParty> list);
    public boolean containsTransactionForCounterParty(FiccCounterParty c);
    public boolean containsTransactionForCounterParty(String cptyId);


    public List<FiccTransaction> getTransactions(FiccCounterParty c);

    public List<FiccTransaction> getTransactionsBefore(LocalDate date);
    public List<FiccTransaction> getTransactionsAfter(LocalDate date);
    public List<FiccTransaction> getTransactionsBetween(LocalDate dateAfter, LocalDate dateBefore);
    public List<FiccTransaction> getTransactionsBetween(FiccCounterParty cp, LocalDate dateAfter, LocalDate dateBefore);
    public List<FiccTransaction> getTransactionsBetween(List<FiccCounterParty> listOfCounterParties, LocalDate dateAfter, LocalDate dateBefore);

    public void commit() throws IOException;
}
