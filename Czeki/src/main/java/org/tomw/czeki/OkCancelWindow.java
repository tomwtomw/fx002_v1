/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Display an OK/Cancel dialog window
 * @author tomw
 */
public class OkCancelWindow {

    private final String title;
    private final String headerText;
    private final String contentText;

    public OkCancelWindow(String title, String headerText, String contentText) {
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
    }

    public boolean display() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(this.title);
        alert.setHeaderText(this.headerText);
        alert.setContentText(this.contentText);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

}
