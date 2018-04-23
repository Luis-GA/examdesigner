package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Choice;
import model.TestQuestion;
import util.DialogUtil;

import java.util.ArrayList;

public class TestQuestionOverviewController{

    @FXML
    private ListView<ChoiceHBox> choicesList;

    private Stage stage;

    @FXML
    public void initialize() {
        choicesList.getItems().add(new ChoiceHBox(choicesList.getItems(), new Choice()));
    }

    @FXML
    private void addChoice() {
        choicesList.getItems().add(new ChoiceHBox(choicesList.getItems(), new Choice()));
    }

    public TestQuestion getChoices() {
        TestQuestion aux = new TestQuestion();
        ObservableMap<String, Choice> choices = FXCollections.observableHashMap();
        ObservableList<String> correctChoices  = FXCollections.observableList(new ArrayList<>());
        char key = 'a';
        for(ChoiceHBox choiceHBox : choicesList.getItems()) {
            choices.put(String.valueOf(key), choiceHBox.choice);
            if(choiceHBox.isCorrect()) {
                correctChoices.add(key + ". " + choiceHBox.title);
            }
        }
        aux.setChoices(choices);
        aux.setCorrectChoices(correctChoices);
        return aux;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class ChoiceHBox extends GridPane {
        private TextField title = new TextField();
        private Button deleteButton = new Button();
        private Button openButton = new Button();
        private ObservableList<ChoiceHBox> list;
        private CheckBox isCorrect = new CheckBox();
        private Choice choice;

        public ChoiceHBox(ObservableList<ChoiceHBox> list, Choice choice) {
            super();

            this.list = list;
            this.choice = choice;

            ImageView openImage = new ImageView(new Image("images/ic_open_in_new_black.png"));
            openImage.setFitHeight(15);
            openImage.setFitWidth(15);
            openButton.setGraphic(openImage);
            openButton.setOnAction(event -> openContentObject());

            ImageView deleteImage = new ImageView(new Image("images/ic_delete_forever_black.png"));
            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            deleteButton.setGraphic(deleteImage);
            deleteButton.setOnAction(event -> deleteContentObject());

            title.setFont(Font.font(12));
            title.setMaxHeight(100);
            title.textProperty().bindBidirectional(this.choice.titleProperty());

            RowConstraints singleRow = new RowConstraints();
            this.getRowConstraints().add(singleRow);

            ColumnConstraints titleColumn = new ColumnConstraints();
            titleColumn.setHgrow(Priority.ALWAYS);
            this.getColumnConstraints().add(new ColumnConstraints(20));
            this.getColumnConstraints().add(titleColumn);
            this.getColumnConstraints().add(new ColumnConstraints(10));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            this.getColumnConstraints().add(new ColumnConstraints(40));

            this.add(isCorrect, 0, 0);
            this.add(title, 1, 0);
            this.add(deleteButton, 3, 0);
            this.add(openButton, 4, 0);
        }

        private void deleteContentObject() {
            this.list.remove(this);
        }

        private void openContentObject() {
            choice.setBodyObjects(DialogUtil.showContentObjectDialog(this.choice.getBodyObjects(), stage));
        }

        public boolean isCorrect() {
            return isCorrect.isSelected();
        }
    }
}