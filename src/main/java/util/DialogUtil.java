package util;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ContentObject;
import model.EssayQuestion;
import model.Exam;
import model.TestQuestion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtil {

    private static System.Logger logger = System.getLogger(DialogController.class.getName());

    private static DialogController showDialog(String view, String title, Stage stage) {

        if(stage == null) {
            stage = MainApp.getPrimaryStage();
        }

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
            dialogStage.initOwner(stage);
            dialogStage.getIcons().add(new Image("images/exam_designer_256.png"));
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            return controller;

        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while showing dialog");
        }
        return null;
    }

    public static List<ContentObject> showContentObjectDialog(List<ContentObject> contentObjects, Stage stage) {
        ContentObjectDialogController controller = (ContentObjectDialogController) showDialog("../view/ContentObjectDialog.fxml", "title.contentObject", stage);
        controller.setContentObjects(contentObjects);
        controller.getDialogStage().showAndWait();
        return controller.getContentObjectList();
    }

    public static void showSettingsDialog() {
        showDialog("../view/SettingsDialog.fxml", "title.settings", null).getDialogStage().showAndWait();
    }

    public static void showAboutDialog() {
        showDialog("../view/AboutDialog.fxml", "title.about", null).getDialogStage().showAndWait();
    }

    public static void showQuestionOverviewDialog(TestQuestion testQuestion, EssayQuestion essayQuestion, Stage stage) {

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/QuestionOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            VBox vBox = loader.load();

            TestQuestionOverviewController testController = new TestQuestionOverviewController();
            Node testQuestionNode = getNode("../view/TestQuestionOverview.fxml", testController);
            testController.setStage(stage);
            EssayQuestionOverviewController essayController = new EssayQuestionOverviewController();
            Node essayQuestionNode = getNode("../view/EssayQuestionOverview.fxml", essayController);
            essayController.setStage(stage);

            vBox.getChildren().add(testQuestionNode);
            vBox.getChildren().add(essayQuestionNode);

            AnchorPane buttonsAnchorPane = new AnchorPane();
            HBox buttonsHBox = new HBox();
            buttonsHBox.setSpacing(10);
            Button saveButton = new Button();
            Button cancelButton = new Button();
            buttonsHBox.getChildren().add(saveButton);
            buttonsHBox.getChildren().add(cancelButton);
            saveButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.save"));
            cancelButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.cancel"));
            cancelButton.setOnAction(event -> stage.close());
            buttonsAnchorPane.getChildren().add(buttonsHBox);
            buttonsAnchorPane.setBottomAnchor(buttonsHBox, Double.valueOf(10));
            buttonsAnchorPane.setRightAnchor(buttonsHBox, Double.valueOf(10));
            vBox.getChildren().add(buttonsAnchorPane);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString("title.editQuestion"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(stage);
            dialogStage.getIcons().add(new Image("images/exam_designer_256.png"));
            Scene scene = new Scene(vBox);
            dialogStage.setScene(scene);
            QuestionOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setNodes(testQuestionNode, essayQuestionNode);
            controller.setQuestions(testQuestion, essayQuestion);
            controller.setQuestions(testQuestion, essayQuestion);
            controller.setSaveButton(saveButton);

            // Show the dialog and wait until the user closes it
            controller.getDialogStage().showAndWait();

        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while showing dialog");
        }
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

    public static void showGenerateWordDialog(Exam exam, boolean solutions) {
        ExamParser examParser = new ExamParser(exam);
        FileUtil.writeWordFile(MainApp.getPrimaryStage(), examParser, solutions);
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

    private static Node getNode(String view, Object controller) {
        Node auxNode;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            auxNode = loader.load();
            controller = loader.getController();
        }  catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing specific questionOverview");
            auxNode = new Pane();
        }
        return auxNode;
    }
}
