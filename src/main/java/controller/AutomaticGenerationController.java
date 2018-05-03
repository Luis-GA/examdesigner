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
    ListView<TopicChoice> topics;

    Exam exam;

    @FXML
    public void initialize() {

        DatabaseManager db = DatabaseManager.getInstance();
        List<String> topicList = db.getTopics();
        topics.setItems(FXCollections.observableArrayList());

        for(String topic : topicList) {
            topics.getItems().add(new TopicChoice(topic));
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
        for(TopicChoice topicChoice : topics.getItems()) {
            if(topicChoice.isChecked()) {
                selectedTopics.add(topicChoice.getTopic());
            }
        }

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showWorkIndicator(this.exam, (exam) -> {
            ExamGenerator.generateExam(this.exam, Integer.valueOf(difficulty.getValue()), Integer.valueOf(this.exam.durationProperty().getValue()), Integer.valueOf(this.exam.weightProperty().getValue()), selectedTopics);
            return true;
        });
    }

    private class TopicChoice extends HBox {
        Label topicLabel = new Label();
        CheckBox checkBox = new CheckBox();

        public TopicChoice(String topic) {
            super();
            topicLabel.setText(topic);

            this.getChildren().add(checkBox);
            this.getChildren().add(topicLabel);
        }

        public boolean isChecked() {
            return checkBox.isSelected();
        }

        public String getTopic() {
            return topicLabel.getText();
        }
    }
}
