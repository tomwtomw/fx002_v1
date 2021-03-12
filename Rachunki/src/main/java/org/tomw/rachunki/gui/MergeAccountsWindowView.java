package org.tomw.rachunki.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tomw.fxmlutils.AnchorPaneWrapper;
import org.tomw.fxmlutils.buttons.CloseWindowButton;
import org.tomw.fxmlutils.buttons.LeftArrowButtonSmall;
import org.tomw.fxmlutils.buttons.RightArrowButtonSmall;
import org.tomw.fxmlutils.elements.controls.ExitButton;
import org.tomw.rachunki.entities.Konto;

public class MergeAccountsWindowView {

    private static int XSIZE=1200;
    private static int YSIZE=1000;

    private BorderPane borderPane = new BorderPane();

    private AnchorPane topPane = new AnchorPane();
    private AnchorPane leftPane = new AnchorPane();
    private AnchorPane rightPane = new AnchorPane();
    private AnchorPane bottomPane = new AnchorPane();
    private AnchorPane centerPane = new AnchorPane();

    private MergeAccountsForm mergeAccountsForm;
    private AccountsTableView tableViewLeft ;
    private AccountsTableView tableViewRight ;

    public MergeAccountsForm getMergeAccountsForm() {
        return mergeAccountsForm;
    }

    public AccountsTableView getTableViewLeft() {
        return tableViewLeft;
    }

    public AccountsTableView getTableViewRight() {
        return tableViewRight;
    }

    public void initialize() {

        createBorderPanes();

        SplitPane centerSplitpane = new SplitPane();
        centerSplitpane.setOrientation(Orientation.VERTICAL);

        SplitPane accountsSplitPane = new SplitPane();

        accountsSplitPane.setOrientation(Orientation.HORIZONTAL);
        accountsSplitPane.setDividerPositions(0.5f);

        tableViewLeft = new AccountsTableView();
        tableViewRight = new AccountsTableView();

        accountsSplitPane.getItems().addAll(
                AnchorPaneWrapper.wrapInAnchorPane(tableViewLeft.getTableView()),
                AnchorPaneWrapper.wrapInAnchorPane(tableViewRight.getTableView())
        );

        mergeAccountsForm = new MergeAccountsForm();
        mergeAccountsForm.create();

        centerSplitpane.getItems().addAll(
                AnchorPaneWrapper.wrapInAnchorPane(accountsSplitPane),
                AnchorPaneWrapper.wrapInAnchorPane(mergeAccountsForm)
        );
        centerSplitpane.setDividerPositions(0.5f);
        AnchorPaneWrapper.anchor(centerSplitpane);

        centerPane.getChildren().add(centerSplitpane);

        borderPane.setBottom(bottomPane);
        borderPane.setTop(topPane);
        borderPane.setLeft(leftPane);
        borderPane.setRight(rightPane);
        borderPane.setCenter(centerPane);
    }

    public void showAndWait() {

        AnchorPane anchorpane = AnchorPaneWrapper.wrapInAnchorPane(borderPane);

        Scene scene = new Scene(anchorpane);

        Stage stage = new Stage();

        stage.setTitle("Merge Accounts");
        stage.initModality(Modality.APPLICATION_MODAL);

        //stage.initStyle(StageStyle.DECORATED);

        //stage.initStyle(StageStyle.UNDECORATED);
        //stage.initStyle(StageStyle.TRANSPARENT);
        //stage.initStyle(StageStyle.UNIFIED);
        stage.initStyle(StageStyle.UTILITY);

        stage.setWidth(XSIZE);
        stage.setHeight(YSIZE);

        stage.setScene(scene);

        stage.show();
    }

    private void createBorderPanes() {

        topPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        topPane.getChildren().add(new Label("TOP"));

        leftPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        leftPane.getChildren().add(new Label("leftPane"));

        rightPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        rightPane.getChildren().add(new Label("rightPane"));

        bottomPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        bottomPane.getChildren().addAll(new LeftArrowButtonSmall(), new CloseWindowButton());
    }

    public void showAndWait2() {

//        VBox vBox = new VBox(new Label("A JavaFX Label"));
//        Scene scene = new Scene(vBox);


        Label helloWorld = new Label("Hello world");
        Label top = new Label("Top");
        Label bottom = new Label("Bottom");
        Label left = new Label("Left");
        Label right = new Label("Right");

        GridPane gridpane = new GridPane();
        //gridpane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(50, 50, 50));

        Button exitButton = new ExitButton();
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setConstraints(anchorPane, 0,0);
        gridpane.getChildren().add(anchorPane);

        GridPane.setConstraints(exitButton, 5,0);
        gridpane.getChildren().add(exitButton);
        AnchorPane.setTopAnchor(gridpane, 1.0);
        //AnchorPane.setLeftAnchor(gridpane, 1.0);
        AnchorPane.setRightAnchor(gridpane, 1.0);
        AnchorPane.setBottomAnchor(gridpane, 1.0);

        AnchorPane gridPaneWrapper = new AnchorPane();
        gridPaneWrapper.getChildren().add(gridpane);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(helloWorld);
        borderPane.setBottom(gridPaneWrapper);
        borderPane.setTop(top);
        borderPane.setLeft(left);
        borderPane.setRight(right);


        AnchorPane.setTopAnchor(borderPane, 10.0);
        AnchorPane.setLeftAnchor(borderPane, 10.0);
        AnchorPane.setRightAnchor(borderPane, 65.0);
        AnchorPane.setBottomAnchor(borderPane, 10.0);

        AnchorPane anchorpane = new AnchorPane();
        anchorpane.getChildren().add(borderPane);


        Scene scene = new Scene(anchorpane);

        Stage stage = new Stage();

        stage.setTitle("Merge Accounts");
        stage.initModality(Modality.APPLICATION_MODAL);

        //stage.initStyle(StageStyle.DECORATED);

        //stage.initStyle(StageStyle.UNDECORATED);
        //stage.initStyle(StageStyle.TRANSPARENT);
        //stage.initStyle(StageStyle.UNIFIED);
        stage.initStyle(StageStyle.UTILITY);

        stage.setWidth(XSIZE);
        stage.setHeight(YSIZE);

        stage.setScene(scene);

        stage.show();

    }

    public void clearFormLeft() {
        getMergeAccountsForm().clearFormLeft();
    }

    public void clearFormRight() {
        getMergeAccountsForm().clearFormRight();
    }

    public void clearFormCenter() {
        getMergeAccountsForm().clearFormCenter();
    }
}
