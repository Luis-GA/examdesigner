package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Exam;
import model.ExamPart;
import util.ExamParser;
import util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Dialogs {

    private static System.Logger logger = System.getLogger(DialogController.class.getName());

    // Reference to the main application.
    private MainApp mainApp;

    public Dialogs(MainApp mainApp) {
        this.mainApp = mainApp;
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

            DialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while showing dialog");
        }
    }

    public void showSettingsDialog() {
        showDialog("../view/SettingsDialog.fxml", "title.settings");
    }

    public void showAboutDialog(){
        showDialog("../view/AboutDialog.fxml", "title.about");
    }

    public Path showOpenExamDialog(){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ResourceBundle.getBundle("languages/labels").getString("lbl.jsonFiles"), "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            return file.toPath();
        } else return null;
    }

    public void showSaveAsExamDialog(Exam exam) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ResourceBundle.getBundle("languages/labels").getString("lbl.jsonFiles"), "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {

            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }

            ExamParser examParser = new ExamParser(exam);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(examParser.toJson());
            } catch (IOException e) {
                logger.log(System.Logger.Level.ERROR, "Error trying to save exam as JSON file");
            }
        }
    }
}
