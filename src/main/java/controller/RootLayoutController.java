package controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import model.Exam;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import util.DialogUtil;
import util.ExamParser;
import util.FileUtil;

import java.nio.file.Path;

public class RootLayoutController {

    private Exam exam;
    private Exam examOLD;
    private SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    MenuItem menuClose;

    @FXML
    private void initialize() {
        this.exam = new Exam();
        this.examOLD = new Exam();
    }

    @FXML
    public void showSettingsDialog() {
        DialogUtil.showSettingsDialog();
    }

    @FXML
    public void showAboutDialog() {
        DialogUtil.showAboutDialog();
    }

    @FXML
    public void showOpenExamDialog() {
        Path path = DialogUtil.showOpenExamDialog(MainApp.getPrimaryStage());

        if (path != null) {
            String examJson = FileUtil.readFile(path);
            try {
                ExamParser examParser = new ExamParser(examJson);

                if (DialogUtil.showCloseConfirmationDialog()) {
                    sceneManager.changeExamOverviewScene(examParser.parseExam());
                } else {
                    if (!changes()) {
                        sceneManager.changeExamOverviewScene(examParser.parseExam());
                    }
                }
            } catch (IllegalArgumentException e) {
                DialogUtil.showInfoDialog("txt.jsonError");
            }
        }
    }

    @FXML
    public void showSaveAsExamDialog() {
        if(!exam.getTitle().equals("")) {
            DialogUtil.showSaveAsExamDialog(exam);
        } else {
            DialogUtil.showInfoDialog("txt.titleMandatory");
        }
    }

    @FXML
    public void showGenerateDocumentDialog() {
        showGenerateWordDialog(false);
    }

    @FXML
    public void showGenerateSolutionsDialog() {
        showGenerateWordDialog(true);
    }

    private void showGenerateWordDialog(boolean solutions) {
        if(!exam.getTitle().equals("")) {
            DialogUtil.showGenerateWordDialog(exam, solutions);
        } else {
            DialogUtil.showInfoDialog("txt.titleMandatory");
        }
    }

    @FXML
    public void handleSaveExam() {
        if(!exam.getTitle().equals("")) {
            if (changes()) {
                DatabaseManager db = DatabaseManager.getInstance();

                if (examOLD.getTitle().equals("")) {
                    try {
                        db.addExam(new ExamParser(exam).toJson());
                        DialogUtil.showInfoDialog("txt.examSaved");
                        examOLD = exam.copy();
                    } catch (UniqueConstraintException e) {
                        DialogUtil.showInfoDialog("txt.titleInUse");
                    }

                } else {
                    db.updateExam(examOLD.getTitle(), new ExamParser(exam).toJson());
                    examOLD = exam.copy();
                    DialogUtil.showInfoDialog("txt.examSaved");
                }
            }
        } else {
            DialogUtil.showInfoDialog("txt.titleMandatory");
        }

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

        if (DialogUtil.showCloseConfirmationDialog()) {
            sceneManager.changeExamOverviewScene(aux);
        } else {
            if (!changes()) {
                sceneManager.changeExamOverviewScene(aux);
            }
        }
    }

    @FXML
    public void handleClose() {
        if (DialogUtil.showCloseConfirmationDialog()) {
            sceneManager.back();
            sceneManager.reloadWelcomeOverview();
        }
    }

    @FXML
    public void handleDelete() {
        if (DialogUtil.showDeleteConfirmationDialog()) {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            databaseManager.deleteExam(exam.getTitle());
            sceneManager.back();
        }
    }
}
