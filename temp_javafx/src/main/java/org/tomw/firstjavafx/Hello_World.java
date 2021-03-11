package org.tomw.firstjavafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class Hello_World extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn1=new Button("Say, Hello World");

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                System.out.println("hello world");
            }
        });

        StackPane root=new StackPane();
        root.getChildren().add(btn1);
        Scene scene=new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("First JavaFX Application");
        primaryStage.show();
    }

}