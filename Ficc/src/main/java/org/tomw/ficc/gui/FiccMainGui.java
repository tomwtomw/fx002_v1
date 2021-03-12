package org.tomw.ficc.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Main gui class of ficc project
 * Created by tomw on 7/22/2017.
 */
public class FiccMainGui extends Application {
    private final static Logger LOGGER = Logger.getLogger(FiccMainGui.class.getName());

    private static final String MAIN_PAGE_FXML = "/FiccMainPage.fxml";
    private BorderPane rootLayout;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FiccRegistry.primaryStage = primaryStage;
        FiccRegistry.primaryStage.setTitle(FiccRegistry.MAIN_TITLE);

        try {
            initDirectories();
        } catch (FileNotFoundException e) {
           LOGGER.fatal("Failed to init application directories!",e);
        }

        try {
            FiccRegistry.loadConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to locad configuration",e);
        }

        initRootLayout();
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FiccMainGui.class.getResource(MAIN_PAGE_FXML));
            rootLayout =  loader.load();

            FiccRegistry.mainPageController = loader.getController();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            FiccRegistry.primaryStage.setScene(scene);

            FiccRegistry.primaryStage.show();

            FiccRegistry.mainPageController.initTable();


        } catch (IOException e) {
            LOGGER.error("Error occured when initializing root layout ", e);
        }
    }

    private void initDirectories() throws FileNotFoundException {
        FiccRegistry.applicationDirectory = new File(System.getProperty("user.dir"));
        LOGGER.info("Application directory =" + FiccRegistry.applicationDirectory);

        if (isDeploymentInstance()) {
            LOGGER.info("This is deployment instance");
            LOGGER.info("Redirecting output to directory "+FiccRegistry.applicationDirectory);
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
            System.setErr(out);
        }else{
            LOGGER.info("This is development instance");
        }

        FiccRegistry.dataDirectory = selectDataDirectory();
        LOGGER.info("Data directory = " + FiccRegistry.dataDirectory);
        if (!FiccRegistry.dataDirectory.exists()) {
            LOGGER.info("Data directory does not exist, create...");
            FiccRegistry.dataDirectory.mkdirs();
        }

        FiccRegistry.configDirectory = selectConfigDirectory();
        LOGGER.info("Config directory = " + FiccRegistry.configDirectory);
        if (!FiccRegistry.configDirectory.exists()) {
            LOGGER.info("Config directory does not exist, create...");
            FiccRegistry.configDirectory.mkdirs();
        }

        FiccRegistry.loadContext();

        FiccRegistry.initialDirectory = FiccRegistry.dataDirectory;
        LOGGER.info("Start directory =" + FiccRegistry.initialDirectory);

    }

    private static File selectConfigDirectory() {
        if (!isDeploymentInstance()) {
            return new File("C:\\Users\\tomw\\IdeaProjects\\fx001_v2-data\\config");
        } else {
            return new File(FiccRegistry.applicationDirectory, "config");
        }
    }

    private static boolean isDeploymentInstance() {
        // C:\Users\tomw\IdeaProjects\fx001_v2\Ficc\build\libs
        return !FiccRegistry.applicationDirectory.toString().contains("IdeaProjects");
    }

    /**
     * location of the data directory varies, depending if we are in devel or
     * production
     *
     * @return data directory for this application
     */
    private static File selectDataDirectory() {
        if (!isDeploymentInstance()) {
            return new File("C:\\Users\\tomw\\IdeaProjects\\fx001_v2-data\\data");
        } else {
            return new File(FiccRegistry.applicationDirectory, "data");
        }
    }

}
