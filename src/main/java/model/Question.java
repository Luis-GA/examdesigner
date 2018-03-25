package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/** Model class for a Question **/

public abstract class Question {

    public enum Type {
        TEST,
        ESSAY
    }

    protected StringProperty title;
    protected Type type;
    protected ObservableList<ContentObject> bodyObjects;
    protected IntegerProperty weight;
    protected IntegerProperty duration;
    protected Integer idQuestion;

    /** ----- GETTERS ----- **/
    public Integer getIdQuestion() {
        return idQuestion;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getType() {
        return type.name();
    }

    public List<ContentObject> getBodyObjects() {
        return bodyObjects;
    }

    public Integer getWeight() {
        return weight.getValue();
    }

    public Integer getDuration() {
        return duration.getValue();
    }
    /** ------------------- **/

    /** ----- SETTERS ----- **/
    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setBodyObjects(List<ContentObject> bodyObjects) {
        this.bodyObjects = FXCollections.observableList(bodyObjects);
    }

    public void setWeight(Integer weight) {
        this.weight.setValue(weight);
    }

    public void setDuration(Integer duration) {
        this.duration.setValue(duration);
    }
    /** ------------------- **/

    public Question() {
        this.title = new SimpleStringProperty("");
        this.bodyObjects = FXCollections.observableArrayList();
        this.weight = new SimpleIntegerProperty();
        this.duration = new SimpleIntegerProperty(0);
    }
}
