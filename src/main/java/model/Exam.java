package model;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import util.DateUtil;

/** Model class for an Exam **/

public class Exam {

    StringProperty name;
    ObjectProperty<LocalDate> date;
    String subject;

    public Exam(String name, ObjectProperty<LocalDate> date, String subject){

        this.name = new SimpleStringProperty(name);
        this.date = date;
        this.subject = subject;
    }

    public StringProperty nameProperty(){

        return this.name;
    }

    public StringProperty dateProperty(){

        return new SimpleStringProperty(DateUtil.format(this.date.getValue()));
    }

    public String getSubject(){

        return this.subject;
    }
}
