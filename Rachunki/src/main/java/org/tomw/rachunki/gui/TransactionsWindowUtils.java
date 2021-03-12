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

public class TransactionsWindowUtils {
    private final static Logger LOGGER = Logger.getLogger(TransactionsWindowUtils.class.getName());

    public TransactionsWindowResult displayTransactionsForAccountWindow(
            Konto accountToDisplay,
            Transakcja transakcja,
            Window window) {

        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayTransactionsForAccount(accountToDisplay);

        // we want window in standalone mode, displaying limited info
        TransactionsTab tab = new TransactionsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setAccount(accountToDisplay);
        tab.setTransactionToHighlight(transakcja); // which transaction should be highlighted

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Transactions for account " + accountToDisplay.getFullName());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        TransactionsWindowResult result = new TransactionsWindowResult();
        result.setOkClicked(tab.getController().okClicked());
        result.setAccount(accountToDisplay);

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedEntity(tab.getController().getCurrentEntity());
            }else{
                result.setSelectedEntity(null);
            }
        }else{
            result.setSelectedEntity(null);
        }

        return result;
    }

    public AccountsWindowResult displayAccountsWindow(Konto accountsTobehighlighted, Window window) {

        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayAllAccounts();

        // we want window in standalone mode, displaying limited info
        AccountsTab tab = new AccountsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setInitalHighlightedAccount(accountsTobehighlighted);

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Counterparties ");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        AccountsWindowResult result = new AccountsWindowResult();
        result.setOkClicked(tab.getController().okClicked());

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedAccount(tab.getController().getCurrentEntity());
            }else{
                result.setSelectedAccount(null);
            }
        }else{
            result.setSelectedAccount(null);
        }

        return result;
    }

    public AccountsWindowResult displayAccountsPreviouslyKnownToAccountWindow(Konto account,
                                                                              Konto currentCounterPartyToBeHighlighted,
                                                                              Window window) {

        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayAccountsWhichPreviouslyTradedWithAccount(account);

        // we want window in standalone mode, displaying limited info
        AccountsTab tab = new AccountsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setInitalHighlightedAccount(currentCounterPartyToBeHighlighted);

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Counterparties ");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        AccountsWindowResult result = new AccountsWindowResult();
        result.setOkClicked(tab.getController().okClicked());

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedAccount(tab.getController().getCurrentEntity());
            }else{
                result.setSelectedAccount(null);
            }
        }else{
            result.setSelectedAccount(null);
        }

        return result;
    }

    public AccountsWindowResult displayPrimaryAccountsWindow(
            Konto account,
            Konto currentCounterPartyToBeHighlighted,
            Window window) {

        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayPrimaryAccounts(account);

        // we want window in standalone mode, displaying limited info
        AccountsTab tab = new AccountsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setInitalHighlightedAccount(currentCounterPartyToBeHighlighted);

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Counterparties ");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        dialogStage.initOwner(window);
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());

        AccountsWindowResult result = new AccountsWindowResult();
        result.setOkClicked(tab.getController().okClicked());

        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                result.setSelectedAccount(tab.getController().getCurrentEntity());
            }else{
                result.setSelectedAccount(null);
            }
        }else{
            result.setSelectedAccount(null);
        }

        return result;
    }

}
