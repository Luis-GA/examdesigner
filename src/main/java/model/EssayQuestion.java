package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Model class for an Essay Question
 **/

public class EssayQuestion extends Question {

    private ObservableList<Section> sections;
    private IntegerProperty answeringSpace;
    private ObservableList<ContentObject> solutionObjects;

    /**
     * ----- GETTERS -----
     **/
    public List<Section> getSections() {
        return sections;
    }

    public Integer getAnsweringSpace() {
        return answeringSpace.getValue();
    }

    public List<ContentObject> getSolutionObjects() {
        return solutionObjects;
    }
    /** ------------------- **/

    /**
     * ----- SETTERS -----
     **/
    public void setSections(List sections) {
        this.sections = FXCollections.observableList(sections);
    }

    public void setAnsweringSpace(Integer answeringSpace) {
        this.answeringSpace.setValue(answeringSpace);
    }

    public void setSolutionObjects(List<ContentObject> bodyObjects) {
        this.solutionObjects = FXCollections.observableList(bodyObjects);
    }
    /**
     * -------------------
     **/

    public EssayQuestion() {
        super();
        this.type = Type.ESSAY;
        this.sections = FXCollections.observableArrayList();
        this.answeringSpace = new SimpleIntegerProperty(1);
    }
}
