package util;

import model.ContentObject;
import model.Question;
import java.util.List;

public abstract class QuestionParser {

    private String title;
    private String type;
    private List<ContentObject> bodyObjects;
    private Integer weight;
    private Integer duration;

    public QuestionParser(Question question){
        this.title = question.getTitle();
        this.type = question.getType();
        this.weight = question.getWeight();
        this.duration = question.getDuration();
        this.bodyObjects = question.getBodyObjects();
    }

    public Question parseQuestion(Question aux){
        aux.setTitle(this.title);
        aux.setType(this.type);
        aux.setWeight(this.weight);
        aux.setDuration(this.duration);
        aux.setBodyObjects(this.bodyObjects);

        return aux;
    }

    abstract Question parseQuestion();
}
