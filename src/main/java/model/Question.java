package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;

/** Model class for a Question **/

public class Question {

    protected StringProperty title;
    protected StringProperty type;
    protected ArrayList bodyObjects;
    protected IntegerProperty weight;
    protected IntegerProperty duration;

}
