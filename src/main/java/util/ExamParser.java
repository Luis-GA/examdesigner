package util;

import com.google.gson.GsonBuilder;
import model.Exam;
import com.google.gson.Gson;
import model.ExamPart;
import model.Question;
import java.util.ArrayList;
import java.util.List;

/** Class to parse an format Exam objects into Primitive attributes to generate and recover from json file**/

public class ExamParser {

    private String title;
    private String subject;
    private String modality;

    private Integer duration;
    private Integer weigh;
    private Integer numQuestions;

    private String logo;

    private String examDate;
    private String publicationDate;
    private String reviewDate;

    private Boolean nameField;
    private Boolean surnameField;
    private Boolean idNumberField;
    private Boolean groupField;

    private String instructionDetails;

    private List<ExamPartParser> parts;

    public ExamParser(Exam exam){
        this.title = exam.getTitle();
        this.subject = exam.getSubject();
        this.modality = exam.getModality();

        this.duration = exam.getDuration();
        this.weigh = exam.getWeigh();
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
        for(ExamPart part : aux){
            this.parts.add(new ExamPartParser(part));
        }
    }

    public Exam parseExam(){
        Exam aux = new Exam();

        aux.setTitle(this.title);
        aux.setSubject(this.subject);
        aux.setModality(this.modality);

        aux.setDuration(this.duration);
        aux.setWeigh(this.weigh);
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
        for(ExamPartParser part : this.parts){
            auxList.add(part.parseExamPart());
        }
        aux.setParts(auxList);

        return aux;
    }

    public ExamParser (String examJson){
        RuntimeTypeAdapterFactory<QuestionParser> adapter = RuntimeTypeAdapterFactory
                .of(QuestionParser.class, "type")
                .registerSubtype(TestQuestionParser.class, Question.Type.TEST.name())
                .registerSubtype(EssayQuestionParser.class, Question.Type.ESSAY.name());
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        ExamParser aux = gson.fromJson(examJson, ExamParser.class);

        this.numQuestions = aux.numQuestions;
        this.title = aux.title;
        this.duration = aux.duration;
        this.groupField = aux.groupField;
        this.idNumberField = aux.idNumberField;
        this.instructionDetails = aux.instructionDetails;
        this.modality = aux.modality;
        this.nameField = aux.nameField;
        this.subject = aux.subject;
        this.surnameField = aux.surnameField;
        this.weigh = aux.weigh;
        this.parts = aux.parts;

        this.logo = aux.logo;

        this.examDate = aux.examDate;
        this.publicationDate = aux.publicationDate;
        this.reviewDate = aux.reviewDate;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}