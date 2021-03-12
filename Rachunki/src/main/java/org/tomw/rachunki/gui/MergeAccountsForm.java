package org.tomw.rachunki.gui;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.tomw.documentfile.DocumentFile;
import org.tomw.fxmlutils.AnchorPaneWrapper;
import org.tomw.fxmlutils.buttons.CloseWindowButton;
import org.tomw.fxmlutils.buttons.CommonButton;
import org.tomw.fxmlutils.buttons.LeftArrowButtonSmall;
import org.tomw.fxmlutils.buttons.RightArrowButtonSmall;
import org.tomw.fxmlutils.labels.BoldLabel;

import java.util.ArrayList;
import java.util.Collection;

import static org.tomw.utils.TomwStringUtils.BLANK;

public class MergeAccountsForm extends GridPane{

    private static final int DESCRIPTION_COLUMN=0;
    private static final int LEFT_PROPERTY_COLUMN=1;
    private static final int LEFT_ARROW_COLUMN=2;
    private static final int MERGED_PROPERTY_COLUMN=3;
    private static final int RIGHT_ARROW_COLUMN=4;
    private static final int RIGHT_PROPERTY_COLUMN=5;

    private static final int TITLE_ROW = 0;
    private static final int SHORT_NAME_ROW = TITLE_ROW+1;
    private static final int FULL_NAME_ROW = SHORT_NAME_ROW+1;
    private static final int COMMENT_ROW = FULL_NAME_ROW+1;
    private static final int DOCUMENT_ROW = COMMENT_ROW+1;
    private static final int CONTROLLS_ROW = DOCUMENT_ROW+1;
    private static final int OK_CANCEL_ROW = CONTROLLS_ROW+1;

    private static final double TEXT_SIZE=14;

    private TextField leftShortNameTextField = new TextField();
    private Button leftArrowShortNameButton = new RightArrowButtonSmall();
    private TextField centerShortNameTextField  = new TextField();
    private Button rightArrowShortNameButton = new LeftArrowButtonSmall();
    private TextField rightShortNameTextField = new TextField();

    private TextField leftFullNameTextField = new TextField();
    private Button leftArrowFullNameButton = new RightArrowButtonSmall();
    private TextField centerFullNameTextField = new TextField();
    private Button rightArrowFullNameButton = new LeftArrowButtonSmall();
    private TextField rightFullNameTextField = new TextField();

    private TextArea leftCommentTextArea = new TextArea();
    private Button leftArrowCommentButton = new RightArrowButtonSmall();
    private TextArea centerCommentTextArea = new TextArea();
    private Button rightArrowCommentButton = new LeftArrowButtonSmall();
    private TextArea rightCommentTextArea = new TextArea();

    private Label leftDocumentLabel = new BoldLabel(TEXT_SIZE);
    private Button leftDocumentButton = new RightArrowButtonSmall();
    private Label centerDocumentLabel =  new BoldLabel(TEXT_SIZE);
    private Button rightDocumentButton = new LeftArrowButtonSmall();
    private Label rightDocumentLabel = new BoldLabel(TEXT_SIZE);
    private Collection<DocumentFile> combinedDocuments = new ArrayList<>();

    private Button acceptMergeButton = new CommonButton("Accept");
    private Button clearMergeButton = new CommonButton("Clear");
    private Button cancelMergeButton = new CloseWindowButton("CancelXX");

    private Button okButton = new CommonButton("OK");
    private Button cancelButton = new CommonButton("Cancel");

    TextField getLeftShortNameTextField() {
        return leftShortNameTextField;
    }

    TextField getCenterShortNameTextField() {
        return centerShortNameTextField;
    }

    TextField getRightShortNameTextField() {
        return rightShortNameTextField;
    }

    TextField getLeftFullNameTextField() {
        return leftFullNameTextField;
    }

    TextField getCenterFullNameTextField() {
        return centerFullNameTextField;
    }

    TextField getRightFullNameTextField() {
        return rightFullNameTextField;
    }

    TextArea getLeftCommentTextArea() {
        return leftCommentTextArea;
    }

    TextArea getCenterCommentTextArea() {
        return centerCommentTextArea;
    }

    TextArea getRightCommentTextArea() {
        return rightCommentTextArea;
    }

    public static int getDescriptionColumn() {
        return DESCRIPTION_COLUMN;
    }

    Label getLeftDocumentLabel() {
        return leftDocumentLabel;
    }

    Label getCenterDocumentLabel() {
        return centerDocumentLabel;
    }

    Label getRightDocumentLabel() {
        return rightDocumentLabel;
    }

    Button getLeftArrowShortNameButton() {
        return leftArrowShortNameButton;
    }

    Button getRightArrowShortNameButton() {
        return rightArrowShortNameButton;
    }

    Button getLeftArrowFullNameButton() {
        return leftArrowFullNameButton;
    }

    Button getRightArrowFullNameButton() {
        return rightArrowFullNameButton;
    }

    Button getLeftArrowCommentButton() {
        return leftArrowCommentButton;
    }

    Button getRightArrowCommentButton() {
        return rightArrowCommentButton;
    }

    Button getLeftDocumentButton() {
        return leftDocumentButton;
    }

    Button getRightDocumentButton() {
        return rightDocumentButton;
    }

    public Collection<DocumentFile> getCombinedDocuments() {
        return combinedDocuments;
    }

    public void setCombinedDocuments(Collection<DocumentFile> combinedDocuments) {
        this.combinedDocuments = combinedDocuments;
    }

    public Button getAcceptMergeButton() {
        return acceptMergeButton;
    }

    public Button getClearMergeButton() {
        return clearMergeButton;
    }

    public Button getCancelMergeButton() {
        return cancelMergeButton;
    }

    MergeAccountsForm(){}

    public void create(){
        createFirstRow();
        createShortNameRow();
        createFullNameRow();
        createCommentsRow();
        createControllsRow();
        createDocumentsRow();
        createOkcancelRow();

        //this.setGridLinesVisible( true );

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow( Priority.ALWAYS );

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow( Priority.ALWAYS );

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow( Priority.ALWAYS );

        ColumnConstraints descriptionColumn =  new ColumnConstraints( 100.);

        this.getColumnConstraints().addAll(
                descriptionColumn,
                col1,
                new ColumnConstraints( 40 ),
                col2,
                new ColumnConstraints( 40 ),
                col3
        );
    }

    private void createFirstRow() {

        Label descriptionLabel = new BoldLabel("Property", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(descriptionLabel),
                MergeAccountsForm.DESCRIPTION_COLUMN,  MergeAccountsForm.TITLE_ROW);

        Label leftLabel = new BoldLabel("Left", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(leftLabel),
                MergeAccountsForm.LEFT_PROPERTY_COLUMN, MergeAccountsForm.TITLE_ROW);

        Label mergeLabel = new BoldLabel("Merged Property", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(mergeLabel),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.TITLE_ROW);

        Label rightLabel = new BoldLabel("Right", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(rightLabel),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.TITLE_ROW);
    }

    private void createShortNameRow() {

        Label descriptionLabel = new BoldLabel("ShortName", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(descriptionLabel),
                MergeAccountsForm.DESCRIPTION_COLUMN,  MergeAccountsForm.SHORT_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.leftShortNameTextField),
                MergeAccountsForm.LEFT_PROPERTY_COLUMN, MergeAccountsForm.SHORT_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(leftArrowShortNameButton),
                MergeAccountsForm.LEFT_ARROW_COLUMN,  MergeAccountsForm.SHORT_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(centerShortNameTextField),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.SHORT_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightArrowShortNameButton),
                MergeAccountsForm.RIGHT_ARROW_COLUMN, MergeAccountsForm.SHORT_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightShortNameTextField),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.SHORT_NAME_ROW);
    }

    private void createFullNameRow() {

        Label descriptionLabel = new BoldLabel("FullName", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(descriptionLabel),
                MergeAccountsForm.DESCRIPTION_COLUMN,  MergeAccountsForm.FULL_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.leftFullNameTextField),
                MergeAccountsForm.LEFT_PROPERTY_COLUMN, MergeAccountsForm.FULL_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(leftArrowFullNameButton),
                MergeAccountsForm.LEFT_ARROW_COLUMN,  MergeAccountsForm.FULL_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(centerFullNameTextField),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.FULL_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightArrowFullNameButton),
                MergeAccountsForm.RIGHT_ARROW_COLUMN, MergeAccountsForm.FULL_NAME_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightFullNameTextField),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.FULL_NAME_ROW);
    }

    private void createCommentsRow() {
        Label descriptionLabel = new BoldLabel("Comments", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(descriptionLabel),
                MergeAccountsForm.DESCRIPTION_COLUMN,  MergeAccountsForm.COMMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.leftCommentTextArea),
                MergeAccountsForm.LEFT_PROPERTY_COLUMN, MergeAccountsForm.COMMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(leftArrowCommentButton),
                MergeAccountsForm.LEFT_ARROW_COLUMN,  MergeAccountsForm.COMMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(centerCommentTextArea),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.COMMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightArrowCommentButton),
                MergeAccountsForm.RIGHT_ARROW_COLUMN, MergeAccountsForm.COMMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightCommentTextArea),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.COMMENT_ROW);
    }

    private void createDocumentsRow() {
        Label descriptionLabel = new BoldLabel("Documents", TEXT_SIZE);
        this.add(AnchorPaneWrapper.wrapInAnchorPane(descriptionLabel),
                MergeAccountsForm.DESCRIPTION_COLUMN,  MergeAccountsForm.DOCUMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.leftDocumentLabel),
                MergeAccountsForm.LEFT_PROPERTY_COLUMN, MergeAccountsForm.DOCUMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(leftDocumentButton),
                MergeAccountsForm.LEFT_ARROW_COLUMN,  MergeAccountsForm.DOCUMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(centerDocumentLabel),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.DOCUMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightDocumentButton),
                MergeAccountsForm.RIGHT_ARROW_COLUMN, MergeAccountsForm.DOCUMENT_ROW);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(this.rightDocumentLabel),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.DOCUMENT_ROW);
    }

    private void createControllsRow() {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(acceptMergeButton,clearMergeButton,cancelMergeButton);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(toolBar),
                MergeAccountsForm.MERGED_PROPERTY_COLUMN,  MergeAccountsForm.CONTROLLS_ROW);
    }

    private void createOkcancelRow() {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(okButton,cancelButton);

        this.add(AnchorPaneWrapper.wrapInAnchorPane(toolBar),
                MergeAccountsForm.RIGHT_PROPERTY_COLUMN,  MergeAccountsForm.OK_CANCEL_ROW);
    }

    void clearFormLeft() {
        leftShortNameTextField.setText(BLANK);
        leftFullNameTextField.setText(BLANK);
        leftCommentTextArea.setText(BLANK);
        leftDocumentLabel.setText(BLANK);
    }

    void clearFormRight() {
        rightShortNameTextField.setText(BLANK);
        rightFullNameTextField.setText(BLANK);
        rightCommentTextArea.setText(BLANK);
        rightDocumentLabel.setText(BLANK);
    }

    void clearFormCenter() {
        centerShortNameTextField.setText(BLANK);
        centerFullNameTextField.setText(BLANK);
        centerCommentTextArea.setText(BLANK);
        centerDocumentLabel.setText(BLANK);
    }
}
