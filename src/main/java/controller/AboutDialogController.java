package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.util.ResourceBundle;

public class AboutDialogController {

    private Stage dialogStage;

    @FXML
    Hyperlink hyperlink;

    @FXML
    public void handleClickLink() {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        try {
            desktop.browse(new URI(ResourceBundle.getBundle("languages/labels").getString("link.gitHub")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void handleClose(){
        dialogStage.close();
    }
}
