package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Exam;
import util.ExamParser;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Dialogs {

    private static System.Logger logger = System.getLogger(DialogController.class.getName());

    private static void showDialog(String view, String title) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString(title));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(MainApp.getPrimaryStage());
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

    public static void showSettingsDialog() {
        showDialog("../view/SettingsDialog.fxml", "title.settings");
    }

    public static void showAboutDialog() {
        showDialog("../view/AboutDialog.fxml", "title.about");
    }

    public static Path showOpenExamDialog(Stage stage) {

        if(stage == null) {
            stage = MainApp.getPrimaryStage();
        }

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ResourceBundle.getBundle(MainApp.LABELS).getString("lbl.jsonFiles"), "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            return file.toPath();
        } else return null;
    }

    public static void showSaveAsExamDialog(Exam exam) {
        ExamParser examParser = new ExamParser(exam);
        FileUtil.writeJsonFile(MainApp.getPrimaryStage(), examParser.toJson());
    }

    public static void showExportDialog(Stage stage, String questions) {
        FileUtil.writeJsonFile(stage, questions);
    }
}
