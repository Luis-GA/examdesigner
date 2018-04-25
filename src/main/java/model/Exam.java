package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for an Exam
 **/

public class Exam {

    private StringProperty title;
    private StringProperty subject;
    private StringProperty modality;
    private StringProperty duration;
    private StringProperty weight;
    private StringProperty numQuestions;
    private ImageView logo;
    private ObjectProperty<LocalDate> examDate;
    private ObjectProperty<LocalDate> publicationDate;
    private ObjectProperty<LocalDate> reviewDate;
    private BooleanProperty nameField;
    private BooleanProperty surnameField;
    private BooleanProperty idNumberField;
    private BooleanProperty groupField;
    private StringProperty instructionDetails;

    private ObservableList<ExamPart> parts;

    /** ----- GETTERS ----- **/
    public String getTitle() {
        return title.getValue();
    }

    public String getSubject() {
        return subject.getValue();
    }

    public String getModality() {
        return modality.getValue();
    }

    public Integer getDuration() {
        return Integer.parseInt(duration.getValue());
    }

    public int getWeight() {
        return Integer.parseInt(weight.get());
    }

    public Integer getNumQuestions() {
        return Integer.parseInt(numQuestions.getValue());
    }

    public Image getLogo() {
        return logo.getImage();
    }

    public String getExamDate() {
        return DateUtil.format(examDate.getValue());
    }

    public String getPublicationDate() {
        return DateUtil.format(publicationDate.getValue());
    }

    public String getReviewDate() {
        return DateUtil.format(reviewDate.getValue());
    }

    public Boolean getNameField() {
        return nameField.getValue();
    }

    public Boolean getSurnameField() {
        return surnameField.getValue();
    }

    public Boolean getIdNumberField() {
        return idNumberField.getValue();
    }

    public Boolean getGroupField() {
        return groupField.getValue();
    }

    public String getInstructionDetails() {
        return instructionDetails.getValue();
    }

    public List<ExamPart> getParts() {
        return parts;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty modalityProperty() {
        return modality;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public StringProperty weightProperty() {
        return weight;
    }

    public StringProperty numQuestionsProperty() {
        return numQuestions;
    }

    public ObjectProperty<LocalDate> examDateProperty() {
        return examDate;
    }

    public ObjectProperty<LocalDate> publicationDateProperty() {
        return publicationDate;
    }

    public ObjectProperty<LocalDate> reviewDateProperty() {
        return reviewDate;
    }

    public boolean isNameField() {
        return nameField.get();
    }

    public BooleanProperty nameFieldProperty() {
        return nameField;
    }

    public boolean isSurnameField() {
        return surnameField.get();
    }

    public BooleanProperty surnameFieldProperty() {
        return surnameField;
    }

    public boolean isIdNumberField() {
        return idNumberField.get();
    }

    public BooleanProperty idNumberFieldProperty() {
        return idNumberField;
    }

    public boolean isGroupField() {
        return groupField.get();
    }

    public BooleanProperty groupFieldProperty() {
        return groupField;
    }

    public StringProperty instructionDetailsProperty() {
        return instructionDetails;
    }

    public ImageView getLogoView() {
        return this.logo;
    }
    /** ------------------- **/

    /** ----- SETTERS ----- **/
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public void setModality(String modality) {
        this.modality.setValue(modality);
    }

    public void setDuration(Integer duration) {
        this.duration.setValue(duration.toString());
    }

    public void setWeight(Integer weigh) {
        this.weight.setValue(weigh.toString());
    }

    public void setNumQuestions(Integer numQuestions) {
        this.numQuestions.setValue(numQuestions.toString());
    }

    public void setLogo(Image logo) {
        this.logo = new ImageView(logo);
    }

    public void setLogo(String url) {
        this.logo = new ImageView(new Image(url));
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate.setValue(examDate);
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate.setValue(publicationDate);
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate.setValue(reviewDate);
    }

    public void setNameField(Boolean nameField) {
        this.nameField.setValue(nameField);
    }

    public void setSurnameField(Boolean surnameField) {
        this.surnameField.setValue(surnameField);
    }

    public void setIdNumberField(Boolean idNumberField) {
        this.idNumberField.setValue(idNumberField);
    }

    public void setGroupField(Boolean groupField) {
        this.groupField.setValue(groupField);
    }

    public void setInstructionDetails(String instructionDetails) {
        this.instructionDetails.set(instructionDetails);
    }

    public void setParts(List<ExamPart> parts) {
        this.parts = FXCollections.observableList(parts);
    }
    /** ------------------- **/

    public Exam() {
        this.title = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.modality = new SimpleStringProperty("");

        this.duration = new SimpleStringProperty("0");
        this.weight = new SimpleStringProperty("0");
        this.numQuestions = new SimpleStringProperty("0");

        //this.logo = new ImageView(new Image("images/logo_default.png"));

        this.examDate = new SimpleObjectProperty<>(LocalDate.now());
        this.publicationDate = new SimpleObjectProperty<>(LocalDate.now());
        this.reviewDate = new SimpleObjectProperty<>(LocalDate.now());

        this.nameField = new SimpleBooleanProperty(false);
        this.surnameField = new SimpleBooleanProperty(false);
        this.idNumberField = new SimpleBooleanProperty(false);
        this.groupField = new SimpleBooleanProperty(false);

        this.instructionDetails = new SimpleStringProperty("");

        this.parts = FXCollections.observableArrayList();
    }

    @Override
    public boolean equals(Object other) {

        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Exam)) return false;

        Exam exam = (Exam) other;

        if (!this.title.getValue().equals(exam.title.getValue()))
            return false;
        if (!this.subject.getValue().equals(exam.subject.getValue()))
            return false;
        if (!this.modality.getValue().equals(exam.modality.getValue()))
            return false;
        if (!this.duration.getValue().equals(exam.duration.getValue()))
            return false;
        if (!this.weight.getValue().equals(exam.weight.getValue()))
            return false;
        if (!this.numQuestions.getValue().equals(exam.numQuestions.getValue()))
            return false;
        //TODO fix logo
        /*
        if(ImageUtil.getBase64(this.logo) != ImageUtil.getBase64(exam.logo))
            return false;
        */
        if (!this.examDate.getValue().equals(exam.examDate.getValue()))
            return false;
        if (!this.publicationDate.getValue().equals(exam.publicationDate.getValue()))
            return false;
        if (!this.reviewDate.getValue().equals(exam.reviewDate.getValue()))
            return false;
        if (!this.nameField.getValue().equals(exam.nameField.getValue()))
            return false;
        if (!this.surnameField.getValue().equals(exam.surnameField.getValue()))
            return false;
        if (!this.idNumberField.getValue().equals(exam.idNumberField.getValue()))
            return false;
        if (!this.groupField.getValue().equals(exam.groupField.getValue()))
            return false;
        if (!this.instructionDetails.getValue().equals(exam.instructionDetails.getValue()))
            return false;

        //TODO implement ExamPart.equals() and iterate the parts list
        return true;
    }

    @Override
    public int hashCode(){
        return this.title.hashCode();
    }

    public Exam copy() {

        Exam aux = new Exam();

        aux.setTitle(this.title.getValue());
        aux.setSubject(this.subject.getValue());
        aux.setModality(this.modality.getValue());

        aux.setDuration(Integer.parseInt(this.duration.getValue()));
        aux.setWeight(Integer.parseInt(this.weight.getValue()));
        aux.setNumQuestions(Integer.parseInt(this.numQuestions.getValue()));

        aux.setLogo(this.logo.getImage());

        aux.setExamDate(this.examDate.getValue());
        aux.setPublicationDate(this.publicationDate.getValue());
        aux.setReviewDate(this.reviewDate.getValue());

        aux.setNameField(this.nameField.getValue());
        aux.setSurnameField(this.surnameField.getValue());
        aux.setIdNumberField(this.idNumberField.getValue());
        aux.setGroupField(this.groupField.getValue());

        aux.setInstructionDetails(this.instructionDetails.getValue());

        List<ExamPart> auxList = new ArrayList<>();
        for (ExamPart part : this.parts) {
            auxList.add(part.copy());
        }
        aux.setParts(auxList);

        return aux;
    }
}
