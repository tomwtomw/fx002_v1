package org.tomw.fxmlutils;


import javafx.scene.control.Button;
import org.apache.log4j.Logger;

/**
 * Class which controls which CRUD buttons should be enabled or disabled
 */
public class CrudButtonsModeSelector {
    private final static Logger LOGGER = Logger.getLogger(CrudButtonsModeSelector.class.getName());

    private Button newButton;
    private Button saveButton;
    private Button deleteButton;
    private Button addButton;
    private Button removeButton;
    private Button cancelButton;
    private Button clearButton;
    private Button okButton;

    public CrudButtonsModeSelector(Button newButton,
                                   Button saveButton,
                                   Button deleteButton,
                                   Button addButton,
                                   Button removeButton,
                                   Button cancelbutton,
                                   Button clearButton,
                                   Button okButton) {
        this.newButton = newButton;
        this.saveButton = saveButton;
        this.deleteButton = deleteButton;
        this.addButton = addButton;
        this.removeButton = removeButton;
        this.cancelButton = cancelbutton;
        this.clearButton = clearButton;
        this.okButton = okButton;
    }

    public void activateButtons(boolean standaloneMode, boolean limitedMode, Object currentEntity){
        LOGGER.trace("standaloneMode="+standaloneMode+" limitedMode="+limitedMode+" currentEntity="+currentEntity);
        if(!standaloneMode && currentEntity==null){
            setMode1();
        }
        if(!standaloneMode && currentEntity!=null){
            setMode2();
        }
        if(standaloneMode && limitedMode && currentEntity==null){
            setMode3();
        }
        if(standaloneMode && limitedMode && currentEntity!=null){
            setMode4();
        }
        if(standaloneMode && !limitedMode && currentEntity==null){
            setMode5();
        }
        if(standaloneMode && !limitedMode && currentEntity!=null){
            setMode6();
        }
    }

    private void setMode6() {
        LOGGER.trace("mode 6");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(true);
        this.deleteButton.disableProperty().set(false);
        this.addButton.disableProperty().set(true);
        this.removeButton.disableProperty().set(true);
        this.cancelButton.disableProperty().set(false);
        this.clearButton.disableProperty().set(true);
        this.okButton.disableProperty().set(false);
    }

    private void setMode5() {
        LOGGER.trace("mode 5");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(true);
        this.deleteButton.disableProperty().set(true);
        this.addButton.disableProperty().set(true);
        this.removeButton.disableProperty().set(true);
        this.cancelButton.disableProperty().set(false);
        this.clearButton.disableProperty().set(true);
        this.okButton.disableProperty().set(true);
    }

    private void setMode4() {
        LOGGER.trace("mode 4");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(false);
        this.deleteButton.disableProperty().set(false);
        this.addButton.disableProperty().set(false);
        this.removeButton.disableProperty().set(false);
        this.cancelButton.disableProperty().set(false);
        this.clearButton.disableProperty().set(false);
        this.okButton.disableProperty().set(false);
    }

    private void setMode3() {
        LOGGER.trace("mode 3");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(true);
        this.deleteButton.disableProperty().set(true);
        this.addButton.disableProperty().set(false);
        this.removeButton.disableProperty().set(true);
        this.cancelButton.disableProperty().set(false);
        this.clearButton.disableProperty().set(true);
        this.okButton.disableProperty().set(true);
    }

    private void setMode2() {
        LOGGER.trace("mode 2");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(false);
        this.deleteButton.disableProperty().set(false);
        this.addButton.disableProperty().set(true);
        this.removeButton.disableProperty().set(true);
        this.cancelButton.disableProperty().set(true);
        this.clearButton.disableProperty().set(false);
        this.okButton.disableProperty().set(true);
    }

    private void setMode1() {
        LOGGER.trace("mode 1");
        this.newButton.disableProperty().set(false);
        this.saveButton.disableProperty().set(true);
        this.deleteButton.disableProperty().set(true);
        this.addButton.disableProperty().set(true);
        this.removeButton.disableProperty().set(true);
        this.cancelButton.disableProperty().set(true);
        this.clearButton.disableProperty().set(true);
        this.okButton.disableProperty().set(true);
    }
}
