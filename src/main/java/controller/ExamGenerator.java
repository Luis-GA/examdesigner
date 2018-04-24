package controller;

import model.Exam;
import model.ExamPart;

import java.util.ArrayList;
import java.util.List;

public class ExamGenerator {

public Exam generateExam(Exam exam, int difficulty, int duration, int essaypercent, String[] topics){
    List<ExamPart> parts=new ArrayList<>();
    if(essaypercent!=0){
        ExamPart essayPart = new ExamPart();
        parts.add(generateEssayPart(essayPart));
    }



    if(essaypercent!=100) {
        ExamPart testPart = new ExamPart();
        parts.add(generateTestPart(testPart));
    }

    exam.setParts(parts);

    return exam;
}

private ExamPart generateEssayPart(ExamPart essayPart){
    //TODO
    return essayPart;
}

private ExamPart generateTestPart(ExamPart testPart){
    //TODO
        return testPart;
    }

}
