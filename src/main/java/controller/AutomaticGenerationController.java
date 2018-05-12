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

        for(int i=0; i<5; i++) {
            difficulty.getItems().add(i);
        }
        difficulty.setValue(0);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @FXML
    public  void handleGenerate() {
        List<String> selectedTopics = new ArrayList<>();
        for(TopicChoiceHBox topicChoice : testTopics.getItems()) {
            if(topicChoice.isChecked()) {
                selectedTopics.add(topicChoice.getTopic());
            }
        }

        //TODO send essay topics to the method

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showWorkIndicator(this.exam, (exam) -> {
            ExamGenerator.generateExam(this.exam, Integer.valueOf(difficulty.getValue()), Integer.valueOf(this.exam.durationProperty().getValue()), (int) percentageSlider.getValue(), selectedTopics);
            return true;
        });
    }
}
