package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Exam;
import util.DialogUtil;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class ExamOverviewController {

    @FXML
    TextField title;
    @FXML
    TextField subject;
    @FXML
    TextField modality;
    @FXML
    TextField duration;
    @FXML
    TextField weight;
    @FXML
    TextField numQuestions;
    @FXML
    ImageView logo;
    @FXML
    DatePicker examDate;
    @FXML
    DatePicker publicationDate;
    @FXML
    DatePicker reviewDate;
    @FXML
    CheckBox nameField;
    @FXML
    CheckBox surnameField;
    @FXML
    CheckBox idNumberField;
    @FXML
    CheckBox groupField;
    @FXML
    TextArea instructionDetails;

    Exam exam;

    @FXML
    private void initialize() {
        title.setText("");
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        title.textProperty().bindBidirectional(exam.titleProperty());
        subject.textProperty().bindBidirectional(exam.subjectProperty());
        modality.textProperty().bindBidirectional(exam.modalityProperty());
        duration.textProperty().bindBidirectional(exam.durationProperty());
        weight.textProperty().bindBidirectional(exam.weightProperty());
        numQuestions.textProperty().bindBidirectional(exam.numQuestionsProperty());
        logo.imageProperty().bindBidirectional(exam.getLogoView().imageProperty());
        examDate.valueProperty().bindBidirectional(exam.examDateProperty());
        publicationDate.valueProperty().bindBidirectional(exam.publicationDateProperty());
        reviewDate.valueProperty().bindBidirectional(exam.reviewDateProperty());
        nameField.selectedProperty().bindBidirectional(exam.nameFieldProperty());
        surnameField.selectedProperty().bindBidirectional(exam.surnameFieldProperty());
        idNumberField.selectedProperty().bindBidirectional(exam.idNumberFieldProperty());
        groupField.selectedProperty().bindBidirectional(exam.groupFieldProperty());
        instructionDetails.textProperty().bindBidirectional(exam.instructionDetailsProperty());

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        duration.setTextFormatter(new TextFormatter<String>(integerFilter));
        weight.setTextFormatter(new TextFormatter<String>(integerFilter));
        numQuestions.setTextFormatter(new TextFormatter<String>(integerFilter));
    }

    @FXML
    public void openLogoImageFile() {
        Path path = DialogUtil.showOpenImageDialog(MainApp.getPrimaryStage());

        if(path != null) {
            try {
                Image image = new Image("file:///" + path.toString());
                logo.setImage(image);
            } catch (Exception e) {
                DialogUtil.showInfoDialog("txt.imageError");
            }
        }
    }

    @FXML
    public void handleNext() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setAutomaticGenerationScene(this.exam);
    }
}
