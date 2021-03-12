package org.tomw.guiutils.documentgalery.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ort.tomw.guiutils.entities.imagefile.ImageFile;

import java.util.Iterator;

public class DocumentService {
    private ObservableList<ImageFile> observableListOfDocuments = FXCollections.observableArrayList();

    public ObservableList<ImageFile> getObservableListOfDocuments() {
        return observableListOfDocuments;
    }

    public void setObservableListOfDocuments(ObservableList<ImageFile> observableListOfDocuments) {
        this.observableListOfDocuments = observableListOfDocuments;
    }

    /**
     * main constructor
     * @param list
     */
    public DocumentService(ObservableList<ImageFile> list){
        setObservableListOfDocuments(list);
    }

    public boolean remove(ImageFile document){
        return observableListOfDocuments.remove(document);
    }

    public ImageFile remove(String id){
        Iterator<ImageFile> iter = observableListOfDocuments.iterator();
        while(iter.hasNext()){
            ImageFile document = (ImageFile)iter.next();
            if(document.getId().equals(id)){
                iter.remove();
                return document;
            }
        }
        return null;
    }

    public void add(ImageFile document){
        getObservableListOfDocuments().add(document);
    }
}
