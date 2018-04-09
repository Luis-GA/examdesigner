package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import model.Exam;
import util.ExamParser;
import util.FileUtil;

import java.nio.file.Path;

public class RootLayoutController {


    // Reference to the main application.
    private MainApp mainApp;
    private Dialogs dialogs;
    private Exam exam;
    private Exam examOLD;

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
    public void showAboutDialog(){
        dialogs.showAboutDialog();
    }

    @FXML
    public void showOpenExamDialog(){
        dialogs.showOpenExamDialog();
    }

    @FXML
    public void showSaveAsExamDialog(){
        dialogs.showSaveAsExamDialog();
    }

    @FXML
    public void handleSaveExam(){
        DatabaseManager db = DatabaseManager.getInstance();
        db.addExam(new ExamParser(exam).toJson());
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        this.examOLD = exam.clone();
    }

    public boolean changes() {
        return !examOLD.equals(exam);
    }

    @FXML
    public void handleNewFile(){

    }

    @FXML
    public void handleClose(){

    }
}
