package org.tomw.rachunki.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.config.SelfIdentificationService;
import org.tomw.filestoredao.FileDao;
import org.tomw.filestoredao.FileDaoDirImpl;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.config.RachunkiConfigurationImpl;
import org.tomw.rachunki.core.*;
import org.tomw.sysutils.TomwSystemUtils;

import java.io.IOException;

public class RachunkiMain extends Application {
    private final static Logger LOGGER = Logger.getLogger(RachunkiMain.class.getName());

    private BorderPane rootLayout;

    public static void main(String[] args) {
        LOGGER.info("Application starts...");

        SelfIdentificationServiceRachunki selfIdentificationService = new SelfIdentificationServiceRachunki();

        LOGGER.info("selfIdentificationService="+selfIdentificationService);

        // redirect output
        TomwSystemUtils.redirectStdOutStdErr(selfIdentificationService);

        RachunkiConfiguration config = new RachunkiConfigurationImpl(selfIdentificationService);

        LOGGER.info("config="+config.toString());
        LOGGER.info("Application directory = "+config.getApplicationDirectory());
        LOGGER.info("File store directory = "+config.getFileStoreDirectory());
        LOGGER.info("Document directory = "+config.getDocumentFileDirectory());
        LOGGER.info("Work directory="+ SelfIdentificationService.currentDir());

        try {
            FileDao fileDao = new FileDaoDirImpl(config);
            RachunkiDao dao = new RachunkiDaoHibernateImpl(config,fileDao);

            RachunkiService service = new RachunkiServiceImpl(dao,config);

            RachunkiMainRegistry.getRegistry().setDao(dao);
            RachunkiMainRegistry.getRegistry().setService(service);
            RachunkiMainRegistry.getRegistry().setSelfIdentificationService(selfIdentificationService);

            RachunkiMainRegistry.getRegistry().setConfig(config);

            LOGGER.info("Registry initialized");

        } catch (Exception e) {
            String message = "Cannot read/parse configuration files";
            LOGGER.fatal(message,e);
            System.exit(0);
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("We are in start method");

        primaryStage.setX(100.);
        primaryStage.setY(100.);

        RachunkiMainRegistry.getRegistry().setPrimaryStage(primaryStage);

        initRootLayout();
    }

    private void initRootLayout() {
        BorderPane rootLayout;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RachunkiMainRegistry.class.getResource(RachunkiMainRegistry.MAIN_PAGE_FXML));
            rootLayout =  loader.load();

            RachunkiMainRegistry.getRegistry().setRootLayout(rootLayout);

            RachunkiMainController mainPageController = loader.getController();
            String title = RachunkiMainRegistry.getRegistry().getSelfIdentificationService().toString();
            mainPageController.fillInstyanceNameLabel(title);

            RachunkiMainRegistry.getRegistry().setMainPageController(mainPageController);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            RachunkiMainRegistry.getRegistry().getPrimaryStage().setScene(scene);
            RachunkiMainRegistry.getRegistry().getPrimaryStage().setTitle(title);

            RachunkiMainRegistry.getRegistry().getPrimaryStage().show();
        } catch (IOException e) {
            LOGGER.error("Error occured when initializing root layout ", e);
        }
    }
}
