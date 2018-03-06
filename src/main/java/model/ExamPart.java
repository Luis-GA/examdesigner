package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;

/** Model class for an ExamPart **/

public class ExamPart {

    protected StringProperty title;
    protected IntegerProperty weigh;
    protected IntegerProperty duration;
    protected StringProperty instructions;
    protected ArrayList<Question> questions;

}
