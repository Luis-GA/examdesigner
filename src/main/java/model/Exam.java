package model;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import util.DateUtil;

/** Model class for an Exam **/

public class Exam {

    protected StringProperty title;
    protected StringProperty subject;
    protected StringProperty modality;

    protected IntegerProperty duration;
    protected IntegerProperty weigh;
    protected IntegerProperty numQuestions;

    protected Image logo;

    protected ObjectProperty<LocalDate> examDate;
    protected ObjectProperty<LocalDate> publicationDate;
    protected ObjectProperty<LocalDate> reviewDate;

    protected BooleanProperty nameField;
    protected BooleanProperty surnameField;
    protected BooleanProperty idNumberField;
    protected BooleanProperty groupField;

    protected StringProperty instructionDetails;

    protected ObservableList<ExamPart> parts;

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
        return duration.getValue();
    }

    public int getWeigh() {
        return weigh.get();
    }

    public Integer getNumQuestions() {
        return numQuestions.getValue();
    }

    public Image getLogo() {
        return logo;
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
        this.duration.setValue(duration);
    }

    public void setWeigh(Integer weigh) {
        this.weigh.setValue(weigh);
    }

    public void setNumQuestions(Integer numQuestions) {
        this.numQuestions.setValue(numQuestions);
    }

    public void setLogo(Image logo) {
        this.logo = logo;
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

    public Exam(){
        this.title = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.modality = new SimpleStringProperty("");

        this.duration = new SimpleIntegerProperty(60);
        this.weigh = new SimpleIntegerProperty();
        this.numQuestions = new SimpleIntegerProperty(0);

        this.logo = null;

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

}
