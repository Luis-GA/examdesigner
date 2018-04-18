package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Model class for a Question
 **/

public abstract class Question {

    public enum Type {
        TEST,
        ESSAY
    }

    private StringProperty title;
    protected Type type;
    private ObservableList<ContentObject> bodyObjects;
    private IntegerProperty weight;
    private IntegerProperty duration;
    private IntegerProperty difficulty;
    private Integer idQuestion;

    private StringProperty category;
    private StringProperty subject;
    private StringProperty topic;
    private StringProperty subtopic;

    /**
     * ----- GETTERS -----
     **/
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

    public Integer getDifficulty() {
        return difficulty.getValue();
    }

    public String getCategory() {
        return category.getValue();
    }

    public String getSubject() {
        return subject.getValue();
    }

    public String getTopic() {
        return topic.getValue();
    }

    public String getSubtopic() {
        return subtopic.getValue();
    }
    /** ------------------- **/

    /**
     * ----- SETTERS -----
     **/
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

    public void setDifficulty(Integer difficulty) {
        this.difficulty.setValue(difficulty);
    }

    public void setCategory(String category) {
        this.category.setValue(category);
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public void setTopic(String topic) {
        this.topic.setValue(topic);
    }

    public void setSubtopic(String subtopic) {
        this.subtopic.setValue(subtopic);
    }

    /**
     * -------------------
     **/

    public Question() {
        this.title = new SimpleStringProperty("");
        this.bodyObjects = FXCollections.observableArrayList();
        this.weight = new SimpleIntegerProperty();
        this.duration = new SimpleIntegerProperty(0);
        this.difficulty = new SimpleIntegerProperty(0);

        this.category = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.topic = new SimpleStringProperty("");
        this.subtopic = new SimpleStringProperty("");

        this.idQuestion = new Integer((int)System.currentTimeMillis());
    }
}
