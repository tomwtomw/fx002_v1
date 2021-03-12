package org.tomw.rachunki.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.core.TransactionUtils;
import org.tomw.rachunki.entities.Konto;
import org.tomw.utils.TomwStringUtils;

import java.util.*;

public class PrimaryAccountSummaryWindowController {
    private final static Logger LOGGER = Logger.getLogger(PrimaryAccountSummaryWindowController.class.getName());

    private RachunkiService service;
    private PrimaryAccountSummaryWindowView view;

    private Map<Konto,Double> clearedTransactionsMap = new HashMap<>();
    private Map<Konto,Double> pendingTransactionsMap = new HashMap<>();
    private Map<Konto,Double> allTransactionsMap = new HashMap<>();

    public void setService(RachunkiService service) {
        this.service = service;
    }

    public void setView(PrimaryAccountSummaryWindowView view) {
        this.view = view;
    }

    public PrimaryAccountSummaryWindowView getView() {
        return view;
    }

    public void initialize() {
        TableColumn<Konto,String> shortNameColumnLeft =  getView().getTableView().getShortNameColumn();
        shortNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());

        TableColumn<Konto,String> fullNameColumnLeft =  getView().getTableView().getFullNameColumn();
        fullNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

        TableColumn<Konto,String> commentColumnLeft =  getView().getTableView().getCommentColumn();
        commentColumnLeft.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        TableColumn<Konto,String> clearedTransactionsSumColumn =  getView().getTableView().getClearedTransactionsSumColumn();
        clearedTransactionsSumColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        clearedTransactionsSumColumn.setCellValueFactory(cellData -> getSumOfClearedTransactionsFor(cellData.getValue()));

        TableColumn<Konto,String> pendingTransactionsSumColumn =  getView().getTableView().getPendingTransactionsSumColumn();
        pendingTransactionsSumColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        pendingTransactionsSumColumn.setCellValueFactory(cellData -> getSumOfPendingTransactionsFor(cellData.getValue()));

        TableColumn<Konto,String> allTransactionsSumColumn =  getView().getTableView().getAllTransactionsSumColumn();
        allTransactionsSumColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        allTransactionsSumColumn.setCellValueFactory(cellData -> getSumOfAllTransactionsFor(cellData.getValue()));


        TableView<Konto> tableView = getView().getTableView().getTableView();
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Konto> observable, Konto oldValue, Konto newValue) -> {
                    displayEntity(newValue);
                });

    }

    private ObservableValue<String> getSumOfClearedTransactionsFor(Konto konto) {
        if(clearedTransactionsMap.containsKey(konto)){
            double clearedTransactionsAmount = clearedTransactionsMap.get(konto);
            return new SimpleStringProperty(TomwStringUtils.money2String(clearedTransactionsAmount)+" ");
        }else{
            return new SimpleStringProperty("0.00 ");
        }
    }

    private ObservableValue<String> getSumOfPendingTransactionsFor(Konto konto) {
        if(pendingTransactionsMap.containsKey(konto)){
            double pendingTransactionsAmount = pendingTransactionsMap.get(konto);
            return new SimpleStringProperty(TomwStringUtils.money2String(pendingTransactionsAmount)+" ");
        }else{
            return new SimpleStringProperty("0.00 ");
        }
    }

    private ObservableValue<String> getSumOfAllTransactionsFor(Konto konto) {
        if(allTransactionsMap.containsKey(konto)){
            double allTransactionsAmount = allTransactionsMap.get(konto);
            return new SimpleStringProperty(TomwStringUtils.money2String(allTransactionsAmount)+" ");
        }else{
            return new SimpleStringProperty("0.00 ");
        }
    }

    private void displayEntity(Konto newValue) {
    }

    public void initTable() {
        Collection<Konto> accounts = service.getPrimaryAccounts();

        updateSums(accounts);

        getView().getTableView().addtoDisplay(accounts);
    }

    private void updateSums(Collection<Konto> accounts) {

        clearedTransactionsMap.clear();
        pendingTransactionsMap.clear();
        allTransactionsMap.clear();

        for(Konto account: accounts) {

            List<Double> sums = new ArrayList<>();
            List<Integer> nTransactions = new ArrayList<>();

            TransactionUtils.calculateSums(
                    service.getTransactionsForAccount(account),
                    account, sums, nTransactions
            );
            double clearedTransactionsAmount = sums.get(0);
            int clearedTransactionsNumberOf = nTransactions.get(0);
            clearedTransactionsMap.put(account,clearedTransactionsAmount);

            double pendingTransactionsAmount = sums.get(1);
            int pendingTransactionsNumberOf = nTransactions.get(1);
            pendingTransactionsMap.put(account,pendingTransactionsAmount);

            double allTransactionsAmount = sums.get(2);
            int allTransactionsNumberOf = nTransactions.get(2);
            allTransactionsMap.put(account,allTransactionsAmount);
        }

    }
}
