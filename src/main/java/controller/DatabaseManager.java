package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.stage.Stage;
import model.Exam;
import model.Question;
import org.dizitart.no2.*;
import org.dizitart.no2.exceptions.IndexingException;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;
import util.*;

import java.util.ArrayList;
import java.util.List;

import static org.dizitart.no2.filters.Filters.eq;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Nitrite db = Nitrite.builder().filePath("exam-designer.db").openOrCreate();
    private static System.Logger logger = System.getLogger(DatabaseManager.class.getName());

    private DatabaseManager() {
        throw new IllegalStateException("Utility class");
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            instance.initializeIndexes();
        }
        return instance;
    }

    private void initializeIndexes() {

        try {
            IndexOptions indexOptions = new IndexOptions();
            indexOptions.setIndexType(IndexType.Fulltext);

            NitriteCollection questions = db.getCollection("questions");
            questions.createIndex("type", indexOptions);
            questions.createIndex("topic", indexOptions);

            NitriteCollection exams = db.getCollection("exams");
            exams.createIndex("title", indexOptions);
        } catch (IndexingException e) {
            logger.log(System.Logger.Level.INFO, "Indexes already exist");
        }
    }

    public void addQuestion(String questionString) {
        NitriteCollection questions = db.getCollection("questions");
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document questionDocument = nitriteMapper.parse(questionString);
        questions.insert(questionDocument);
    }

    public void addExam(String examString) {
        NitriteCollection exams = db.getCollection("exams");
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document questionDocument = nitriteMapper.parse(examString);
        exams.insert(questionDocument);
    }

    public void deleteExam(String examTitle) {
        NitriteCollection collection = db.getCollection("exams");
        collection.remove(eq("title", examTitle));
    }

    public List<String> getExams() {

        NitriteCollection exams = db.getCollection("exams");
        Cursor cursor = exams.find();

        ArrayList<String> examsTitles = new ArrayList<>();
        for (Document exam : cursor) {
            examsTitles.add((String)exam.get("title"));
        }

        return examsTitles;
    }

    public Exam getExam(String title) {
        NitriteCollection collection = db.getCollection("exams");
        Cursor cursor = collection.find(eq("title", title));
        Document doc = cursor.firstOrDefault();
        JacksonMapper jacksonMapper = new JacksonMapper();
        ExamParser aux = new ExamParser(jacksonMapper.toJson(doc));

        return aux.parseExam();
    }

    public void exportQuestions(Stage stage) {
        NitriteCollection collection = db.getCollection("questions");
        Cursor cursor = collection.find();

        ArrayList<QuestionParser> questionsList = new ArrayList<>();

        for(Document question : cursor){
            QuestionParser aux;
            JacksonMapper jacksonMapper = new JacksonMapper();
            String type = (String)question.get("type");

            if(type.equals(Question.Type.TEST.name())) {
                aux = new TestQuestionParser(jacksonMapper.toJson(question));
            } else {
                aux = new EssayQuestionParser(jacksonMapper.toJson(question));
            }

            questionsList.add(aux);
        }

        Questions questions = new Questions();
        questions.setQuestions(questionsList);

        Dialogs.showExportDialog(stage, questions.toJson());
    }

    public void importQuestions(String jsonString) throws IllegalArgumentException{

            List<QuestionParser> questions = new Questions(jsonString).getQuestions();

            for(QuestionParser question : questions){
                if(question instanceof  TestQuestionParser) {
                    question.setType(Question.Type.TEST.name());
                } else {
                    question.setType(Question.Type.ESSAY.name());
                }
                addQuestion(question.toJson());
            }
    }

    private class Questions {

        private List<QuestionParser> questionsList;

        public Questions() {
            this.questionsList = new ArrayList<>();
        }

        public Questions(String jsonString) {

            RuntimeTypeAdapterFactory<QuestionParser> adapter = RuntimeTypeAdapterFactory
                    .of(QuestionParser.class, "type")
                    .registerSubtype(TestQuestionParser.class, Question.Type.TEST.name())
                    .registerSubtype(EssayQuestionParser.class, Question.Type.ESSAY.name());
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

            Questions aux;

            try {
                aux = gson.fromJson(jsonString, Questions.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid exam JSON file");
            }

            this.questionsList = aux.getQuestions();
        }

        public String toJson() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(this);
        }

        public List<QuestionParser> getQuestions() {
            return this.questionsList;
        }

        public void setQuestions(List<QuestionParser> questions) {
            this.questionsList = questions;
        }
    }
}
