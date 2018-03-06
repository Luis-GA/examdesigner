package model;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.*;
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

    protected ArrayList parts;

    public Exam(){
        this.title = new SimpleStringProperty();
        this.subject = new SimpleStringProperty();
        this.modality = new SimpleStringProperty();

        this.duration = new SimpleIntegerProperty();
        this.weigh = new SimpleIntegerProperty();
        this.numQuestions = new SimpleIntegerProperty();

        this.logo = null;

        this.examDate = new SimpleObjectProperty<>();
        this.publicationDate = new SimpleObjectProperty<>();
        this.reviewDate = new SimpleObjectProperty<>();

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
