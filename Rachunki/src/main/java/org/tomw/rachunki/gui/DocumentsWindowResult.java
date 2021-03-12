package org.tomw.rachunki.gui;

import org.tomw.documentfile.DocumentFile;
import org.tomw.rachunki.entities.Transakcja;

import java.util.Collection;

public class DocumentsWindowResult {

    private boolean okClicked = false;

    private DocumentFile selectedEntity=null;

    private Collection<DocumentFile> displayedEntities = null;

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public DocumentFile getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(DocumentFile selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public Collection<DocumentFile> getDisplayedEntities() {
        return displayedEntities;
    }

    public void setDisplayedEntities(Collection<DocumentFile> displayedEntities) {
        this.displayedEntities = displayedEntities;
    }
}
