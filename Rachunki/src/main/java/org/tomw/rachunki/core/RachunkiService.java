package org.tomw.rachunki.core;

import javafx.collections.ObservableList;
import org.tomw.documentfile.DocumentFile;
import org.tomw.documentfile.DocumentFileService;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

public interface  RachunkiService extends DocumentFileService {

    // check documents
    CheckDocument getCheckDocument(int id);
    CheckDocument createCheck(String checkNumber, String comment, String memo, File imageFront, File imageBack, boolean deleteSourceFiles);
    ObservableList<CheckDocument> getAllCheckDocuments();
    ObservableList<CheckDocument> getAllCheckDocumentsWithCheckNumber(String checkNumber);
    void save(CheckDocument checkDocument);
    void delete(CheckDocument checkDocument);
    void deleteCheckDocument(int id);

    boolean isValid(Transakcja transakcja);

    ObservableList<CheckDocument> getCheckDocuments(Konto konto);
    Collection<CheckDocument> getDisplayedCheckDocuments();
    void displayCheckDocuments(Collection<CheckDocument> checkDocuments);
    void displayCheckDocuments(ObservableList<CheckDocument> checkDocuments);
    void displayAllCheckDocuments();
    void clearDisplayedCheckDocuments();

    // document files
    ObservableList<DocumentFile> getDocuments(Konto konto);
    ObservableList<DocumentFile> getDocuments(Transakcja transakcja);

    Collection<Konto> getAccountsWhichContain(DocumentFile documentFile);
    Collection<Transakcja> getTransactionsWhichContain(DocumentFile documentFile);

    void displayDocumentsForAccount(Konto konto);
    void displayDocumentsForTransaction(Transakcja transakcja);

    // transactions
    Transakcja getTransaction(CheckDocument checkDocument);
    Transakcja getTransakcja(int id);
    Transakcja getTransakcjaByOldId(String oldId);
    Collection<Transakcja> getTransactions(Collection<Integer> ids);
    ObservableList<Transakcja> getTransactionsForCheckNumber(String checkNumber);
    ObservableList<Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate);
    ObservableList<Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate);
    ObservableList<Transakcja> getTransactionsBetweenAccounts(Konto account1, Konto account2);
    ObservableList<Transakcja> getDisplayedTransactions();

    ObservableList<Transakcja> getTransactionsForAccount(Konto konto);
    ObservableList<Transakcja> getTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter);

    void displayTransactions(Collection<Transakcja> transactions);
    void displayTransactions(ObservableList<Transakcja> transactions);
    void displayTransactionsForAccount(Konto konto);
    void displayTransactionsForAccount(Konto konto, LocalDate startDate);
    void displayTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate);

    void displayTransactionsBetweenAccounts(Konto account1, Konto account2);
    void displayTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter);

    void clearDisplayedTransactions();

    void save(Transakcja transakcja);
    void delete(Transakcja transakcja);
    void deleteTransakcjaById(int id);

    // ======== Accounts ==============

    Konto getAccount(int id);
    /**
     * Get account object by looking on its old id, from Czeki project
     * @param id id, as used in Czeki project
     * @return Konto, account object
     */
    Konto getKontoWithOldId(String id);
    void save(Konto konto);
    void delete(Konto konto) throws RachunkiException;
    void deleteKontoById(int id) throws RachunkiException;

    Collection<Konto> getAccounts(Collection<Integer> ids);
    ObservableList<Konto> getAllAccounts();
    ObservableList<Konto> getAllActiveAccounts();
    ObservableList<Konto> getAllLocalAccounts();
    ObservableList<Konto> getAllActiveLocalAccounts();
    ObservableList<Konto> getDisplayedAccounts();
    ObservableList<Konto> getDisplayedLocalAccounts();
    ObservableList<Konto> getDisplayedPrimaryAccounts();

    /**
     * Get all accounts with given short name (ignore trailing whitespaces)
     * @param shortName short name
     * @return list of accounts
     */
    ObservableList<Konto> getAccountsWithShortName(String shortName);
    /**
     * Get all accounts with given full name (ignore trailing whitespaces)
     * @param fullName full name
     * @return list of accounts
     */
    ObservableList<Konto> getAccountsWithFullName(String fullName);

    /**
     * get which account have in the past traded with given account
     * @param account account
     * @return observalble list of accounts which traded with given account
     */
    ObservableList<Konto> getAllAccountsWhichAreTradingWith(Konto account);

    /**
     * get primary accounts
     * @return
     */
    public ObservableList<Konto> getPrimaryAccounts();

    MergeAccountsResult mergeAccounts(Konto leftSelectedAccount, Konto rightSelectedAccount, Konto newAccount);

    /**
     * Take documents for account1,2, combine the lists and give the documents to newAccount. Remove
     * documents from account1,2
     * @param account1 first account
     * @param account2 second account
     * @param newAccount account which should inherit documents from account 1 and 2
     */
    void combineDocuments(Konto account1, Konto account2, Konto newAccount) throws IOException;

    void displayAllAccounts();
    void displayAccounts(Collection<Konto> accounts);
    void displayAccounts(ObservableList<Konto> accounts);
    void clearDisplayedAccounts();
    void displayAccountsUsingFilters(String shortNameFilter, String fullNameFilter);
    void displayAccountsWhichPreviouslyTradedWithAccount(Konto account);
    void displayPrimaryAccounts(Konto account);

    boolean isDisplayed(Transakcja transaction);
    boolean isDisplayed(Konto konto);
    boolean isDisplayed(DocumentFile  document);

    void addToDisplay(Transakcja transaction);
    void addToDisplay(Konto konto);
    void addToDisplay(DocumentFile  document);

    void removeFromDisplay(Transakcja transaction);
    void removeFromDisplay(Konto konto);

    Integer getNumberOfTransactionsForAccount(Konto konto);

    //TODO remove this
    Collection<Transakcja> getAllTransactions();


}
