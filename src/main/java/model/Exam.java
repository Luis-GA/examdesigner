package model;

import java.time.LocalDate;

import javafx.beans.property.*;

import javafx.scene.image.Image;
import util.DateUtil;

/** Model class for an Exam **/

public class Exam {

    public StringProperty title;
    public StringProperty subject;
    public StringProperty modality;

    public IntegerProperty duration;
    public IntegerProperty weigh;
    public IntegerProperty numQuestions;

    public Image logo;

    public ObjectProperty<LocalDate> examDate;
    public ObjectProperty<LocalDate> publicationDate;
    public ObjectProperty<LocalDate> reviewDate;

    public BooleanProperty nameField;
    public BooleanProperty surnameField;
    public BooleanProperty idNumberField;
    public BooleanProperty groupField;

    public StringProperty instructionDetails;

    public Exam(){
        this.title = new SimpleStringProperty();
        this.subject = new SimpleStringProperty();
        this.modality = new SimpleStringProperty();

        this.duration = new SimpleIntegerProperty();
        this.weigh = new SimpleIntegerProperty();
        this.numQuestions = new SimpleIntegerProperty();

        this.logo = null;

        this.examDate = new SimpleObjectProperty<LocalDate>();
        this.publicationDate = new SimpleObjectProperty<LocalDate>();
        this.reviewDate = new SimpleObjectProperty<LocalDate>();

        this.nameField = new SimpleBooleanProperty();
        this.surnameField = new SimpleBooleanProperty();
        this.idNumberField = new SimpleBooleanProperty();
        this.groupField = new SimpleBooleanProperty();

        this.instructionDetails = new SimpleStringProperty();
    }

    public String getTitle(){
        return this.title.getValue();
    }

    public StringProperty examDateProperty(){
        return new SimpleStringProperty(DateUtil.format(this.examDate.getValue()));
    }

    public Integer getNumQuestions(){
        return this.numQuestions.getValue();
    }

}
