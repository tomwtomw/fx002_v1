package org.tomw.gui.textareaeditor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class TekstAreaEditorMain extends Application {
    private final static Logger LOGGER = Logger.getLogger(TekstAreaEditorMain.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                //TextAreaEditor editor = new TextAreaEditor();
//                editor.setTitle("Demo of TextAreaEditor");
//                editor.setText("Input text");
                //editor.init();

                TextAreaEditor editor = new TextAreaEditor("Title of demo","fill the text...");
                System.out.println("Ok clicked: "+editor.okClicked());
                System.out.println("Text entered = "+editor.getText());
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        LOGGER.info("Driver of TekstAreaEditor starts...");

        launch(args);
    }
}
