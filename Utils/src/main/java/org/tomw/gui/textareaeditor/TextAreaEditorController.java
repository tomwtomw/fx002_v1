package org.tomw.gui.textareaeditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static org.tomw.utils.TomwStringUtils.BLANK;

public class TextAreaEditorController implements Initializable {

    @FXML
    private Label topLabel;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextArea textArea;

    private boolean okButtonPressed=false;
    private Stage dialogStage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleOkButtonPressed(){
        okButtonPressed=true;
        dialogStage.close();
    }
    @FXML
    public void handleCancelButtonPressed(){
        okButtonPressed=false;
        dialogStage.close();
    }

    public boolean isOkButtonPressed(){
        return okButtonPressed;
    }

    void setTitle(String inputText) {
        topLabel.setText(inputText);
    }

    void setText(String inputText) {
        textArea.setText(inputText);
    }

    String getText() {
        if(textArea.getText()!=null) {
            return textArea.getText().replaceAll("\n", "\r\n");
        }else{
            return BLANK;
        }
    }
}
