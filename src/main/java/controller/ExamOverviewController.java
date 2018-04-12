package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Exam;

import java.nio.file.Path;
import java.util.ResourceBundle;
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
        title.textProperty().bindBidirectional(exam.title);
        subject.textProperty().bindBidirectional(exam.subject);
        modality.textProperty().bindBidirectional(exam.modality);
        duration.textProperty().bindBidirectional(exam.duration);
        weight.textProperty().bindBidirectional(exam.weight);
        numQuestions.textProperty().bindBidirectional(exam.numQuestions);
        logo.imageProperty().bindBidirectional(exam.logo.imageProperty());
        examDate.valueProperty().bindBidirectional(exam.examDate);
        publicationDate.valueProperty().bindBidirectional(exam.publicationDate);
        reviewDate.valueProperty().bindBidirectional(exam.reviewDate);
        nameField.selectedProperty().bindBidirectional(exam.nameField);
        surnameField.selectedProperty().bindBidirectional(exam.surnameField);
        idNumberField.selectedProperty().bindBidirectional(exam.idNumberField);
        groupField.selectedProperty().bindBidirectional(exam.groupField);
        instructionDetails.textProperty().bindBidirectional(exam.instructionDetails);

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
        Path path = Dialogs.showOpenImageDialog(MainApp.getPrimaryStage());

        if(path != null) {
            try {
                Image image = new Image("file:///" + path.toString());
                logo.setImage(image);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(MainApp.getPrimaryStage());
                alert.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString("title.imageError"));
                alert.setHeaderText(null);
                alert.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString("txt.imageError"));
                alert.showAndWait();
            }
        }
    }
}
