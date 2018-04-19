package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.EssayQuestion;
import model.Question;
import model.TestQuestion;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class QuestionOverviewController extends DialogController{

    private VBox vBox;
    private static System.Logger logger = System.getLogger(QuestionOverviewController.class.getName());
    private Node testQuestionNode, essayQuestionNode;
    private TestQuestion testQuestion;
    private EssayQuestion essayQuestion;

    @FXML
    TextField title;
    @FXML
    TextField difficulty;
    @FXML
    TextField duration;
    @FXML
    TextField weight;
    @FXML
    TextField category;
    @FXML
    TextField subject;
    @FXML
    TextField topic;
    @FXML
    TextField subtopic;
    @FXML
    ComboBox typeSelector;

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

    }

    private Node getNode(String view) {
        Node auxNode;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            auxNode = loader.load();
        }  catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing specific questionOverview");
            auxNode = new Pane();
        }
        return auxNode;
    }

    public void setQuestion(TestQuestion testQuestion, EssayQuestion essayQuestion) {
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

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        duration.setTextFormatter(new TextFormatter<String>(integerFilter));
        weight.setTextFormatter(new TextFormatter<String>(integerFilter));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }

    public void setQuestions(TestQuestion testQuestion, EssayQuestion essayQuestion) {

    }

    public void setNodes(Node testQuestionNode, Node essayQuestionNode) {
        this.testQuestionNode = testQuestionNode;
        this.essayQuestionNode = essayQuestionNode;
        this.essayQuestionNode.setVisible(false);
        this.essayQuestionNode.setManaged(false);
    }
}
