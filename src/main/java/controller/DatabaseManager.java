package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.stage.Stage;
import model.Exam;
import model.Question;
import org.dizitart.no2.*;
import org.dizitart.no2.exceptions.IndexingException;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import org.dizitart.no2.filters.Filters;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dizitart.no2.filters.Filters.*;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Nitrite db = Nitrite.builder().filePath("exam-designer.db").openOrCreate();
    private static System.Logger logger = System.getLogger(DatabaseManager.class.getName());

    private static final String QUESTIONS = "questions";
    private static final String EXAMS = "exams";
    private static final String TITLE = "title";
    private static final String VALUE = "value";
    public static final String TEST_TOPICS = "test_topics";
    public static final String ESSAY_TOPICS = "essay_topics";
    private static final String TOPIC = "topic";

    private DatabaseManager() {
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
            //TODO check manually if index exists before creating it
            IndexOptions examIndexOptions = new IndexOptions();
            examIndexOptions.setIndexType(IndexType.Unique);

            IndexOptions idQuestionIndexOptions = new IndexOptions();
            idQuestionIndexOptions.setIndexType(IndexType.Unique);

            IndexOptions questionIndexOptions = new IndexOptions();
            questionIndexOptions.setIndexType(IndexType.NonUnique);

            NitriteCollection questions = db.getCollection(QUESTIONS);
            questions.createIndex("idQuestion",  idQuestionIndexOptions);
            questions.createIndex("title", questionIndexOptions);
            questions.createIndex("type", questionIndexOptions);
            questions.createIndex(TOPIC, questionIndexOptions);

            NitriteCollection exams = db.getCollection(EXAMS);
            exams.createIndex(TITLE, examIndexOptions);
        } catch (IndexingException e) {
            logger.log(System.Logger.Level.INFO, "Indexes already exist");
        }
    }

    public void addQuestion(Integer idQuestion, String questionString) {
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document questionDocument = nitriteMapper.parse(questionString);
        String newTopic = (String) questionDocument.get(TOPIC);

        String typeString = (String) questionDocument.get("type");
        String typeCollection;
        Question.Type questionType;
        if(typeString.compareTo(Question.Type.TEST.name()) == 0) {
            typeCollection = TEST_TOPICS;
            questionType = Question.Type.TEST;
        } else {
            typeCollection = ESSAY_TOPICS;
            questionType = Question.Type.ESSAY;
        }

        // Check if topic exists, if not add topic to list
        if(!topicExists(questionType, newTopic)) {
            addTopic(typeCollection, newTopic);
        }

        if(idQuestion == -1) {
            idQuestion = Integer.valueOf((int)System.currentTimeMillis());
        }
        try {
            NitriteCollection questions = db.getCollection(QUESTIONS);
            questions.insert(questionDocument);
        } catch (UniqueConstraintException e) {
            updateQuestion(idQuestion, questionString);
        }
    }

    public void updateQuestion(Integer idQuestion, String questionString) {
        String oldTopic = (String) getQuestionDocument(idQuestion).get(TOPIC);

        NitriteCollection collection = db.getCollection(QUESTIONS);
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document questionDocument = nitriteMapper.parse(questionString);
        String newTopic = (String) questionDocument.get(TOPIC);
        collection.update(eq("idQuestion", idQuestion), questionDocument);

        String typeString = (String) questionDocument.get("type");
        String typeCollection;
        Question.Type questionType;
        if(typeString.compareTo(Question.Type.TEST.name()) == 0) {
            typeCollection = TEST_TOPICS;
            questionType = Question.Type.TEST;
        } else {
            typeCollection = ESSAY_TOPICS;
            questionType = Question.Type.ESSAY;
        }

        // Check if old topic exists, delete if not, check if new topic exists, if not add it
        if(!topicExists(questionType, oldTopic)) {
            deleteTopic(typeCollection, oldTopic);
        }
        if(!topicExists(questionType, newTopic)) {
            addTopic(typeCollection, newTopic);
        }
    }

    public void deleteQuestion(Integer questionId) {
        Document questionDocument = getQuestionDocument(questionId);
        String topic = (String) questionDocument.get(TOPIC);
        NitriteCollection collection = db.getCollection(QUESTIONS);
        collection.remove(eq("idQuestion", questionId));

        String typeString = (String) questionDocument.get("type");
        String typeCollection;
        Question.Type questionType;
        if(typeString.compareTo(Question.Type.TEST.name()) == 0) {
            typeCollection = TEST_TOPICS;
            questionType = Question.Type.TEST;
        } else {
            typeCollection = ESSAY_TOPICS;
            questionType = Question.Type.ESSAY;
        }

        // Check if topic exists, if not delete it
        if(!topicExists(questionType, topic)) {
            deleteTopic(typeCollection, topic);
        }
    }

    private Document getQuestionDocument(Integer questionId) {
        NitriteCollection collection = db.getCollection(QUESTIONS);
        Cursor cursor = collection.find(eq("idQuestion", questionId));
        return cursor.firstOrDefault();
    }

    public Question getQuestion(Integer questionId) {
        Document questionDocument = getQuestionDocument(questionId);
        QuestionParser aux;
        JacksonMapper jacksonMapper = new JacksonMapper();
        String type = (String) questionDocument.get("type");

        if (type.equals(Question.Type.TEST.name())) {
            aux = new TestQuestionParser(jacksonMapper.toJson(questionDocument));
        } else {
            aux = new EssayQuestionParser(jacksonMapper.toJson(questionDocument));
        }

        return aux.parseQuestion();
    }

    public ArrayList<Question> getQuestions(String questionTopic, String questionType) {
        NitriteCollection questionsDB = db.getCollection(QUESTIONS);
        Cursor cursor = questionsDB.find(and(eq(TOPIC, questionTopic), eq("type", questionType)));
        ArrayList<Question> questions = new ArrayList<>();

        JacksonMapper jacksonMapper = new JacksonMapper();
        QuestionParser questionParser;
        String aux;
        Question quest;
        if (questionType.compareTo(Question.Type.TEST.name()) == 0) {
            for (Document question : cursor) {
                questionParser = new TestQuestionParser(jacksonMapper.toJson(question));
                questions.add(questionParser.parseQuestion());

            }

        } else {
            for (Document question : cursor) {
                questionParser = new EssayQuestionParser(jacksonMapper.toJson(question));
                questions.add(questionParser.parseQuestion());

            }
        }
        return questions;
    }

    public void addExam(String examString) {
        NitriteCollection exams = db.getCollection(EXAMS);
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document examDocument = nitriteMapper.parse(examString);
        exams.insert(examDocument);
    }

    public void updateExam(String examTitle, String examString) {
        NitriteCollection collection = db.getCollection(EXAMS);
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document examDocument = nitriteMapper.parse(examString);
        collection.update(eq(TITLE, examTitle), examDocument);
    }

    public void deleteExam(String examTitle) {
        NitriteCollection collection = db.getCollection(EXAMS);
        collection.remove(eq(TITLE, examTitle));
    }

    public List<String> getExams() {

        NitriteCollection exams = db.getCollection(EXAMS);
        Cursor cursor = exams.find();

        ArrayList<String> examsTitles = new ArrayList<>();
        for (Document exam : cursor) {
            examsTitles.add((String) exam.get(TITLE));
        }

        return examsTitles;
    }

    public Exam getExam(String title) {
        NitriteCollection collection = db.getCollection(EXAMS);
        Cursor cursor = collection.find(eq(TITLE, title));
        Document doc = cursor.firstOrDefault();
        JacksonMapper jacksonMapper = new JacksonMapper();

        ExamParser aux = new ExamParser(jacksonMapper.toJson(doc));

        return aux.parseExam();
    }

    private void addTopic(String collectionName, String topicValue) {
        NitriteCollection topics = db.getCollection(collectionName);
        NitriteMapper nitriteMapper = new JacksonMapper();
        Document doc = nitriteMapper.parse("{value: \"" + topicValue + "\"}");
        topics.insert(doc);
    }

    private void deleteTopic(String collectionName, String topicValue) {
        NitriteCollection collection = db.getCollection(collectionName);
        collection.remove(eq(VALUE, topicValue));
    }

    private boolean topicExists(Question.Type type, String topicValue) {
        NitriteCollection collection = db.getCollection(QUESTIONS);
        Cursor cursor = collection.find(and(eq(TOPIC, topicValue), eq("type", type.name())));
        for(Document doc : cursor) {
            return true;
        }
        return false;
    }

    public void exportQuestions(Stage stage) {
        NitriteCollection collection = db.getCollection(QUESTIONS);
        Cursor cursor = collection.find();

        ArrayList<QuestionParser> questionsList = new ArrayList<>();

        for (Document question : cursor) {
            QuestionParser aux;
            JacksonMapper jacksonMapper = new JacksonMapper();
            String type = (String) question.get("type");

            if (type.equals(Question.Type.TEST.name())) {
                aux = new TestQuestionParser(jacksonMapper.toJson(question));
            } else {
                aux = new EssayQuestionParser(jacksonMapper.toJson(question));
            }

            questionsList.add(aux);
        }

        Questions questions = new Questions();
        questions.setQuestions(questionsList);

        DialogUtil.showExportDialog(stage, questions.toJson());
    }

    public void importQuestions(String jsonString) {

        List<QuestionParser> questions = new Questions(jsonString).getQuestions();

        for(QuestionParser question : questions){
            if(question instanceof  TestQuestionParser) {
                question.setType(Question.Type.TEST.name());
            } else {
                question.setType(Question.Type.ESSAY.name());
            }
            addQuestion(-1, question.toJson());
        }
    }

    public List<String> getTopics(Question.Type questionType) {
        String typeCollection;
        if(questionType.equals(Question.Type.TEST)) {
            typeCollection = TEST_TOPICS;
        } else {
            typeCollection = ESSAY_TOPICS;
        }

        NitriteCollection topicsCollection = db.getCollection(typeCollection);
        Cursor cursor = topicsCollection.find();

        ArrayList<String> topicsText = new ArrayList<>();
        for (Document topic : cursor) {
            topicsText.add((String) topic.get(VALUE));
        }

        return topicsText;
    }

    public Map<Integer, String> searchQuestions(String title) {
        String aux = title.replaceAll("\\*", "");
        aux = aux.trim().replaceAll(" +", " ");
        aux = aux.replaceAll(" ", "*)(?=.*");
        aux = "(?i)(?=.*" + aux + "*)";

        NitriteCollection collection = db.getCollection(QUESTIONS);
        Cursor cursor = collection.find(regex("title", aux));

        Map<Integer, String> questions = new HashMap<>();

        for (Document question : cursor) {
            questions.put((Integer)question.get("idQuestion"), (String)question.get("title"));
        }

        return questions;
    }

    public void cleanDatabase() {
        db.getCollection(QUESTIONS).remove(Filters.ALL);
        db.getCollection(EXAMS).remove(Filters.ALL);
        db.getCollection(TEST_TOPICS).remove(Filters.ALL);
        db.getCollection(ESSAY_TOPICS).remove(Filters.ALL);
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
