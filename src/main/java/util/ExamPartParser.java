package util;

import model.EssayQuestion;
import model.ExamPart;
import model.Question;
import model.TestQuestion;

import java.util.ArrayList;
import java.util.List;

public class ExamPartParser {

    private String title;
    private Integer weight;
    private Integer duration;
    private String instructions;

    private List<QuestionParser> questions;

    public ExamPartParser(ExamPart examPart) {
        this.title = examPart.getTitle();
        this.weight = examPart.getWeigh();
        this.duration = examPart.getDuration();
        this.instructions = examPart.getInstructions();

        this.questions = new ArrayList<>();
        List<Question> aux = examPart.getQuestions();
        for (Question question : aux) {
            if (question instanceof TestQuestion)
                this.questions.add(new TestQuestionParser((TestQuestion) question));
            else if (question instanceof EssayQuestion)
                this.questions.add(new EssayQuestionParser((EssayQuestion) question));
        }
    }

    public ExamPart parseExamPart() {
        ExamPart aux = new ExamPart();

        aux.setTitle(this.title);
        aux.setWeigh(this.weight);
        aux.setDuration(this.duration);
        aux.setInstructions(this.instructions);

        List<Question> auxList = new ArrayList<>();
        for (QuestionParser question : this.questions) {
            auxList.add(question.parseQuestion());
        }
        aux.setQuestions(auxList);

        return aux;
    }
}
