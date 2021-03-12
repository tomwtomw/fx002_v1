package org.tomw.rachunki.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
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

public class PrimaryAccountSummaryWindowView {

    private static int XSIZE=600;
    private static int YSIZE=500;

    private BorderPane borderPane = new BorderPane();

    private AnchorPane topPane = new AnchorPane();
    private AnchorPane leftPane = new AnchorPane();
    private AnchorPane rightPane = new AnchorPane();
    private AnchorPane bottomPane = new AnchorPane();
    private AnchorPane centerPane = new AnchorPane();

    private AccountsSummaryTableView tableView;

    public AccountsSummaryTableView getTableView() {
        return tableView;
    }

    public void initialize() {

        createBorderPanes();

        SplitPane accountsSplitPane = new SplitPane();

        accountsSplitPane.setOrientation(Orientation.HORIZONTAL);
        accountsSplitPane.setDividerPositions(0.5f);

        tableView = new AccountsSummaryTableView();

        accountsSplitPane.getItems().addAll(
                AnchorPaneWrapper.wrapInAnchorPane(tableView.getTableView())
        );
        AnchorPaneWrapper.anchor(accountsSplitPane);

        centerPane.getChildren().add(accountsSplitPane);

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

        stage.setTitle("Primary Accounts Summary");
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
        bottomPane.getChildren().addAll(new CloseWindowButton());
    }
    
}
