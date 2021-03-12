package org.tomw.rachunki.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.gui.textareaeditor.TextAreaEditor;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.tomw.utils.TomwStringUtils.EOL;

public class RachunkiMainController implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(RachunkiMainController.class.getName());

    private RachunkiService service;

    public RachunkiService getService() {
        return service;
    }

    public void setService(RachunkiService service) {
        this.service = service;
    }

    // fxml properties
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab accountsTab;
    @FXML
    private Tab transactionsTab;
    @FXML
    private Tab documentsTab;

    private AccountsTabController accountsTabController;
//    private TransactionsTabController transactionsTabController;
//    private DocumentsTabController  documentsTabController;

    @FXML
    private Label instanceNameLabel;

    @FXML
    public void accountsTabSelected(){
        if(accountsTab.isSelected() && accountsTabController!=null){
            accountsTabController.refresh();
        }
    }
    @FXML
    public void transactionsTabSelected(){
//        transactionsTabController.refresh();
    }
    @FXML
    public void documentsTabselected(){
       // documentsTabController.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("We are in imitialize");
        setService(RachunkiMainRegistry.getRegistry().getService());
        initTabs();
    }

    private void initTabs() {
        initAccountsTab();
//        initEntriesTab();
//        initDocumentsTab();
    }

    private void initAccountsTab() {
        if (!RachunkiMainRegistry.getRegistry().isAccountsTabDefined()) {
            AccountsTab tab = new AccountsTab();
            tab.setService(getService());
            tab.init();
            accountsTab.setContent(tab.getPage());
            accountsTabController=tab.getController();
            RachunkiMainRegistry.getRegistry().setAccountsTab(tab);
        }
    }

    @FXML
    private void menuFileExitAction() {
        LOGGER.info("menuFileExitAction");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save data before exit?");
        alert.setHeaderText("Save data before exit?");
        alert.setContentText("Should I save data before exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.saveAction();
        }

        RachunkiMainRegistry.getRegistry().getConfig().getContext().persist();

        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void menuHelpCompareFiccAction() {
        LOGGER.info("menuHelpCompareFiccAction");
        FiccComparatorWindow ficcComparatorWindow = new FiccComparatorWindow();
        ficcComparatorWindow.display();
    }


    /**
     * save dao content
     */
    private void saveAction() {
        //TODO implement
//        saveTransactions();
//        saveAccounts();
    }

    public void fillInstyanceNameLabel(String text){
        this.instanceNameLabel.setText(text);
    }


    @FXML
    public void menuHelpShowConfigAction(){
        LOGGER.info("menuHelpShowConfigAction");
        String content="Base configuration:"+EOL;
        content = content+ RachunkiMainRegistry.getRegistry().getConfig().toString()+EOL;
        content=content+"Context:"+EOL;
        content=content+RachunkiMainRegistry.getRegistry().getConfig().getContext().toString()+EOL;
        TextAreaEditor editor = new TextAreaEditor(
                "Current configuration",
                content);
        if (editor.okClicked()) {
            LOGGER.info("ok clicked");
        }
    }
}


