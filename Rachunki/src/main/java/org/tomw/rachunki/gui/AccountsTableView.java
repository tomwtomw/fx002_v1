package org.tomw.rachunki.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.tomw.rachunki.entities.Konto;

import java.util.Collection;

public class AccountsTableView extends TableViewWrapper<Konto, String>{

    private TableColumn<Konto, String>  shortNameColumn;
    private TableColumn<Konto, String>  fullNameColumn;
    private TableColumn<Konto, String>  commentColumn;

    private ObservableList<Konto> listOfDisplayedAccounts = FXCollections.observableArrayList();

    public TableColumn getShortNameColumn() {
        return shortNameColumn;
    }

    public TableColumn getFullNameColumn() {
        return fullNameColumn;
    }

    public TableColumn getCommentColumn() {
        return commentColumn;
    }

    public ObservableList<Konto> getListOfDisplayedAccounts() {
        return listOfDisplayedAccounts;
    }

    public AccountsTableView(){
        super();
        shortNameColumn = defineShortNameColumn();
        fullNameColumn = defineFullNameColumn();
        commentColumn = defineCommentColumn();

        getTableView().getColumns().addAll(shortNameColumn,fullNameColumn,commentColumn);
        getTableView().setItems(listOfDisplayedAccounts);

    }

    private TableColumn<Konto, String> defineShortNameColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Short Name");
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.2));
        return column;
    }

    private TableColumn<Konto, String> defineFullNameColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Full Name");
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.3));
        return column;
    }

    private TableColumn<Konto, String> defineCommentColumn() {
        TableColumn<Konto, String> column = new TableColumn<>("Comment");
        column.setResizable(true);
        column.prefWidthProperty().bind(getTableView().widthProperty().multiply(0.5));
        return column;
    }

    public void addtoDisplay(Collection<Konto> accounts){
        getListOfDisplayedAccounts().addAll(accounts);
    }

}
