package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

/**
 * Model class for a Test Question
 **/

public class TestQuestion extends Question {

    public ObservableMap<String, Choice> choices;
    public ObservableList<String> correctChoices;

    /**
     * ----- GETTERS -----
     **/
    public Map<String, Choice> getChoices() {
        return choices;
    }

    public List<String> getCorrectChoices() {
        return correctChoices;
    }
    /** ------------------- **/

    /**
     * ----- SETTERS -----
     **/
    public void setChoices(Map<String, Choice> choices) {
        this.choices = FXCollections.observableMap(choices);
    }

    public void setCorrectChoices(List<String> correctChoices) {
        this.correctChoices = FXCollections.observableList(correctChoices);
    }

    /**
     * -------------------
     **/

    public TestQuestion() {
        super();
        this.type = Type.TEST;
        this.choices = FXCollections.observableHashMap();
        this.correctChoices = FXCollections.observableArrayList();
    }
}
