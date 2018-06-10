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
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.ContentObject;
import model.EssayQuestion;
import model.Section;
import util.DialogUtil;

import java.util.ResourceBundle;

public class EssayQuestionOverviewController {

    @FXML
    private ListView<SectionHBox> sectionsList;
    private ObservableList<ContentObject> solutionObjects;
    private EssayQuestion essayQuestion;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setQuestion(EssayQuestion essayQuestion) {
        this.essayQuestion = essayQuestion;
    }

    public void updateQuestion() {
        essayQuestion.setSections(sectionsList.getItems());
        essayQuestion.setSolutionObjects(solutionObjects);
    }

    @FXML
    public void initialize() {
        sectionsList.getItems().add(new SectionHBox(sectionsList.getItems(), new Section()));
    }

    @FXML
    private void addSection() {
        sectionsList.getItems().add(new SectionHBox(sectionsList.getItems(), new Section()));
    }

    @FXML
    private void setContent() {
        this.solutionObjects = (ObservableList<ContentObject>) DialogUtil.showContentObjectDialog(this.solutionObjects, this.stage);
    }

    public class SectionHBox extends GridPane {
        private TextField title = new TextField();
        private Button deleteButton = new Button();
        private Button contentButton = new Button();
        private Button solutionButton = new Button();
        private ObservableList<SectionHBox> list;
        private Section section;

        public SectionHBox(ObservableList<SectionHBox> list, Section section) {
            super();

            this.list = list;
            this.section = section;

            contentButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.content"));
            contentButton.setOnAction(event -> openContentObject());

            solutionButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.solution"));
            solutionButton.setOnAction(event -> openSolutionObject());

            ImageView deleteImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_delete_forever_black.png").toString()));
            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            deleteButton.setGraphic(deleteImage);
            deleteButton.setOnAction(event -> deleteContentObject());

            title.setFont(Font.font(12));
            title.setMaxHeight(100);
            title.textProperty().bindBidirectional(this.section.titleProperty());

            RowConstraints singleRow = new RowConstraints();
            this.getRowConstraints().add(singleRow);

            ColumnConstraints titleColumn = new ColumnConstraints();
            titleColumn.setHgrow(Priority.ALWAYS);

            this.getColumnConstraints().add(titleColumn);
            this.getColumnConstraints().add(new ColumnConstraints(10));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            this.getColumnConstraints().add(new ColumnConstraints());
            this.getColumnConstraints().add(new ColumnConstraints(10));
            this.getColumnConstraints().add(new ColumnConstraints());

            this.add(title, 0, 0);
            this.add(deleteButton, 2, 0);
            this.add(contentButton, 3, 0);
            this.add(solutionButton, 5, 0);
        }

        private void deleteContentObject() {
            this.list.remove(this);
        }

        private void openContentObject() {
            section.setBodyObjects(DialogUtil.showContentObjectDialog(this.section.getBodyObjects(), stage));
        }

        private void openSolutionObject() {
            section.setSolutionObjects(DialogUtil.showContentObjectDialog(this.section.getSolutionObjects(), stage));
        }
    }
}
