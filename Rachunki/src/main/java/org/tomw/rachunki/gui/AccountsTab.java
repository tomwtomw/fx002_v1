package org.tomw.rachunki.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.entities.Konto;

import java.io.IOException;

public class AccountsTab {
    private final static Logger LOGGER = Logger.getLogger(AccountsTab.class.getName());

    private static final String FXML_PAGE = "/AccountsTab.fxml";

    private AnchorPane page;
    private FXMLLoader loader = null;
    private AccountsTabController controller = null;

    private RachunkiService service;

    private String title=null;

    private Konto initalHighlightedAccount = null;

    // whether window runs as a tab or standalone window
    private boolean standaloneMode = false;

    // if false, then window will display all entities of given type, elose only limited
    private boolean limitedMode = false;

    public boolean isLimitedMode() {
        return limitedMode;
    }

    public void setLimitedMode(boolean limitedMode) {
        this.limitedMode = limitedMode;
    }

    public boolean isStandaloneMode() {
        return standaloneMode;
    }

    void setStandaloneMode(boolean standaloneMode) {
        this.standaloneMode = standaloneMode;
    }

    public void setInitalHighlightedAccount(Konto initalHighlightedAccount) {
        this.initalHighlightedAccount = initalHighlightedAccount;
    }

    public AccountsTab(){}

    public AccountsTab(boolean isStandalone, boolean isLimited){
        this.limitedMode=isLimited;
        this.standaloneMode=isStandalone;
    }

    public void setTitle(String title){
        this.title=title;
    }

    AnchorPane getPage() {
        return page;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public AccountsTabController getController() {
        return controller;
    }

    public RachunkiService getService() {
        return service;
    }

    void setService(RachunkiService service) {
        this.service = service;
    }

    void init() {
        try {
            if (loader == null) {
                LOGGER.info("loader is null, load page");
                loader = new FXMLLoader();
                LOGGER.info("Create " + FXML_PAGE + " page for the first time");
                LOGGER.info("Load page " + FXML_PAGE);
                loader.setLocation(AccountsTab.class.getResource(FXML_PAGE));
                page = loader.load();
                LOGGER.info("page=" + page);
                controller = loader.getController();
                controller.setService(getService());
                LOGGER.info("Page " + FXML_PAGE + " standalone mode="+isStandaloneMode());
                controller.setStandaloneMode(isStandaloneMode());
                controller.setLimitedMode(isLimitedMode());
                if(title!=null){
                    controller.setTitle(title);
                }
                controller.setInitialHighlightedAccount(initalHighlightedAccount);
                controller.initTable();
            } else {
                LOGGER.info("loader is " + loader);
            }
        } catch (IOException e) {
            LOGGER.error("Error occured while loading " + FXML_PAGE + " page", e);
        }
    }


}
