/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.selecttransactions;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.CounterpartiesNameComparator;
import org.tomw.czeki.entities.Transaction;
import org.tomw.timeutils.TimeLimitGenerator;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class SelectTransactionsController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(SelectTransactionsController.class.getName());

    @FXML
    private TableView<CounterParty> counterpartiesTable;
    @FXML
    private TableColumn<CounterParty, String> counterpartyShortNameColumn;
    @FXML
    private TableColumn<CounterParty, String> counterpartyFullNameColumn;
//    @FXML
//    private TableColumn<CounterParty, String> counterpartyCommentColumn;

    // radio Buttons
    private final ToggleGroup group = new ToggleGroup();
    @FXML
    private RadioButton thisMonthRadioButton;
    @FXML
    private RadioButton thisYearRadioButton;
    @FXML
    private RadioButton pastMonthRadioButton;
    @FXML
    private RadioButton pastYearRadioButton;
    @FXML
    private RadioButton timeIntervalRadioButton;
    @FXML
    private RadioButton allDatesRadioButton;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private CheckBox allCounterpartiesCheckBox;

    private final ObservableList<CounterParty> counterpartiesData = FXCollections.observableArrayList();
    private final List<CounterParty> listOfSelectedCounterparties = new ArrayList<>();
    private LocalDate selectedStartDate = null;
    private LocalDate selectedEndDate = null;

    private final ObservableList<Transaction> selectedTransactions = FXCollections.observableArrayList();

    public List<CounterParty> getListOfSelectedCounterparties() {
        return listOfSelectedCounterparties;
    }

    public ObservableList<Transaction> getSelectedTransactions() {
        return selectedTransactions;
    }

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private boolean okClicked = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        thisMonthRadioButton.setToggleGroup(group);
        thisYearRadioButton.setToggleGroup(group);
        pastMonthRadioButton.setToggleGroup(group);
        pastYearRadioButton.setToggleGroup(group);
        timeIntervalRadioButton.setToggleGroup(group);
        allDatesRadioButton.setToggleGroup(group);
        allDatesRadioButton.selectedProperty().set(true);

        counterpartyShortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyFullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        //counterpartyCommentColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        counterpartyShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty());
        counterpartyFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        resetErrorMessages();
        this.okClicked = false;

        counterpartiesTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        counterpartiesTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                listOfSelectedCounterparties.clear();
                for (CounterParty cp : counterpartiesTable.getSelectionModel().getSelectedItems()) {
                    LOGGER.info("cp=" + cp);
                    listOfSelectedCounterparties.add(cp);
                }
                //showFileDetails(list);
            }
        }
        );
    }

    public void initCounterPartiesTable() {
        LOGGER.info("We are in initCounterPartiesTable");
        if (counterpartiesTable == null) {
            LOGGER.error("counterpartiesTable is null");
        }
        LOGGER.info("number of cp=" + this.counterpartiesData.size());

        reloadCounterPartiesTable();
    }

    @FXML
    private void okButtonAction() {
        System.out.println("okButtonAction");
        selectTransactions();
        okClicked = true;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButtonAction() {
        System.out.println("cancelButtonAction");
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void radioButtonAction() {
        LOGGER.info("radioButtonAction");
        if (thisMonthRadioButton.isSelected()) {
            LOGGER.info("thisMonthRadioButton");
            setThisMonthLimits();
        }
        if (thisYearRadioButton.isSelected()) {
            LOGGER.info("thisYearRadioButton");
            setThisYearLimits();
        }
        if (pastMonthRadioButton.isSelected()) {
            LOGGER.info("pastMonthRadioButton");
            setPastMonthLimits();
        }
        if (pastYearRadioButton.isSelected()) {
            LOGGER.info("pastYearRadioButton");
            setPastYearLimits();
        }
        if (timeIntervalRadioButton.isSelected()) {
            LOGGER.info("timeIntervalRadioButton");
            selectedStartDate = startDatePicker.getValue();
            selectedEndDate = endDatePicker.getValue();
        }
        if (allDatesRadioButton.isSelected()) {
            LOGGER.info("allDatesRadioButton");
            selectedStartDate = null;
            selectedEndDate = null;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void refreshCounterPartiesTable() {
        counterpartiesTable.getColumns().get(0).setVisible(false);
        counterpartiesTable.getColumns().get(0).setVisible(true);
    }

    private void showCounterPartyDetails(CounterParty newValue) {
        //TODO implement
    }

    private void resetErrorMessages() {
        //TODO implement
    }

    private void reloadCounterPartiesTable() {
        initCounterPartiesData(CzekiRegistry.currentAccount.getDao().getCounterParties().values());
        counterpartiesTable.setItems(counterpartiesData);
    }

    private void initCounterPartiesData(Collection<CounterParty> values) {
        List<CounterParty> listOfCounterparties = new ArrayList<>(values);
        Collections.sort(listOfCounterparties, new CounterpartiesNameComparator());
        counterpartiesData.clear();
        for (CounterParty cp : listOfCounterparties) {
            counterpartiesData.add(cp);
        }
    }

    private void setThisMonthLimits() {
        selectedStartDate = TimeLimitGenerator.getStartOfCurrentMonth();
        selectedEndDate = TimeLimitGenerator.getEndOfCurrentMonth();
    }

    private void setThisYearLimits() {
        selectedStartDate = TimeLimitGenerator.getStartOfCurrentYear();
        selectedEndDate = TimeLimitGenerator.getEndOfCurrentYear();
    }

    private void setPastMonthLimits() {
        selectedStartDate = TimeLimitGenerator.getStartOfPreviousMonth();
        selectedEndDate = TimeLimitGenerator.getEndOfPreviousMonth();
    }

    private void setPastYearLimits() {
        selectedStartDate = TimeLimitGenerator.getStartOfPreviousYear();
        selectedEndDate = TimeLimitGenerator.getEndOfPreviousYear();
    }

    /**
     * select transactions which have counterparty
     */
    private void selectTransactions() {
        selectedTransactions.clear();
        
        if (allCounterpartiesCheckBox.selectedProperty().get()) {
            LOGGER.info("checkbox is pressed");
            for (Transaction t : CzekiRegistry.currentAccount.getDao().getTransactionsData(selectedStartDate, selectedEndDate)) {
                LOGGER.info("selected t=" + t);
                selectedTransactions.add(t);
            }
        } else {
            LOGGER.info("checkbox is not pressed");
            for (Transaction t : CzekiRegistry.currentAccount.getDao().getTransactionsData(listOfSelectedCounterparties, selectedStartDate, selectedEndDate)) {
                LOGGER.info("selected t=" + t);
                selectedTransactions.add(t);
            }
        }
    }
}
