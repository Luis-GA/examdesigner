package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Section {

    private StringProperty title;
    private ObservableList<ContentObject> bodyObjects;
    private ObservableList<ContentObject> solutionObjects;

    /**
     * ----- GETTERS -----
     **/
    public String getTitle() {
        return title.getValue();
    }

    public List<ContentObject> getBodyObjects() {
        return bodyObjects;
    }

    public List<ContentObject> getSolutionObjects() {
        return solutionObjects;
    }

    public StringProperty titleProperty() {
        return title;
    }
    /** ------------------- **/

    /**
     * ----- SETTERS -----
     **/
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setBodyObjects(List<ContentObject> bodyObjects) {
        this.bodyObjects = FXCollections.observableList(bodyObjects);
    }

    public void setSolutionObjects(List<ContentObject> solutionObjects) {
        this.solutionObjects = FXCollections.observableList(solutionObjects);
    }

    /**
     * -------------------
     **/

    public Section() {
        this.title = new SimpleStringProperty("");
        this.bodyObjects = FXCollections.observableArrayList();
        this.solutionObjects = FXCollections.observableArrayList();
    }
}
