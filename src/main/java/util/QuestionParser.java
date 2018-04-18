package util;

import model.ContentObject;
import model.Question;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestionParser {

    protected String title;
    protected String type;
    protected List<ContentObjectParser> bodyObjects;
    protected Integer weight;
    protected Integer duration;
    protected Integer difficulty;
    protected Integer idQuestion;

    protected String category;
    protected String subject;
    protected String topic;
    protected String subtopic;

    public QuestionParser() {}

    public QuestionParser(Question question) {
        this.title = question.getTitle();
        this.type = question.getType();
        this.weight = question.getWeight();
        this.duration = question.getDuration();
        this.difficulty = question.getDifficulty();
        this.idQuestion = question.getIdQuestion();

        this.category = question.getCategory();
        this.subject = question.getSubject();
        this.topic = question.getTopic();
        this.subtopic = question.getSubtopic();

        this.bodyObjects = new ArrayList<>();
        List<ContentObject> aux = question.getBodyObjects();
        for (ContentObject contentObject : aux) {
            this.bodyObjects.add(new ContentObjectParser(contentObject));
        }
    }

    public Question parseQuestion(Question aux) {
        aux.setTitle(this.title);
        aux.setWeight(this.weight);
        aux.setDuration(this.duration);
        aux.setDifficulty(this.difficulty);
        aux.setIdQuestion(this.idQuestion);

        aux.setCategory(this.category);
        aux.setSubject(this.subject);
        aux.setTopic(this.topic);
        aux.setSubtopic(this.subtopic);

        List<ContentObject> auxList = new ArrayList<>();
        for (ContentObjectParser contentObject : this.bodyObjects) {
            auxList.add(contentObject.parseContentObject());
        }
        aux.setBodyObjects(auxList);

        return aux;
    }

    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
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

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public Integer getIdQuestion() {
        return idQuestion;
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

    public Integer getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public abstract Question parseQuestion();

    public abstract String toJson();

    public abstract void setType();
}
