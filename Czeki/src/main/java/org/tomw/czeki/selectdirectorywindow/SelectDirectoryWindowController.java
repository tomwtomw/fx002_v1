/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.selectdirectorywindow;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.tomw.czeki.FileSelectors;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class SelectDirectoryWindowController implements Initializable {

    private File initialDirectory;
    private File selectedDirectory;

    @FXML
    Button okButton;
    private boolean okClicked = false;

    @FXML
    Button selectDirectoryButton;

    @FXML
    private Label selectedDirectoryLabel;

    @FXML
    private Label titleLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setTitle(String title) {
        this.titleLabel.setText(title);
    }

    public void setInitialDirectory(File initialDirectory) {
        this.initialDirectory = initialDirectory;
        this.selectedDirectoryLabel.setText(this.initialDirectory.toString());
    }

    public boolean okButtonClicked() {
        return okClicked;
    }

    public File getSelectedDirectory() {
        return this.selectedDirectory;
    }

    @FXML
    public void selectDirectoryButtonAction() {
        System.out.println("selectDirectoryButtonAction");
        File newSelectedDirectory = FileSelectors.selectDirectory();
        if (newSelectedDirectory != null) {
            if (!newSelectedDirectory.equals(initialDirectory)) {
                selectedDirectory=newSelectedDirectory;
                selectedDirectoryLabel.setText(selectedDirectory.toString());
            }
        }
    }

    @FXML
    public void okButtonAction() {
        okClicked = true;
        closeWindow();
    }

    @FXML
    public void cancelButtonAction() {
        okClicked = false;
        closeWindow();
    }

    public void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
