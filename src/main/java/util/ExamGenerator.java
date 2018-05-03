package util;

import controller.DatabaseManager;
import model.Exam;
import model.ExamPart;
import model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExamGenerator {

    private ExamGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static Exam generateExam(Exam exam, int difficulty, int duration, int essaypercent, List<String> topics) {
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        List<ExamPart> parts = new ArrayList<>();

        //Essay part
        if (essaypercent != 0) {
            ExamPart essayPart = new ExamPart();
            ArrayList<Question> questions = new ArrayList<>();
            for (int i = 0; i < topics.size(); i++) {
                questions.addAll(databaseManager.getQuestions(topics.get(i), "Essay"));
            }


            parts.add(generateEssayPart(essayPart, difficulty, duration, essaypercent, questions));
        }

        //Test part
        if (essaypercent != 100) {
            ExamPart testPart = new ExamPart();
            ArrayList<Question> questions;
            do {
                questions = new ArrayList<>();
                for (int i = 0; i < topics.size(); i++) {
                    questions.addAll(databaseManager.getQuestions(topics.get(i), "TEST"));
                }

                testPart = generateTestPart(testPart, duration, questions);
                testPart.setWeigh(100 - essaypercent);
            }while(testPart.getDuration()>duration || testPart.getWeigh()>100-essaypercent);
            parts.add(testPart);//Final evaluation
        }


        exam.setParts(parts);

        return exam;
    }

    private static ExamPart generateEssayPart(ExamPart essayPart, int difficulty, int duration, int essaypercent, List<Question> questions) {
        //TODO
        return essayPart;
    }

    private static ExamPart generateTestPart(ExamPart testPart, int duration, List<Question>questions) {
        //TODO
        questions=randomOrderList(questions);
        List<Question> selectedQuestions= new ArrayList<>();
        Question question;

        boolean condition= true;
        int counter=0;
        int pregDuration;
        int totalDuration=duration;

        while(condition){
            question= questions.get(counter);
            counter++;
            pregDuration=question.getDuration();
            if(pregDuration==-1)
                pregDuration=2;//TODO:hacer un fichero de constantes

            duration= duration - pregDuration;
            if (duration>=0)
                selectedQuestions.add(question);
            else{
                condition=false;
            }
        }
        testPart.setQuestions(selectedQuestions);
        testPart.setTitle("TEST");
        testPart.setDuration(totalDuration);
        return testPart;
    }

    private static List<Question> randomOrderList(List<Question>questions){
        Random random = new Random();
        Collections.shuffle(questions, random);
        return questions;
    }
}
