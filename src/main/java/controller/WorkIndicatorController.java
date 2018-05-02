package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;

public class WorkIndicatorController extends RootLayoutController{

    @FXML
    private HBox pane;

    private final ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
    private final VBox vbox = new VBox();

    @FXML
    private void initialize() {
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(progressIndicator);
        pane.getChildren().add(vbox);
    }
}

