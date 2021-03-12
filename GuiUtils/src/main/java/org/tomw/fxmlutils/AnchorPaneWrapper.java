package org.tomw.fxmlutils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneWrapper {

    public static void anchor(Node node){
        AnchorPane.setTopAnchor(node, 1.0);
        AnchorPane.setLeftAnchor(node, 1.0);
        AnchorPane.setRightAnchor(node, 1.0);
        AnchorPane.setBottomAnchor(node, 1.0);

    }

    public static AnchorPane wrapInAnchorPane(Node node){
        anchor(node);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(node);
        return anchorPane;
    }
}
