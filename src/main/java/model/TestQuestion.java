package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/** Model class for a Test Question **/

public class TestQuestion extends Question {

    protected ObservableList<Section> choices;
    protected StringProperty correctChoice;

    /** ----- GETTERS ----- **/
    public List<Section> getChoices() {
        return choices;
    }

    public String getCorrectChoice() {
        return correctChoice.getValue();
    }
    /** ------------------- **/

    /** ----- SETTERS ----- **/
    public void setChoices(List<Section> choices) {
        this.choices = FXCollections.observableList(choices);
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice.setValue(correctChoice);
    }
    /** ------------------- **/

    public TestQuestion() {
        super();
        this.type = Type.TEST;
        this.choices = FXCollections.observableArrayList();
        this.correctChoice = new SimpleStringProperty();
    }
}
