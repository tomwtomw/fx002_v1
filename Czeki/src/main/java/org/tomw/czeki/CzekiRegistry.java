/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import org.tomw.czeki.transactionoverview.TransactionsOverviewController;
import java.io.File;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Class for storing configuration parameters
 *
 * @author tomw
 */
public class CzekiRegistry {

    public static final String MAIN_TITLE = "Checkbook transactions";

    public static String CSV_SEPARATOR = "\t";
    public static final String EOL = "\n";
    public static final String BLANK = "";
    public static final String ERROR = "Error";

    public static File applicationDirectory;
    public static final String DATA_DIRECTORY_KEY = "DataDirectory";
    public static File dataDirectory;
    
    public static File initialDirectory;   
    
    public static File defaultImageDirectory;

    //public static CzekiDao dao;

    public static String MAIN_PAGE_FXML = "/MainPageNew.fxml";
    public static String TRANSACTIONS_OVERVIEW = "/transactionoverview/TransactionsOverview.fxml";
    //public static String TRANSACTIONS_OVERVIEW = "/TransactionsOverview.fxml";

    public static Stage primaryStage;
    public static BorderPane mainPageLayout;
    public static String UNKNOWN_COUNTERPARTY = "UNKNOWN_COUNTERPARTY";

    public static String COUNTERPARTY_OVERVIEW_FXML = "/counterparties/CounterpartiesOverview.fxml";
    public static String SELECT_TRANSACTIONS_FXML = "/selecttransactions/SelectTransactions.fxml";
    public static String MERGE_COUNTERPARTIES_FXML = "/mergecounterparties/MergeCounterParties.fxml";
    public static String COUNTERPARTY_SUMMARY_FXML = "/cpsummary/CounterPartySummary.fxml";
    
    public static String ACCOUNT_CRUD_FXML = "/AccountCrudFXML.fxml";

    public static CzekiContext context = new CzekiContextFileImpl();
    public static final String CZEK_IMAGE_DISPLAY_FXML = "/czekiimagedisplay/CzekiImageDisplay.fxml";
    public static String SELECT_IMAGE_DIRECTORY_FXML= "/selectdirectorywindow/SelectDirectoryWindow.fxml";
    public static String IMPORT_IMAGES_FXML="/fileupload/FileUploadWindow.fxml";
    
    public static String FILE_COPY_PROGRESS_FXML="/fileupload/FileCopyProgress.fxml";
    
    public static String IMAGE_STORE_BROWSER_FXML ="/imageview/ImageStoreBrowser.fxml";
    public static String CHECK_IMAGE_SELECTOR_FXML="/imageview/CheckImageSelector.fxml";
    public static String DISPLAY_IMAGE_BACK_AND_FRONT_FXML="/imageview/DisplayCheckImagesBackAndFront.fxml";
    
    // pages
    public static TransactionsOverviewController transactionsOverviewController ;
    public static AccountsDao accountsDao;
    public static Account currentAccount = null;
    
    public static MainPageNewController mainPageController;
    
        
    public static String toStringSummary(){
        String result="";
        result=result+currentAccount.toStringSummary()+EOL;
        result=result+DATA_DIRECTORY_KEY+"\t"+dataDirectory+EOL;
        return result;
    }

}
