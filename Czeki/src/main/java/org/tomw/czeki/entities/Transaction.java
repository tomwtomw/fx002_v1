/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.utils.TomwStringUtils;

/**
 * Transaction class
 *
 *
 * @author tomw
 */
public class Transaction {

    private final static Logger LOGGER = Logger.getLogger(Transaction.class.getName());

    public static String ID = "id";
    public static String DATE = "Date";
    public static String MEMO = "Memo";
    public static String COMMENT = "Comment";
    public static String TRANSACTION_AMOUNT = "Transaction amount";
    public static String AMOUNT = "Amount";
    public static String COUNTERPARTY = "Counterparty";
    public static String COUNTERPARTY_ID = "Counterparty id";
    public static String CHECK_NO = "Check Number";
    public static String CLEARED = "Cleared";

    public static String CHECK_IMAGE_FRONT = "CheckImageFront";
    public static String CHECK_IMAGE_BACK = "CheckImageBack";

    public static String DOCUMENTS = "Documents";

    private StringProperty idProperty;
    private ObjectProperty<LocalDate> transactionDateProperty;
    private StringProperty memoProperty;
    private StringProperty commentProperty;
    private DoubleProperty transactionAmountProperty;
    private ObjectProperty<CounterParty> counterPartyProperty;
    private StringProperty checkNumberProperty;
    private StringProperty clearedProperty;

    private double sumClearedTransactions;
    private double sumAllTransactions;

    private CheckImage imageFront = null;
    private CheckImage imageBack = null;

    private List<TransactionDocument> transactionDccuments;

    public static final String SEPARATOR = "\t";
    public static final String BLANK = "";
    public static final String EOL = "\n";

    public Transaction() {
        this(UUID.randomUUID().toString(), null, 0.0, null, null, null, null);
    }

    public Transaction(LocalDate date) {
        this(UUID.randomUUID().toString(), date, 0.0, null, null, null, null);
    }

    public Transaction(LocalDate date, double amount, String checkNumber, CounterParty counterParty, String memo, String comment) {
        this(UUID.randomUUID().toString(), date, amount, checkNumber, counterParty, memo, comment);
    }

    public Transaction(LocalDate date,
            double amount,
            String checkNumber,
            CounterParty counterParty,
            String memo,
            String comment,
            String cleared) {
        this(UUID.randomUUID().toString(), date, amount, checkNumber, counterParty, memo, comment, cleared);
    }

    public Transaction(String id, LocalDate date, double amount, String checkNumber, CounterParty counterParty, String memo, String comment) {
        this.transactionDateProperty = new SimpleObjectProperty<>(date);
        this.idProperty = new SimpleStringProperty(id);
        this.transactionAmountProperty = new SimpleDoubleProperty(amount);
        this.checkNumberProperty = new SimpleStringProperty(checkNumber);
        this.counterPartyProperty = new SimpleObjectProperty<>(counterParty);
        this.commentProperty = new SimpleStringProperty(comment);
        this.memoProperty = new SimpleStringProperty(memo);
        this.clearedProperty = new SimpleStringProperty(BLANK);
        this.sumClearedTransactions = 0.0;
        this.sumAllTransactions = 0.0;
    }

    public Transaction(String id, LocalDate date, double amount, String checkNumber, CounterParty counterParty, String memo, String comment, String cleared) {
        this.transactionDateProperty = new SimpleObjectProperty<>(date);
        this.idProperty = new SimpleStringProperty(id);
        this.transactionAmountProperty = new SimpleDoubleProperty(amount);
        this.checkNumberProperty = new SimpleStringProperty(checkNumber);
        this.counterPartyProperty = new SimpleObjectProperty<>(counterParty);
        this.commentProperty = new SimpleStringProperty(comment);
        this.memoProperty = new SimpleStringProperty(memo);
        this.clearedProperty = new SimpleStringProperty(cleared);
        this.sumClearedTransactions = 0.0;
        this.sumAllTransactions = 0.0;
    }

    public Transaction(String id, LocalDate date, double amount, String checkNumber,
            CounterParty counterParty, String memo, String comment,
            CheckImage imageFront, CheckImage imageBack) {
        this.transactionDateProperty = new SimpleObjectProperty<>(date);
        this.idProperty = new SimpleStringProperty(id);
        this.transactionAmountProperty = new SimpleDoubleProperty(amount);
        this.checkNumberProperty = new SimpleStringProperty(checkNumber);
        this.counterPartyProperty = new SimpleObjectProperty<>(counterParty);
        this.commentProperty = new SimpleStringProperty(comment);
        this.memoProperty = new SimpleStringProperty(memo);
        this.clearedProperty = new SimpleStringProperty(BLANK);
        this.imageFront = imageFront;
        this.imageBack = imageBack;

        this.sumClearedTransactions = 0.0;
        this.sumAllTransactions = 0.0;
    }

    public Transaction(String id, LocalDate date, double amount, String checkNumber,
            CounterParty counterParty, String memo, String comment,
            CheckImage imageFront, CheckImage imageBack, String cleared) {
        this.transactionDateProperty = new SimpleObjectProperty<>(date);
        this.idProperty = new SimpleStringProperty(id);
        this.transactionAmountProperty = new SimpleDoubleProperty(amount);
        this.checkNumberProperty = new SimpleStringProperty(checkNumber);
        this.counterPartyProperty = new SimpleObjectProperty<>(counterParty);
        this.commentProperty = new SimpleStringProperty(comment);
        this.memoProperty = new SimpleStringProperty(memo);
        this.clearedProperty = new SimpleStringProperty(cleared);
        this.imageFront = imageFront;
        this.imageBack = imageBack;

        this.sumClearedTransactions = 0.0;
        this.sumAllTransactions = 0.0;
    }

    // === comparators ====//
    public static class CompareByDate implements Comparator<Transaction> {

        @Override
        public int compare(Transaction o1, Transaction o2) {
            return o1.getTransactionDate().compareTo(o2.getTransactionDate());
        }
    }

    public StringProperty getIdProperty() {
        return idProperty;
    }

    public String getId() {
        return getIdProperty().get();
    }

    public ObjectProperty<LocalDate> getTransactionDateProperty() {
        return transactionDateProperty;
    }

    public LocalDate getTransactionDate() {
        return (LocalDate) getTransactionDateProperty().get();
    }

    public void setTransactionDateProperty(ObjectProperty<LocalDate> transactionDateProperty) {
        this.transactionDateProperty = transactionDateProperty;
    }

    public void setTransactionDate(LocalDate date) {
        setTransactionDateProperty(new SimpleObjectProperty<>(date));
    }

    public StringProperty getCommentProperty() {
        return commentProperty;
    }

    public String getComment() {
        return getCommentProperty().get();
    }

    public void setCommentProperty(StringProperty commentProperty) {
        this.commentProperty = commentProperty;
    }

    public void setComment(String comment) {
        setCommentProperty(new SimpleStringProperty(comment));
    }

    public DoubleProperty getTransactionAmountProperty() {
        return transactionAmountProperty;
    }

    public Double getTransactionAmount() {
        return getTransactionAmountProperty().get();
    }

    public void setTransactionAmountProperty(DoubleProperty transactionAmountProperty) {
        this.transactionAmountProperty = transactionAmountProperty;
    }

    public void setTransactionAmount(double amount) {
        setTransactionAmountProperty(new SimpleDoubleProperty(amount));
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

    public ObjectProperty<CounterParty> getCounterPartyProperty() {
        return counterPartyProperty;
    }

    public CounterParty getCounterParty() {
        return ((CounterParty) getCounterPartyProperty().get());
    }

    public void setCounterPartyProperty(ObjectProperty<CounterParty> counterPartyProperty) {
        this.counterPartyProperty = counterPartyProperty;
    }

    public void setCounterParty(CounterParty counterParty) {
        setCounterPartyProperty(new SimpleObjectProperty<>(counterParty));
    }

    public StringProperty getCheckNumberProperty() {
        return checkNumberProperty;
    }

    public String getCheckNumber() {
        return checkNumberProperty.get();
    }

    public void setCheckNumberProperty(StringProperty checkNumberProperty) {
        this.checkNumberProperty = checkNumberProperty;
    }

    public void setCheckNumber(String checkNumber) {
        this.setCheckNumberProperty(new SimpleStringProperty(checkNumber));
    }

    public StringProperty getMemoProperty() {
        return memoProperty;
    }

    public String getMemo() {
        return memoProperty.get();
    }

    public void setMemoProperty(StringProperty memoProperty) {
        this.memoProperty = memoProperty;
    }

    public void setMemo(String memo) {
        this.setMemoProperty(new SimpleStringProperty(memo));
    }

    public void setImageFront(CheckImage image) {
        //TODO co jezeli ten image nie jest w dao?
        imageFront = image;
    }

    //TODO co jezeli ktos wpisze nieprawidlowy numer czeku, lub nielasciwa strone? Exception?
    public void setImageBack(CheckImage image) {
        imageBack = image;
    }

    public CheckImage getImageFront() {
        return imageFront;
    }

    public CheckImage getImageBack() {
        return imageBack;
    }

    public boolean hasImageFront() {
        return this.imageFront != null;
    }

    public boolean hasImageBack() {
        return this.imageBack != null;
    }

    public StringProperty getClearedProperty() {
        return clearedProperty;
    }

    public void setClearedProperty(StringProperty clearedProperty) {
        this.clearedProperty = clearedProperty;
    }

    public void setCleared(String t) {
        this.clearedProperty.set(t);
    }

    public void setCleared(boolean b) {
        this.clearedProperty.set(TomwStringUtils.boolean2BlankY(b));
    }

    public boolean isCleared() {
        return TomwStringUtils.stringToBoolean(this.clearedProperty.get());
    }

    /**
     * reset sums
     */
    public void resetSums() {
        this.sumClearedTransactions = 0.0;
        this.sumAllTransactions = 0.0;
    }

    /**
     * convert transaction into csv representation
     *
     * @return
     */
    public String toCsv() {
        String result = "";
        result = result + this.getTransactionDate().toString().trim() + SEPARATOR;
        result = result + this.getTransactionAmount().toString().trim() + SEPARATOR;
        result = result + this.getCheckNumber().trim() + SEPARATOR;
        result = result + this.getClearedProperty().get().trim() + SEPARATOR;
        result = result + this.getCounterParty().getShortName().trim() + SEPARATOR;
        result = result + this.getComment().trim() + SEPARATOR;
        result = result + this.getMemo().trim();
        return result;
    }

    /**
     * full csv header, includes information about image names
     *
     * @return
     */
    public static String fullHeader() {
        String columnHeaders = Transaction.DATE + Transaction.SEPARATOR
                + Transaction.AMOUNT + Transaction.SEPARATOR
                + Transaction.CHECK_NO + Transaction.SEPARATOR
                + Transaction.CLEARED + Transaction.SEPARATOR
                + Transaction.COUNTERPARTY + Transaction.SEPARATOR
                + Transaction.COMMENT + Transaction.SEPARATOR
                + Transaction.MEMO + Transaction.SEPARATOR
                + Transaction.CHECK_IMAGE_FRONT + Transaction.SEPARATOR
                + Transaction.CHECK_IMAGE_BACK + Transaction.SEPARATOR;
        return columnHeaders;
    }

    /**
     * return csv representation of transaction, including image information
     *
     * @return
     */
    public String toFullCsv() {
        String result = toCsv();
        if (this.imageFront != null) {
            result = result + SEPARATOR + this.imageFront.getFileNameProperty().get() + SEPARATOR;
        } else {
            result = result + BLANK + SEPARATOR;
        }
        if (this.imageBack != null) {
            result = result + this.imageBack.getFileNameProperty().get() + SEPARATOR;
        } else {
            result = result + BLANK + SEPARATOR;
        }
        return result;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public String toShortString() {
        return "Transaction{" +

                ", transactionDateProperty=" + transactionDateProperty.getValue() +
                ", transactionAmountProperty=" + transactionAmountProperty.getValue() +
                ", counterPartyProperty=" + counterPartyProperty.getValue().getShortName() +
                ", checkNumberProperty=" + checkNumberProperty.getValue() +
                " idProperty=" + idProperty.getValue() +
                '}';
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, this.getId());
        json.put(DATE, this.getTransactionDate().toString());
        json.put(COMMENT, this.getComment());
        json.put(TRANSACTION_AMOUNT, this.getTransactionAmount());
        if (this.getCounterParty() != null) {
            json.put(COUNTERPARTY_ID, this.getCounterParty().getId());
        } else {
            json.put(COUNTERPARTY_ID, CzekiRegistry.BLANK);
        }
        json.put(CLEARED, this.getClearedProperty().get());
        json.put(CHECK_NO, this.getCheckNumber());
        json.put(MEMO, this.getMemo());

        if (this.imageFront != null) {
            json.put(CHECK_IMAGE_FRONT, this.imageFront.toJson());
        }
        if (this.imageBack != null) {
            json.put(CHECK_IMAGE_BACK, this.imageBack.toJson());
        }

        return json;
    }

    public String toJsonString() {
        return toJson().toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.idProperty.get());
        hash = 59 * hash + Objects.hashCode(this.transactionDateProperty.get());
        hash = 59 * hash + Objects.hashCode(this.memoProperty.get());
        hash = 59 * hash + Objects.hashCode(this.commentProperty.get());
        hash = 59 * hash + Objects.hashCode(this.transactionAmountProperty.get());
        hash = 59 * hash + Objects.hashCode(this.counterPartyProperty.get());
        hash = 59 * hash + Objects.hashCode(this.checkNumberProperty.get());

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        if (!Objects.equals(this.idProperty.get(), other.idProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.transactionDateProperty.get(), other.transactionDateProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.memoProperty.get(), other.memoProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.commentProperty.get(), other.commentProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.transactionAmountProperty.get(), other.transactionAmountProperty.get())) {
            return false;
        }
        return Objects.equals(this.counterPartyProperty.get(), other.counterPartyProperty.get());
    }

    // === comparators for sorting
    /**
     * comparator for comparing transactions by date
     */
    public static Comparator<Transaction> TransactionByDateComparator
            = new Comparator<Transaction>() {

                @Override
                public int compare(Transaction t1, Transaction t2) {
                    LocalDate d1 = t1.getTransactionDate();
                    LocalDate d2 = t2.getTransactionDate();
                    return d1.compareTo(d2);
                }

            };

}
