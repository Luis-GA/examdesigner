package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import model.Exam;
import model.Question;
import util.ExamGenerator;

import java.util.ArrayList;
import java.util.List;

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
        List<String> testTopicList = db.getTopics(Question.Type.TEST);
        List<String> essayTopicList = db.getTopics(Question.Type.ESSAY);
        testTopics.setItems(FXCollections.observableArrayList());
        essayTopics.setItems(FXCollections.observableArrayList());

        for(String topic : testTopicList) {
            testTopics.getItems().add(new TopicChoiceHBox(topic));
        }

        for(String topic : essayTopicList) {
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
    public void handleGenerate() {
        List<String> testSelectedTopics = new ArrayList<>();
        for(TopicChoiceHBox testTopicChoice : testTopics.getItems()) {
            if(testTopicChoice.isChecked()) {
                testSelectedTopics.add(testTopicChoice.getTopic());
            }
        }
        List<String> essaySelectedTopics = new ArrayList<>();
        for(TopicChoiceHBox essayTopicChoice : essayTopics.getItems()) {
            if(essayTopicChoice.isChecked()) {
                essaySelectedTopics.add(essayTopicChoice.getTopic());
            }
        }

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showWorkIndicator(this.exam, (exam) -> {
            try{
            ExamGenerator.generateExam(this.exam, Integer.valueOf(difficulty.getValue()), Integer.valueOf(this.exam.durationProperty().getValue()), (int) (100-percentageSlider.getValue()),essaySelectedTopics ,testSelectedTopics);}
            catch (Exception e){
                //TODO: Implement the ui
            }
            return true;
        });
    }

    @FXML
    public void handleBack() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.back();
    }
}
