package org.tomw.ficc.gui;

/**
 * Registry class for FICC application
 * Created by tomw on 7/29/2017.
 */

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.tomw.ficc.core.FiccDao;
import org.tomw.ficc.core.FiccDaoJsonImpl;
import org.tomw.ficc.core.FiccIngestor;

import java.io.File;
import java.io.IOException;

public class FiccRegistry {
    private final static Logger LOGGER = Logger.getLogger(FiccRegistry.class.getName());

    public static final String MAIN_TITLE="Ficc Transactions";

    public static final String SELECT_TRANSACTIONS_FXML = "/SelectTransactions.fxml";

    public static File applicationDirectory;
    public static File dataDirectory;
    public static File initialDirectory;
    public static File configDirectory;

    public static FiccDao dao;
    public static FiccIngestor ingestor;

    public static Stage primaryStage;

    public static FiccMainPageController mainPageController;

    public static void loadConfig() throws IOException{
        //TODO implement
        LOGGER.warn("loadConfig() implemented partially only!");
        File daoFile = new File(dataDirectory,"ficcDao.json");
        try {
            LOGGER.info("Init dao");
            dao = new FiccDaoJsonImpl(daoFile);
        } catch (ParseException e) {
            LOGGER.error("Failed to init dao!",e);
            throw new IOException();
        }
        ingestor = new FiccIngestor(dao);

    }

    /**
     * save configuraiton data
     */
    public static void saveConfig() {
        //TODO implement
        LOGGER.warn("saveConfig() not implemented yet!");
    }

    /**
     * load application context
     */
    public static void loadContext() {
        //TODO implement
        LOGGER.warn("loadContext() not implemented yet!");
    }

    /**
     * persist applicaiton context
     */
    public static void saveContext() {
        //TODO implement
        LOGGER.warn("saveContext() not implemented yet!");
    }
}
