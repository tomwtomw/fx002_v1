package org.tomw.rachunki.core;

import org.tomw.documentfile.DocumentFile;
import org.tomw.filestoredao.FileDao;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

import java.io.File;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public interface RachunkiDao extends FileDao {

    // ============= Accounts related methods ====//
    Konto getKonto(int id);
    Konto getKontoByOldId(String oldId);
    Collection<Konto> getAllAccounts();
    Collection<Konto> getAllActiveAccounts();
    Collection<Konto> getAllLocalAccounts();
    Collection<Konto> getAllPrimaryAccounts();
    Collection<Konto> getAllActiveLocalAccounts();
    Collection<Konto> getAccountsWhichContainDocument(DocumentFile document);
    Collection<Konto> getAccountsWhereNamesContain(String shortNameFilter, String fullNameFilter);

    Collection<Konto> getAccountsWithShortName(String shortName);
    Collection<Konto> getAccountsWithFullName(String fullName);

    void save(Konto konto);
    void saveAccounts(Collection<Konto> kontoCollection);
    void delete(Konto konto);
    void deleteKonto(int id);

    // =========== Transactions methods =============/
    Transakcja getTransakcja(int id);
    Transakcja getTransakcjaByOldId(String oldId);
    Transakcja getTransactionForCheckDocument(CheckDocument checkDocument);
    Collection<Transakcja> getAllTransactions(); //TODO remove this method one day
    void save(Transakcja transakcja);
    void saveTransactions(Collection<Transakcja> transactions);
    void delete(Transakcja transakcja);
    void deleteTransakcja(int id);

    Map<Integer,Transakcja> getTransactionsWhereBuyerIsAccount(Konto konto);
    Map<Integer,Transakcja> getTransactionsWhereSellerIsAccount(Konto konto);
    Map<Integer,Transakcja> getTransactionsForAccount(Konto konto);
    Map<Integer,Transakcja> getTransactionsBetweenAccounts(Konto account1, Konto account2);
    Map<Integer,Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate);
    Map<Integer,Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate);
    Collection<Transakcja> getTransactionsForCheckNumber(String checkNumber);

    Collection<Transakcja> getTransactinsWhichContain(DocumentFile document);


    // ==== Check document methods =============//
    CheckDocument getCheckDocument(int id);
    Collection<CheckDocument> getCheckDocumentsInvolvingAccount(Konto konto);
    Collection<CheckDocument> getAllCheckDocumentsWithCheckNumber(String checkNumber);
    Collection<CheckDocument>  getAllCheckDocuments();
    void save(CheckDocument checkDocument);
    void saveCheckDocuments(Collection<CheckDocument> checkDocuments);
    void delete(CheckDocument checkDocument);
    void deleteCheckDocument(int id);


    // === DocumentFile File methods =====//
    DocumentFile getDocumentFile(int id);
    Collection<DocumentFile> getAllDocumentFiles();
    Collection<DocumentFile> getDocumentFilesNotPersonalPictures();
    void save(DocumentFile documentFile);
    void saveDocumentFiles(Collection<DocumentFile> documentFiles);
    void delete(DocumentFile documentFile);
    void deleteDocumentFile(int id);
    DocumentFile upload(File documentToBeUploaded);
    DocumentFile upload(File documentToBeUploaded, boolean deleteSource);
    File download(File destination, DocumentFile documentFile);


    Integer getNumberOfTransactionsForAccount(Konto konto);

    void flush();
    void close();


    Map<Integer, Transakcja> getTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter);



}
