package org.tomw.rachunki.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.tomw.daoutils.DataIntegrityException;
import org.tomw.documentfile.DocumentFile;
import org.tomw.identifiable.IdentifiableUtils;
import org.tomw.mediautils.MediaFileUtils;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.entities.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RachunkiServiceImpl implements RachunkiService {
    private final static Logger LOGGER = Logger.getLogger(RachunkiServiceImpl.class.getName());

    private ObservableList<Konto> listOfDisplayedAccounts = FXCollections.observableArrayList();

    private ObservableList<Transakcja> listOfDisplayedTransactions = FXCollections.observableArrayList();

    private ObservableList<DocumentFile> listOfDisplayedDocuments = FXCollections.observableArrayList();
    private ObservableList<CheckDocument> listOfDisplayedCheckDocuments = FXCollections.observableArrayList();

    private RachunkiDao dao;
    private RachunkiConfiguration config;

    private KontoShortNameComparator kontoShortNameComparator = new KontoShortNameComparator();

    public RachunkiServiceImpl(RachunkiDao dao, RachunkiConfiguration config) {
        this.dao = dao;
        this.config = config;
    }

    @Override
    public CheckDocument getCheckDocument(int id) {
        return dao.getCheckDocument(id);
    }

    @Override
    public CheckDocument createCheck(String checkNumber, String comment, String memo, File imageFront, File imageBack, boolean deleteSourceFiles) {
        CheckDocument check = new CheckDocument();
        check.setCheckNumber(checkNumber);
        check.setComment(comment);
        check.setMemo(memo);

        if (imageFront != null) {
            DocumentFile imageFrontDocument = dao.upload(imageFront, deleteSourceFiles);
            save(imageFrontDocument);
            check.setCheckImageFront(imageFrontDocument);
        }
        if (imageBack != null) {
            DocumentFile imageBackDocument = dao.upload(imageBack, deleteSourceFiles);
            save(imageBackDocument);
            check.setCheckImageBack(imageBackDocument);
        }
        save(check);

        return check;
    }

    @Override
    public ObservableList<CheckDocument> getAllCheckDocuments() {
        ObservableList<CheckDocument> listOfcheckDocuments = FXCollections.observableArrayList();
        listOfcheckDocuments.addAll(this.dao.getAllCheckDocuments());
        return listOfcheckDocuments;
    }

    @Override
    public ObservableList<CheckDocument> getAllCheckDocumentsWithCheckNumber(String checkNumber) {
        ObservableList<CheckDocument> listOfcheckDocuments = FXCollections.observableArrayList();
        listOfcheckDocuments.addAll(this.dao.getAllCheckDocumentsWithCheckNumber(checkNumber));
        return listOfcheckDocuments;
    }

    @Override
    public ObservableList<CheckDocument> getCheckDocuments(Konto konto) {
        ObservableList<CheckDocument> listOfcheckDocuments = FXCollections.observableArrayList();
        Collection<CheckDocument> list = this.dao.getCheckDocumentsInvolvingAccount(konto);
        listOfcheckDocuments.addAll(list);
        return listOfcheckDocuments;
    }

    @Override
    public Collection<CheckDocument> getDisplayedCheckDocuments() {
        return listOfDisplayedCheckDocuments;
    }


    @Override
    public void displayCheckDocuments(Collection<CheckDocument> checkDocuments) {
        listOfDisplayedCheckDocuments.addAll(checkDocuments);
    }

    @Override
    public void displayCheckDocuments(ObservableList<CheckDocument> checkDocuments) {
        listOfDisplayedCheckDocuments.addAll(checkDocuments);
    }

    @Override
    public void displayAllCheckDocuments() {
        clearDisplayedCheckDocuments();
        displayCheckDocuments(this.dao.getAllCheckDocuments());
    }

    @Override
    public void clearDisplayedCheckDocuments() {
        listOfDisplayedCheckDocuments.clear();
    }

    @Override
    public ObservableList<DocumentFile> getDocuments(Konto konto) {
        ObservableList<DocumentFile> list = FXCollections.observableArrayList();
        list.addAll(konto.getDocuments());
        return list;
    }

    @Override
    public ObservableList<DocumentFile> getDocuments(Transakcja transakcja) {
        ObservableList<DocumentFile> list = FXCollections.observableArrayList();
        list.addAll(transakcja.getDocuments());
        return list;
    }

    @Override
    public Collection<Konto> getAccountsWhichContain(DocumentFile documentFile) {
        return dao.getAccountsWhichContainDocument(documentFile);
    }

    @Override
    public Collection<Transakcja> getTransactionsWhichContain(DocumentFile documentFile) {
        return dao.getTransactinsWhichContain(documentFile);
    }


    @Override
    public Transakcja getTransaction(CheckDocument checkDocument) {
        return this.dao.getTransactionForCheckDocument(checkDocument);
    }

    @Override
    public void save(CheckDocument checkDocument) {
        if (checkDocument.getCheckImageFront() != null) {
            save(checkDocument.getCheckImageFront());
        }
        if (checkDocument.getCheckImageBack() != null) {
            save(checkDocument.getCheckImageBack());
        }
        dao.save(checkDocument);
    }

    @Override
    public void delete(CheckDocument checkDocument) {
        dao.delete(checkDocument); //TODO remove document from entries
    }

    @Override
    public void deleteCheckDocument(int id) {
        dao.deleteCheckDocument(id);
    }

    @Override
    public boolean isValid(Transakcja transakcja) {
        if (transakcja == null) {
            return false;
        }
        if (transakcja.getSeller() == null) {
            return false;
        }
        if (transakcja.getBuyer() == null) {
            return false;
        }
        if (transakcja.getTransactionDate() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Konto getAccount(int id) {
        return this.dao.getKonto(id);
    }

    @Override
    public ObservableList<Konto> getAllAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        list.addAll(this.dao.getAllAccounts());
        return list;
    }

    @Override
    public ObservableList<Konto> getAllActiveAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        list.addAll(this.dao.getAllActiveAccounts());
        return list;
    }

    @Override
    public ObservableList<Konto> getAllLocalAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        list.addAll(this.dao.getAllLocalAccounts());
        return list;
    }

    @Override
    public ObservableList<Konto> getAllActiveLocalAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        list.addAll(this.dao.getAllActiveLocalAccounts());
        return list;
    }

    @Override
    public ObservableList<Konto> getDisplayedAccounts() {
        return this.listOfDisplayedAccounts;
    }

    @Override
    public ObservableList<Konto> getDisplayedLocalAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        for (Konto konto : getDisplayedAccounts()) {
            if (konto.isAccountLocal()) {
                list.add(konto);
            }
        }
        return list;
    }

    @Override
    public ObservableList<Konto> getDisplayedPrimaryAccounts() {
        ObservableList<Konto> list = FXCollections.observableArrayList();
        for (Konto konto : getDisplayedAccounts()) {
            if (konto.isAccountPrimary()) {
                list.add(konto);
            }
        }
        return list;
    }

    @Override
    public ObservableList<Konto> getAccountsWithShortName(String shortName) {
        shortName = shortName.trim();
        Collection<Konto> selectedAccounts = this.dao.getAccountsWithShortName(shortName);
        ObservableList<Konto> result = FXCollections.observableArrayList();
        for (Konto account : selectedAccounts) {
            if (shortName.equals(account.getShortName())) {
                result.add(account);
            }
        }
        return result;
    }

    @Override
    public ObservableList<Konto> getAccountsWithFullName(String fullName) {
        fullName = fullName.trim();
        Collection<Konto> selectedAccounts = this.dao.getAccountsWithFullName(fullName);
        ObservableList<Konto> result = FXCollections.observableArrayList();
        for (Konto account : selectedAccounts) {
            if (fullName.equals(account.getFullName())) {
                result.add(account);
            }
        }
        return result;
    }

    @Override
    public ObservableList<Konto> getAllAccountsWhichAreTradingWith(Konto account) {
        Map<Integer,Transakcja> transactionsForAccount = this.dao.getTransactionsForAccount(account);

       Set<Konto> result = new HashSet<>();

        for(Transakcja transakcja : transactionsForAccount.values()){
            if(transakcja.getBuyer()!=null && !transakcja.getBuyer().equals(account)){
                result.add(transakcja.getBuyer());
            }
            if(transakcja.getSeller()!=null && !transakcja.getSeller().equals(account)){
                result.add(transakcja.getSeller());
            }
        }
        // let us convert result from set to observable list

        ObservableList<Konto> resultWithNoRepetitions = FXCollections.observableArrayList();
        for(Konto konto : result){
            resultWithNoRepetitions.add(konto);
        }

        return resultWithNoRepetitions;
    }

    @Override
    public ObservableList<Konto> getPrimaryAccounts() {
        Collection<Konto> primaryAccounts = this.dao.getAllPrimaryAccounts();

        ObservableList<Konto> result = FXCollections.observableArrayList();
        for(Konto konto : primaryAccounts){
            result.add(konto);
        }

        return result;
    }

    @Override
    public MergeAccountsResult mergeAccounts(Konto account1, Konto account2, Konto newAccount) {

        MergeAccountsResult result = new MergeAccountsResult();

        try {
            // first copy the documents from old accounts to new one
            combineDocuments(account1, account2, newAccount);
            save(account1);
            save(account2);
            save(newAccount);

            int nTransactions1 = replaceAccount(account1, newAccount);
            int nTransactions2 = replaceAccount(account2, newAccount);

            result.setnTransactions(nTransactions1+nTransactions2);

            delete(account1);
            delete(account2);
            save(newAccount);
            result.setOk(true);
        } catch (Exception e) {
            LOGGER.error("Failed to merge accounts " + account1 + " and " + account2, e);
            result.setOk(false);
        }
        return result;
    }

    private int replaceAccount(Konto account, Konto newAccount) {
        Collection<Transakcja> transactions = this.getTransactionsForAccount(account);
        int numberOfTransactionsmodified = 0;
        for (Transakcja transakcja : transactions) {
            if (transakcja.getBuyer().equals(account)) {
                transakcja.setBuyer(newAccount);
                numberOfTransactionsmodified = numberOfTransactionsmodified + 1;
            }
            if (transakcja.getSeller().equals(account)) {
                transakcja.setSeller(newAccount);
                numberOfTransactionsmodified = numberOfTransactionsmodified + 1;
            }
        }
        return numberOfTransactionsmodified;
    }

    @Override
    public void combineDocuments(Konto account1, Konto account2, Konto newAccount) throws IOException {
        for(DocumentFile df : account1.getDocuments()){
            DocumentFile dfCloned = clone(df);
            newAccount.getDocuments().add(dfCloned);
        }
        account1.getDocuments().clear();
        save(account1);

        for(DocumentFile df : account2.getDocuments()){
            DocumentFile dfCloned = clone(df);
            newAccount.getDocuments().add(dfCloned);
        }
        account2.getDocuments().clear();
        save(account2);

        save(newAccount);
    }

    @Override
    public void displayAllAccounts() {
        clearDisplayedAccounts();
        displayAccounts(this.dao.getAllAccounts());
    }

    @Override
    public void displayAccounts(Collection<Konto> accounts) {
        this.listOfDisplayedAccounts.clear();
        this.listOfDisplayedAccounts.addAll(accounts);
        Collections.sort(
                this.listOfDisplayedAccounts,
                kontoShortNameComparator
        );
    }

    @Override
    public void displayAccounts(ObservableList<Konto> accounts) {
        clearDisplayedAccounts();
        this.listOfDisplayedAccounts.addAll(accounts);
    }

    @Override
    public void clearDisplayedAccounts() {
        this.listOfDisplayedAccounts.clear();
    }

    /**
     * Display accounts where short and full names contain the privided strings.
     * If those strings are blank or empty, ignore them
     *
     * @param shortNameFilter filter on short name. Account short name must contain this substring.
     * @param fullNameFilter  Account full name must contain this substring.
     */
    @Override
    public void displayAccountsUsingFilters(String shortNameFilter, String fullNameFilter) {
        Collection<Konto> selectedAccounts = this.dao.getAccountsWhereNamesContain(shortNameFilter, fullNameFilter);
        displayAccounts(selectedAccounts);
    }

    @Override
    public void displayAccountsWhichPreviouslyTradedWithAccount(Konto account) {
        Collection<Konto> selectedAccounts = getAllAccountsWhichAreTradingWith(account);
        displayAccounts(selectedAccounts);
    }

    @Override
    public void displayPrimaryAccounts(Konto account) {
        // the account parameter is left in case we would like to remove the account from the final list
        Collection<Konto> selectedAccounts = getPrimaryAccounts();
        displayAccounts(selectedAccounts);
    }


    @Override
    public ObservableList<Transakcja> getTransactionsForAccount(Konto konto) {
        ObservableList<Transakcja> resultList = FXCollections.observableArrayList();
        resultList.addAll(dao.getTransactionsForAccount(konto).values());
        return resultList;
    }

    @Override
    public ObservableList<Transakcja> getTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter) {
        ObservableList<Transakcja> resultList = FXCollections.observableArrayList();
        resultList.addAll(dao.getTransactionsForAccountHavingCheckNumberAndCpty(
                konto,
                checkNumberFilter,
                counterPartyShortNameFilter
        ).values());
        return resultList;
    }

    @Override
    public void displayTransactionsForAccount(Konto konto) {
        this.listOfDisplayedTransactions.clear();
        this.listOfDisplayedTransactions.addAll(getTransactionsForAccount(konto));
    }

    @Override
    public void clearDisplayedTransactions() {
        this.listOfDisplayedTransactions.clear();
    }

    @Override
    public void save(Transakcja transakcja) {
        // validate transaction first
        // TODO this will break unit tests. They need to be fixed.
        if (isValid(transakcja)) {

            if (transakcja.getBuyer() != null) {
                this.dao.save(transakcja.getBuyer());
            }
            if (transakcja.getSeller() != null) {
                this.dao.save(transakcja.getSeller());
            }
            for (DocumentFile df : transakcja.getDocuments()) {
                this.dao.save(df);
            }
            this.dao.save(transakcja);
        } else {
            throw new RuntimeException("Transaction cannot be saved: " + transakcja);
        }
    }

    @Override
    public ObservableList<Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate) {
        ObservableList<Transakcja> resultList = FXCollections.observableArrayList();
        resultList.addAll(dao.getTransactionsForAccount(konto, startDate).values());
        return resultList;
    }

    @Override
    public void displayTransactionsForAccount(Konto konto, LocalDate startDate) {
        this.listOfDisplayedTransactions.clear();
        this.listOfDisplayedTransactions.addAll(getTransactionsForAccount(konto, startDate));
    }

    @Override
    public void displayTransactions(Collection<Transakcja> transactions) {
        clearDisplayedTransactions();
        this.listOfDisplayedTransactions.addAll(transactions);
    }

    @Override
    public void displayTransactions(ObservableList<Transakcja> transactions) {
        clearDisplayedTransactions();
        this.listOfDisplayedTransactions.addAll(transactions);
    }

    @Override
    public ObservableList<Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate) {
        ObservableList<Transakcja> resultList = FXCollections.observableArrayList();
        Map<Integer, Transakcja> result = dao.getTransactionsForAccount(konto, startDate, endDate);
        resultList.addAll(result.values());
        return resultList;
    }

    @Override
    public ObservableList<Transakcja> getTransactionsBetweenAccounts(Konto account1, Konto account2) {
        ObservableList<Transakcja> resultList = FXCollections.observableArrayList();
        Map<Integer, Transakcja> result = dao.getTransactionsBetweenAccounts(account1, account2);
        resultList.addAll(result.values());
        return resultList;
    }

    @Override
    public ObservableList<Transakcja> getDisplayedTransactions() {
        return this.listOfDisplayedTransactions;
    }

    @Override
    public void displayTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate) {
        this.listOfDisplayedTransactions.clear();
        this.listOfDisplayedTransactions.addAll(getTransactionsForAccount(konto, startDate, endDate));
    }

    @Override
    public void displayTransactionsBetweenAccounts(Konto account1, Konto account2) {
        this.listOfDisplayedTransactions.clear();
        this.listOfDisplayedTransactions.addAll(getTransactionsBetweenAccounts(account1, account2));
    }

    @Override
    public void displayTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter) {
        this.listOfDisplayedTransactions.clear();
        Collection<Transakcja> transactionsToDisplay = getTransactionsForAccountHavingCheckNumberAndCpty(
                konto,
                checkNumberFilter,
                counterPartyShortNameFilter);
        List<Transakcja> listOfTransactions = new ArrayList<>();
        listOfTransactions.addAll(transactionsToDisplay);
        Collections.sort(listOfTransactions, TransakcjaComparators.dateComparator);
        this.listOfDisplayedTransactions.addAll(listOfTransactions);
    }

    @Override
    public void save(Konto konto) {
        for (DocumentFile df : konto.getDocuments()) {
            dao.save(df);
        }
        dao.save(konto);
        if (!IdentifiableUtils.contains(getDisplayedAccounts(), konto)) {
            getDisplayedAccounts().add(konto);
        }
    }

    @Override
    public Collection<Konto> getAccounts(Collection<Integer> ids) {
        //TODO eliminate duplicates
        List<Konto> result = new ArrayList<>();
        for (Integer id : ids) {
            Konto konto = dao.getKonto(id.intValue());
            if (konto != null) {
                result.add(konto);
            }
        }
        return result;
    }

    @Override
    public Transakcja getTransakcja(int id) {
        return dao.getTransakcja(id);
    }

    @Override
    public Transakcja getTransakcjaByOldId(String oldId) {
        return dao.getTransakcjaByOldId(oldId);
    }

    @Override
    public Collection<Transakcja> getTransactions(Collection<Integer> ids) {
        List<Transakcja> result = new ArrayList<>();
        for (Integer id : (new HashSet<>(ids))) {
            Transakcja transakcja = dao.getTransakcja(id);
            if (transakcja != null) {
                result.add(transakcja);
            }
        }
        return result;
    }

    @Override
    public ObservableList<Transakcja> getTransactionsForCheckNumber(String checkNumber) {
        ObservableList<Transakcja> result = FXCollections.observableArrayList();
        result.addAll(dao.getTransactionsForCheckNumber(checkNumber));
        return result;
    }

    @Override
    public void delete(Transakcja transakcja) {
        // TODO add here logis to remove dependencies on check numbers, documents etc.
        dao.delete(transakcja);
        if (isDisplayed(transakcja)) {
            removeFromDisplay(transakcja);
        }
    }

    @Override
    public void deleteTransakcjaById(int id) {
        delete(dao.getTransakcja(id));
    }

    @Override
    public void delete(Konto konto) throws RachunkiException {
        dao.delete(konto);
        if (IdentifiableUtils.contains(getDisplayedAccounts(), konto)) {
            IdentifiableUtils.remove(getDisplayedAccounts(), konto);
        }
    }

    @Override
    public void deleteKontoById(int id) throws RachunkiException {
        delete(dao.getKonto(id));
    }

    @Override
    public Konto getKontoWithOldId(String id) {
        return dao.getKontoByOldId(id);
    }

    @Override
    public DocumentFile getDocumentFile(int id) {
        return dao.getDocumentFile(id);
    }

    @Override
    public void delete(DocumentFile documentFile) throws DataIntegrityException {
        dao.delete(documentFile);
    }

    @Override
    public Image getImage(DocumentFile documentFile) {
        if (documentFile != null) {
            if (documentFile.getInternalFileName() != null && MediaFileUtils.isImage(documentFile.getInternalFileName())) {
                File imageFile = this.dao.getPathToFile(documentFile.getInternalFileName());
                return new Image(imageFile.toURI().toString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean internalFileExists(DocumentFile documentFile) {
        if (documentFile != null) {
            File internalFile = this.dao.getPathToFile(documentFile.getInternalFileName());
            if(internalFile==null){
                return false;
            }else {
                return internalFile.exists();
            }
        } else {
            return false;
        }
    }

    @Override
    public long internalFileSize(DocumentFile documentFile) {
        if(internalFileExists(documentFile)){
            File internalFile = this.dao.getPathToFile(documentFile.getInternalFileName());
            return internalFile.length();
        }else{
            return -1L;
        }
    }

    @Override
    public boolean canBeDisplayed(DocumentFile documentFile) {
        if (documentFile != null && documentFile.getInternalFileName() != null &&
                MediaFileUtils.isImage(dao.getPathToFile(documentFile.getInternalFileName()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void displayAllDocuments() {
        displayDocumentFiles(dao.getAllDocumentFiles());
    }

    @Override
    public void displayAllDocumentsNotPersonalPicture() {
        displayDocumentFiles(dao.getDocumentFilesNotPersonalPictures());
    }

    @Override
    public void displayDocumentsForAccount(Konto konto) {
        displayOnly(konto.getDocuments());
    }

    @Override
    public void displayDocumentsForTransaction(Transakcja transakcja) {
        displayOnly(transakcja.getDocuments());
    }

    @Override
    public void displayOnly(Collection<DocumentFile> documents) {
        clearDisplayedDocuments();
        this.listOfDisplayedDocuments.addAll(documents);
    }

    @Override
    public void displayDocumentFiles(Collection<DocumentFile> documents) {
        displayOnly(documents);
    }

    @Override
    public ObservableList<DocumentFile> getListOfDisplayedDocuments() {
        return this.listOfDisplayedDocuments;
    }

    @Override
    public void save(DocumentFile documentFile) {
        dao.save(documentFile);
        if (!IdentifiableUtils.contains(getListOfDisplayedDocuments(), documentFile)) {
            getListOfDisplayedDocuments().add(documentFile);
        }
    }

    @Override
    public void removeFromDisplay(DocumentFile currentEntity) {
        Iterator iter = this.listOfDisplayedDocuments.iterator();
        while (iter.hasNext()) {
            DocumentFile currentdocument = (DocumentFile) iter.next();
            if (currentEntity.getId() == currentdocument.getId()) {
                iter.remove();
            }
        }
    }

    @Override
    public void clearDisplayedDocuments() {
        listOfDisplayedDocuments.clear();
    }

    @Override
    public File uploadFile(DocumentFile documentFile, File externalFile) throws IOException {
        return uploadFile(documentFile, externalFile, false);
    }

    @Override
    public File uploadFile(DocumentFile documentFile, File externalFile, boolean deleteSource) throws IOException {
        return uploadFile(documentFile, externalFile, deleteSource, false);
    }

    @Override
    public File uploadFile(DocumentFile documentFile, File externalFile, boolean deleteSource, boolean overwriteDestination) throws IOException {
        String externalFileName = FilenameUtils.getName(externalFile.toString());
        String externalFileExtension = FilenameUtils.getExtension(externalFile.toString());

        //String internalName = documentFile.getId() + "." + externalFileExtension;
        String internalName = this.dao.internalNameWithInternalDirectory(
                documentFile.getId(),
                externalFileExtension);

        // second order of business is to copy file to local area
        dao.upload(externalFile, internalName);

        // if we are here then the copy operation was successful

        documentFile.setExternalFileName(externalFileName);
        documentFile.setInternalFileName(internalName);
        documentFile.setUploadDateTime(LocalDateTime.now());

        save(documentFile);
        // verify if save was successful
        DocumentFile df2 = dao.getDocumentFile(documentFile.getId());
        if (df2 == null) {
            String message = "Failed to save to database entity " + documentFile;
            LOGGER.error(message);
            throw new IOException(message);
        }
        if (deleteSource) {
            LOGGER.info("Delete source file " + externalFile.toString());
            externalFile.delete();
        }

        return dao.getPathToFile(internalName);
    }

    @Override
    public File downloadFile(DocumentFile documentFile, File destinationDirectory) throws IOException {
        File destinationFile = null;

        if(internalFileExists(documentFile)) {
            if (destinationDirectory != null &&
                    destinationDirectory.exists() && destinationDirectory.isDirectory()) {

                String externalFilename = documentFile.getExternalFileName();
                destinationFile = new File(destinationDirectory, externalFilename);

                this.dao.download(documentFile.getInternalFileName(), destinationFile);
            }
        }

        return destinationFile;
    }

    @Override
    public void deleteFile(String intenalFileName) {
        this.dao.deleteFile(intenalFileName);
    }

    @Override
    public void deleteFileFromDocument(DocumentFile documentFile) {
        if (documentFile.getInternalFileName() != null) {
            deleteFile(documentFile.getInternalFileName());

            documentFile.setExternalFileName(null);
            documentFile.setInternalFileName(null);

            save(documentFile);
        }
    }

    @Override
    public DocumentFile createDocumentFile(File file) {
        return createDocumentFile(file, true);
    }

    @Override
    public DocumentFile createDocumentFile(File file, boolean deleteOriginalFile) {
        DocumentFile documentFile = dao.upload(file, deleteOriginalFile);
        return documentFile;
    }

    @Override
    public DocumentFile clone(DocumentFile document) throws IOException {

        DocumentFile clonedDocument = new DocumentFile();
        save(clonedDocument);

        clonedDocument.setDocumentTitle(document.getDocumentTitle());
        clonedDocument.setDocumentType(document.getDocumentType());
        clonedDocument.setDocumentDescription(document.getDocumentDescription());
        clonedDocument.setComment(document.getComment());

        if(internalFileExists(document)) {
            Path path = Files.createTempDirectory("cloneDocuments");
            File downloadedFile = downloadFile(document, path.toFile());
            uploadFile(clonedDocument, downloadedFile, true);
            path.toFile().delete();
        }

        save(clonedDocument);

        return clonedDocument;
    }

    @Override
    public boolean isDisplayed(Transakcja transaction) {
        return IdentifiableUtils.contains(getDisplayedTransactions(), transaction);
    }

    @Override
    public boolean isDisplayed(Konto konto) {
        return IdentifiableUtils.contains(getDisplayedAccounts(), konto);
    }

    @Override
    public boolean isDisplayed(DocumentFile document) {
        return IdentifiableUtils.contains(getListOfDisplayedDocuments(), document);
    }

    @Override
    public void addToDisplay(Transakcja transaction) {
        if (!isDisplayed(transaction)) {
            getDisplayedTransactions().add(transaction);
        }
    }

    @Override
    public void addToDisplay(Konto konto) {
        if (!isDisplayed(konto)) {
            getDisplayedAccounts().add(konto);
        }
    }

    @Override
    public void addToDisplay(DocumentFile document) {
        if (!isDisplayed(document)) {
            getListOfDisplayedDocuments().add(document);
        }
    }

    @Override
    public void removeFromDisplay(Transakcja transaction) {
        IdentifiableUtils.remove(getDisplayedTransactions(), transaction);
    }

    @Override
    public void removeFromDisplay(Konto konto) {
        IdentifiableUtils.remove(getDisplayedAccounts(), konto);
    }

    @Override
    public Integer getNumberOfTransactionsForAccount(Konto konto) {
        return dao.getNumberOfTransactionsForAccount(konto);
    }

    @Override
    public Collection<Transakcja> getAllTransactions() {
        return dao.getAllTransactions();
    }


}
