package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class Choice {

    protected StringProperty title;
    protected ObservableList<ContentObject> bodyObjects;

    /** ----- GETTERS ----- **/
    public String getTitle() {
        return title.getValue();
    }

    public List<ContentObject> getBodyObjects() {
        return bodyObjects;
    }
    /** ------------------- **/

    /** ----- SETTERS ----- **/
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setBodyObjects(List<ContentObject> bodyObjects) {
        this.bodyObjects = FXCollections.observableList(bodyObjects);
    }
    /** ------------------- **/

    public Choice() {
        this.title = new SimpleStringProperty("");
        this.bodyObjects = FXCollections.observableArrayList();
    }
}
