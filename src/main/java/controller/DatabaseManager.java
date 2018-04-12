package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.stage.Stage;
import model.Exam;
import model.Question;
import org.dizitart.no2.*;
import org.dizitart.no2.exceptions.IndexingException;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;
import util.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.dizitart.no2.filters.Filters.eq;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Nitrite db = Nitrite.builder().filePath("exam-designer.db").openOrCreate();
    private static System.Logger logger = System.getLogger(DatabaseManager.class.getName());

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

            NitriteCollection exams = db.getCollection("exams");
            questions.createIndex("title", indexOptions);
        } catch (IndexingException e) {
            logger.log(System.Logger.Level.INFO, "Indexes already exist");
        }
    }

    private DatabaseManager() {}

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
        Exam aux = new Exam();

        aux.setTitle((String)doc.get("title"));
        aux.setSubject((String)doc.get("subject"));
        aux.setModality((String)doc.get("modality"));

        aux.setDuration((Integer) doc.get("duration"));
        aux.setWeigh((Integer) doc.get("weight"));
        aux.setNumQuestions((Integer) doc.get("numQuestions"));

        aux.setLogo(ImageUtil.getImage((String)doc.get("logo")));

        aux.setExamDate(DateUtil.parse((String)doc.get("examDate")));
        aux.setPublicationDate(DateUtil.parse((String)doc.get("publicationDate")));
        aux.setReviewDate(DateUtil.parse((String)doc.get("reviewDate")));

        aux.setNameField((Boolean)doc.get("nameField"));
        aux.setSurnameField((Boolean)doc.get("surnameField"));
        aux.setIdNumberField((Boolean)doc.get("idNumberField"));
        aux.setGroupField((Boolean)doc.get("groupField"));

        aux.setInstructionDetails((String)doc.get("instructionDetails"));

        //TODO finish parts list
        /*
        List<ExamPart> auxList = new ArrayList<>();
        for(ExamPart part : this.parts){
            auxList.add(part.parseExamPart());
        }
        aux.setParts(auxList);
        */

        return aux;
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

    public void importQuestions(String jsonString) {

        try {
            List<QuestionParser> questions = new Questions(jsonString).getQuestions();

            for(QuestionParser question : questions){
                if(question instanceof  TestQuestionParser) {
                    question.setType(Question.Type.TEST.name());
                } else {
                    question.setType(Question.Type.ESSAY.name());
                }
                addQuestion(question.toJson());
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    private class Questions {

        private List<QuestionParser> questions;

        public Questions() {
            this.questions = new ArrayList<>();
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

            this.questions = aux.getQuestions();
        }

        public String toJson() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(this);
        }

        public List<QuestionParser> getQuestions() {
            return this.questions;
        }

        public void setQuestions(List<QuestionParser> questions) {
            this.questions = questions;
        }
    }
}
