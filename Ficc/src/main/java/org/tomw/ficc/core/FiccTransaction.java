package org.tomw.ficc.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import org.json.simple.JSONObject;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.UniqueIdGenerator;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Ficc transaction object
 * Created by tomw on 7/22/2017.
 */
public class FiccTransaction {
    public static final String ID="Id";
    public static final String DATE="Date";
    public static final String TRANSACTION="Transaction";
    public static final String COUNTERPARTY_NAME="CounterPartyName";
    public static final String COUNTERPARTY_ID="CounterPartyId";
    public static final String MEMO="Memo";
    public static final String AMOUNT="Amount";
    public static final String COMMENT = "Comment";

    public static final String BLANK = "";

    private String id;
    private LocalDate date;
    private String transaction;
    private String counterPartyId;
    private FiccCounterParty counterparty;
    private String memo;
    private double amount;
    private String comment;

    private StringProperty idProperty;
    private ObjectProperty<LocalDate> dateProperty;
    private StringProperty transactionProperty;
    private ObjectProperty<FiccCounterParty> counterPartyProperty;
    private StringProperty memoProperty;
    private DoubleProperty amountProperty;
    private StringProperty commentProperty;

    public FiccTransaction(){
        initFxmlProperties();
        setId((new UniqueIdGenerator()).generate());

    }

    @JsonCreator
    public FiccTransaction(
            @JsonProperty(ID) String idIn,
            @JsonProperty(DATE) String dateIn,
            @JsonProperty(TRANSACTION) String transactionIn,
            @JsonProperty(COUNTERPARTY_ID) String counterPartyIdIn,
            @JsonProperty(MEMO) String memoIn,
            @JsonProperty(AMOUNT) String amountIn,
            @JsonProperty(COMMENT) String commentIn
    ) {
        initFxmlProperties();
        setId(idIn);
        setDate(LocalDateUtils.fromString(dateIn));
        setTransaction(transactionIn);
        setCounterPartyId(counterPartyIdIn);
        setCounterparty(null);
        setMemo(memoIn);
        setAmount(Double.parseDouble(amountIn));
        setComment(commentIn);
    }

    private void initFxmlProperties(){
        idProperty = new SimpleStringProperty(BLANK);
        dateProperty = new SimpleObjectProperty<>(LocalDate.now());
        transactionProperty = new SimpleStringProperty(BLANK);
        counterPartyProperty = new SimpleObjectProperty<>(null);
        memoProperty = new SimpleStringProperty(BLANK);
        amountProperty = new SimpleDoubleProperty(0.0);
        commentProperty=  new SimpleStringProperty(BLANK);
    }

    /**
     * Convert cpty to json
     * @return cpty as json
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, getId());
        json.put(DATE, getDate().toString());
        json.put(TRANSACTION, getTransaction());
        json.put(COUNTERPARTY_ID, getCounterparty().getId());
        json.put(MEMO, getMemo());
        json.put(AMOUNT, getAmount());
        json.put(COMMENT, getComment());
        return json;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.idProperty.set(id);
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.dateProperty = new SimpleObjectProperty<>(date);
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
        this.transactionProperty.set(transaction);
    }

    public FiccCounterParty getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(FiccCounterParty counterparty) {
        this.counterparty = counterparty;
        if(this.counterparty!=null) {
            setCounterPartyId(counterparty.getId());
            this.counterPartyProperty = new SimpleObjectProperty<>(counterparty);
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
        this.memoProperty.set(memo);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.amountProperty.set(amount);
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        this.commentProperty.set(comment);
    }

    public String getIdProperty() {
        return idProperty.get();
    }

    public StringProperty idPropertyProperty() {
        return idProperty;
    }

    public ObjectProperty<LocalDate> datePropertyProperty() {
        return dateProperty;
    }
    public LocalDate getDateProperty() {
        return dateProperty.get();
    }

    public String getTransactionProperty() {
        return transactionProperty.get();
    }

    public StringProperty transactionPropertyProperty() {
        return transactionProperty;
    }

    public String getMemoProperty() {
        return memoProperty.get();
    }

    public StringProperty memoPropertyProperty() {
        return memoProperty;
    }

    public double getAmountProperty() {
        return amountProperty.get();
    }

    public DoubleProperty amountPropertyProperty() {
        return amountProperty;
    }

    public String getCommentProperty() {
        return commentProperty.get();
    }

    public StringProperty commentPropertyProperty() {
        return commentProperty;
    }

    /**
     * Compare using id property
     * @param o
     * @return true if id property is the same
     */
    public boolean equalsById(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccTransaction that = (FiccTransaction) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return true;
    }

    /**
     * Compare using all properties exept id
     * @param o input object
     * @return true if all properties (not counting id) agree
     */
    public boolean equalsWithoutId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccTransaction that = (FiccTransaction) o;

        if (Double.compare(that.amount, amount) != 0) return false;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;
        if (counterparty != null ? !counterparty.equals(that.counterparty) : that.counterparty != null) return false;
        return memo != null ? memo.equals(that.memo) : that.memo == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccTransaction that = (FiccTransaction) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;
        if (counterPartyId != null ? !counterPartyId.equals(that.counterPartyId) : that.counterPartyId != null)
            return false;
        if (counterparty != null ? !counterparty.equals(that.counterparty) : that.counterparty != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
        result = 31 * result + (counterPartyId != null ? counterPartyId.hashCode() : 0);
        result = 31 * result + (counterparty != null ? counterparty.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FiccTransaction{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", transaction='" + transaction + '\'' +
                ", counterPartyId='" + counterPartyId + '\'' +
                ", counterparty=" + counterparty +
                ", memo='" + memo + '\'' +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                '}';
    }

    public static Comparator<FiccTransaction> FiccTransactionByDataComparator
            = new Comparator<FiccTransaction>() {

        @Override
        public int compare(FiccTransaction t1, FiccTransaction t2) {
            return t1.getDate().compareTo(t2.getDate());
        }

    };

}
