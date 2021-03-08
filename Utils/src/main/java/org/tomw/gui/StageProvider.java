package org.tomw.gui;

import javafx.stage.Stage;

/**
 * provides primary stage for application
 */
public interface StageProvider {

    public Stage getPrimaryStage();

    public  void setPrimaryStage(Stage primaryStage);

}
