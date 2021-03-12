package org.tomw.rachunki.core;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.tomw.config.SelfIdentificationService;
import org.tomw.gui.StageProvider;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.gui.*;

public class RachunkiMainRegistry implements StageProvider {

    public static final String MAIN_PAGE_FXML = "/RachunkiMain.fxml";

    private RachunkiDao dao;
    private RachunkiService service;
    private RachunkiConfiguration config;

    SelfIdentificationService selfIdentificationService;

    private Stage primaryStage;

    private BorderPane rootLayout;

    private RachunkiMainController mainPageController;

    private AccountsTab accountsTab=null;
    private DocumentsTab documentsTab=null;
//    private CategoriesTab categoriesTab=null;

    private AccountsTabController accountsTabController=null;
    private DocumentsTabController documentsTabController=null;
//    private CategoriesTabController categoriesTabController=null;

    public RachunkiConfiguration getConfig() {
        return config;
    }
    public void setConfig(RachunkiConfiguration config) {
        this.config = config;
    }

    public RachunkiDao getDao() {
        return dao;
    }
    public void setDao(RachunkiDao dao) {
        this.dao = dao;
    }

    public RachunkiService getService() {
        return service;
    }

    public void setService(RachunkiService service) {
        this.service = service;
    }

    public SelfIdentificationService getSelfIdentificationService() {
        return selfIdentificationService;
    }

    public void setSelfIdentificationService(SelfIdentificationService selfIdentificationService) {
        this.selfIdentificationService = selfIdentificationService;
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public  void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RachunkiMainController getMainPageController() {
        return mainPageController;
    }

    public void setMainPageController(RachunkiMainController mainPageController) {
        this.mainPageController = mainPageController;
    }

    private static RachunkiMainRegistry registry = null;

    public static RachunkiMainRegistry getRegistry() {
        if(registry==null){
            registry = new RachunkiMainRegistry();
        }
        return registry;
    }

    private RachunkiMainRegistry(){
    }


    public boolean isAccountsTabDefined() {
        return this.accountsTab!=null;
    }

    public AccountsTab getAccountsTab() {
        return accountsTab;
    }

    public void setAccountsTab(AccountsTab accountsTab) {
        this.accountsTab = accountsTab;
    }

    public boolean isDocumentsTabDefined() {
        return this.documentsTab!=null;
    }

    public DocumentsTab getDocumentsTab() {
        return documentsTab;
    }

    public void setDocumentsTab(DocumentsTab documentsTab) {
        this.documentsTab = documentsTab;
    }
}
