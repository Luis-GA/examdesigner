package model;

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
    private StringProperty weight;
    private StringProperty duration;
    private StringProperty difficulty;
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
        return Integer.valueOf(weight.getValue());
    }

    public Integer getDuration() {
        return Integer.valueOf(duration.getValue());
    }

    public Integer getDifficulty() {
        return Integer.valueOf(difficulty.getValue());
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

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty weightProperty() {
        return weight;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty topicProperty() {
        return topic;
    }

    public StringProperty subtopicProperty() {
        return subtopic;
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
        this.weight.setValue(weight.toString());
    }

    public void setDuration(Integer duration) {
        this.duration.setValue(duration.toString());
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty.setValue(difficulty.toString());
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
        this.weight = new SimpleStringProperty("0");
        this.duration = new SimpleStringProperty("0");
        this.difficulty = new SimpleStringProperty("0");

        this.category = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.topic = new SimpleStringProperty("");
        this.subtopic = new SimpleStringProperty("");

        this.idQuestion = Integer.valueOf((int)System.currentTimeMillis());
    }
}
