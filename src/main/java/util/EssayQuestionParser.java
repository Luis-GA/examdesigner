package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ContentObject;
import model.EssayQuestion;
import model.Question;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class EssayQuestionParser extends QuestionParser {

    protected List<SectionParser> sections;
    protected Integer answeringSpace;
    protected List<ContentObjectParser> solutionObjects;

    public EssayQuestionParser(EssayQuestion essayQuestion) {
        super(essayQuestion);
        this.answeringSpace = essayQuestion.getAnsweringSpace();

        this.sections = new ArrayList<>();
        List<Section> aux = essayQuestion.getSections();
        for (Section section : aux) {
            this.sections.add(new SectionParser(section));
        }

        this.solutionObjects = new ArrayList<>();
        List<ContentObject> aux2 = essayQuestion.getSolutionObjects();
        for (ContentObject contentObject : aux2) {
            this.solutionObjects.add(new ContentObjectParser(contentObject));
        }
    }

    public EssayQuestionParser(String questionJson) {
        Gson gson = new GsonBuilder().create();
        EssayQuestionParser aux = gson.fromJson(questionJson, EssayQuestionParser.class);

        this.setTitle(aux.getTitle());
        this.setType(aux.getType());
        this.setBodyObjects(aux.getBodyObjects());
        this.setWeight(aux.getWeight());
        this.setDuration(aux.getDuration());

        this.sections = aux.sections;
        this.answeringSpace = aux.answeringSpace;
        this.solutionObjects = aux.solutionObjects;
    }

    public Question parseQuestion() {
        EssayQuestion aux = new EssayQuestion();

        aux = (EssayQuestion) super.parseQuestion(aux);
        aux.setAnsweringSpace(this.answeringSpace);

        List<Section> auxList = new ArrayList<>();
        for (SectionParser section : this.sections) {
            auxList.add(section.parseSection());
        }
        aux.setSections(auxList);

        List<ContentObject> auxList2 = new ArrayList<>();
        for (ContentObjectParser contentObject : this.solutionObjects) {
            auxList2.add(contentObject.parseContentObject());
        }
        aux.setSolutionObjects(auxList2);

        return aux;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public void setType() {
        this.type = Question.Type.ESSAY.name();
    }
}
