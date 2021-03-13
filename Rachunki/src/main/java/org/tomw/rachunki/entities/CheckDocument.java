package org.tomw.rachunki.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.tomw.documentfile.DocumentFile;
import org.tomw.identifiable.Identifiable;

import javax.persistence.*;

import static org.tomw.utils.TomwStringUtils.BLANK;

@Entity
@Table
public class CheckDocument implements Identifiable {
    private final static Logger LOGGER = Logger.getLogger(CheckDocument.class.getName());

    public static final String FRONT = "F";
    public static final String BACK = "B";

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty checkNumber = new SimpleStringProperty(BLANK);
    private final StringProperty comment = new SimpleStringProperty(BLANK);


    private final StringProperty memo = new SimpleStringProperty(BLANK);

    private DocumentFile checkImageFront;
    private DocumentFile checkImageBack;

    public CheckDocument(){}

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
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @Column
    public String getMemo() {
        return memo.get();
    }

    public StringProperty memoProperty() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo.set(memo);
    }

    @OneToOne
    public DocumentFile getCheckImageFront() {
        return checkImageFront;
    }

    public void setCheckImageFront(DocumentFile checkImageFront) {
        this.checkImageFront = checkImageFront;
    }

    @OneToOne
    public DocumentFile getCheckImageBack() {
        return checkImageBack;
    }

    public DocumentFile getCheckImage(String side){
        if (FRONT.equals(side)) {
            return getCheckImageFront();
        }else{
            if (BACK.equals(side)) {
                return getCheckImageBack();
            }else{
                throw new RuntimeException("Unknonw check side: "+side);
            }
        }
    }

    public void setCheckImage(String side,DocumentFile checkImage ){
        if (FRONT.equals(side)) {
            setCheckImageFront(checkImage);
        }else{
            if (BACK.equals(side)) {
                setCheckImageBack(checkImage);
            }else{
                throw new RuntimeException("Unknonw check side: "+side);
            }
        }
    }

    public void setCheckImageBack(DocumentFile checkImageBack) {
        this.checkImageBack = checkImageBack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckDocument that = (CheckDocument) o;

        return id.get() == that.getId();
    }

    @Override
    public int hashCode() {
        return (new Integer(id.get())).hashCode();
    }

    @Override
    public String toString() {
        return "CheckDocument{" +
                "id=" + id +
                ", checkNumber=" + checkNumber +
                '}';
    }
}
