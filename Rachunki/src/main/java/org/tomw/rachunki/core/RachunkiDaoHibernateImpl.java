package org.tomw.rachunki.core;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.tomw.documentfile.DocumentFile;
import org.tomw.filestoredao.FileDao;
import org.tomw.hibernate.SessionFactoryFactory;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.SessionFactoryFactoryRachunkiImpl;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RachunkiDaoHibernateImpl implements RachunkiDao{
    private final static Logger LOGGER = Logger.getLogger(RachunkiDaoHibernateImpl.class.getName());

    private SessionFactoryFactory sessionFactoryFactory;

    private SessionFactory sf;
    private Session session;
    private RachunkiConfiguration configuration;

    private FileDao fileDao;

    public FileDao getFileDao() {
        return fileDao;
    }

    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    public RachunkiDaoHibernateImpl(RachunkiConfiguration configuration, FileDao fileDaoInput) {
        LOGGER.info("open dao...");

        this.configuration = configuration;

        sessionFactoryFactory = new SessionFactoryFactoryRachunkiImpl(configuration);
        sf = sessionFactoryFactory.buildSessionFactory();
        session = sf.openSession();

        this.fileDao = fileDaoInput;
    }

    RachunkiDaoHibernateImpl(SessionFactory sfInput, RachunkiConfiguration configuration, FileDao fileDaoInput) {
        LOGGER.info("open dao...");
        sf = sfInput;
        session = sf.openSession();
        this.configuration = configuration;

        fileDao = fileDaoInput;
    }

    @Override
    public Konto getKonto(int id) {
        return session.get(Konto.class, id);
    }

    @Override
    public Konto getKontoByOldId(String oldId) {
        String hql = "from Konto entry where :oldid in elements(entry.oldIds)";
        Query query = session.createQuery(hql);
        query.setParameter("oldid",oldId);
        List<Konto> list = query.getResultList();
        Map<Integer, Konto> result = new HashMap<>();
        for(Object o : list){
            Konto entry = (Konto)o;
            result.put(entry.getId(),entry);
        }
        if(result.size()>1){
            String message = "Something is wrong, more than one Account with old id "+oldId;
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
        if(result.size()==1){
            return result.values().iterator().next();
        }
        return null;
    }

    @Override
    public Collection<Konto> getAllAccounts() {
        List<Konto> result = new ArrayList<>();
        for(Object o : session.createCriteria(Konto.class).list()){
            Konto konto = (Konto)o;
            result.add(konto);
        }
        return result;
    }

    @Override
    public Collection<Konto> getAllActiveAccounts() {
        String hql = "FROM Konto E WHERE E.accountActive is true ";
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Collection<Konto> getAllLocalAccounts() {
        String hql = "FROM Konto E WHERE E.accountLocal is true ";
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Collection<Konto> getAllPrimaryAccounts() {
        String hql = "FROM Konto E WHERE E.accountPrimary is true ";
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Collection<Konto> getAllActiveLocalAccounts() {
        String hql = "FROM Konto E WHERE E.accountLocal is true and E.accountActive is true";
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Collection<Konto> getAccountsWhichContainDocument(DocumentFile document) {
        String hql = "from Konto entry where :document in elements(entry.documents)";
        Query query = session.createQuery(hql);
        query.setParameter("document",document);
        List<Konto> list = query.getResultList();
        return list;
    }

    @Override
    public Collection<Konto> getAccountsWhereNamesContain(String shortNameFilter, String fullNameFilter) {
        String hql = "from Konto E where E.shortName LIKE :shortNameFilter and E.fullName LIKE :fullNameFilter ";
        Query query = session.createQuery(hql);
        query.setParameter("shortNameFilter","%"+shortNameFilter+"%");
        query.setParameter("fullNameFilter","%"+fullNameFilter+"%");
        List<Konto> list = query.getResultList();
        return list;
    }

    @Override
    public Collection<Konto> getAccountsWithShortName(String shortName) {
        shortName=shortName.trim();
        String hql = "from Konto E where E.shortName LIKE :shortName ";
        Query query = session.createQuery(hql);
        query.setParameter("shortName","%"+shortName+"%");
        List<Konto> list = query.getResultList();

        List<Konto> result = new ArrayList<>();
        for(Konto account : list){
            if(shortName.equals(account.getShortName())){
                result.add(account);
            }
        }
        return list;
    }

    @Override
    public Collection<Konto> getAccountsWithFullName(String fullName) {
        fullName=fullName.trim();
        String hql = "from Konto E where E.fullName LIKE :fullName ";
        Query query = session.createQuery(hql);
        query.setParameter("fullName","%"+fullName+"%");
        List<Konto> list = query.getResultList();

        List<Konto> result = new ArrayList<>();
        for(Konto account : list){
            if(fullName.equals(account.getShortName())){
                result.add(account);
            }
        }
        return list;
    }

    @Override
    public void save(Konto konto) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(konto);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to save entity " + konto, e);
        }
    }

    @Override
    public void saveAccounts(Collection<Konto> kontoCollection) {
        for(Konto konto : kontoCollection){
            save(konto);
        }
    }

    @Override
    public void delete(Konto konto) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(konto);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to delete entity " + konto, e);
        }
    }

    @Override
    public void deleteKonto(int id) {
        Konto konto = getKonto(id);
        if(konto!=null){
            delete(konto);
        }
    }

    @Override
    public Transakcja getTransakcja(int id) {
        return session.get(Transakcja.class, id);
    }

    @Override
    public Transakcja getTransakcjaByOldId(String oldId) {
        String hql = "from Transakcja entry where :oldid in elements(entry.oldIds)";
        Query query = session.createQuery(hql);
        query.setParameter("oldid",oldId);
        List<Transakcja> list = query.getResultList();
        Map<Integer, Transakcja> result = new HashMap<>();
        for(Object o : list){
            Transakcja entry = (Transakcja)o;
            result.put(entry.getId(),entry);
        }
        if(result.size()>1){
            String message = "Something is wrong, more than one Account with old id "+oldId;
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
        if(result.size()==1){
            return result.values().iterator().next();
        }
        return null;
    }

    @Override
    public Transakcja getTransactionForCheckDocument(CheckDocument checkDocument) {
        String hql = "FROM Transakcja E WHERE E.checkDocument = :checkDocument";
        Query query = session.createQuery(hql);
        query.setParameter("checkDocument",checkDocument);
        List<Transakcja> result =  query.getResultList();
        if(result==null || result.size()==0){
            return null;
        }else {
            if (result.size() == 1) {
                return result.get(0);
            } else {
                String message = "Received more than one transaction for check document " + checkDocument;
                LOGGER.error(message);
                throw new RuntimeException(message);
            }
        }
    }

    @Override
    public Collection<Transakcja> getAllTransactions() {
        List<Transakcja> result = new ArrayList<>();
        for(Object o : session.createCriteria(Transakcja.class).list()){
            result.add((Transakcja)o);
        }
        return result;
    }

    @Override
    public void save(Transakcja transakcja) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(transakcja);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to save entity " + transakcja, e);
        }
    }

    @Override
    public void saveTransactions(Collection<Transakcja> transactions) {
        for(Transakcja t : transactions){
            save(t);
        }
    }

    @Override
    public void delete(Transakcja transakcja) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(transakcja);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to delete entity " + transakcja, e);
        }
    }

    @Override
    public void deleteTransakcja(int id) {
        Transakcja transakcja = getTransakcja(id);
        if(transakcja!=null){
            delete(transakcja);
        }
    }

    @Override
    public CheckDocument getCheckDocument(int id) {
        return session.get(CheckDocument.class,id);
    }

    @Override
    public Collection<CheckDocument> getCheckDocumentsInvolvingAccount(Konto konto) {
        List<CheckDocument> result = new ArrayList<>();
        Map<Integer,Transakcja> transactions = getTransactionsForAccount(konto);
        for(Transakcja t : transactions.values()){
            if(t.getCheckDocument()!=null){
                result.add(t.getCheckDocument());
            }
        }
        return result;
    }

    @Override
    public Collection<CheckDocument> getAllCheckDocumentsWithCheckNumber(String checkNumber) {
        String hql = "FROM CheckDocument E WHERE E.checkNumber = :checkNumber";
        Query query = session.createQuery(hql);
        query.setParameter("checkNumber",checkNumber);
        List<CheckDocument> result =  query.getResultList();
        return result;
    }

    @Override
    public Collection<CheckDocument> getAllCheckDocuments() {
        List<CheckDocument> result = new ArrayList<>();
        for(Object o : session.createCriteria(CheckDocument.class).list()){
           result.add((CheckDocument)o);
        }
        return result;
    }

    @Override
    public void save(CheckDocument checkDocument) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(checkDocument);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to save entity " + checkDocument, e);
        }
    }

    @Override
    public void saveCheckDocuments(Collection<CheckDocument> checkDocuments) {
        for(CheckDocument cd : checkDocuments){
            save(cd);
        }
    }

    @Override
    public void delete(CheckDocument checkDocument) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // check document will delete its own check images, since nobody else can use them
            if(checkDocument.getCheckImageBack()!=null){
                session.delete(checkDocument.getCheckImageBack());
            }
            if(checkDocument.getCheckImageFront()!=null){
                session.delete(checkDocument.getCheckImageFront());
            }
            session.delete(checkDocument);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to delete entity " + checkDocument, e);
        }
    }

    @Override
    public void deleteCheckDocument(int id) {
        CheckDocument entity = getCheckDocument(id);
        if(entity!=null){
            delete(entity);
        }
    }

    @Override
    public DocumentFile getDocumentFile(int id) {
        return session.get(DocumentFile.class, id);
    }

    @Override
    public Collection<DocumentFile> getAllDocumentFiles() {
        List<DocumentFile> result = new ArrayList<>();
        for(Object o : session.createCriteria(DocumentFile.class).list()){
            result.add((DocumentFile)o);
        }
        return result;
    }

    @Override
    public Collection<DocumentFile> getDocumentFilesNotPersonalPictures() {
//        String hql = "FROM DocumentFile E WHERE E.documentType != :documentType";
//        Query query = session.createQuery(hql);
//        query.setParameter("documentType",DocumentFile.PERSON_PICTURE);
//        return  query.getResultList();
        Criteria criteria = session.createCriteria(DocumentFile.class);
        criteria.add(Restrictions.ne("documentType", DocumentFile.PERSON_PICTURE));
        List<DocumentFile> result = criteria.list();
        return result;
    }

    @Override
    public void save(DocumentFile documentFile) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(documentFile);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to save entity " + documentFile, e);
        }
    }

    @Override
    public void saveDocumentFiles(Collection<DocumentFile> documentFiles) {
        for(DocumentFile df : documentFiles){
            save(df);
        }
    }

    @Override
    public void delete(DocumentFile documentFile) {
        Transaction tx = null;
        try {
            String internalFileid = documentFile.getInternalFileName();

            tx = session.beginTransaction();
            session.delete(documentFile);
            session.flush();
            tx.commit();

            if(internalFileid!=null) {
                fileDao.deleteFile(internalFileid);
            }

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to delete entity " + documentFile, e);
        }
    }

    @Override
    public void deleteDocumentFile(int id) {
        DocumentFile entity = getDocumentFile(id);
        if(entity!=null){
            delete(entity);
        }
    }

    @Override
    public DocumentFile upload(File documentToBeUploaded) {
        return upload(documentToBeUploaded, false);
    }

    @Override
    public DocumentFile upload(File documentToBeUploaded, boolean deleteSource) {
        if(documentToBeUploaded==null || !documentToBeUploaded.exists()){
            return null;
        }else {

            String externalFileName = FilenameUtils.getName(documentToBeUploaded.toString());
            String externalFileExtension = FilenameUtils.getExtension(documentToBeUploaded.toString());
            String externalBaseName = FilenameUtils.getBaseName(documentToBeUploaded.toString());

            // first order of business is to create document file entity
            DocumentFile df = new DocumentFile();

            df.setComment("Uploaded from " + documentToBeUploaded);
            df.setExternalFileName(externalFileName);
            df.setDocumentTitle("Title: " + externalFileName);
            df.setDocumentDescription("Original file: " + documentToBeUploaded);
            df.setUploadDateTime(LocalDateTime.now());

            save(df);

            String internalName = internalNameWithInternalDirectory(df.getId(), externalFileExtension);

            df.setInternalFileName(internalName);

            // second order of business is to copy file to local area
            File localFileLocation = this.getPathToFile(df.getInternalFileName());

            try {
                FileUtils.copyFile(documentToBeUploaded, localFileLocation);
                if (!localFileLocation.exists() || localFileLocation.length() != documentToBeUploaded.length()) {
                    throw new IOException("Failed to copy " + documentToBeUploaded + " to " + localFileLocation);
                }
                // if we are here then the copy operation was successful
                save(df);
                // verify if save was successful
                DocumentFile df2 = getDocumentFile(df.getId());
                if (df2 == null) {
                    throw new IOException("Failed to save to database entity " + df);
                }
                // if we are here, then we have succesfully uploaded and stored in db the document
                if (deleteSource) {
                    documentToBeUploaded.delete();
                }
                return df;

            } catch (IOException e) {
                LOGGER.error("failed to upload document " + documentToBeUploaded);
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public File download(File destinationDir, DocumentFile documentFile) {
        File localFilename = this.getPathToFile(documentFile.getInternalFileName());
        File remoteFileName = new File(destinationDir, documentFile.getExternalFileName());

        try {
            FileUtils.copyFile(localFilename, remoteFileName);
            if (!remoteFileName.exists() || localFilename.length() != remoteFileName.length()) {
                throw new IOException("Failed to copy " + localFilename + " to " + remoteFileName);
            }
            return remoteFileName;

        } catch (IOException e) {
            LOGGER.error("failed to download document " + documentFile.toString());
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Integer getNumberOfTransactionsForAccount(Konto konto) {
        if(konto!=null && session.get(Konto.class,konto.getId())!=null){
        //if(konto.getId()!=0) {
            // it means account has been already saved
            Query query = session.createQuery(
                    "select count(*) from Transakcja t where t.buyer=:konto or t.seller=:konto");
            query.setParameter("konto", konto);
            List list = query.getResultList();
            Long count = (Long) list.get(0);
            return count.intValue();
        }else{
            return 0;
        }
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsWhereBuyerIsAccount(Konto konto) {

        String hql = "from Transakcja t where t.buyer = :buyer";
        Query query = session.createQuery(hql);
        query.setParameter("buyer",konto);
        List<Transakcja> list = query.getResultList();
        Map<Integer, Transakcja> result = new HashMap<>();
        for(Transakcja t : list){
            result.put(t.getId(),t);
        }
        return result;
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsWhereSellerIsAccount(Konto konto) {
        String hql = "from Transakcja t where t.seller = :seller";
        Query query = session.createQuery(hql);
        query.setParameter("seller",konto);
        List<Transakcja> list = query.getResultList();
        Map<Integer, Transakcja> result = new HashMap<>();
        for(Transakcja t : list){
            result.put(t.getId(),t);
        }
        return result;
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsForAccount(Konto konto) {
        Map<Integer, Transakcja> result = getTransactionsWhereBuyerIsAccount(konto);
        Map<Integer, Transakcja> result2 = getTransactionsWhereSellerIsAccount(konto);
        result.putAll(result);
        result.putAll(result2);
        return result;
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsBetweenAccounts(Konto account1, Konto account2) {
        String hql = "from Transakcja t where t.buyer = :account1 and t.seller = :account2 or t.buyer = :account2 and t.seller = :account1";

        Query query = session.createQuery(hql);
        query.setParameter("account1",account1);
        query.setParameter("account2",account2);

        List<Transakcja> list = query.getResultList();
        Map<Integer, Transakcja> result = new HashMap<>();
        for(Transakcja t : list){
            result.put(t.getId(),t);
        }
        return result;
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate) {
        //TODO paramerize this to make it more efficient using hql

        Map<Integer, Transakcja> allTransactions = getTransactionsForAccount(konto);
        Map<Integer, Transakcja> result = new HashMap<>();

        for(Integer txId : allTransactions.keySet()){
            Transakcja tx = allTransactions.get(txId);
            if(tx!=null && !tx.getTransactionDate().isBefore(startDate)){
                result.put(txId,tx);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsForAccount(Konto konto, LocalDate startDate, LocalDate endDate) {
        //TODO paramerize this to make it more efficient using hql
        Map<Integer, Transakcja> allTransactions = getTransactionsForAccount(konto);
        Map<Integer, Transakcja> result = new HashMap<>();

        for(Integer txId : allTransactions.keySet()){
            Transakcja tx = allTransactions.get(txId);
            if(tx!=null && !tx.getTransactionDate().isBefore(startDate) && tx.getTransactionDate().isBefore(endDate)){
                result.put(txId,tx);
            }
        }
        return result;
    }

    @Override
    public Collection<Transakcja> getTransactionsForCheckNumber(String checkNumber) {
        Collection<Transakcja> result = new ArrayList<>();

        for(Transakcja t : getAllTransactions()){
            if(t.getCheckDocument()!=null){
                if(checkNumber.equals(t.getCheckDocument().getCheckNumber())){
                    result.add(t);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<Transakcja> getTransactinsWhichContain(DocumentFile document) {
        String hql = "from Transakcja entry where :document in elements(entry.documents)";
        Query query = session.createQuery(hql);
        query.setParameter("document",document);
        List<Transakcja> list = query.getResultList();
        return list;
    }

    @Override
    public void flush() {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.flush();
            tx.commit();
            LOGGER.info("Session flushed");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Failed to flush session ", e);
        }
    }

    @Override
    public void close() {
        flush();
        session.close();
        sf.close();
        LOGGER.info("DAO closed!");
    }

    @Override
    public Map<Integer, Transakcja> getTransactionsForAccountHavingCheckNumberAndCpty(Konto konto, String checkNumberFilter, String counterPartyShortNameFilter) {
        //TODO make the query more efficient using hql
        Map<Integer, Transakcja> allTransactions = getTransactionsForAccount(konto);
        Map<Integer, Transakcja> result = new HashMap<>();

        for(Integer txId : allTransactions.keySet()){
            Transakcja tx = allTransactions.get(txId);
            if((tx.getCheckNumber()!=null && tx.getCheckNumber().contains(checkNumberFilter))
                    || (StringUtils.isEmpty(tx.getCheckNumber()) && StringUtils.isEmpty(checkNumberFilter))
                    ){

                if(tx.getBuyer().getShortName().contains(counterPartyShortNameFilter) ||
                        tx.getSeller().getShortName().contains(counterPartyShortNameFilter)  ){
                    result.put(tx.getId(),tx);
                }
            }
        }
        return result;
    }

    // methods from FileDao interface
    @Override
    public void upload(File externalFile, String internalFileName) throws IOException {
        this.fileDao.upload(externalFile, internalFileName);
    }

    @Override
    public void upload(File externalFile, File internalFile) throws IOException {
        this.fileDao.upload(externalFile, internalFile);
    }

    @Override
    public void upload(File externalFile, String internalFileName, boolean deleteSource) throws IOException {
        this.fileDao.upload(externalFile, internalFileName, deleteSource);
    }

    @Override
    public void upload(File externalFile, File internalFile, boolean deleteSource) throws IOException {
        this.fileDao.upload(externalFile, internalFile, deleteSource);
    }

    @Override
    public void download(String internalFileId, File externalFile) throws IOException {
        this.fileDao.download(internalFileId, externalFile);
    }

    @Override
    public void deleteFile(String internalFileId) {
        this.fileDao.deleteFile(internalFileId);
    }

    @Override
    public void deleteFile(File internalFile) {
        this.fileDao.deleteFile(internalFile);
    }

    @Override
    public File getPathToFile(String internalFileId) {
        return this.fileDao.getPathToFile(internalFileId);
    }

    @Override
    public File getPathToFile(File internalFile) {
        return this.fileDao.getPathToFile(internalFile);
    }

    @Override
    public String getInternalSubdirectory(int id) {
        return this.fileDao.getInternalSubdirectory(id);
    }

    @Override
    public String internalNameWithInternalDirectory(int id, String extension) {
        return this.fileDao.internalNameWithInternalDirectory(id,extension);
    }
}
