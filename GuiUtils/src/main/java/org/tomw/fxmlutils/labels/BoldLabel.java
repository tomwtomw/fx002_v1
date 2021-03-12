package org.tomw.fxmlutils.labels;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static org.tomw.utils.TomwStringUtils.BLANK;

public class BoldLabel extends Label{

    public BoldLabel(double size){
        this(BLANK, size);
    }

    public BoldLabel(String text, double size){
        super(text);
        setFont(Font.font("System", FontWeight.BOLD,size));
    }

    public void anchor(){
        AnchorPane.setTopAnchor(this, 1.0);
        AnchorPane.setLeftAnchor(this, 1.0);
        AnchorPane.setRightAnchor(this, 1.0);
        AnchorPane.setBottomAnchor(this, 1.0);
    }

    public AnchorPane wrapInAnchorPane(){
        anchor();
        AnchorPane anchorpane= new AnchorPane();
        anchorpane.getChildren().add(this);
        return anchorpane;
    }
}
