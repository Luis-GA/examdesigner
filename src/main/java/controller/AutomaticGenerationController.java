package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class AutomaticGenerationController {

    @FXML
    AnchorPane pane;
    @FXML
    TextField difficulty;
    @FXML
    TextField duration;
    @FXML
    TextField weight;
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

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        duration.setTextFormatter(new TextFormatter<String>(integerFilter));
        weight.setTextFormatter(new TextFormatter<String>(integerFilter));
        difficulty.setTextFormatter(new TextFormatter<String>(integerFilter));
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
        sceneManager.showWorkIndicator(this.exam);
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
