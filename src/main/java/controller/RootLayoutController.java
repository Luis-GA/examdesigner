package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import model.Exam;
import util.ExamParser;
import util.FileUtil;

import java.nio.file.Path;
import java.util.ResourceBundle;

public class RootLayoutController {


    // Reference to the main application.
    private MainApp mainApp;
    private Dialogs dialogs;
    private Exam exam;
    private Exam examOLD;
    private SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    MenuItem menuClose;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dialogs = new Dialogs(mainApp);
    }

    @FXML
    private void initialize() {
        this.exam = new Exam();
        this.examOLD = new Exam();
    }

    @FXML
    public void showSettingsDialog() {
        dialogs.showSettingsDialog();
    }

    @FXML
    public void showAboutDialog() {
        dialogs.showAboutDialog();
    }

    @FXML
    public void showOpenExamDialog() {
        Path path = dialogs.showOpenExamDialog();

        if (path != null) {
            String examJson = FileUtil.readFile(path);
            try {
                ExamParser examParser = new ExamParser(examJson);

                if (mainApp.closeConfirmation()) {
                    sceneManager.changeExamOverviewScene(examParser.parseExam());
                } else {
                    if (!changes()) {
                        sceneManager.changeExamOverviewScene(examParser.parseExam());
                    }
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString("title.jsonError"));
                alert.setHeaderText(null);
                alert.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString("txt.jsonError"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void showSaveAsExamDialog() {
        dialogs.showSaveAsExamDialog(exam);
    }

    @FXML
    public void handleSaveExam() {
        DatabaseManager db = DatabaseManager.getInstance();
        db.addExam(new ExamParser(exam).toJson());
        examOLD = exam.copy();
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        this.examOLD = exam.copy();
    }

    public boolean changes() {
        return !examOLD.equals(exam);
    }

    @FXML
    public void handleNewFile() {
        Exam aux = new Exam();

        if (mainApp.closeConfirmation()) {
            sceneManager.changeExamOverviewScene(aux);
        } else {
            if (!changes()) {
                sceneManager.changeExamOverviewScene(aux);
            }
        }
    }

    @FXML
    public void handleClose() {
        if (mainApp.closeConfirmation()) {
            sceneManager.back();
        } else {
            if (!changes()) {
                sceneManager.back();
            }
        }
    }

    @FXML
    public void handleDelete() {
        if (sceneManager.deleteConfirmation()) {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            databaseManager.deleteExam(exam.getTitle());
            sceneManager.back();
        }
    }
}
