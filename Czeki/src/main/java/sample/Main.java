package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tomw.czeki.imageview.CheckImage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void xmain(String[] args) {
        launch(args);
    }

    public static void main(String[] args){
//        System.out.println(CheckImage.filanemeIsNewStyle("2017-08-03-4027.gif"));
//        System.out.println(CheckImage.filanemeIsNewStyle("2017-08-03-4027"));
    }
}
