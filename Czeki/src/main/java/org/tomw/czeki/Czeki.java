/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.envutils.TomwEnvUtils;

/**
 *
 * @author tomw
 */
public class Czeki extends Application {

    private final static Logger LOGGER = Logger.getLogger(Czeki.class.getName());

    private BorderPane rootLayout;

    private static final String NETBEANS = "NetBeans";

    /**
     * Initialize application data in CzekiRegistry
     * @throws FileNotFoundException if something goes wrong
     */
    private static void initDirectories() throws FileNotFoundException {
        CzekiRegistry.applicationDirectory = new File(System.getProperty("user.dir"));
        LOGGER.info("Application directory =" + CzekiRegistry.applicationDirectory);

        if (isDeploymentInstance()) {
            LOGGER.info("redirecting output to directory "+CzekiRegistry.applicationDirectory);
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
            System.setErr(out);
        }

        CzekiRegistry.dataDirectory = selectDataDirectory();

        LOGGER.info("Data directory = " + CzekiRegistry.dataDirectory);
        if (!CzekiRegistry.dataDirectory.exists()) {
            LOGGER.info("Data directory does not exist, create...");
            CzekiRegistry.dataDirectory.mkdirs();
        }

        CzekiRegistry.defaultImageDirectory = new File(CzekiRegistry.dataDirectory, "images");
        LOGGER.info("defaultImageDirectory directory = " + CzekiRegistry.defaultImageDirectory);
        if (!CzekiRegistry.defaultImageDirectory.exists()) {
            CzekiRegistry.defaultImageDirectory.mkdirs();
        }

        CzekiRegistry.initialDirectory = CzekiRegistry.dataDirectory;
        LOGGER.info("Start directory =" + CzekiRegistry.initialDirectory);

        CzekiRegistry.accountsDao = new AccountsDaoJsonImpl();
        LOGGER.info("Accounts dao loaded: "+CzekiRegistry.accountsDao.toString());

        LOGGER.info("Load context ...");
        CzekiRegistry.context.load();

        LOGGER.info("Known accounts");
        for (Account account : CzekiRegistry.accountsDao.getAllAccounts().values()) {
            LOGGER.info(account);
        }

        LOGGER.info("Current account =" + CzekiRegistry.currentAccount);
    }

    /**
     * location of the data directory varies, depending if we are in devel or
     * production
     *
     * @return location of the data directory
     */
    public static File selectDataDirectory() {
        if (!isDeploymentInstance()) {
            if(TomwEnvUtils.isLaptop()){
                return new File("C:\\Users\\tomw2\\Documents\\IdeaProjectsData\\Czeki\\data");
            }else {
                return new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data");
            }

        } else {
            return new File(CzekiRegistry.applicationDirectory, "data");
        }
    }

    /**
     * decide if this is deployment instance or development
     *
     * @return true if this is deployment instance
     */
    public static boolean isDeploymentInstance() {
        return !CzekiRegistry.applicationDirectory.toString().contains(NETBEANS) &&
                !CzekiRegistry.applicationDirectory.toString().contains("IdeaProjects");
    }

    @Override
    public void start(Stage stage) throws Exception {

        CzekiRegistry.primaryStage = stage;
        CzekiRegistry.primaryStage.setTitle(CzekiRegistry.MAIN_TITLE);

        initRootLayout();

        showTransactionsOverview();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.MAIN_PAGE_FXML));
            rootLayout = (BorderPane) loader.load();
            
            CzekiRegistry.mainPageController = (MainPageNewController)loader.getController();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            CzekiRegistry.primaryStage.setScene(scene);
            
            CzekiRegistry.primaryStage.show();
        } catch (IOException e) {
            LOGGER.error("Error occured when initializing root layout ", e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Czeki starts...");

        try {
            initDirectories();
        } catch (FileNotFoundException ex) {
            LOGGER.error("File not found",ex);
        }
        launch(args);
    }

    private void showTransactionsOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            LOGGER.debug("location="+CzekiRegistry.TRANSACTIONS_OVERVIEW);
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.TRANSACTIONS_OVERVIEW));
            AnchorPane transactionsOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(transactionsOverview);

            // Give the controller access to the main app.
            CzekiRegistry.transactionsOverviewController = loader.getController();
            CzekiRegistry.transactionsOverviewController.initTransactionsTable();

        } catch (IOException e) {
            LOGGER.error("Error occured while displaying purchase overview", e);
        }
    }
}