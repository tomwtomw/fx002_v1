package org.tomw.rachunki.ingest;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.tomw.czeki.Account;
import org.tomw.czeki.AccountsDao;
import org.tomw.czeki.dao.CzekiDao;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.documentfile.DocumentFile;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import static org.tomw.utils.TomwStringUtils.BLANK;

/**
 * Convert old entities from the old syte, from Czeki project
 * to the current project
 */
public class TransactionsConverter {
    private final static Logger LOGGER = Logger.getLogger(TransactionsConverter.class.getName());


    private RachunkiService service;
    private AccountsDao accountsDao;

    /**
     * Constructor
     * @param service service object
     */
    public TransactionsConverter(RachunkiService service, AccountsDao accountsDao){
        this.accountsDao=accountsDao;
        this.service=service;
    }

    public Konto convert(Account account){
        //LOGGER.info("Convert account "+account);
        Konto konto = new Konto();

        konto.setFullName(account.getName());
        konto.setShortName(account.getShortName());
        konto.getOldIds().add(account.getId());
        konto.setComment(account.getComment());
        // accounts are, per definition, local
        konto.setAccountLocal(true);

        return konto;
    }

    public Konto convert(CounterParty counterparty){
        Konto konto = new Konto();

        konto.setFullName(counterparty.getName());
        konto.setShortName(counterparty.getShortName());
        konto.getOldIds().add(counterparty.getId());
        konto.setComment(counterparty.getComment());

        Konto kontoSaved = service.getKontoWithOldId(counterparty.getId());

        if(kontoSaved!=null){
            LOGGER.debug("Old account "+counterparty.toString()+" is already saved as new "+
                            kontoSaved.toString());
            return kontoSaved;
        }else{
            return konto;
        }
    }

//    private StringProperty idProperty;
//    private StringProperty nameProperty;
//    private StringProperty shortNameProperty;
//    private List<String> otherNames;
//    private StringProperty commentProperty;

    /**
     * Convert transaction object (from czeki module) to Transakcja (from rachunki)
     * One of the parties in the transaction is the counterparty object in transaction
     * The other is Konto
     *
     * @param service rachunki service
     * @param oldTransaction transaction
     * @param mainKonto the other party
     * @return Transakcja object to be persisted in db
     */
    public Transakcja convert(RachunkiService service,
                              Transaction oldTransaction,
                              Konto mainKonto,
                              Map<CounterParty,Konto> cpToKontoMap) {
        Transakcja transakcja = new Transakcja();

        transakcja.getOldIds().add(oldTransaction.getId());
        transakcja.setTransactionDate(oldTransaction.getTransactionDate());

        double amount = oldTransaction.getTransactionAmount();
        transakcja.setTransactionAmount(oldTransaction.getTransactionAmount());

        // first get its counterparty
        CounterParty cp = oldTransaction.getCounterParty();
        Konto konto2;
        if (cpToKontoMap.containsKey(cp)) {
            konto2 = cpToKontoMap.get(cp);
        }else{
            konto2 = convert(cp);
        }

        if(amount>0.){
            transakcja.setSeller(mainKonto);
            transakcja.setClearedSeller(oldTransaction.isCleared());
            transakcja.setBuyer(konto2);
        }else{
            transakcja.setSeller(konto2);
            transakcja.setBuyer(mainKonto);
            transakcja.setClearedBuyer(oldTransaction.isCleared());
        }
        transakcja.setTransactionDate(oldTransaction.getTransactionDate());
        transakcja.setMemo(oldTransaction.getMemo());
        transakcja.setComment(oldTransaction.getComment());
        transakcja.setCheckNumber(oldTransaction.getCheckNumber());

        // now handle check images
        //boolean handleImages = false;
        boolean handleImages = true;
        if(handleImages) {
            if (oldTransaction.getImageBack() != null && oldTransaction.getImageFront() != null) {

                File imageFileFront = oldTransaction.getImageFront().getFile();
                File imageFileBack = oldTransaction.getImageBack().getFile();

                CheckDocument checkDocument = service.createCheck(
                        oldTransaction.getCheckNumber(),
                        oldTransaction.getComment(),
                        oldTransaction.getMemo(),
                        imageFileFront,
                        imageFileBack,
                        false
                );
                transakcja.setCheckDocument(checkDocument);
                //CheckDocument createCheck(String checkNumber, String comment, String memo, File imageFront, File imageBack, boolean deleteSourceFiles);

            } else if (oldTransaction.getImageBack() == null && oldTransaction.getImageFront() != null) {
                File imageFileFront = oldTransaction.getImageFront().getFile();
                File imageFileBack = null;
                CheckDocument checkDocument = service.createCheck(
                        oldTransaction.getCheckNumber(),
                        oldTransaction.getComment(),
                        oldTransaction.getMemo(),
                        imageFileFront,
                        imageFileBack,
                        false
                );
                transakcja.setCheckDocument(checkDocument);
            } else if (oldTransaction.getImageBack() != null && oldTransaction.getImageFront() == null) {
                File imageFileFront = null;
                File imageFileBack = oldTransaction.getImageBack().getFile();
                CheckDocument checkDocument = service.createCheck(
                        oldTransaction.getCheckNumber(),
                        oldTransaction.getComment(),
                        oldTransaction.getMemo(),
                        imageFileFront,
                        imageFileBack,
                        false
                );
                transakcja.setCheckDocument(checkDocument);
            }
        }
        // TODo handle cases thwre only one side of image is present
        if(oldTransaction.getImageBack()==null && oldTransaction.getImageFront()!=null) {
            LOGGER.warn("Front image is not null, back is null");
        }
        if(oldTransaction.getImageBack()!=null && oldTransaction.getImageFront()==null) {
            LOGGER.error("Front image is null, back is not null");
        }

        return transakcja;
    }

//    public Transakcja convertXXX(RachunkiService rachunkiService, Transaction oldTransaction, Konto localKonto, Map<String, Konto> mapLocalCpToRemoteKonto) {
//        // does it map to excisting Konto?
//        if(mapLocalCpToRemoteKonto.get(oldTransaction.getCounterParty().getId())!=null){
//            Konto remoteKonto = mapLocalCpToRemoteKonto.get(oldTransaction.getCounterParty().getId());
//            Transakcja matchedTransakcja = matchTransaction(
//                    oldTransaction,
//                    rachunkiService.getTransactionsForAccount(remoteKonto)
//            );
//            if(matchedTransakcja!=null){
//                enrichNewTransaction(matchedTransakcja,oldTransaction);
//                return matchedTransakcja;
//            }else{
//                throw new RuntimeException("Failed to match transaction to "+oldTransaction);
//            }
//        }else{
//            // no need to make connection with existing transaction,
//            return convert(rachunkiService, oldTransaction, localKonto);
//        }
//
//    }

    private void enrichNewTransaction(Transakcja matchedTransakcja, Transaction oldTransaction) {
        matchedTransakcja.setMemo(matchedTransakcja.getMemo()+" ; "+oldTransaction.getMemo());
        matchedTransakcja.setMemo(matchedTransakcja.getComment()+" ; "+oldTransaction.getComment());
    }

    public Transakcja matchTransaction(Transaction oldTransaction, ObservableList<Transakcja> transactionsForAccount) {
        for(Transakcja transakcja : transactionsForAccount){
            if (theyMatch(oldTransaction, transakcja)) {
                return transakcja;
            }
        }
        return null;
    }

    private boolean theyMatch(Transaction oldTransaction, Transakcja transakcja) {
        boolean amountsMatch = oldTransaction.getTransactionAmount()+transakcja.getTransactionAmount()==0.;
        boolean checkNumbersMatch = TomwStringUtils.stringNullEquals(
                oldTransaction.getCheckNumber(),
                transakcja.getCheckNumber());
        boolean datesmatch = oldTransaction.getTransactionDate().equals(transakcja.getTransactionDate());

        boolean result = amountsMatch && checkNumbersMatch && datesmatch;
        return result;
    }

//    private Collection<String> oldIds =  new HashSet<>();

//    private ObjectProperty<LocalDate> transactionDate  =   new SimpleObjectProperty(null);
//    private ObjectProperty<LocalDate> transactionClearedSellerSideDate  =   new SimpleObjectProperty(null);
//    private ObjectProperty<LocalDate> transactionClearedBuyerSideDate  =   new SimpleObjectProperty(null);
//
//    private StringProperty memoProperty  = new SimpleStringProperty(BLANK);
//    private StringProperty commentProperty  = new SimpleStringProperty(BLANK);
//    private DoubleProperty transactionAmount  = new SimpleDoubleProperty(0.0);
//
//    private ObjectProperty<Konto> seller =   new SimpleObjectProperty(null);
//    private ObjectProperty<Konto> buyer =   new SimpleObjectProperty(null);
//    private StringProperty checkNumber  = new SimpleStringProperty(BLANK);
//
//    private BooleanProperty clearedSeller = new SimpleBooleanProperty(false);
//    private BooleanProperty clearedBuyer = new SimpleBooleanProperty(false);
//
//    private Collection<DocumentFile> documents = new ArrayList<>();
//
//    private CheckDocument checkDocument;
}
