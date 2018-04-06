package controller;

import org.dizitart.no2.*;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;

import java.util.ArrayList;
import java.util.List;

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
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.setIndexType(IndexType.Fulltext);

        NitriteCollection questions = db.getCollection("questions");
        questions.createIndex("type", indexOptions);

        NitriteCollection exams = db.getCollection("exams");
        questions.createIndex("title", indexOptions);
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

    public List<String> getExams() {

        NitriteCollection exams = db.getCollection("exams");
        Cursor cursor = exams.find();

        ArrayList<String> examsTitles = new ArrayList<>();
        for (Document exam : cursor) {
            examsTitles.add((String)exam.get("title"));
        }

        return examsTitles;
    }
}
