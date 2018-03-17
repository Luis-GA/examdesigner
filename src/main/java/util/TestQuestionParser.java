package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Question;
import model.Section;
import model.TestQuestion;
import java.util.ArrayList;
import java.util.List;

public class TestQuestionParser extends QuestionParser{

    private List<SectionParser> choices;
    private String correctChoice;

    public TestQuestionParser(TestQuestion testQuestion){
        super(testQuestion);
        this.correctChoice = testQuestion.getCorrectChoice();

        this.choices = new ArrayList<>();
        List<Section> aux = testQuestion.getChoices();
        for(Section section : aux){
            this.choices.add(new SectionParser(section));
        }
    }

    public TestQuestionParser (String questionJson){
        Gson gson = new GsonBuilder().create();
        TestQuestionParser aux = gson.fromJson(questionJson, TestQuestionParser.class);

        this.setTitle(aux.getTitle());
        this.setType(aux.getType());
        this.setBodyObjects(aux.getBodyObjects());
        this.setWeight(aux.getWeight());
        this.setDuration(aux.getDuration());

        this.choices = aux.choices;
        this.correctChoice = aux.correctChoice;
    }

    public Question parseQuestion(){
        TestQuestion aux = new TestQuestion();

        aux = (TestQuestion) super.parseQuestion(aux);
        aux.setCorrectChoice(this.correctChoice);

        List<Section> auxList = new ArrayList<>();
        for(SectionParser section : this.choices){
            auxList.add(section.parseSection());
        }
        aux.setChoices(auxList);

        return aux;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
