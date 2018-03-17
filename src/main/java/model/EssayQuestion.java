package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/** Model class for an Essay Question **/

public class EssayQuestion extends Question {

    protected ObservableList<Section> sections;
    protected IntegerProperty answeringSpace;

    /** ----- GETTERS ----- **/
    public List<Section> getSections() {
        return sections;
    }

    public Integer getAnsweringSpace() {
        return answeringSpace.getValue();
    }
    /** ------------------- **/

    /** ----- SETTERS ----- **/
    public void setSections(List sections) {
        this.sections = FXCollections.observableList(sections);
    }

    public void setAnsweringSpace(Integer answeringSpace) {
        this.answeringSpace.setValue(answeringSpace);
    }
    /** ------------------- **/

    public EssayQuestion() {
        super();
        this.type = Type.ESSAY;
        this.sections = FXCollections.observableArrayList();
        this.answeringSpace = new SimpleIntegerProperty(1);
    }
}
