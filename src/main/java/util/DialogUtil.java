package util;

import controller.DialogController;
import controller.MainApp;
import controller.SceneManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ContentObject;
import model.Exam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtil {

    private static DialogController showDialog(String view, String title) {
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
            return controller;

        } catch (IOException e) {
            System.Logger logger = System.getLogger(DialogController.class.getName());
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while showing dialog");
        }
        return null;
    }

    public static void showContentObjectDialog(List<ContentObject> contentObjects) {
        DialogController controller = showDialog("../view/ContentObjectDialog.fxml", "title.contentObject");
        controller.getDialogStage().show();
        controller.setContentObjects(contentObjects);
    }

    public static void showSettingsDialog() {
        showDialog("../view/SettingsDialog.fxml", "title.settings").getDialogStage().showAndWait();
    }

    public static void showAboutDialog() {
        showDialog("../view/AboutDialog.fxml", "title.about").getDialogStage().showAndWait();
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

    public static Path showOpenImageDialog(Stage stage) {

        if(stage == null) {
            stage = MainApp.getPrimaryStage();
        }

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ResourceBundle.getBundle(MainApp.LABELS).getString("lbl.imageFiles"), "*.png", "*.jpg", "*.jpeg");
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

    public static void showInfoDialog(String labelKey) {
        Alert languageChangedDialog = new Alert(Alert.AlertType.INFORMATION);
        languageChangedDialog.setHeaderText(null);
        languageChangedDialog.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString(labelKey));

        Stage stage = (Stage) languageChangedDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/exam_designer_256.png"));

        languageChangedDialog.showAndWait();
    }

    public static boolean showCloseConfirmationDialog() {
        SceneManager sceneManager = SceneManager.getInstance();
        if (sceneManager.changes()) {
            return showConfirmationDialog("btn.exit", "txt.exit");
        } else {
            return true;
        }
    }

    public static boolean showDeleteConfirmationDialog() {
        return showConfirmationDialog("btn.delete", "txt.deleteConfirmation");
    }

    public static boolean showConfirmationDialog(String title, String text) {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString(title));
        closeConfirmation.setHeaderText(null);
        closeConfirmation.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString(text));
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(MainApp.getPrimaryStage());

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();

        if (closeResponse.isPresent()) {
            return ButtonType.OK.equals(closeResponse.get());
        } else {
            return false;
        }
    }
}
