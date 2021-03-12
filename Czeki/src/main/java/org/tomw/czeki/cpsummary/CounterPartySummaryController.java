/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.cpsummary;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.utils.TomwStringUtils;

/**
 * FXML Controller class
 *
 * Will control displaying summary of amounts per counterparty for different
 * time periods
 *
 * @author tomw
 */
public class CounterPartySummaryController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(CounterPartySummaryController.class.getName());

    private boolean okClicked = false;

    private ObservableList<Transaction> listOfTransactions;
    private final Map<CounterParty, CounterPartySummary> counterPartiesSummariesMap = new HashMap<>();

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<CounterPartySummary> counterPartySummaryTable;
    @FXML
    private TableColumn<CounterPartySummary, String> shortNameColumn;
    @FXML
    private TableColumn<CounterPartySummary, String> fullNameColumn;
    @FXML
    private TableColumn<CounterPartySummary, String> sumClearedColumn;
    @FXML
    private TableColumn<CounterPartySummary, String> sumAllColumn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        fullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        sumClearedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        sumAllColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        shortNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCounterParty().getShortNameProperty());
        fullNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCounterParty().getNameProperty());
        sumClearedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumCleared())));
        sumAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumAll())));

        counterPartySummaryTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends CounterPartySummary> observable, CounterPartySummary oldValue, CounterPartySummary newValue) -> {
                    showCounterPartySummaryDetails(newValue);
                });

        setTableContent();

        redisplay();
    }

    @FXML
    public void okButtonAction() {
        LOGGER.info("okButtonAction");
        okClicked = true;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    void setTransactions(ObservableList<Transaction> transactions) {
        listOfTransactions = transactions;
    }

    void initCounterPartiesTable() {
        LOGGER.info("initCounterPartiesTable");
        for (Transaction t : listOfTransactions) {
            CounterParty cp = t.getCounterParty();
            if (!counterPartiesSummariesMap.keySet().contains(cp)) {
                CounterPartySummary cps = new CounterPartySummary(cp);
                counterPartiesSummariesMap.put(cp, cps);
                LOGGER.info("cps=" + cps);
            }
            counterPartiesSummariesMap.get(cp).addTransaction(t);
        }
        setTableContent();
    }

    private void showCounterPartySummaryDetails(CounterPartySummary newValue) {
        //TODO implement
    }

    private void redisplay() {
        //TODO implement
    }

    private void setTableContent() {
        ObservableList<CounterPartySummary> listOfCounterPartySummaries = FXCollections.observableArrayList();
        listOfCounterPartySummaries.addAll(counterPartiesSummariesMap.values());
        counterPartySummaryTable.setItems(listOfCounterPartySummaries);
    }
}
