package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import model.Exam;
import util.ExamParser;
import util.FileUtil;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class WelcomeOverviewController {

    @FXML
    private ListView<String> examList;

    // Reference to the main application.
    private MainApp mainApp;
    private Dialogs dialogs;
    private SceneManager sceneManager;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dialogs = new Dialogs(mainApp);
    }

    @FXML
    private void initialize() {
        DatabaseManager db = DatabaseManager.getInstance();
        ObservableList<String> examListList = FXCollections.observableArrayList(db.getExams());

        // Add observable list data to the table
        examList.setItems(examListList);

        sceneManager = SceneManager.getInstance();
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
        Path path = dialogs.showOpenExamDialog();

        if (path != null) {
            String examJson = FileUtil.readFile(path);
            try {
                ExamParser examParser = new ExamParser(examJson);
                sceneManager.setExamOverviewScene(examParser.parseExam());
            } catch(IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(ResourceBundle.getBundle("languages/labels").getString("title.jsonError"));
                alert.setHeaderText(null);
                alert.setContentText(ResourceBundle.getBundle("languages/labels").getString("txt.jsonError"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void newExam(){
        Exam exam = new Exam();
        sceneManager.setExamOverviewScene(exam);
    }
}
