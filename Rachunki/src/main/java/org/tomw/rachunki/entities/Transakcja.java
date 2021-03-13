package org.tomw.rachunki.entities;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import org.apache.log4j.Logger;
//import org.tomw.czeki.entities.Transaction;
import org.tomw.documentfile.DocumentFile;
import org.tomw.identifiable.Identifiable;
import org.tomw.utils.TomwStringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static org.tomw.utils.TomwStringUtils.BLANK;

@Entity
@Table
public class Transakcja implements Identifiable {
    private final static Logger LOGGER = Logger.getLogger(Transakcja.class.getName());

    private final IntegerProperty id = new SimpleIntegerProperty();

    private Collection<String> oldIds = new HashSet<>();

    private ObjectProperty<LocalDate> transactionDate = new SimpleObjectProperty(null);
    private ObjectProperty<LocalDate> transactionClearedSellerSideDate = new SimpleObjectProperty(null);
    private ObjectProperty<LocalDate> transactionClearedBuyerSideDate = new SimpleObjectProperty(null);

    private StringProperty memo = new SimpleStringProperty(BLANK);
    private StringProperty comment = new SimpleStringProperty(BLANK);
    private StringProperty shortComment = new SimpleStringProperty(BLANK);

    public double getTransactionDirectionAmount() {
        return transactionDirectionAmount.get();
    }

    public DoubleProperty transactionDirectionAmountProperty() {
        return transactionDirectionAmount;
    }

    public void setTransactionDirectionAmount(double transactionDirectionAmount) {
        this.transactionDirectionAmount.set(transactionDirectionAmount);
    }

    private DoubleProperty transactionAmount = new SimpleDoubleProperty(0.0);

    private DoubleProperty transactionDirectionAmount = new SimpleDoubleProperty(0.0);

    private ObjectProperty<Konto> seller = new SimpleObjectProperty(null);
    private ObjectProperty<Konto> buyer = new SimpleObjectProperty(null);
    private StringProperty checkNumber = new SimpleStringProperty(BLANK);

    private BooleanProperty clearedSeller = new SimpleBooleanProperty(false);
    private BooleanProperty clearedBuyer = new SimpleBooleanProperty(false);
    private StringProperty clearedLocal = new SimpleStringProperty("");
    private StringProperty clearedRemote = new SimpleStringProperty("");

    private Collection<DocumentFile> documents = new ArrayList<>();

    private CheckDocument checkDocument;

    private double sumClearedTransactions = 0.0;
    private double sumAllTransactions = 0.0;

    private StringProperty runningSumClearedTransactions = new SimpleStringProperty("");
    private StringProperty runningSumAllTransactions = new SimpleStringProperty("");

    public Transakcja() {
    }

    public Transakcja(LocalDate date) {
        setTransactionDate(date);
    }

    public Transakcja(LocalDate date, Konto buyer, Konto seller) {
        setTransactionDate(date);
        setBuyerAndSeller(buyer, seller);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    //@OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    @CollectionTable(name = "oldTransakcjaIds")
    @Column(name = "oldIds")
    public Collection<String> getOldIds() {
        return oldIds;
    }

    public void setOldIds(Collection<String> oldIds) {
        this.oldIds = oldIds;
    }

    @Column
    public LocalDate getTransactionDate() {
        return transactionDate.get();
    }

    public ObjectProperty<LocalDate> transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate.set(transactionDate);
    }

    @Column
    public LocalDate getTransactionClearedSellerSideDate() {
        return transactionClearedSellerSideDate.get();
    }

    public ObjectProperty<LocalDate> transactionClearedSellerSideDateProperty() {
        return transactionClearedSellerSideDate;
    }

    public void setTransactionClearedSellerSideDate(LocalDate transactionClearedSellerSideDate) {
        this.transactionClearedSellerSideDate.set(transactionClearedSellerSideDate);
    }

    @Column
    public LocalDate getTransactionClearedBuyerSideDate() {
        return transactionClearedBuyerSideDate.get();
    }

    public ObjectProperty<LocalDate> transactionClearedBuyerSideDateProperty() {
        return transactionClearedBuyerSideDate;
    }

    public void setTransactionClearedBuyerSideDate(LocalDate transactionClearedBuyerSideDate) {
        this.transactionClearedBuyerSideDate.set(transactionClearedBuyerSideDate);
    }

    @Column
    @Lob
    public String getMemo() {
        return memo.get();
    }

    public StringProperty memoProperty() {
        return memo;
    }

    public void setMemo(String memoIn) {
        this.memo.set(memoIn);
    }

    @Column
    @Lob
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String commentIn) {
        this.comment.set(commentIn);
        this.shortComment.set(TomwStringUtils.firstLine(commentIn));
    }

    public String getShortComment() {
        return shortComment.get();
    }

    public StringProperty shortCommentProperty() {
        return shortComment;
    }

    public void setShortComment(String shortComment) {

    }

    @Column
    public double getTransactionAmount() {
        return transactionAmount.get();
    }

    public DoubleProperty transactionAmountProperty() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount.set(transactionAmount);
    }

    public void updateAmount(double amount, Konto account){
        // if amount is negative, then account should be buyer
        if(amount<0.){
            if(account.equals(getBuyer())){
                // simply save the amount
                setTransactionAmount(-amount);
            }else{
                if(account.equals(getSeller())){
                    // negative amount means that account PAYS money.
                    // so it is not seller.
                    // we need to swap buyer and seller
                    swapBuyerAndSeller();
                    setTransactionAmount(-amount);
                }else{
                    throw new RuntimeException("Account "+account+" does not belong to transaction "+this.toString());
                }
            }
        }else{
            // if we are here then amount is positive\
            // positive amount means that account is seller
            if(account.equals(getSeller())){
                setTransactionAmount(amount);
            }else{
                if(account.equals(getBuyer())){
                    // poisitive amount means that account is seller
                    // swap buyer and seller
                    swapBuyerAndSeller();
                    setTransactionAmount(amount);
                }else{
                    throw new RuntimeException("Account "+account+" does not belong to transaction "+this.toString());
                }
            }
        }
    }

    public  void swapBuyerAndSeller() {
        Konto temp = getBuyer();
        setBuyer(getSeller());
        setSeller(temp);
    }

    public DoubleProperty getTransactionDirectionAmount(Konto currentAccount) {
        if (getBuyer()!=null && getBuyer().equals(currentAccount)) {
            this.transactionDirectionAmount.set(-java.lang.Math.abs(getTransactionAmount()));
            return this.transactionDirectionAmount;
        } else if (getSeller()!=null && getSeller().equals(currentAccount)) {
            this.transactionDirectionAmount.set(java.lang.Math.abs(getTransactionAmount()));
            return this.transactionDirectionAmount;
        } else {
            throw new RuntimeException(toString() + " does not contain account " + currentAccount);
        }
    }

    @ManyToOne
    public Konto getSeller() {
        return seller.get();
    }

    public ObjectProperty<Konto> sellerProperty() {
        return seller;
    }

    public void setSeller(Konto seller) {
        this.seller.set(seller);
    }

    @ManyToOne
    public Konto getBuyer() {
        return buyer.get();
    }

    public ObjectProperty<Konto> buyerProperty() {
        return buyer;
    }

    public void setBuyer(Konto buyer) {
        this.buyer.set(buyer);
    }

    public void setBuyerAndSeller(Konto buyer, Konto seller) {
        setBuyer(buyer);
        setSeller(seller);
    }

    /**
     * return the other party of the transaction, not konto
     *
     * @param konto
     * @return
     */
    public ObjectProperty<Konto> getOther(Konto konto) {
        if (this.seller.get().equals(konto)) {
            return this.buyer;
        } else {
            return this.seller;
        }
    }

    @Column
    public String getCheckNumber() {
        return checkNumber.get();
    }

    public StringProperty checkNumberProperty() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber.set(checkNumber);
    }

    @Column
    public boolean isClearedSeller() {
        return clearedSeller.get();
    }

    public BooleanProperty clearedSellerProperty() {
        return clearedSeller;
    }

    public void setClearedSeller(boolean cleared) {
        this.clearedSeller.set(cleared);
    }

    @Column
    public boolean isClearedBuyer() {
        return clearedBuyer.get();
    }

    public BooleanProperty clearedBuyerProperty() {
        return clearedBuyer;
    }

    public void setClearedBuyer(boolean cleared) {
        this.clearedBuyer.set(cleared);
    }

    @Transient
    public String getClearedLocal() {
        return clearedLocal.get();
    }

    public StringProperty clearedLocalProperty() {
        return clearedLocal;
    }

    public void setClearedLocal(String clearedLocal) {
        this.clearedLocal.set(clearedLocal);
    }

    @Transient
    public String getClearedRemote() {
        return clearedRemote.get();
    }

    public StringProperty clearedRemoteProperty() {
        return clearedRemote;
    }

    public void setClearedRemote(String clearedRemote) {
        this.clearedRemote.set(clearedRemote);
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Collection<DocumentFile> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<DocumentFile> documents) {
        this.documents = documents;
    }

    @OneToOne
    public CheckDocument getCheckDocument() {
        return checkDocument;
    }

    public void setCheckDocument(CheckDocument checkDocument) {
        this.checkDocument = checkDocument;
    }

    public double getSumClearedTransactions() {
        return sumClearedTransactions;
    }

    public void setSumClearedTransactions(double sumClearedTransactions) {
        this.sumClearedTransactions = sumClearedTransactions;
    }

    public double getSumAllTransactions() {
        return sumAllTransactions;
    }

    public void setSumAllTransactions(double sumAllTransactions) {
        this.sumAllTransactions = sumAllTransactions;
    }

    @Transient
    public String getRunningSumClearedTransactions() {
        return runningSumClearedTransactions.get();
    }

    public StringProperty runningSumClearedTransactionsProperty() {
        return runningSumClearedTransactions;
    }

    public void setRunningSumClearedTransactions(String runningSumClearedTransactions) {
        this.runningSumClearedTransactions.set(runningSumClearedTransactions);
    }

    @Transient
    public String getRunningSumAllTransactions() {
        return runningSumAllTransactions.get();
    }

    public StringProperty runningSumAllTransactionsProperty() {
        return runningSumAllTransactions;
    }

    public void setRunningSumAllTransactions(String runningSumAllTransactions) {
        this.runningSumAllTransactions.set(runningSumAllTransactions);
    }


    // === utility methods ===

    /**
     * verify if the transaction is cleared on the account
     *
     * @param account account
     * @return true if cleared
     */
    public boolean isCleared(Konto account) {
        if (getSeller().equals(account)) {
            return isClearedSeller();
        }
        if (getBuyer().equals(account)) {
            return isClearedBuyer();
        }
        throw new RuntimeException("Tried to clear account " + account +
                " on transaciton which does not involve this account " + toString());
    }

    public void setCleared(Konto account, boolean cleared){
        if (getSeller().equals(account)) {
            setClearedSeller(cleared);
        }else {
            if (getBuyer().equals(account)) {
                setClearedBuyer(cleared);
            }else{
                throw new RuntimeException("Tried to clear account " + account +
                        " on transaciton which does not involve this account " + toString());
            }
        }
    }


    public boolean isClearedLocal(Konto account) {
        return isCleared(account);
    }

    /**
     * Transaction has buyer and seler. once we are in a "select transactions for a particular account"
     * mode we can think of "current account" and counterparty. Counterparty is the account in transaction
     * which is not current account.
     * This methid takes one account, then verifies buyer and seller and returns the one which is not equal to this account.
     *
     * @param account
     * @return
     */
    public Konto getCounterParty(Konto account) {
        if (account == null) {
            throw new RuntimeException("Account cannot be null for counterparty " + this.toString());
        } else {
            if (account.equals(getSeller())) {
                return getBuyer();
            }
            if (account.equals(getBuyer())) {
                return getSeller();
            }
            throw new RuntimeException("Tried to find counterparty for account " + account +
                    " on transaciton which does not involve this account " + toString());
        }
    }

    /**
     * One of the accounts is the currentAccount. The other is counterparty. We want to
     * find the other account and replace it with thr new one
     *
     * @param currentAccount
     * @param newCounterParty
     */
    public void setNewCounterParty(Konto currentAccount, Konto newCounterParty) {
        if (currentAccount == null) {
            throw new RuntimeException("Account cannot be null for counterparty " + this.toString());
        }
        if (newCounterParty == null) {
            throw new RuntimeException("Counterparty cannot be null");
        }
        if (currentAccount.equals(newCounterParty)) {
            throw new RuntimeException("current Account and new counterparty are equal: " + currentAccount);
        }
        if (currentAccount.equals(getSeller())) {
            setBuyer(newCounterParty);
            return;
        }
        if (currentAccount.equals(getBuyer())) {
            setSeller(newCounterParty);
            return;
        }
        throw new RuntimeException("Tried to find counterparty for account " + currentAccount +
                " on transaciton which does not involve this account " + toString());

    }

    public boolean isClearedRemote(Konto account) {
        if (getSeller().equals(account)) {
            return isClearedBuyer();
        }
        if (getBuyer().equals(account)) {
            return isClearedSeller();
        }
        throw new RuntimeException("Tried to clear account " + account +
                " on transaciton which does not involve this account " + toString());
    }

    public void setClearedLocal(boolean cleared, Konto localAccount) {
        if (localAccount == null) {
            throw new RuntimeException("Local Account cannot be null for to set cleared local field  " + this.toString());
        } else {
            if (localAccount.equals(getSeller())) {
                setClearedSeller(cleared);
                return;
            }
            if (localAccount.equals(getBuyer())) {
                setClearedBuyer(cleared);
                return;
            }
            throw new RuntimeException("Tried to find counterparty for account " + localAccount +
                    " on transaciton which does not involve this account " + toString());
        }
    }

    public void setClearedRemote(boolean cleared, Konto localAccount) {
        if (localAccount == null) {
            throw new RuntimeException("Local Account cannot be null for to set cleared local field  " + this.toString());
        } else {
            if (localAccount.equals(getSeller())) {
                setClearedBuyer(cleared);
                return;
            }
            if (localAccount.equals(getBuyer())) {
                setClearedSeller(cleared);
                return;
            }
            throw new RuntimeException("Tried to find counterparty for account " + localAccount +
                    " on transaciton which does not involve this account " + toString());
        }
    }


    // === comparators ====//
    @Transient
    public static Comparator<Transakcja> transactionByDateComparator
            = new Comparator<Transakcja>() {

        @Override
        public int compare(Transakcja t1, Transakcja t2) {
            LocalDate d1 = t1.getTransactionDate();
            LocalDate d2 = t2.getTransactionDate();
            return d1.compareTo(d2);
        }

    };


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transakcja)) return false;

        Transakcja that = (Transakcja) o;

        return id.get() == that.id.get();
    }

    @Override
    public int hashCode() {
        return id.get();
    }

    @Override
    public String toString() {

        String sellerShortname;
        if (seller.get() != null) {
            sellerShortname = seller.get().getShortName() + "," + seller.get().getId();
        } else {
            sellerShortname = "";
        }

        String buyerShortname;
        if (buyer.get() != null) {
            buyerShortname = buyer.get().getShortName() + "," + buyer.get().getId();
        } else {
            buyerShortname = "";
        }

        return "Transakcja{" +
                "id=" + id.get() +
                ", transactionDate=" + transactionDate.get() +
                ", transactionAmount=" + transactionAmount.get() +
                ", seller=(" + sellerShortname + ")" +
                ", buyer=(" + buyerShortname + ")" +
                ", checkNumber=" + checkNumber.get() +
                '}';
    }



}
