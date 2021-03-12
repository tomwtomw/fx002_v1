package org.tomw.rachunki.gui;

import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class TableViewWrapper<E,T> {

    private TableView<E> tableView;

    public TableView getTableView() {
        return tableView;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public TableViewWrapper(){
        this(new TableView());
    }

    public TableViewWrapper(TableView table){
        setTableView(table);
    }

//    /**
//     * define distance from edge of table to edge of enclosing anchor pane
//     * @param leftSpace in pixels
//     * @param topSpace in pixels
//     * @param rightSpace in pixels
//     * @param bottomSpace in pixels
//     */
//    public void anchor(double leftSpace, double topSpace, double rightSpace, double bottomSpace){
//        AnchorPane.setTopAnchor(tableView, topSpace);
//        AnchorPane.setBottomAnchor(tableView, bottomSpace);
//        AnchorPane.setLeftAnchor(tableView, leftSpace);
//        AnchorPane.setRightAnchor(tableView, rightSpace);
//    }
//    /**
//    * define distance from edge of table to edge of enclosing anchor pane to be 0.0
//    */
//    public void anchor(){
//        anchor(0.0,0.0,0.0,0.0);
//    }
//
//
}
