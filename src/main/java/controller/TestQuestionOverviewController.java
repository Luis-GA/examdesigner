package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import model.Choice;

import java.util.ResourceBundle;

public class TestQuestionOverviewController{

    @FXML
    ListView<ChoiceHBox> choicesList;

    @FXML
    public void initialize() {
        choicesList.getItems().add(new ChoiceHBox(choicesList.getItems(), new Choice()));
    }

    public class ChoiceHBox extends GridPane {
        private TextField title = new TextField();
        private Button deleteButton = new Button();
        private Button openButton = new Button();
        private ObservableList<ChoiceHBox> list;

        public ChoiceHBox(ObservableList<ChoiceHBox> list, Choice choice) {
            super();

            this.list = list;

            ImageView openImage = new ImageView(new Image("images/ic_open_in_new_black.png"));
            openImage.setFitHeight(15);
            openImage.setFitWidth(15);
            deleteButton.setGraphic(openImage);
            openButton.setOnAction(event -> openContent());

            ImageView deleteImage = new ImageView(new Image("images/ic_delete_forever_black.png"));
            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            deleteButton.setGraphic(deleteImage);
            deleteButton.setOnAction(event -> deleteContentObject());

            title.setFont(Font.font(12));
            title.setMaxHeight(100);
            title.textProperty().bindBidirectional(choice.titleProperty());

            this.getColumnConstraints().add(new ColumnConstraints(20));
            this.getColumnConstraints().add(new ColumnConstraints(300));
            this.getColumnConstraints().add(new ColumnConstraints(10));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            RowConstraints singleRow = new RowConstraints();
            this.getRowConstraints().add(singleRow);

            //TODO add checkbox
            this.add(title, 1, 0);
            this.add(openButton, 3, 0);
            this.add(deleteButton, 4, 0);
        }

        private void deleteContentObject() {
            this.list.remove(this);
        }

        private void openContent() {

        }
    }
}
