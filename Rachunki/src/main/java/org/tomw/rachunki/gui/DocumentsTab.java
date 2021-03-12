package org.tomw.rachunki.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;

import java.io.IOException;

public class DocumentsTab {
    private final static Logger LOGGER = Logger.getLogger(DocumentsTab.class.getName());

    private static final String FXML_PAGE = "/DocumentsTab.fxml";

    private AnchorPane page;
    private FXMLLoader loader = null;
    private DocumentsTabController controller = null;

    private RachunkiService service;

    private String title=null;

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

    public DocumentsTab(){}

    public DocumentsTab(boolean isStandalone, boolean isLimited){
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

    public DocumentsTabController getController() {
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

                RachunkiMainRegistry.getRegistry().setDocumentsTab(this);
                controller = loader.getController();
                controller.setService(getService());
                controller.setContext(RachunkiMainRegistry.getRegistry().getConfig().getContext());
                controller.setStage(RachunkiMainRegistry.getRegistry().getPrimaryStage());

                LOGGER.info("Page " + FXML_PAGE + " standalone mode="+isStandaloneMode());
                controller.setStandaloneMode(isStandaloneMode());
                controller.setLimitedMode(isLimitedMode());
                if(title!=null){
                    controller.setTitle(title);
                }
                controller.initTable();
            } else {
                LOGGER.info("loader is " + loader);
            }
        } catch (IOException e) {
            LOGGER.error("Error occured while loading " + FXML_PAGE + " page", e);
        }
    }
}
