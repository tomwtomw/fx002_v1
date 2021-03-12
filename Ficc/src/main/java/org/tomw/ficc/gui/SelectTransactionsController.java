package org.tomw.ficc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.ficc.core.FiccCounterParty;
import org.tomw.ficc.core.FiccTimeLimitGenerator;
import org.tomw.ficc.core.FiccTransaction;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class SelectTransactionsController  implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(FiccMainPageController.class.getName());

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TableView<FiccCounterParty> counterpartiesTable;
    @FXML
    private TableColumn<FiccCounterParty, String> counterpartyShortNameColumn;
    @FXML
    private TableColumn<FiccCounterParty, String> counterpartyFullNameColumn;
    @FXML
    private TableColumn<FiccCounterParty, String> counterpartyCommentColumn;

    // radio Buttons
    private final ToggleGroup dateGroup = new ToggleGroup();
    @FXML
    private RadioButton thisMonthRadioButton;
    @FXML
    private RadioButton thisPeriodRadioButton;
    @FXML
    private RadioButton thisYearRadioButton;
    @FXML
    private RadioButton pastMonthRadioButton;
    @FXML
    private RadioButton pastPeriodRadioButton;
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

    private final ToggleGroup copunterpartyGroup = new ToggleGroup();
    @FXML
    private RadioButton allCounterPartiesRadioButton;
    @FXML
    private RadioButton selectedCounterPartiesRadioButton;
    @FXML
    private Button invertSelectionButton;


    private final ObservableList<FiccCounterParty> counterpartiesData = FXCollections.observableArrayList();
    private final List<FiccCounterParty> listOfSelectedCounterparties = new ArrayList<>();
    private LocalDate selectedStartDate = null;
    private LocalDate selectedEndDate = null;

    private boolean okClicked = false;

    private final ObservableList<FiccTransaction> selectedTransactions = FXCollections.observableArrayList();

    public List<FiccCounterParty> getListOfSelectedCounterparties() {
        return listOfSelectedCounterparties;
    }

    public ObservableList<FiccTransaction> getSelectedTransactions() {
        return selectedTransactions;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRadioButtons();

        //TODO implement
        counterpartyShortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyFullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyCommentColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        counterpartyShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().namePropertyProperty());
        counterpartyFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().namePropertyProperty());
        counterpartyCommentColumn.setCellValueFactory(cellData -> cellData.getValue().getCommentPropertyProperty());
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        LOGGER.warn("okButtonAction");
        selectTransactions();
        okClicked = true;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        LOGGER.warn("cancelButtonAction ");
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void invertSelectionButtonAction(ActionEvent event) {
        LOGGER.warn("invertSelectionButtonAction not implemented yet");
    }
    @FXML
    public void dateGroupRadioButtonAction(ActionEvent event) {
        LOGGER.info("dateGroupRadioButtonAction");
        if(thisMonthRadioButton.isPressed()){
            selectedStartDate = FiccTimeLimitGenerator.getStartOfCurrentMonth();
            selectedEndDate = FiccTimeLimitGenerator.getEndOfCurrentMonth().plusDays(1);
        }
        if(thisPeriodRadioButton.isPressed()){
            selectedStartDate = FiccTimeLimitGenerator.getStartOfCurrentPeriod();
            selectedEndDate = FiccTimeLimitGenerator.getEndOfCurrentMonth().plusDays(1);
        }
        if(thisYearRadioButton.isPressed()){
            selectedStartDate = FiccTimeLimitGenerator.getStartOfCurrentYear();
            selectedEndDate = FiccTimeLimitGenerator.getEndOfCurrentYear().plusDays(1);
        }
        if(pastMonthRadioButton.isPressed()){
            selectedStartDate = FiccTimeLimitGenerator.getStartOfCurrentMonth().minusMonths(1);
            selectedEndDate = FiccTimeLimitGenerator.getStartOfCurrentMonth();
        }
        if(pastPeriodRadioButton.isPressed()){
            selectedStartDate = FiccTimeLimitGenerator.getStartOfPeriod(FiccTimeLimitGenerator.getStartOfCurrentMonth());
            selectedEndDate = FiccTimeLimitGenerator.getStartOfCurrentPeriod();
        }
        if(timeIntervalRadioButton.isPressed()){
            selectedStartDate = startDatePicker.getValue();
            selectedEndDate = endDatePicker.getValue();
        }
        if(allDatesRadioButton.isPressed()){
            selectedStartDate=null;
            selectedEndDate=null;
        }
    }

    @FXML
    public void counterPartyGroupRadioButtonAction(ActionEvent event) {
        LOGGER.info("counterPartyGroupRadioButtonAction");
        if(allCounterPartiesRadioButton.isPressed()){
            listOfSelectedCounterparties.clear();
        }
        if(selectedCounterPartiesRadioButton.isPressed()){
            //TODO fill selected counterparties here
        }
    }

    private void selectTransactions() {
        selectedTransactions.clear();

        if (allCounterPartiesRadioButton.isSelected()) {
            LOGGER.info("select all counterparties is pressed");
            for (FiccTransaction t : FiccRegistry.dao.getAllFiccTransactions()) {
                LOGGER.info("selected t=" + t);
                selectedTransactions.add(t);
            }
        } else {
            LOGGER.info("checkbox is not pressed");
            for (FiccTransaction t : FiccRegistry.dao.getTransactionsBetween(listOfSelectedCounterparties, selectedStartDate, selectedEndDate)) {
                LOGGER.info("selected t=" + t);
                selectedTransactions.add(t);
            }
        }
    }

    public void initCounterPartiesTable() {
        LOGGER.info("We are in initCounterPartiesTable");
        if (counterpartiesTable == null) {
            LOGGER.error("counterpartiesTable is null");
        }
        LOGGER.info("number of cp=" + this.counterpartiesData.size());

        reloadCounterPartiesTable();
    }

    private void reloadCounterPartiesTable() {
        initCounterPartiesData(FiccRegistry.dao.getAllFiccCounterParties());
        counterpartiesTable.setItems(counterpartiesData);
    }

    private void initCounterPartiesData(Collection<FiccCounterParty> values) {
        List<FiccCounterParty> listOfCounterparties = new ArrayList<>(values);
        //TODO add sorting here
        //Collections.sort(listOfCounterparties, new CounterpartiesNameComparator());
        counterpartiesData.clear();
        counterpartiesData.addAll(listOfCounterparties);
    }

    private void initRadioButtons(){
        thisMonthRadioButton.setToggleGroup(dateGroup);
        thisPeriodRadioButton.setToggleGroup(dateGroup);
        thisYearRadioButton.setToggleGroup(dateGroup);
        pastMonthRadioButton.setToggleGroup(dateGroup);
        pastPeriodRadioButton.setToggleGroup(dateGroup);
        pastYearRadioButton.setToggleGroup(dateGroup);
        timeIntervalRadioButton.setToggleGroup(dateGroup);
        allDatesRadioButton.setToggleGroup(dateGroup);

        allCounterPartiesRadioButton.setToggleGroup(copunterpartyGroup);
        selectedCounterPartiesRadioButton.setToggleGroup(copunterpartyGroup);
    }

    public boolean isOkClicked() {
        //TODO implement
        return true;
    }
}
