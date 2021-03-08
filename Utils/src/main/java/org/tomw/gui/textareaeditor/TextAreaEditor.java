package org.tomw.gui.textareaeditor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class TextAreaEditor {
    private final static Logger LOGGER = Logger.getLogger(TextAreaEditor.class.getName());
    private static final String FXML_PAGE = "/TextAreaEditor.fxml";

    private AnchorPane page;
    private FXMLLoader loader = null;
    private TextAreaEditorController controller = null;

    private String title;
    private String text;

    AnchorPane getPage() {
        return page;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public TextAreaEditorController getController() {
        return controller;
    }

    public boolean okClicked(){
        return controller.isOkButtonPressed();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextAreaEditor(){}

    public TextAreaEditor(String title, String text){
        setTitle(title);
        setText(text);
        display();
    }

    public void display() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TextAreaEditor.class.getResource(FXML_PAGE));
            page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(getTitle());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTitle(getTitle());
            controller.setText(getText());

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            setText(controller.getText());
        } catch (IOException e) {
            LOGGER.error("Error occured ", e);
        }
    }
}
