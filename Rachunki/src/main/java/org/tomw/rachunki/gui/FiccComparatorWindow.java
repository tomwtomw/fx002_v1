package org.tomw.rachunki.gui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.ficc.xls.FiccExcelReader;
import org.tomw.ficc.xls.FiccExcelRecord;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.ficc.FiccFileSelector;
import org.tomw.rachunki.ficc.FiccFileSelectorResult;
import org.tomw.utils.LocalDateUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.tomw.utils.TomwStringUtils.EOL;

/**
 * Class which displays window for comparing ficc files
 * Follows example from
 * https://o7planning.org/en/11533/opening-a-new-window-in-javafx
 */
public class FiccComparatorWindow {
    private final static Logger LOGGER = Logger.getLogger(FiccComparatorWindow.class.getName());

    private int windowWidth=1000;
    private int windowHeight=500;

    private Button closeWindowButton;

    private TextField file1TextField;
    private TextField file2TextField;

    private File selectedFile1=null;
    private File selectedFile2=null;

    TextArea summaryTextArea;
    TextArea newTransactionsTextArea;

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void display(){
        LOGGER.info("Display Ficc comparator window");

        Stage primaryStage = RachunkiMainRegistry.getRegistry().getPrimaryStage();

        GridPane gridPane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(15);
//        column1.setMaxWidth(getWindowWidth()*0.15);
//        column1.setMinWidth(getWindowWidth()*0.15);
        gridPane.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(80);
        gridPane.getColumnConstraints().add(column2);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(5);
//        column3.setMaxWidth(getWindowWidth()*0.05);
//        column3.setMinWidth(getWindowWidth()*0.05);
        gridPane.getColumnConstraints().add(column3);


        AtomicInteger rowCounter = new AtomicInteger(0);

        Label titleLabel = new Label("Compare ficc transaction files");
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setFont(new Font("Arial", 24));
        gridPane.add(titleLabel, 1, rowCounter.getAndIncrement());

        Label selectFileLabel1 = new Label("Old file:");
        selectFileLabel1.setStyle("-fx-font-weight: bold");
        gridPane.add(selectFileLabel1, 0, rowCounter.get());
        file1TextField = new TextField();
        file1TextField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(file1TextField, true);
        GridPane.setFillHeight(file1TextField, true);
        gridPane.add(file1TextField, 1, rowCounter.get());
        Button selectFile1Button = new Button("Select");
        selectFile1Button.setStyle("-fx-font-weight: bold");
        selectFile1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                LOGGER.info("selectFile1Button button clicked");
                selectFile1ButtonAction();
            }
        });
        gridPane.add(selectFile1Button, 2, rowCounter.get());
        rowCounter.incrementAndGet();

        Label selectFileLabel2 = new Label("New File:");
        selectFileLabel2.setStyle("-fx-font-weight: bold");
        gridPane.add(selectFileLabel2, 0, rowCounter.get());
        file2TextField = new TextField();
        gridPane.add(file2TextField, 1, rowCounter.get());
        Button selectFile2Button = new Button("Select");
        selectFile2Button.setStyle("-fx-font-weight: bold");
        selectFile2Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                LOGGER.info("selectFile2Button button clicked");
                selectFile2ButtonAction();
            }
        });
        gridPane.add(selectFile2Button, 2, rowCounter.get());
        rowCounter.incrementAndGet();

        Button compareButton = new Button("Compare");
        compareButton.setStyle("-fx-font-weight: bold");
        compareButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                LOGGER.info("Compare button clicked");
                compareFiles(selectedFile1,selectedFile2);
            }
        });
        gridPane.add(compareButton, 0, rowCounter.get());
        rowCounter.incrementAndGet();


        Label summary = new Label("Summary:");
        summary.setStyle("-fx-font-weight: bold");
        gridPane.add(summary, 0, rowCounter.get());
        summaryTextArea = new TextArea();
        gridPane.add(summaryTextArea, 1, rowCounter.get());
        rowCounter.incrementAndGet();

        Label newTransactions = new Label("New Transactions:");
        newTransactions.setStyle("-fx-font-weight: bold");
        gridPane.add(newTransactions, 0, rowCounter.get());
        newTransactionsTextArea = new TextArea();
        gridPane.add(newTransactionsTextArea, 1, rowCounter.get());
        rowCounter.incrementAndGet();

        closeWindowButton = new Button("Close");
        closeWindowButton.setStyle("-fx-font-weight: bold");
        closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                closeButtonAction();
            }
        });
        gridPane.add(closeWindowButton, 2, rowCounter.getAndIncrement());

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(gridPane);

        Scene secondScene = new Scene(secondaryLayout, getWindowWidth(), getWindowHeight());

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Ficc Transaction Comparator");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);

        newWindow.show();

    }

    /**
     * Compare csv files
     * @param transactionsFile1 first file
     * @param transactionsFile2 second file
     */
    private void compareFiles(File transactionsFile1, File transactionsFile2) {
        LOGGER.info("Compare files "+transactionsFile1+" "+transactionsFile2);
        if(transactionsFile1==null || transactionsFile2==null ){
            LOGGER.error("One of selected files is null");
        }

        List<FiccExcelRecord> listOfRecords = new ArrayList<>();
        FiccExcelReader reader = new FiccExcelReader();

        String records = "";
        String report="";

        try {
            LocalDate cutofDate = LocalDate.now().minus(2, ChronoUnit.MONTHS);

            listOfRecords = reader.getDifferenceAfterDate(
                    transactionsFile2,
                    transactionsFile1,
                    cutofDate
            );
            for(FiccExcelRecord record : listOfRecords){
                records = records+record+EOL;
                System.out.println(record);
            }

            report = reader.prepareReport(transactionsFile2,
                    transactionsFile1,
                    cutofDate
            );
            System.out.println("report=\n"+report);
        } catch (Exception e) {
            LOGGER.error("Exception occured",e);
        }

        this.summaryTextArea.setText(report);
        this.newTransactionsTextArea.setText(records);

    }

    /**
     * select file 1 for comparison
     */
    private void selectFile1ButtonAction() {

        Stage stage =  getCurrentStage();

        FiccFileSelector fileSelector = new FiccFileSelector();
        FiccFileSelectorResult result = fileSelector.selectFile(1,stage);
        LOGGER.info("File selector result OK clicked="+ result.isOkClicked());
        LOGGER.info("File selector result file selected="+ result.getSelectedFile());
        if(result.isOkClicked()){
            this.file1TextField.setText(result.getSelectedFile().toString());
            this.selectedFile1=result.getSelectedFile();
        }
    }

    /**
     * select file 2 for comparison
     */
    private void selectFile2ButtonAction() {

        Stage stage =  getCurrentStage();

        FiccFileSelector fileSelector = new FiccFileSelector();
        FiccFileSelectorResult result = fileSelector.selectFile(2,stage);
        LOGGER.info("File selector result OK clicked="+ result.isOkClicked());
        LOGGER.info("File selector result file selected="+ result.getSelectedFile());

        if(result.isOkClicked()){
            this.file2TextField.setText(result.getSelectedFile().toString());
            this.selectedFile2=result.getSelectedFile();
        }
    }

    private Stage getCurrentStage(){
        return (Stage) closeWindowButton.getScene().getWindow();
    }

    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage =  getCurrentStage();
        stage.close();
    }
}
