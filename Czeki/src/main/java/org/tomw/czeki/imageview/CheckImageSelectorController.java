/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class CheckImageSelectorController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(CheckImageSelectorController.class.getName());

    @FXML
    private Label checkNumberLabel;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Button resetCheckNumberButton;
    @FXML
    private Button resetFileNameButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField checkNumberTextField;
    @FXML
    private TextField fileNameTextField;

    @FXML
    private ImageView imageView;

    @FXML
    private TableView<CheckImage> checkImageTable;
    @FXML
    private TableColumn<CheckImage, String> nameColumn;
    @FXML
    private TableColumn<CheckImage, String> checkNumberColumn;
    @FXML
    private TableColumn<CheckImage, String> checkFrontBackColumn;

    private CheckImage selectedCheckImage;

    public CheckImage getSelectedCheckImage() {
        return selectedCheckImage;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        checkNumberColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        checkFrontBackColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
        checkNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckNumberProperty());
        checkFrontBackColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckFrontBackProperty());

        checkImageTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends CheckImage> observable, CheckImage oldValue, CheckImage newValue) -> {
                    showPictureImageDetails(newValue);
                });
    }

    private void showPictureImageDetails(CheckImage checkImage) {
        LOGGER.info("PictureImage details: " + checkImage.toString());
        Image image = new Image(checkImage.getFile().toURI().toString());
        imageView.setImage(image);
        selectedCheckImage = checkImage;
    }

    public void initCheckImageTable() {
        LOGGER.info("We are in initCheckImageTable");
        if (checkImageTable == null) {
            LOGGER.error("checkImageTable is null");
        }
        checkImageTable.setItems(getListOfCheckImages());
    }

    public void initCheckImageTable(List<CheckImage> listOfImages) {
        LOGGER.info("We are in initCheckImageTable(List<CheckImage>)");
        if (checkImageTable == null) {
            LOGGER.error("checkImageTable is null");
        }
        checkImageTable.setItems(getListOfCheckImages(listOfImages));
    }

    @FXML
    private void resetCheckNumberButtonAction() {
        LOGGER.info("resetCheckNumberButtonAction");
    }

    @FXML
    private void resetFileNameButtonAction() {
        LOGGER.info("resetFileNameButtonAction");
    }

    @FXML
    private void acceptButtonAction() {
        LOGGER.info("acceptButtonAction");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButtonAction() {
        LOGGER.info("cancelButtonAction");

        selectedCheckImage = null;

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    /**
     * return observable list of all check images
     *
     * @return
     */
    private ObservableList<CheckImage> getListOfCheckImages() {
        return getListOfCheckImages(CzekiRegistry.currentAccount.getDao().getAllImages());
    }

    /**
     * take given list of images and convert it into observable list of images
     *
     * @param listOfImages
     * @return
     */
    private ObservableList<CheckImage> getListOfCheckImages(List<CheckImage> listOfImages) {
        ObservableList<CheckImage> checkImagesObservableList = FXCollections.observableArrayList();
        for (CheckImage checkImage : listOfImages) {
            checkImagesObservableList.add(checkImage);
        }
        return checkImagesObservableList;
    }

}
