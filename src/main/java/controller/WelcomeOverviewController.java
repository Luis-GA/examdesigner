package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ResourceBundle;

public class WelcomeOverviewController extends DialogController {

    private static System.Logger logger = System.getLogger(WelcomeOverviewController.class.getName());

    @FXML
    private ListView<String> examList;

    // Reference to the main application.
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        setMainApp(mainApp);

        ObservableList<String> examListList = FXCollections.observableArrayList();

        /** Just for testing **/
        examListList.add("Multiplexores");
        examListList.add("Algoritmos Voraces");
        examListList.add("Patrones Software");
        examListList.add("Ecuaciones en Diferencias");

        // Add observable list data to the table
        examList.setItems(examListList);
    }

    private void showDialog(String view, String title){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(ResourceBundle.getBundle("languages/labels").getString(title));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.getIcons().add(new Image("images/exam_designer_256.png"));
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            DialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while showing dialog");
        }
    }

    @FXML
    public void showSettingsDialog() {
        showDialog("../view/SettingsDialog.fxml", "title.settings");
    }

    @FXML
    public void showAboutDialog(){
        showDialog("../view/AboutDialog.fxml", "title.about");
    }


}
