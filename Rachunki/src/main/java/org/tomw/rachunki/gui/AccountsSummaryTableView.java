package org.tomw.rachunki.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.tomw.rachunki.entities.Konto;

import java.util.Collection;

public class AccountsSummaryTableView extends TableViewWrapper<Konto, String>{
    private TableColumn<Konto, String> shortNameColumn;
    private TableColumn<Konto, String>  fullNameColumn;
    private TableColumn<Konto, String>  commentColumn;

    private TableColumn<Konto, String>  clearedTransactionsSumColumn;
    private TableColumn<Konto, String>  pendingTransactionsSumColumn;
    private TableColumn<Konto, String>  allTransactionsSumColumn;

    private ObservableList<Konto> listOfDisplayedAccounts = FXCollections.observableArrayList();

    public TableColumn<Konto, String> getShortNameColumn() {
        return shortNameColumn;
    }

    public TableColumn<Konto, String> getFullNameColumn() {
        return fullNameColumn;
    }

    public TableColumn<Konto, String> getCommentColumn() {
        return commentColumn;
    }

    public TableColumn<Konto, String> getClearedTransactionsSumColumn() {
        return clearedTransactionsSumColumn;
    }

    public TableColumn<Konto, String> getPendingTransactionsSumColumn() {
        return pendingTransactionsSumColumn;
    }

    public TableColumn<Konto, String> getAllTransactionsSumColumn() {
        return allTransactionsSumColumn;
    }

    public ObservableList<Konto> getListOfDisplayedAccounts() {
        return listOfDisplayedAccounts;
    }

   public AccountsSummaryTableView(){
       super();
       shortNameColumn = defineShortNameColumn();
       fullNameColumn = defineFullNameColumn();
       commentColumn = defineCommentColumn();

       clearedTransactionsSumColumn = defineClearedTransactionsSumColumn();
       pendingTransactionsSumColumn = definePendingTransactionsSumColumn();
       allTransactionsSumColumn = defineAllTransactionsSumColumn();

       getTableView().getColumns().addAll(shortNameColumn,fullNameColumn,commentColumn,
               clearedTransactionsSumColumn,pendingTransactionsSumColumn,allTransactionsSumColumn);
       getTableView().setItems(listOfDisplayedAccounts);
   }

    private TableColumn<Konto, String> defineShortNameColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Short Name");
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.15));
        return column;
    }

    private TableColumn<Konto, String> defineFullNameColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Full Name");
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.2));
        return column;
    }

    private TableColumn<Konto, String> defineCommentColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Comment");
        column.setResizable(true);
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.2));
        return column;
    }

    private TableColumn<Konto,String> defineClearedTransactionsSumColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Cleared");
        column.setResizable(true);
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.15));
        return column;
    }

    private TableColumn<Konto,String> definePendingTransactionsSumColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Pending");
        column.setResizable(true);
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.15));
        return column;
    }

    private TableColumn<Konto,String> defineAllTransactionsSumColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("All");
        column.setResizable(true);
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.15));
        return column;
    }


    public void addtoDisplay(Collection<Konto> accounts){
        getListOfDisplayedAccounts().addAll(accounts);
    }
    //===============
}
