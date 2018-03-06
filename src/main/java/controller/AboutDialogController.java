package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import java.awt.*;
import java.net.URI;
import java.util.ResourceBundle;

public class AboutDialogController extends DialogController {

    private static System.Logger logger = System.getLogger(AboutDialogController.class.getName());

    @FXML
    Hyperlink hyperlink;

    @FXML
    public void handleClickLink() {
        Desktop desktop =Desktop.getDesktop();
        try {
            desktop.browse(new URI(ResourceBundle.getBundle("languages/labels").getString("link.gitHub")));
        } catch (Exception e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to open a link in the browser");
        }

    }

    @FXML
    public void handleClose(){
        dialogStage.close();
    }
}
