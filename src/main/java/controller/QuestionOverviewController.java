package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.EssayQuestion;
import model.Question;
import model.TestQuestion;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class QuestionOverviewController {

    private AnchorPane anchorPane;
    private static System.Logger logger = System.getLogger(QuestionOverviewController.class.getName());
    private Pane testQuestionGrid, essayQuestionGrid;
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
        testQuestionGrid = getPane("../view/TestQuestionOverview.fxml");
        essayQuestionGrid = getPane("../view/EssayQuestionOverview.fxml");
        anchorPane.setBottomAnchor(testQuestionGrid, Double.valueOf(50));
        anchorPane.setBottomAnchor(essayQuestionGrid, Double.valueOf(50));
        essayQuestionGrid.setVisible(false);

        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add(Question.Type.TEST.name());
        typeList.add(Question.Type.TEST.name());
        typeSelector.setItems(typeList);
        typeSelector.getSelectionModel().select(Question.Type.TEST.name());
    }

    @FXML
    public void showSetContentDialog() {

    }

    @FXML
    public void handleTypeChange() {
        if(typeSelector.getSelectionModel().equals(Question.Type.TEST.name())) {
            essayQuestionGrid.setVisible(false);
            testQuestionGrid.setVisible(true);
        } else {
            testQuestionGrid.setVisible(false);
            essayQuestionGrid.setVisible(true);
        }
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    private Pane getPane(String view) {
        Pane auxPane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            auxPane = loader.load();
        }  catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing specific questionOverview");
            auxPane = new Pane();
        }
        return auxPane;
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
}
