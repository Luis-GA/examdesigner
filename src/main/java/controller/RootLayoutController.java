package controller;

import javafx.fxml.FXML;
import util.ExamParser;
import util.FileUtil;

import java.nio.file.Path;

public class RootLayoutController {


    // Reference to the main application.
    private MainApp mainApp;
    private Dialogs dialogs;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dialogs = new Dialogs(mainApp);
    }

    @FXML
    private void initialize() {
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
        /*
        File examFile = getPathFromSomewhere
        if (examFile != null) {
            mainApp.savePersonDataToFile(examFile);
        } else {
            showSaveAsExamDialog();
        }
        */
    }
}
