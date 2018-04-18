package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Choice;
import model.Question;
import model.TestQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestQuestionParser extends QuestionParser {

    protected Map<String, ChoiceParser> choices;
    protected List<String> correctChoices;

    public TestQuestionParser(TestQuestion testQuestion) {
        super(testQuestion);
        this.correctChoices = new ArrayList<>();

        for (String correctChoice : testQuestion.getCorrectChoices()) {
            correctChoices.add(correctChoice);
        }

        this.choices = new HashMap<>();
        Map<String, Choice> aux = testQuestion.getChoices();

        aux.forEach((k, v) -> this.choices.put(k, new ChoiceParser(v)));
    }

    public TestQuestionParser(String questionJson) {
        Gson gson = new GsonBuilder().create();
        TestQuestionParser aux = gson.fromJson(questionJson, TestQuestionParser.class);

        this.setTitle(aux.getTitle());
        this.setType(aux.getType());
        this.setBodyObjects(aux.getBodyObjects());
        this.setWeight(aux.getWeight());
        this.setDuration(aux.getDuration());

        this.choices = aux.choices;
        this.correctChoices = aux.correctChoices;
    }

    public Question parseQuestion() {
        TestQuestion aux = new TestQuestion();

        aux = (TestQuestion) super.parseQuestion(aux);
        aux.setCorrectChoices(this.correctChoices);

        Map<String, Choice> auxMap = new HashMap<>();

        this.choices.forEach((k, v) -> auxMap.put(k, v.parseChoice()));

        return aux;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public void setType() {
        this.type = Question.Type.TEST.name();
    }
}
