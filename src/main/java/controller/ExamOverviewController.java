package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Exam;

public class ExamOverviewController {

    @FXML
    TextField title;

    Exam exam;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        title.setText("");
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        title.textProperty().bindBidirectional(exam.title);
    }
}
