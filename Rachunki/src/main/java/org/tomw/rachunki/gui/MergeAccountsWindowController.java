package org.tomw.rachunki.gui;

import javafx.beans.value.ObservableValue;
import static org.tomw.utils.TomwStringUtils.BLANK;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.documentfile.DocumentFile;
import org.tomw.guiutils.alerts.ErrorAlert;
import org.tomw.guiutils.alerts.InformationAlert;
import org.tomw.rachunki.core.MergeAccountsResult;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.entities.Konto;
import org.tomw.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MergeAccountsWindowController {
    private final static Logger LOGGER = Logger.getLogger(MergeAccountsWindowController.class.getName());

    private MergeAccountsWindowView view;
    private RachunkiService service;

    private Konto leftSelectedAccount=null;
    private Konto rightSelectedAccount=null;
    private Collection<DocumentFile> centerDocuments = new ArrayList<>();

    private MergeAccountsWindowView getView() {
        return view;
    }

    void setView(MergeAccountsWindowView view) {
        this.view = view;
    }

    public RachunkiService getService() {
        return service;
    }

    public void setService(RachunkiService service) {
        this.service = service;
    }

    void initialize(){
        // define columns in left table
        TableColumn<Konto,String> shortNameColumnLeft =  getView().getTableViewLeft().getShortNameColumn();
        shortNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());

        TableColumn<Konto,String> fullNameColumnLeft =  getView().getTableViewLeft().getFullNameColumn();
        fullNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

        TableColumn<Konto,String> commentColumnLeft =  getView().getTableViewLeft().getCommentColumn();
        commentColumnLeft.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        TableView<Konto> tableLeft = getView().getTableViewLeft().getTableView();
        tableLeft.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Konto> observable, Konto oldValue, Konto newValue) -> {
                    displayEntityLeft(newValue);
                });

        defineArrowButtonsLeft();
        defineArrowButtonsRight();

        // define columns in right table
        TableColumn<Konto,String> shortNameColumnRight =  getView().getTableViewRight().getShortNameColumn();
        shortNameColumnRight.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());

        TableColumn<Konto,String> fullNameColumnRight =  getView().getTableViewRight().getFullNameColumn();
        fullNameColumnRight.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

        TableColumn<Konto,String> commentColumnRight =  getView().getTableViewRight().getCommentColumn();
        commentColumnRight.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        TableView<Konto> tableRight = getView().getTableViewRight().getTableView();
        tableRight.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Konto> observable, Konto oldValue, Konto newValue) -> {
                    displayEntityRight(newValue);
                });
        
        defineControlsButtons();
    }

    private void defineControlsButtons() {
        MergeAccountsForm form = getView().getMergeAccountsForm();

        form.getAcceptMergeButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                acceptMergeButtonAction();
            }
        });

        form.getClearMergeButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                clearMergeButtonAction();
            }
        });
    }

    private void clearMergeButtonAction() {
        MergeAccountsForm form = getView().getMergeAccountsForm();
        form.clearFormCenter();
    }

    private void acceptMergeButtonAction() {
        boolean mergeCanGoAhead = validateMerge();
        if(mergeCanGoAhead){
            LOGGER.info("Merge can go ahead");
            MergeAccountsResult mergeResult = mergeAccounts(leftSelectedAccount,rightSelectedAccount);
            if(mergeResult.isOk()){
                String message = "Accounts merged. Number of transactions modified+"+mergeResult.getnTransactions();
                LOGGER.info(message);
                displayMergeSummary(message);
            }else{
                String message = "Failed to merge accounts";
                LOGGER.error(message);
                displayMergeError(message);
            }
        }else{
            String message="Accounts cannot be merged";
            LOGGER.error(message);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(getView().getMergeAccountsForm().getAcceptMergeButton().getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText(message);
            String errorMessage = message;
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
    }

    private void displayMergeError(String message) {
        ErrorAlert errorAlert = new ErrorAlert();
        errorAlert.error(message);
    }

    private void displayMergeSummary(String message) {
        InformationAlert informationAlert = new InformationAlert();
        informationAlert.info(message);
    }

    private MergeAccountsResult mergeAccounts(Konto leftSelectedAccount, Konto rightSelectedAccount) {
        MergeAccountsForm form = getView().getMergeAccountsForm();
        String newShortName = form.getCenterShortNameTextField().getText();
        String newFullName = form.getCenterFullNameTextField().getText();
        String newComment = form.getCenterCommentTextArea().getText();

        Konto newAccount = new Konto();
        newAccount.setShortName(newShortName);
        newAccount.setFullName(newFullName);
        newAccount.setComment(newComment);

        service.save(newAccount);
        MergeAccountsResult result = service.mergeAccounts(leftSelectedAccount,rightSelectedAccount,newAccount);
        return result;
    }

    /**
     * merge can go ahead if
     * 1. new short name is defined and there is no other account with this short name
     * 2. New full name is defined and there is o other account with this full name
     * @return
     */
    private boolean validateMerge() {
        MergeAccountsForm form = getView().getMergeAccountsForm();
        String newShortName = form.getCenterShortNameTextField().getText();
        if(StringUtils.isBlank(newShortName)){
            LOGGER.error("Accounts cannot be merged: new short name is blank : "+newShortName);
            return false;
        }
        // verify whether this short name has been taken
        Collection<Konto> existingAccountsWithThisShortName = service.getAccountsWithShortName(newShortName);
        if(!existingAccountsWithThisShortName.isEmpty()){
            LOGGER.error("There are already accounts with this short name "+(Konto)existingAccountsWithThisShortName.iterator().next());
            return false;
        }
        String newFullName = form.getCenterFullNameTextField().getText();
        if(StringUtils.isBlank(newFullName)){
            LOGGER.error("Accounts cannot be merged: new full name is blank : "+newShortName);
            return false;
        }
        // verify whether this full name has been taken
        Collection<Konto> existingAccountsWithThisFullName = service.getAccountsWithFullName(newFullName);
        if(!existingAccountsWithThisFullName.isEmpty()){
            LOGGER.error("There are already accounts with this full name "+(Konto)existingAccountsWithThisFullName.iterator().next());
            return false;
        }
        return true;
    }


    private void defineArrowButtonsLeft() {

        getView().getMergeAccountsForm().getLeftArrowShortNameButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                leftArrowShortNameButtonAction();
            }
        });

        getView().getMergeAccountsForm().getLeftArrowFullNameButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                leftArrowFullNameButtonAction();
            }
        });

        getView().getMergeAccountsForm().getLeftArrowCommentButton().setOnAction((EventHandler) event -> leftArrowCommentButtonAction());

        getView().getMergeAccountsForm().getLeftDocumentButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                leftArrowDocumentButtonAction();
            }
        });
    }

    private void defineArrowButtonsRight() {

        getView().getMergeAccountsForm().getRightArrowShortNameButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                rightArrowShortNameButtonAction();
            }
        });

        getView().getMergeAccountsForm().getRightArrowFullNameButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                rightArrowFullNameButtonAction();
            }
        });

        getView().getMergeAccountsForm().getRightArrowCommentButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                rightArrowCommentButtonAction();
            }
        });

        getView().getMergeAccountsForm().getRightDocumentButton().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                rightArrowDocumentButtonAction();
            }
        });
    }

    private void rightArrowDocumentButtonAction() {
        LOGGER.info("rightArrowDocumentButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        this.centerDocuments = combineDocuments(this.centerDocuments,rightSelectedAccount);
        form.getCenterDocumentLabel().setText(this.centerDocuments.size()+BLANK);
    }

    private void rightArrowCommentButtonAction() {
        LOGGER.info("rightArrowCommentButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        String centerComment = form.getCenterCommentTextArea().getText();
        centerComment = centerComment + form.getRightCommentTextArea().getText();
        form.getCenterCommentTextArea().setText(centerComment);
    }

    private void rightArrowFullNameButtonAction() {
        LOGGER.info("rightArrowFullNameButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        String centerFullName = form.getCenterFullNameTextField().getText();
        centerFullName = centerFullName + form.getRightFullNameTextField().getText();
        form.getCenterFullNameTextField().setText(centerFullName);
    }

    private void rightArrowShortNameButtonAction() {
        LOGGER.info("rightArrowShortNameButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        String centerShortName = form.getCenterShortNameTextField().getText();
        centerShortName = centerShortName + form.getRightShortNameTextField().getText();
        form.getCenterShortNameTextField().setText(centerShortName);
    }

    private void leftArrowDocumentButtonAction() {
        LOGGER.info("leftArrowDocumentButtonAction");
        MergeAccountsForm form = getView().getMergeAccountsForm();

        this.centerDocuments = combineDocuments(this.centerDocuments,leftSelectedAccount);
        form.getCenterDocumentLabel().setText(this.centerDocuments.size()+BLANK);
    }

    private Collection<DocumentFile> combineDocuments(Collection<DocumentFile> documents, Konto leftSelectedAccount) {
        Map<Integer,DocumentFile> result = new HashMap<>();
        for(DocumentFile df:documents){
            result.put(df.getId(),df);
        }
        if(leftSelectedAccount!=null && leftSelectedAccount.getDocuments()!=null) {
            for (DocumentFile df : leftSelectedAccount.getDocuments()) {
                result.put(df.getId(), df);
            }
        }
        return result.values();
    }

    private void leftArrowCommentButtonAction() {
        LOGGER.info("leftArrowCommentButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        String centerComment = form.getCenterCommentTextArea().getText();
        centerComment = centerComment + form.getLeftCommentTextArea().getText();
        form.getCenterCommentTextArea().setText(centerComment);
    }

    private void leftArrowShortNameButtonAction() {
        LOGGER.info("leftArrowShortNameButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        LOGGER.info("rightArrowShortNameButtonAction");
        String centerShortName = form.getCenterShortNameTextField().getText();
        centerShortName = centerShortName + form.getLeftShortNameTextField().getText();
        form.getCenterShortNameTextField().setText(centerShortName);
    }

    private void leftArrowFullNameButtonAction() {
        LOGGER.info("leftArrowFullNameButtonAction");

        MergeAccountsForm form = getView().getMergeAccountsForm();

        String centerFullName = form.getCenterFullNameTextField().getText();
        centerFullName = centerFullName + form.getLeftFullNameTextField().getText();
        form.getCenterFullNameTextField().setText(centerFullName);
    }

    void initTable(){
        Collection<Konto> accounts = service.getAllAccounts();

        getView().getTableViewLeft().addtoDisplay(accounts);
        getView().getTableViewRight().addtoDisplay(accounts);
    }

    private void displayEntityLeft(Konto account) {
        if(account!=null) {
            leftSelectedAccount=account;
            getView().clearFormCenter();
            getView().clearFormLeft();
            getView().getMergeAccountsForm().getLeftShortNameTextField().setText(account.getShortName());
            getView().getMergeAccountsForm().getLeftFullNameTextField().setText(account.getFullName());
            getView().getMergeAccountsForm().getLeftCommentTextArea().setText(account.getComment());
            getView().getMergeAccountsForm().getLeftDocumentLabel().setText(
                    CollectionUtils.size2String(account.getDocuments())
            );
        }
    }

    private void displayEntityRight(Konto account) {
        if(account!=null) {
            rightSelectedAccount=account;
            getView().clearFormCenter();
            getView().clearFormRight();
            getView().getMergeAccountsForm().getRightShortNameTextField().setText(account.getShortName());
            getView().getMergeAccountsForm().getRightFullNameTextField().setText(account.getFullName());
            getView().getMergeAccountsForm().getRightCommentTextArea().setText(account.getComment());
            getView().getMergeAccountsForm().getRightDocumentLabel().setText(
                    CollectionUtils.size2String(account.getDocuments())
            );
        }
    }
}
