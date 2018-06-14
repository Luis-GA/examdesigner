package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import model.Exam;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import util.DialogUtil;
import util.ExamParser;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class WelcomeOverviewController {

    public static class HBoxCell extends GridPane {
        Label titleLabel = new Label();
        Button deleteButton = new Button();
        Button openButton = new Button();

        HBoxCell(String titleText) {
            super();
            titleLabel.setText(titleText);
            titleLabel.setFont(Font.font(12));

            ImageView deleteImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_delete_forever_black.png").toString()));
            ImageView openImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_open_in_new_black.png").toString()));

            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            openImage.setFitHeight(15);
            openImage.setFitWidth(15);

            deleteButton.setGraphic(deleteImage);
            openButton.setGraphic(openImage);

            deleteButton.setOnAction(event -> deleteExam(event.getSource()));

            openButton.setOnAction(event -> openExamFromList(event.getSource()));

            ColumnConstraints titleColumn = new ColumnConstraints();
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

        private static void deleteExam(Object button) {
            if (DialogUtil.showDeleteConfirmationDialog("txt.deleteExamConfirmation")) {
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
    }

    @FXML
    private ListView<HBoxCell> examList = new ListView<>();

    private static SceneManager sceneManager;
    private static List<Button> deleteButtonList;
    private static List<Button> openButtonList;
    private static List<String> exams;

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
        DialogUtil.showSettingsDialog();
    }

    @FXML
    public void showAboutDialog() {
        DialogUtil.showAboutDialog();
    }

    @FXML
    public void showOpenExamDialog() {

        String examJson = FileUtil.readJsonFile(MainApp.getPrimaryStage());

        if(examJson != null) {
            try {
                ExamParser examParser = new ExamParser(examJson);
                if(!examParser.isValid())  {
                    throw new IllegalArgumentException();
                } else {
                    DatabaseManager databaseManager = DatabaseManager.getInstance();
                    databaseManager.addExam(examJson);
                    sceneManager.setExamOverviewScene(examParser.parseExam());
                }
            } catch (IllegalArgumentException e) {
                DialogUtil.showInfoDialog("txt.jsonError");
            } catch (UniqueConstraintException e) {
                DialogUtil.showInfoDialog("txt.titleInUse");
            }
        }
    }

    @FXML
    public void newExam() {
        Exam exam = new Exam();
        sceneManager.setExamOverviewScene(exam);
    }
}
