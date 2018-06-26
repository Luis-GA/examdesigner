package util;

import controller.DatabaseManager;
import model.Exam;
import model.ExamPart;
import model.Question;

import javax.swing.plaf.DimensionUIResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExamGenerator {

    private ExamGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static Exam generateExam(Exam exam, int difficulty, int duration, int essaypercent, List<String> essayTopics, List<String> testTopics) throws ExamGeneratorException {
        if (duration<=0)
        {   //throw new ExamGeneratorException("txt.invalidDuration");
            duration=60;
            exam.setDuration(60);
        }
        if (testTopics.size()<=0)
            throw new ExamGeneratorException("txt.invalidTestTopics");
        if (essaypercent!=0 && essayTopics.size()<=0)
                throw new ExamGeneratorException("txt.invalidEssayTopics");

        DatabaseManager databaseManager = DatabaseManager.getInstance();

        List<ExamPart> parts = new ArrayList<>();

        //Essay part
        if (essaypercent != 0) {
            ExamPart essayPart = new ExamPart();
            ArrayList<Question> questions = new ArrayList<>();

                for (int i = 0; i < essayTopics.size(); i++) {
                    questions.addAll(databaseManager.getQuestions(essayTopics.get(i), "ESSAY"));
                }



            parts.add(generateEssayPart(essayPart, difficulty, duration, essaypercent, questions));
        }

        //Test part
        if (essaypercent != 100) {
            int weigh;
            if(essaypercent==0)
                weigh=100;
            else{
            duration=duration-parts.get(0).getDuration();
            weigh=100-parts.get(0).getWeigh();}
            ExamPart testPart = new ExamPart();
            ArrayList<Question> questions;
            do {
                questions = new ArrayList<>();
                for (int i = 0; i < testTopics.size(); i++) {
                    questions.addAll(databaseManager.getQuestions(testTopics.get(i), "TEST"));
                }

                testPart = generateTestPart(testPart, duration, questions);

            }while(testPart.getDuration()>duration || testPart.getWeigh()>weigh);
            testPart.setWeigh(weigh);
            testPart.setDuration(duration);
            parts.add(testPart);//Final evaluation
        }


        exam.setParts(parts);
        exam.setPartsChanged(true);
        return exam;
    }

    private static ExamPart generateEssayPart(ExamPart essayPart, int difficulty, int duration, int essaypercent, List<Question> questions) throws ExamGeneratorException {

        questions=randomOrderList(questions);

        int EssayDuration=duration*essaypercent/100;

        questions=ponderation(questions,(60/duration));
        List<Question>  selectedQuestions= new ArrayList<>();

        if(difficulty==-1)
            selectedQuestions = EssayWithoutDiff(selectedQuestions,EssayDuration,essaypercent,questions);
        else
            selectedQuestions = EssayWithDiff(selectedQuestions,EssayDuration,essaypercent,questions, difficulty);


        if (getTotalTime(selectedQuestions)>=duration*essaypercent/100*0.9 && getTotalWeight(selectedQuestions)>=essaypercent*0.9 ){
        essayPart.setQuestions(selectedQuestions);
        essayPart.setTitle("ESSAY");

        essayPart.setDuration(getTotalTime(selectedQuestions));
        essayPart.setWeigh(getTotalWeight(selectedQuestions));
        return essayPart;}
        else
            throw new ExamGeneratorException("txt.notEnoughEssayQuestions");
    }

    private static ExamPart generateTestPart(ExamPart testPart, int duration, List<Question>questions) throws ExamGeneratorException {

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
            if(counter>=questions.size())
                throw new ExamGeneratorException("txt.notEnoughTestQuestions");
            pregDuration=question.getDuration();
            if(pregDuration==-1)
                pregDuration=2;//TODO: create a constants file

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
    private static int getTotalTime(List<Question>questions){
        int time=0;
        for(Question q : questions)
            time=time+q.getDuration();
        return time;
    }
    private static int getTotalWeight(List<Question>questions){
        int weight=0;
        for(Question q : questions)
            weight=weight+q.getWeight();
        return weight;
    }
    private static int getTotalDiff(List<Question>questions){
        int difficulty=0;
        for(Question q : questions)
            difficulty=difficulty+q.getDifficulty();
        return difficulty;
    }
    private static int getAverageDiff(List<Question>questions){
        int n=(getTotalDiff(questions)/getTotalWeight(questions));
        return n;
    }
    private static List<Question> ponderation(List<Question>questions, double ponderation){

        for(Question q : questions)
            q.setWeight((int)(q.getWeight()*ponderation));

        return questions;
    }

    private  static List<Question> EssayWithoutDiff(List<Question>selectedQuestions, int duration,int weigth ,List<Question>questions){
        List<Question> quest= new ArrayList<Question>(questions);
        if (getTotalTime(selectedQuestions)>=duration*0.9 && getTotalWeight(selectedQuestions)>=weigth*0.9)
            return selectedQuestions;
        else{
            for (Question q : quest) {
                selectedQuestions.add(q);
                if (getTotalTime(selectedQuestions)<=duration && getTotalWeight(selectedQuestions)<=weigth)
                ;
                else
                    selectedQuestions.remove(q);
                questions.remove(q);
                EssayWithoutDiff(selectedQuestions,duration,weigth,questions);
                if (getTotalTime(selectedQuestions)>=duration*0.9 && getTotalWeight(selectedQuestions)>=weigth*0.9)
                    return selectedQuestions;
                questions.add(q);

            }

        }

        return selectedQuestions;
    }

    private  static List<Question> EssayWithDiff(List<Question>selectedQuestions, int duration,int weigth ,List<Question>questions, int difficulty){
        List<Question> quest= new ArrayList<Question>(questions);
        if (getTotalTime(selectedQuestions)>=duration*0.9 && getTotalWeight(selectedQuestions)>=weigth*0.9)
            if(getTotalDiff(selectedQuestions)==difficulty)
                return selectedQuestions;
        else{
            for (Question q : quest) {
                selectedQuestions.add(q);
                if (getTotalTime(selectedQuestions)<=duration && getTotalWeight(selectedQuestions)<=weigth)
                    ;
                else
                    selectedQuestions.remove(q);
                questions.remove(q);
                EssayWithoutDiff(selectedQuestions,duration,weigth,questions);
                if (getTotalTime(selectedQuestions)>=duration*0.9 && getTotalWeight(selectedQuestions)>=weigth*0.9)
                    if(getTotalDiff(selectedQuestions)==difficulty)
                        return selectedQuestions;
                questions.add(q);

            }

        }

        return selectedQuestions;
    }

    public static class ExamGeneratorException extends Exception
    {
        public ExamGeneratorException(String message)
        {
            super(message);
        }
    }
}

