package util;

import model.ContentObject;
import model.Question;
import java.util.ArrayList;
import java.util.List;

public abstract class QuestionParser {

    private String title;
    private String type;
    private List<ContentObjectParser> bodyObjects;
    private Integer weight;
    private Integer duration;

    public QuestionParser() {

    }

    public QuestionParser(Question question){
        this.title = question.getTitle();
        this.type = question.getType();
        this.weight = question.getWeight();
        this.duration = question.getDuration();

        this.bodyObjects = new ArrayList<>();
        List<ContentObject> aux = question.getBodyObjects();
        for(ContentObject contentObject : aux){
            this.bodyObjects.add(new ContentObjectParser(contentObject));
        }
    }

    public Question parseQuestion(Question aux){
        aux.setTitle(this.title);
        aux.setWeight(this.weight);
        aux.setDuration(this.duration);

        List<ContentObject> auxList = new ArrayList<>();
        for(ContentObjectParser contentObject : this.bodyObjects){
            auxList.add(contentObject.parseContentObject());
        }
        aux.setBodyObjects(auxList);

        return aux;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBodyObjects(List<ContentObjectParser> bodyObjects) {
        this.bodyObjects = bodyObjects;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public List<ContentObjectParser> getBodyObjects() {
        return bodyObjects;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getDuration() {
        return duration;
    }

    abstract Question parseQuestion();
    abstract String toJson();
}
