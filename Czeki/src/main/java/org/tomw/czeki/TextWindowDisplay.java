/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import javafx.scene.control.Alert;

/**
 *
 * @author tomw
 */
public class TextWindowDisplay {
    private final String title;
    private final String text;
    public TextWindowDisplay(String title,String text){
        this.title=title;
        this.text=text;
    }
    public void display(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
