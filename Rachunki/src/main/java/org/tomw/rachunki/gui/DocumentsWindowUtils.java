package org.tomw.rachunki.gui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.log4j.Logger;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.core.RachunkiServiceImpl;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

public class DocumentsWindowUtils {
    private final static Logger LOGGER = Logger.getLogger(DocumentsWindowUtils.class.getName());

    public DocumentsWindowResult displayDocumentsForAccount(Konto account, Window window) {
        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayDocumentsForAccount(account);

        // we want window in standalone mode, displaying limited info
        DocumentsTab tab = new DocumentsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setTitle("Documents for acccount " + account.getFullName());

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Documents for acccount " + account.getFullName());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        DocumentsWindowResult result = new DocumentsWindowResult();
        result.setOkClicked(tab.getController().okClicked());

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedEntity(tab.getController().getCurrentEntity());
            } else {
                result.setSelectedEntity(null);
            }
            if(tab.getController().getDisplayedEntities()!=null){
                result.setDisplayedEntities(tab.getController().getDisplayedEntities());
            }else{
                result.setDisplayedEntities(null);
            }
        } else {
            result.setSelectedEntity(null);
            result.setDisplayedEntities(null);
        }

        return result;

    }

    public DocumentsWindowResult displayDocumentsForTransaction(Transakcja transakcja, Window window) {
        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayDocumentsForTransaction(transakcja);

        // we want window in standalone mode, displaying limited info
        DocumentsTab tab = new DocumentsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setTitle("Documents for transaction " + transakcja);

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Documents for transaction " + transakcja);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        DocumentsWindowResult result = new DocumentsWindowResult();
        result.setOkClicked(tab.getController().okClicked());

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedEntity(tab.getController().getCurrentEntity());
            } else {
                result.setSelectedEntity(null);
            }
            if(tab.getController().getDisplayedEntities()!=null){
                result.setDisplayedEntities(tab.getController().getDisplayedEntities());
            }else{
                result.setDisplayedEntities(null);
            }
        } else {
            result.setSelectedEntity(null);
            result.setDisplayedEntities(null);
        }

        return result;

    }

}
