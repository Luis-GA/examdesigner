package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.Exam;
import util.ExamGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class AutomaticGenerationController {

    @FXML
    AnchorPane pane;
    @FXML
    ChoiceBox<Integer> difficulty;
    @FXML
    ListView<TopicChoiceHBox> testTopics;
    @FXML
    ListView<TopicChoiceHBox> essayTopics;
    @FXML
    Slider percentageSlider;

    Exam exam;

    @FXML
    public void initialize() {

        DatabaseManager db = DatabaseManager.getInstance();
        List<String> topicList = db.getTopics();
        testTopics.setItems(FXCollections.observableArrayList());
        essayTopics.setItems(FXCollections.observableArrayList());

        for(String topic : topicList) {
            testTopics.getItems().add(new TopicChoiceHBox(topic));
        }

        for(String topic : topicList) {
            essayTopics.getItems().add(new TopicChoiceHBox(topic));
        }

        for(int i=0; i<4; i++) {
            difficulty.getItems().add(i);
        }
        difficulty.setValue(0);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @FXML
    public  void handleGenerate() {
        List<String> testselectedTopics = new ArrayList<>();
        for(TopicChoiceHBox testtopicChoice : testTopics.getItems()) {
            if(testtopicChoice.isChecked()) {
                testselectedTopics.add(testtopicChoice.getTopic());
            }
        }
        List<String> essayselectedTopics = new ArrayList<>();
        for(TopicChoiceHBox essaytopicChoice : essayTopics.getItems()) {
            if(essaytopicChoice.isChecked()) {
                testselectedTopics.add(essaytopicChoice.getTopic());
            }
        }

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showWorkIndicator(this.exam, (exam) -> {
            ExamGenerator.generateExam(this.exam, Integer.valueOf(difficulty.getValue()), Integer.valueOf(this.exam.durationProperty().getValue()), (int) (100-percentageSlider.getValue()),essayselectedTopics ,testselectedTopics);
            return true;
        });
    }
}
