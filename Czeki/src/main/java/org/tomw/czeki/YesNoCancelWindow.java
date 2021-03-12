/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 * two option window
 * @author tomw
 */
public class YesNoCancelWindow {

    private final String title;
    private final String headerText;
    private final String contentText;

    public static final String SAVE = "Save";
    public static final String DISCARD = "Discard";
    public static final String CANCEL = "Cancel";

    public YesNoCancelWindow(String title, String headerText, String contentText) {
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
    }

    public String display() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(this.title);
        alert.setHeaderText(this.headerText);
        alert.setContentText(this.contentText);

        ButtonType buttonTypeOne = new ButtonType(SAVE);
        ButtonType buttonTypeTwo = new ButtonType(DISCARD);
        ButtonType buttonTypeCancel = new ButtonType(CANCEL, ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return SAVE;
        } else if (result.get() == buttonTypeTwo) {
            return DISCARD;
        } else {
            return CANCEL;
        }
    }

}
