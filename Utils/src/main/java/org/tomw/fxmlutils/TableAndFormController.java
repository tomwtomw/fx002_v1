package org.tomw.fxmlutils;

import javafx.stage.Stage;

import java.util.List;

public interface TableAndFormController<T> {

    void setTitle(String title);

    T getCurrentEntity();

    void setCurrentEntity(T e);

    List<T> getDisplayedEntities();

    void perform_newButtonAction();

    void perform_saveButtonAction();

    void perform_deleteButtonAction();

    void perform_addButtonAction();

    void perform_removeButtonAction();

    void perform_clearButtonAction();

    void perform_cancelButtonAction();

    void perform_okButtonAction();

    void initTable();

    void displayAllEntities();

    void updateDisplayedEntity();

    void displayEntity(T e);

    void reDisplayCurrentEntity();

    default void resetDisplay(){
        clearForm();
        resetErrorFields();
    }

    void resetErrorFields();

    void clearForm();

    default  void disableForm() {
        setDisabledFlagForControls(true);
    }

    default void enableForm() {
        setDisabledFlagForControls(false);
    }

    void perform_formHasBeenEdited();

    void setEdited(boolean edited);

    boolean formHasUnsavedChanges();

    void setDisabledFlagForControls(boolean flag);

    void setSaveButtonEnabled(boolean enable);

    void setButtonsForStandaloneMode(boolean standaloneMode);

    default boolean validateFormInput() { return true; }

    void refresh();

    void setStandaloneMode(boolean mode);

    boolean isStandaloneMode();

    boolean isLimitedMode();

    void setLimitedMode(boolean limitedMode);

    boolean okClicked();

    void closeWindow();

    void createAndDisplayNewEntity();

    void saveCurrentEntity();

    void formHasNotBeenEdited();

    void saveButtonEnable(boolean b);

    Stage getCurrentStage();
}
