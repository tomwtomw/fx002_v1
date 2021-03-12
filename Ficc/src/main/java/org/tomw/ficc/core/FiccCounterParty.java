package org.tomw.ficc.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONObject;

import java.time.LocalDate;

/**
 * Created by tomw on 7/22/2017.
 */
public class FiccCounterParty {
    public static final String ID="Id";
    public static final String NAME="Name";
    public static final String COMMENT = "Comment";

    public static final String BLANK = "";

    private String id;
    private String name;
    private String comment;

    private StringProperty idProperty = new SimpleStringProperty(BLANK);
    private StringProperty nameProperty = new SimpleStringProperty(BLANK);
    private StringProperty commentProperty = new SimpleStringProperty(BLANK);

    public FiccCounterParty(String name){
        setName(name);
    }

    @JsonCreator
    public FiccCounterParty(
            @JsonProperty(ID) String idIn,
            @JsonProperty(NAME) String nameIn,
            @JsonProperty(COMMENT) String commentIn
           ) {
        setName(nameIn);
        setId(idIn);
        setComment(commentIn);
    }

    /**
     * Convert cpty to json
     * @return cpty as json
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(COMMENT, getComment());
        return json;
    }

    @Override
    public String toString() {
        return "FiccCounterParty{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public boolean equalsByName(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccCounterParty that = (FiccCounterParty) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccCounterParty that = (FiccCounterParty) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.idProperty.set(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty.set(name);
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

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public String getCommentProperty() {
        return commentProperty.get();
    }

    public StringProperty getCommentPropertyProperty() {
        return commentProperty;
    }

    public StringProperty commentPropertyProperty() {
        return commentProperty;
    }
}
