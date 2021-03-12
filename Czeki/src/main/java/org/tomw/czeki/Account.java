/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import org.tomw.czeki.transactionoverview.TransactionUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tomw.czeki.dao.CzekiDao;
import org.tomw.czeki.dao.CzekiDaoJsonImpl;
import org.tomw.czeki.dao.DataFormatException;
import org.tomw.czeki.entities.Transaction;


/**
 * Account Entity
 *
 * @author tomw
 */
public class Account {

    public static String ID = "id";
    public static String NAME = "name";
    public static String SHORT_NAME = "Short Name";
    public static String COMMENT = "Comment";
    public static String FILE_NAME = "File Name";
    public static String IMAGE_DIRECTORY = "Image Directory";

    private StringProperty idProperty;
    private StringProperty nameProperty;
    private StringProperty shortNameProperty;
    private StringProperty commentProperty;
    private StringProperty fileNameProperty;
    private StringProperty imageDirectoryProperty;

    private CzekiDao dao;


    public CzekiDao getDao() {
        return dao;
    }
    
    public Account() {
        this(UUID.randomUUID().toString(), "undefined short name", "undefined name", "undefined file name", "undefined image directory");
    }

    public Account(String shortName)  {
        this(UUID.randomUUID().toString(), shortName, "undefined name", "undefined file name", "undefined image directory");
    }

    public Account(String id, String shortName, String name, String fileNameString, String imageDirectoryString)  {
        this.idProperty = new SimpleStringProperty(id);
        this.shortNameProperty = new SimpleStringProperty(shortName);
        this.nameProperty = new SimpleStringProperty(name);
        this.commentProperty = new SimpleStringProperty(CzekiRegistry.BLANK);
        this.fileNameProperty = new SimpleStringProperty(fileNameString);
        this.imageDirectoryProperty = new SimpleStringProperty(imageDirectoryString);

        this.setDao();
    }    
    
    private void setDao()  {
        this.dao = new CzekiDaoJsonImpl(new File(fileNameProperty.get()), new File(imageDirectoryProperty.get()));
    }

    
    public double getSumClearedTransactions(){
        return TransactionUtils.calculateSum(this.dao.getClearedTransactions());
    }
    
    public double getSumAllTransactions(){
        return TransactionUtils.calculateSum(this.dao.getTransactions().values());
    }
    
    
    
    /**
     * convert account object to jsonobject
     *
     * @return
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(ID, this.idProperty.get());
        json.put(NAME, this.nameProperty.get());
        json.put(SHORT_NAME, this.shortNameProperty.get());
        json.put(FILE_NAME, this.fileNameProperty.get());
        json.put(IMAGE_DIRECTORY, this.imageDirectoryProperty.get());
        json.put(COMMENT, this.commentProperty.get());
        return json;
    }

    /**
     * convert Account object to json string
     *
     * @return
     */
    public String toJsonString() {
        return this.toJson().toString();
    }

    /**
     * create Account object from json object
     *
     * @param json
     * @return
     */
    public static Account fromJson(JSONObject json) {
        return new Account(
                (String) json.get(ID),
                (String) json.get(SHORT_NAME),
                (String) json.get(NAME),
                (String) json.get(FILE_NAME),
                (String) json.get(IMAGE_DIRECTORY)
        );
    }

    /**
     * create account object from json string
     *
     * @param jsonString
     * @return
     * @throws ParseException
     * @throws java.io.IOException
     * @throws org.tomw.czeki.dao.DataFormatException
     */
    public static Account fromJsonString(String jsonString) throws ParseException, IOException, DataFormatException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        return fromJson(json);
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.idProperty.get());
        hash = 47 * hash + Objects.hashCode(this.nameProperty.get());
        hash = 47 * hash + Objects.hashCode(this.shortNameProperty.get());
        hash = 47 * hash + Objects.hashCode(this.commentProperty.get());
        hash = 47 * hash + Objects.hashCode(this.fileNameProperty.get());
        hash = 47 * hash + Objects.hashCode(this.imageDirectoryProperty.get());
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
        final Account other = (Account) obj;
        if (!Objects.equals(this.idProperty.get(), other.idProperty.get())) {
            return false;
        }

        return true;
    }

    /**
     * compare two accounts by their non id fields
     *
     * @param obj
     * @return
     */
    public boolean equalNonIdFields(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;

        if (!Objects.equals(this.nameProperty.get(), other.nameProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.shortNameProperty.get(), other.shortNameProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.commentProperty.get(), other.commentProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.fileNameProperty.get(), other.fileNameProperty.get())) {
            return false;
        }
        if (!Objects.equals(this.imageDirectoryProperty.get(), other.imageDirectoryProperty.get())) {
            return false;
        }
        return true;
    }

    public StringProperty getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(StringProperty idProperty) {
        this.idProperty = idProperty;
    }

    public String getId() {
        return this.getIdProperty().get();
    }

    public void setId(String id) {
        this.idProperty.set(id);
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    public String getName() {
        return this.getNameProperty().get();
    }

    public void setName(String name) {
        this.nameProperty.set(name);
    }

    public StringProperty getShortNameProperty() {
        return shortNameProperty;
    }

    public void setShortNameProperty(StringProperty shortNameProperty) {
        this.shortNameProperty = shortNameProperty;
    }

    public String getShortName() {
        return this.getShortNameProperty().get();
    }

    public void setShortName(String shortName) {
        this.shortNameProperty.set(shortName);
    }

    public StringProperty getCommentProperty() {
        return commentProperty;
    }

    public void setCommentProperty(StringProperty commentProperty) {
        this.commentProperty = commentProperty;
    }

    public String getComment() {
        return this.getCommentProperty().get();
    }

    public void setComment(String comment) {
        this.commentProperty.set(comment);
    }

    public StringProperty getFileNameProperty() {
        return fileNameProperty;
    }

    public void setFileNameProperty(StringProperty fileNameProperty) {
        this.fileNameProperty = fileNameProperty;
        this.setDao();
    }

    public String getFileName() {
        return this.getFileNameProperty().get();
    }

    public void setFileName(String fileName) {
        this.fileNameProperty.set(fileName);
        this.setDao();
    }

    public StringProperty getImageDirectoryProperty() {
        return imageDirectoryProperty;
    }

    public void setImageDirectoryProperty(StringProperty imageDirectoryProperty) {
        this.imageDirectoryProperty = imageDirectoryProperty;
    }

    public String getImageDirectoryString() {
        return this.getImageDirectoryProperty().get();
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectoryProperty.set(imageDirectory);
    }

    /**
     * short summary of the account
     *
     * @return
     */
    public String toStringSummary() {
        String result = "";
        result = result + ID + " " + this.idProperty.get()+CzekiRegistry.EOL;
        result = result + NAME + " " + this.nameProperty.get()+CzekiRegistry.EOL;
        result = result + SHORT_NAME + " " + this.shortNameProperty.get()+CzekiRegistry.EOL;
        result = result + FILE_NAME + " " + this.fileNameProperty.get()+CzekiRegistry.EOL;
        result = result + IMAGE_DIRECTORY + " " + this.imageDirectoryProperty.get()+CzekiRegistry.EOL;
        return result;
    }

    public File getImageDirectory() {
        return new File(getImageDirectoryString());
    }

    
}
