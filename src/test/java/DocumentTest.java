import org.junit.jupiter.api.Test;
import util.DocumentGenerator;
import util.ExamParser;
import util.FileUtil;

import java.nio.file.Paths;

public class DocumentTest {

    @Test
    void testDocumentGeneration() {
        String examJson = FileUtil.readFile(Paths.get("src/test/resources/testExam.json"));
        ExamParser exam = new ExamParser(examJson);
        DocumentGenerator documentGenerator = new DocumentGenerator(exam, true);
        String path = "test.docx";
        documentGenerator.generateDocument(path);
    }
}
