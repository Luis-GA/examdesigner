package model;

import java.time.LocalDate;

import javafx.beans.property.*;

import javafx.scene.image.Image;
import util.DateUtil;

import com.google.gson.Gson;

/** Class to parse an format Exam objects into Primitive attributes to generate and recover from json file**/

public class ExamParser {

    private String title;
    private String subject;
    private String modality;

    private Integer duration;
    private Integer weigh;
    private Integer numQuestions;

    private String logo;

    private String examDate;
    private String publicationDate;
    private String reviewDate;

    private Boolean nameField;
    private Boolean surnameField;
    private Boolean idNumberField;
    private Boolean groupField;

    private String instructionDetails;

    public ExamParser(Exam exam){
        this.title = exam.title.getValue();
        this.subject = exam.subject.getValue();
        this.modality = exam.modality.getValue();

        this.duration = exam.duration.getValue();
        this.weigh = exam.weigh.getValue();
        this.numQuestions = exam.numQuestions.getValue();

        //TODO make logo correct
        this.logo = "D6Ne9rn98NE45UfBD7DN6g7SJd76cD34In65oWe3J4gD65jIj34HefH";

        this.examDate = DateUtil.format(exam.examDate.getValue());
        this.publicationDate = DateUtil.format(exam.publicationDate.getValue());
        this.reviewDate = DateUtil.format(exam.publicationDate.getValue());

        this.nameField = exam.nameField.getValue();
        this.surnameField = exam.surnameField.getValue();
        this.idNumberField = exam.idNumberField.getValue();
        this.groupField = exam.groupField.getValue();

        this.instructionDetails = exam.instructionDetails.getValue();
    }

    public Exam parseExam(){
        Exam aux = new Exam();

        aux.title = new SimpleStringProperty(this.title);
        aux.subject = new SimpleStringProperty(this.subject);
        aux.modality = new SimpleStringProperty(this.modality);

        aux.duration = new SimpleIntegerProperty(this.duration);
        aux.weigh = new SimpleIntegerProperty(this.weigh);
        aux.numQuestions = new SimpleIntegerProperty(this.numQuestions);

        //TODO make logo correct
        aux.logo = null;

        aux.examDate = new SimpleObjectProperty<LocalDate>(DateUtil.parse(this.examDate));
        aux.publicationDate = new SimpleObjectProperty<LocalDate>(DateUtil.parse(this.publicationDate));
        aux.reviewDate = new SimpleObjectProperty<LocalDate>(DateUtil.parse(this.reviewDate));

        aux.nameField = new SimpleBooleanProperty(this.nameField);
        aux.surnameField = new SimpleBooleanProperty(this.surnameField);
        aux.idNumberField = new SimpleBooleanProperty(this.idNumberField);
        aux.groupField = new SimpleBooleanProperty(this.groupField);

        aux.instructionDetails = new SimpleStringProperty(this.instructionDetails);

        return aux;
    }

    public ExamParser (String examJson){
        Gson gson = new Gson();
        ExamParser aux = gson.fromJson(examJson, ExamParser.class);

        this.numQuestions = aux.numQuestions;
        this.title = aux.title;
        this.duration = aux.duration;
        this.groupField = aux.groupField;
        this.idNumberField = aux.idNumberField;
        this.instructionDetails = aux.instructionDetails;
        this.modality = aux.modality;
        this.nameField = aux.nameField;
        this.subject = aux.subject;
        this.surnameField = aux.surnameField;
        this.weigh = aux.weigh;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

}
