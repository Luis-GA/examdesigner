package util;

import javafx.scene.image.Image;
import model.ContentObject;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class DocumentGenerator {

    private ExamParser exam;
    private XWPFDocument document;
    private static final String heading1Style = "heading1";
    private static final String heading2Style = "heading2";
    private static final String paragraphStyle = "paragraph";
    private static System.Logger logger = System.getLogger(DocumentGenerator.class.getName());

    public DocumentGenerator(ExamParser exam){
        this.exam = exam;
        try(FileInputStream fileInputStream = new FileInputStream("template.docx")) {
            document = new XWPFDocument(fileInputStream);
        } catch (Exception e) {
            logger.log(System.Logger.Level.ERROR, "Error loading word template");
        }
    }

    public void generateDocument(String path) {

        setHead();
        setParts();

        try {
            FileOutputStream out = new FileOutputStream(path);
            document.write(out);
            out.close();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHead() {
        XWPFParagraph paragraph;
        XWPFRun run;

        /** -- HEADER -- **/
        XWPFHeader header = document.createHeader(HeaderFooterType.FIRST);
        XWPFTable table;

        try {
            table = header.getTables().get(0);
            XWPFTableRow row = table.getRow(0);
            row.getCell(0);
            row.getCell(1);
        } catch (IndexOutOfBoundsException e) {
            table = header.createTable(1,2);
        }

        setTableAlignment(table, STJc.CENTER);
        widthCellsAcrossRow(table, 0, 0, 1000);
        widthCellsAcrossRow(table, 0, 1, 8000);

        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell = row.getCell(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        paragraph = cell.getParagraphArray(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        insertImage(paragraph, exam.logo);

        cell = row.getCell(1);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        paragraph = cell.getParagraphArray(0);
        paragraph.setStyle(heading1Style);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setText(exam.title);
        run.addTab();
        run.setText(exam.examDate);
        run.addBreak();
        run.addBreak();
        run.setText(exam.subject);

        paragraph = cell.addParagraph();
        paragraph.setStyle(heading2Style);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setText(exam.modality);

        paragraph = cell.addParagraph();
        paragraph.setStyle(paragraphStyle);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();

        if(!exam.examDate.equals("") || !exam.publicationDate.equals("") || !exam.reviewDate.equals(""))
            run.addBreak();
        if(!exam.publicationDate.equals("")) {
            run.setText("Fecha de publicación: " + exam.publicationDate);
        }
        if(!exam.reviewDate.equals("")) {
            if(!exam.publicationDate.equals(""))
                run.addTab();
            run.setText("Fecha de revisión: " + exam.reviewDate);
        }

        /** -- BODY -- **/
        paragraph = document.getLastParagraph();
        paragraph.setStyle(paragraphStyle);
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();

        if(exam.duration > 0) {
            run.setText("Duración: " + exam.duration + " minutos");
            run.addTab();
            run.addTab();
        }
        if(exam.weight > 0) {
            run.setText("Peso: " + exam.weight + "%");
        }
        paragraph = document.createParagraph();
        paragraph.setStyle(paragraphStyle);
        run = paragraph.createRun();
        run.setBold(true);

        if(exam.nameField) {
            run.setText("Nombre: ");
            run.addTab();
            run.addTab();
            run.addTab();
            run.addTab();
        }
        if(exam.surnameField) {
            run.setText("Apellidos: ");
        }
        if(exam.groupField || exam.idNumberField) {
            run.addBreak();
        }
        if(exam.idNumberField) {
            run.setText("Nº de Matrícula: ");
            run.addTab();
            run.addTab();
            run.addTab();
        }
        if(exam.groupField) {
            run.setText("Grupo: ");
        }
        setInstructions(exam.instructionDetails);
    }

    private void setParts() {
        XWPFParagraph paragraph;
        XWPFRun run;
        int i = 0;
        for(ExamPartParser part : exam.parts) {
            paragraph = document.createParagraph();
            paragraph.setStyle(heading1Style);
            run = paragraph.createRun();
            if(i == 0){
                run.addBreak();
            }
            i++;
            run.setText(part.title);

            if(part.duration > 0 || part.weight > 0) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                paragraph.setStyle(paragraphStyle);
            }
            if(part.duration > 0) {
                run.setText("Duración: " + part.duration + " minutos");
                run.addTab();
                run.addTab();
            }
            if(part.weight > 0) {
                run.setText("Peso: " + part.weight + "%");
            }
            setInstructions(part.instructions);
            setQuestions(part.questions);
        }
    }

    private void setInstructions(String instructions) {
        if(!instructions.equals("")) {
            XWPFParagraph paragraph;
            XWPFRun run;
            XWPFTable table;
            table = document.createTable(1, 1);
            setTableAlignment(table, STJc.CENTER);
            widthCellsAcrossRow(table, 0, 0, 9000);
            paragraph = table.getRow(0).getCell(0).getParagraphArray(0);
            paragraph.setStyle(paragraphStyle);
            paragraph.setSpacingAfter(0);
            run = paragraph.createRun();
            run.setText("   INSTRUCCIONES:");
            run.addBreak();
            run.setText(instructions);
        }
    }

    private void setQuestions(List<QuestionParser> questions) {
        XWPFParagraph paragraph;
        XWPFRun run;
        int i = 0;
        for(QuestionParser question : questions) {
            paragraph = document.createParagraph();
            paragraph.setStyle(heading2Style);
            run = paragraph.createRun();
            i++;
            run.setText(i + ". ");
            run.setText(question.title);

            if(question.duration > 0 || question.weight > 0) {
                paragraph = document.createParagraph();
                paragraph.setStyle(paragraphStyle);
                run = paragraph.createRun();
            }
            if(question.duration > 0) {
                run.setText("Duración: " + question.duration + " minutos");
                run.addTab();
            }
            if(question.weight > 0) {
                run.setText("Peso: " + question.weight + "%");
            }
            setQuestion(question);
        }
    }

    private void setQuestion(QuestionParser question) {

        setContentObjects(question.bodyObjects, null);
        question.setType();
        if(question.type.equals("TEST")) {
            setTestQuestion((TestQuestionParser) question);
        } else {
            setEssayQuestion((EssayQuestionParser) question);
        }
    }

    private void setTestQuestion(TestQuestionParser testQuestion) {
        XWPFParagraph paragraph;
        XWPFRun run;
        for(Map.Entry<String, ChoiceParser> entry : testQuestion.choices.entrySet()) {
            paragraph = document.createParagraph();
            paragraph.setStyle(paragraphStyle);
            run = paragraph.createRun();
            run.addTab();
            run.setText(entry.getKey() + ". ");
            run.setText(entry.getValue().title);
            setContentObjects(entry.getValue().bodyObjects, null);
        }

        XWPFTable table;
        table = document.createTable(1, 1);
        setTableAlignment(table, STJc.CENTER);
        widthCellsAcrossRow(table, 0, 0, 9000);
        paragraph = table.getRow(0).getCell(0).getParagraphArray(0);
        paragraph.setStyle(paragraphStyle);
        paragraph.setSpacingAfter(0);
        run = paragraph.createRun();
        run.setText("Opciones correctas: ");
        for(String correctChoice : testQuestion.correctChoices) {
            run.addBreak();
            run = paragraph.createRun();
            run.setBold(true);
            run.addTab();
            run.setText(correctChoice);
        }
        document.createParagraph();
    }

    private void setEssayQuestion(EssayQuestionParser essayQuestion) {
        XWPFParagraph paragraph;
        XWPFRun run;
        int i = 0;
        for(SectionParser section : essayQuestion.sections) {
            paragraph = document.createParagraph();
            paragraph.setStyle(paragraphStyle);
            run = paragraph.createRun();
            run.addTab();
            run.setText(++i + ". ");
            run.setText(section.title);
            setContentObjects(section.bodyObjects, null);
        }

        XWPFTable table;
        table = document.createTable(1, 1);
        setTableAlignment(table, STJc.CENTER);
        widthCellsAcrossRow(table, 0, 0, 9000);
        paragraph = table.getRow(0).getCell(0).getParagraphArray(0);
        paragraph.setStyle(paragraphStyle);
        paragraph.setSpacingAfter(0);
        run = paragraph.createRun();
        run.setText("Solución: ");
        run.addBreak();
        setContentObjects(essayQuestion.solutionObjects, table.getRow(0).getCell(0));

        document.createParagraph();
    }

    private void setContentObjects(List<ContentObjectParser> contentObjects, XWPFTableCell cell) {
        XWPFParagraph paragraph;
        XWPFRun run;
        for(ContentObjectParser contentObject : contentObjects) {
            if(cell != null) {
                paragraph = cell.addParagraph();
            } else {
                paragraph = document.createParagraph();
            }
            if(contentObject.type.equals(ContentObject.Type.IMAGE)) {
                insertImage(paragraph, contentObject.content);
            } else {
                run = paragraph.createRun();
                run.setText(contentObject.content);
            }
        }
    }

    private static void setTableAlignment(XWPFTable table, STJc.Enum justification) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        jc.setVal(justification);
    }

    private static void widthCellsAcrossRow (XWPFTable table, int rowNum, int colNum, int width) {
        XWPFTableCell cell = table.getRow(rowNum).getCell(colNum);
        if (cell.getCTTc().getTcPr() == null)
            cell.getCTTc().addNewTcPr();
        if (cell.getCTTc().getTcPr().getTcW()==null)
            cell.getCTTc().getTcPr().addNewTcW();
        cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) width));
    }

    public void insertImage(XWPFParagraph paragraph, String image){
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun imageRun = paragraph.createRun();
        imageRun.setTextPosition(20);

        Image aux = ImageUtil.getImage(image);
        int width = (int) aux.getWidth();
        int height = (int) aux.getHeight();

        double ratio = height/100;
        height = 100;
        width = (int)(width/ratio);

        try {
            imageRun.addPicture(ImageUtil.getInputStream(image),
                    XWPFDocument.PICTURE_TYPE_JPEG, "image", Units.toEMU(width), Units.toEMU(height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
