package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import model.Exam;
import util.ExamParser;
import util.FileUtil;

import java.nio.Buffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class WelcomeOverviewController {

    public static class HBoxCell extends GridPane {
        Label titleLabel = new Label();
        Button deleteButton = new Button();
        Button openButton = new Button();

        HBoxCell(String titleText) {
            super();
            titleLabel.setText(titleText);
            titleLabel.setFont(Font.font(12));

            ImageView deleteImage = new ImageView(new Image("images/ic_delete_forever_black.png"));
            ImageView openImage = new ImageView(new Image("images/ic_open_in_new_black.png"));

            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            openImage.setFitHeight(15);
            openImage.setFitWidth(15);

            deleteButton.setGraphic(deleteImage);
            openButton.setGraphic(openImage);

            deleteButton.setOnAction((event)->deleteExam(event.getSource()));

            openButton.setOnAction((event)->openExamFromList(event.getSource()));

            ColumnConstraints titleColumn= new ColumnConstraints();
            titleColumn.setHgrow(Priority.ALWAYS);
            this.getColumnConstraints().add(titleColumn);
            this.getColumnConstraints().add(new ColumnConstraints(40));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            RowConstraints singleRow = new RowConstraints(25);
            this.getRowConstraints().add(singleRow);

            this.add(titleLabel, 0, 0);
            this.add(deleteButton, 1, 0);
            this.add(openButton, 2, 0);
        }

        public Button getDeleteButton() {
            return this.deleteButton;
        }

        public Button getOpenButton() {
            return this.openButton;
        }
    }

    private static void deleteExam(Object button) {
        if(sceneManager.deleteConfirmation()) {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            int index = deleteButtonList.indexOf(button);
            databaseManager.deleteExam(exams.get(index));
            sceneManager.reloadWelcomeOverview();
        }
    }

    private static void openExamFromList(Object button) {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        int index = openButtonList.indexOf(button);
        Exam exam = databaseManager.getExam(exams.get(index));
        sceneManager.setExamOverviewScene(exam);
    }

    @FXML
    private ListView<HBoxCell> examList = new ListView<>();

    // Reference to the main application.
    private MainApp mainApp;
    private Dialogs dialogs;
    private static SceneManager sceneManager;
    private static List<Button> deleteButtonList = new ArrayList<>();
    private static List<Button> openButtonList = new ArrayList<>();
    private static List<String> exams;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dialogs = new Dialogs(mainApp);
    }

    @FXML
    private void initialize() {

        DatabaseManager db = DatabaseManager.getInstance();
        exams = db.getExams();
        deleteButtonList = new ArrayList<>();
        openButtonList = new ArrayList<>();

        List<HBoxCell> list = new ArrayList<>();
        for (String title : exams) {
            HBoxCell aux = new HBoxCell(title);
            list.add(aux);
            deleteButtonList.add(aux.getDeleteButton());
            openButtonList.add(aux.getOpenButton());
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
        examList.setItems(myObservableList);

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
