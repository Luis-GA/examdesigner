package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.util.List;

public class TopicsDialogController extends DialogController{

    @FXML
    ListView<RadioButton> topicsList;
    ToggleGroup toggleGroup;

    private String selectedTopic;

    @FXML
    private void initialize() {
        DatabaseManager db = DatabaseManager.getInstance();
        List<String> topicList = db.getTopics();
        topicsList.setItems(FXCollections.observableArrayList());
        toggleGroup = new ToggleGroup();

        for(String topic : topicList) {
            RadioButton aux = new RadioButton(topic);
            aux.setToggleGroup(toggleGroup);
            topicsList.getItems().add(aux);
        }
    }

    public String getTopic() {
        return this.selectedTopic;
    }

    @FXML
    private void handleOk() {
        selectedTopic = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        selectedTopic = null;
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }
}
