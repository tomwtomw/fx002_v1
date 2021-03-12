/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tomw.czeki.CzekiRegistry;

/**
 *
 * @author tomw
 */
public class CounterParty {
    
    public static String ID = "id";
    public static String NAME = "name";
    public static String SHORT_NAME = "Short Name";
    public static String OTHER_NAMES = "Other Names";
    public static String COMMENT = "Comment";
    
    private StringProperty idProperty;
    private StringProperty nameProperty;
    private StringProperty shortNameProperty;
    private List<String> otherNames;
    private StringProperty commentProperty;
    
    public CounterParty() {
        this(UUID.randomUUID().toString(), "undefined short name", "undefined name", new ArrayList<>());
    }
    
    public CounterParty(String shortName) {
        this(UUID.randomUUID().toString(), shortName, shortName, new ArrayList<>());
    }
    
    public CounterParty(String shortName, String name, List<String> otherNames) {
        this(UUID.randomUUID().toString(), shortName, name, otherNames);
    }
    
    public CounterParty(String id, String shortName, String name, List<String> otherNames) {
        this.idProperty = new SimpleStringProperty(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.shortNameProperty = new SimpleStringProperty(shortName);
        this.otherNames = otherNames;
        this.commentProperty = new SimpleStringProperty(CzekiRegistry.BLANK);
    }
    
    public CounterParty(String id, String shortName, String name, List<String> otherNames, String comment) {
        this.idProperty = new SimpleStringProperty(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.shortNameProperty = new SimpleStringProperty(shortName);
        this.otherNames = otherNames;
        this.commentProperty = new SimpleStringProperty(comment);
    }

   
    
    /**
     * parse counterparty from
     *
     * @param inputString
     * @return
     */
    public static CounterParty fromCsv(String inputString) {
        String[] inputSplit = inputString.split(CzekiRegistry.CSV_SEPARATOR);
        String shortName = inputSplit[0];
        String fullName = inputSplit[1];
        List<String> otherNames = new ArrayList<>();
        if (inputSplit.length > 2) {
            for (int i = 2; i < inputSplit.length; i = i + 1) {
                otherNames.add(inputSplit[i]);
            }
        }
        return new CounterParty(UUID.randomUUID().toString(), shortName, fullName, otherNames);
    }

    @Override
    public String toString(){
        return toJsonString();
    }
    
    /**
     * Provide string representation of counterparty
     *
     * @return
     */
    public String toCsv() {
        String result = shortNameProperty.get() + CzekiRegistry.CSV_SEPARATOR + nameProperty.get();
        for (String otherName : otherNames) {
            result = result + CzekiRegistry.CSV_SEPARATOR + otherName;
        }
        return result;
    }

    /**
     * Convert counterparty object into JSON object
     *
     * @return
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, this.idProperty.get());
        json.put(NAME, this.nameProperty.get());
        json.put(SHORT_NAME, this.shortNameProperty.get());
        JSONArray jsonArray = new JSONArray();
        for (String otherName : this.otherNames) {
            jsonArray.add(otherName);
        }
        json.put(OTHER_NAMES, jsonArray);
        json.put(COMMENT, this.commentProperty.get());
        return json;
    }

    /**
     * convert counterparty object to JSONString
     *
     * @return
     */
    public String toJsonString() {
        return this.toJson().toString();
    }

    /**
     * parse JSONString
     *
     * @param jsonString
     * @return
     * @throws ParseException
     */
    public static CounterParty fromJsonString(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        return fromJson(json);
    }
    
    public static CounterParty fromJson(JSONObject json) {
        if (json.keySet().contains(COMMENT)) {
            return new CounterParty(
                    (String) json.get(ID),
                    (String) json.get(SHORT_NAME),
                    (String) json.get(NAME),
                    (List<String>) json.get(OTHER_NAMES),
                    (String) json.get(COMMENT)
            );
        } else {
            return new CounterParty(
                    (String) json.get(ID),
                    (String) json.get(SHORT_NAME),
                    (String) json.get(NAME),
                    (List<String>) json.get(OTHER_NAMES),
                    CzekiRegistry.BLANK
            );
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.idProperty.get());
        hash = 71 * hash + Objects.hashCode(this.nameProperty.get());
        hash = 71 * hash + Objects.hashCode(this.shortNameProperty.get());
        String allNames = "";
        for (String s : this.otherNames) {
            allNames = allNames + s;
        }
        hash = 71 * hash + Objects.hashCode(this.commentProperty.get());
        hash = 71 * hash + Objects.hashCode(allNames);
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
        final CounterParty other = (CounterParty) obj;
        if (!Objects.equals(this.idProperty.get(), other.idProperty.get())) {
            return false;
        }
        
        return equalNonIdFields(obj);
    }
    
    public boolean equalNonIdFields(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final CounterParty other = (CounterParty) obj;
        
        if (!Objects.equals(this.nameProperty.get(), other.nameProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.shortNameProperty.get(), other.shortNameProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.otherNames, other.otherNames)) {
            return false;
        }
        return Objects.equals(this.commentProperty.get(), other.commentProperty.get());
    }
    
    public StringProperty getIdProperty() {
        return idProperty;
    }
    
    public String getId() {
        return idProperty.get();
    }
    
    public StringProperty getNameProperty() {
        return nameProperty;
    }
    
    public String getName() {
        return nameProperty.get();
    }
    
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }
    
    public void setName(String name) {
        setNameProperty(new SimpleStringProperty(name));
    }
    
    public StringProperty getShortNameProperty() {
        return shortNameProperty;
    }
    
    public String getShortName() {
        return shortNameProperty.get();
    }
    
    public void setShortNameProperty(StringProperty shortNameProperty) {
        this.shortNameProperty = shortNameProperty;
    }
    
    public void setShortNameProperty(String shortName) {
        setShortNameProperty(new SimpleStringProperty(shortName));
    }
    
    public List<String> getOtherNames() {
        return otherNames;
    }
    
    public void setOtherNames(List<String> otherNames) {
        this.otherNames = otherNames;
    }
    
    public StringProperty getCommentProperty() {
        return commentProperty;
    }
    
    public void setCommentProperty(StringProperty commentProperty) {
        this.commentProperty = commentProperty;
    }
    
    public String getComment() {
        return commentProperty.get();
    }
    
    public void setComment(String comment) {
        this.commentProperty.set(comment);
    }
    
}
