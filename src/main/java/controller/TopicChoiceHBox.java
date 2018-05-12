package controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TopicChoiceHBox extends HBox {
    Label topicLabel = new Label();
    CheckBox checkBox = new CheckBox();

    public TopicChoiceHBox(String topic) {
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