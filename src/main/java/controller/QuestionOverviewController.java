package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.EssayQuestion;
import model.Question;
import model.TestQuestion;
import util.DialogUtil;
import util.EssayQuestionParser;
import util.TestQuestionParser;

import java.util.function.UnaryOperator;

public class QuestionOverviewController extends DialogController{

    private Node testQuestionNode, essayQuestionNode;
    private TestQuestion testQuestion;
    private EssayQuestion essayQuestion;

    @FXML
    private TextArea title;
    @FXML
    private TextField difficulty;
    @FXML
    private TextField duration;
    @FXML
    private TextField weight;
    @FXML
    private TextField category;
    @FXML
    private TextField subject;
    @FXML
    private TextField topic;
    @FXML
    private TextField subtopic;
    @FXML
    private ComboBox typeSelector;

    @FXML
    public void initialize() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add(Question.Type.TEST.name());
        typeList.add(Question.Type.ESSAY.name());
        typeSelector.setItems(typeList);
        typeSelector.getSelectionModel().select(Question.Type.TEST.name());
        typeSelector.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(newValue.equals(Question.Type.TEST.name())) {
                    essayQuestionNode.setManaged(false);
                    essayQuestionNode.setVisible(false);
                    testQuestionNode.setManaged(true);
                    testQuestionNode.setVisible(true);
                } else {
                    testQuestionNode.setManaged(false);
                    testQuestionNode.setVisible(false);
                    essayQuestionNode.setManaged(true);
                    essayQuestionNode.setVisible(true);
                }
            }
        });
    }

    @FXML
    public void showSetContentDialog() {
        this.testQuestion.setBodyObjects(DialogUtil.showContentObjectDialog(this.testQuestion.getBodyObjects(), this.getDialogStage()));
        this.essayQuestion.setBodyObjects(this.testQuestion.getBodyObjects());
    }

    @FXML
    private void saveQuestion() {

    }

    public void setQuestions(TestQuestion testQuestion, EssayQuestion essayQuestion) {
        this.testQuestion = testQuestion;
        this.essayQuestion = essayQuestion;

        title.textProperty().bindBidirectional(testQuestion.titleProperty());
        title.textProperty().bindBidirectional(essayQuestion.titleProperty());
        subject.textProperty().bindBidirectional(testQuestion.subjectProperty());
        subject.textProperty().bindBidirectional(essayQuestion.subjectProperty());
        duration.textProperty().bindBidirectional(testQuestion.durationProperty());
        duration.textProperty().bindBidirectional(essayQuestion.durationProperty());
        weight.textProperty().bindBidirectional(testQuestion.weightProperty());
        weight.textProperty().bindBidirectional(essayQuestion.weightProperty());
        category.textProperty().bindBidirectional(testQuestion.categoryProperty());
        category.textProperty().bindBidirectional(essayQuestion.categoryProperty());
        subject.textProperty().bindBidirectional(testQuestion.subjectProperty());
        subject.textProperty().bindBidirectional(essayQuestion.subjectProperty());
        topic.textProperty().bindBidirectional(testQuestion.topicProperty());
        topic.textProperty().bindBidirectional(essayQuestion.topicProperty());
        subtopic.textProperty().bindBidirectional(testQuestion.subtopicProperty());
        subtopic.textProperty().bindBidirectional(essayQuestion.subtopicProperty());

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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }

    public void setNodes(Node testQuestionNode, Node essayQuestionNode) {
        this.testQuestionNode = testQuestionNode;
        this.essayQuestionNode = essayQuestionNode;
        this.essayQuestionNode.setVisible(false);
        this.essayQuestionNode.setManaged(false);
    }

    public void setSaveButton(Button saveButton) {
        saveButton.setOnAction(event -> handleSaveQuestion());
    }

    private void handleSaveQuestion() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        if(this.typeSelector.getSelectionModel().getSelectedItem().equals(Question.Type.TEST.name())) {
            databaseManager.addQuestion(this.testQuestion.getIdQuestion(), new TestQuestionParser(this.testQuestion).toJson());
        } else {
            databaseManager.addQuestion(this.essayQuestion.getIdQuestion(), new EssayQuestionParser(this.essayQuestion).toJson());
        }
    }
}
