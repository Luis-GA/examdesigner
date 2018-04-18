package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Exam;
import model.ExamPart;
import model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse an format Exam objects into Primitive attributes to generate and recover from json file
 **/

public class ExamParser {

    protected String title;
    protected String subject;
    protected String modality;

    protected Integer duration;
    protected Integer weight;
    protected Integer numQuestions;

    protected String logo;

    protected String examDate;
    protected String publicationDate;
    protected String reviewDate;

    protected Boolean nameField;
    protected Boolean surnameField;
    protected Boolean idNumberField;
    protected Boolean groupField;

    protected String instructionDetails;

    protected List<ExamPartParser> parts;

    public ExamParser(Exam exam) {
        this.title = exam.getTitle();
        this.subject = exam.getSubject();
        this.modality = exam.getModality();

        this.duration = exam.getDuration();
        this.weight = exam.getWeight();
        this.numQuestions = exam.getNumQuestions();

        this.logo = ImageUtil.getBase64(exam.getLogo());

        this.examDate = exam.getExamDate();
        this.publicationDate = exam.getPublicationDate();
        this.reviewDate = exam.getReviewDate();

        this.nameField = exam.getNameField();
        this.surnameField = exam.getSurnameField();
        this.idNumberField = exam.getIdNumberField();
        this.groupField = exam.getGroupField();

        this.instructionDetails = exam.getInstructionDetails();

        this.parts = new ArrayList<>();
        List<ExamPart> aux = exam.getParts();
        for (ExamPart part : aux) {
            this.parts.add(new ExamPartParser(part));
        }
    }

    public Exam parseExam() {
        Exam aux = new Exam();

        aux.setTitle(this.title);
        aux.setSubject(this.subject);
        aux.setModality(this.modality);

        aux.setDuration(this.duration);
        aux.setWeight(this.weight);
        aux.setNumQuestions(this.numQuestions);

        aux.setLogo(ImageUtil.getImage(this.logo));

        aux.setExamDate(DateUtil.parse(this.examDate));
        aux.setPublicationDate(DateUtil.parse(this.publicationDate));
        aux.setReviewDate(DateUtil.parse(this.reviewDate));

        aux.setNameField(this.nameField);
        aux.setSurnameField(this.surnameField);
        aux.setIdNumberField(this.idNumberField);
        aux.setGroupField(this.groupField);

        aux.setInstructionDetails(this.instructionDetails);

        List<ExamPart> auxList = new ArrayList<>();
        for (ExamPartParser part : this.parts) {
            auxList.add(part.parseExamPart());
        }
        aux.setParts(auxList);

        return aux;
    }

    public ExamParser(String examJson) {
        RuntimeTypeAdapterFactory<QuestionParser> adapter = RuntimeTypeAdapterFactory
                .of(QuestionParser.class, "type")
                .registerSubtype(TestQuestionParser.class, Question.Type.TEST.name())
                .registerSubtype(EssayQuestionParser.class, Question.Type.ESSAY.name());
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

        try {
            ExamParser aux = gson.fromJson(examJson, ExamParser.class);

            this.numQuestions = (aux.numQuestions != null ? aux.numQuestions : Integer.valueOf(0));
            this.title = (aux.title != null ? aux.title : "");
            this.duration = (aux.duration != null ? aux.duration : Integer.valueOf(0));
            this.groupField = (aux.groupField != null ? aux.groupField : false);
            this.idNumberField = (aux.idNumberField != null ? aux.idNumberField : false);
            this.instructionDetails = (aux.instructionDetails != null ? aux.instructionDetails : "");
            this.modality = (aux.modality != null ? aux.modality : "");
            this.nameField = (aux.nameField != null ? aux.nameField : false);
            this.surnameField = (aux.surnameField != null ? aux.surnameField : false);
            this.subject = (aux.subject != null ? aux.subject : "");
            this.weight = (aux.weight != null ? aux.weight : Integer.valueOf(0));
            this.parts = (aux.parts != null ? aux.parts : new ArrayList<>());

            this.logo = (aux.logo != null ? aux.logo : "");

            this.examDate = (aux.examDate != null ? aux.examDate : "");
            this.publicationDate = (aux.publicationDate != null ? aux.publicationDate : "");
            this.reviewDate = (aux.reviewDate != null ? aux.reviewDate : "");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid exam JSON file");
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public boolean isValid() {
        return !this.title.equals("");
    }
}
