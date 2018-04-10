package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.EssayQuestion;
import model.Question;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class EssayQuestionParser extends QuestionParser {

    private List<SectionParser> sections;
    private Integer answeringSpace;

    public EssayQuestionParser(EssayQuestion essayQuestion) {
        super(essayQuestion);
        this.answeringSpace = essayQuestion.getAnsweringSpace();

        this.sections = new ArrayList<>();
        List<Section> aux = essayQuestion.getSections();
        for (Section section : aux) {
            this.sections.add(new SectionParser(section));
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

        return aux;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
