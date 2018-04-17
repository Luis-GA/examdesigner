import org.junit.jupiter.api.Test;
import util.DocumentGenerator;
import util.ExamParser;

public class DocumentTest {

    private final String examJson = "{\n" +
            "  \"title\": \"Algoritmos de cifrado asimétrico\",\n" +
            "  \"subject\": \"Fundamentos de Seguridad\",\n" +
            "  \"modality\": \"Convocatoria Ordinaria\",\n" +
            "  \"duration\": 90,\n" +
            "  \"weight\": 20,\n" +
            "  \"numQuestions\": 20,\n" +
            "  \"logo\": \"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\\u003d\",\n" +
            "  \"examDate\": \"04.05.2015\",\n" +
            "  \"publicationDate\": \"08.05.2015\",\n" +
            "  \"reviewDate\": \"12.05.2015\",\n" +
            "  \"nameField\": true,\n" +
            "  \"surnameField\": true,\n" +
            "  \"idNumberField\": false,\n" +
            "  \"groupField\": true,\n" +
            "  \"instructionDetails\": \"Se permite el uso de calculadora.\",\n" +
            "  \"parts\": [\n" +
            "    {\n" +
            "      \"title\": \"Parte a Desarrollar\",\n" +
            "      \"weight\": 5,\n" +
            "      \"duration\": 45,\n" +
            "      \"instructions\": \"\",\n" +
            "      \"questions\": [\n" +
            "        {\n" +
            "          \"choices\": {\n" +
            "            \"a\": {\n" +
            "              \"title\": \"Utiliza las columnas de la tabla que se muestra a continuación para describir los pasos necesarios para generar la secuencia cifrante del generador G1\",\n" +
            "              \"bodyObjects\": []\n" +
            "            },\n" +
            "            \"b\": {\n" +
            "              \"title\": \"Escribe en la cabecera la operación que se realiza en el apartado 1.1. Muestra la secuencia obtenida en la pregunta 1.1 y su período.\",\n" +
            "              \"bodyObjects\": []\n" +
            "            },\n" +
            "            \"c\": {\n" +
            "              \"title\": \"Respecto a la secuencia S1 obtenida en la pregunta 1.2. ¿Se trata de una m-secuencia? ¿Cumple esta secuencia S1 los tres postulados de Golomb? Justifica detalladamente tus respuestas (NOTA describe la autocorrelación fuera de fase sólo para las 5 primeras iteraciones)\",\n" +
            "              \"bodyObjects\": []\n" +
            "            },\n" +
            "            \"d\": {\n" +
            "              \"title\": \"Se desea cifrar el mensaje M utilizando un nuevo generador G que aumenta la complejidad lineal de G1 uniendo en una operación XOR (suma mod 2) las secuencias cifrantes generadas por G1 y G2. Realiza las operaciones necesarias para obtener el cifrado de M\",\n" +
            "              \"bodyObjects\": []\n" +
            "            }\n" +
            "          },\n" +
            "          \"correctChoices\": [\n" +
            "            \"La longitud de la clave puede ser 5.\"\n" +
            "          ],\n" +
            "          \"title\": \"Si las distancias entre repeticiones de cadenas en el criptograma cifrado con algoritmo de Vigenère son 35, 125, 70:\",\n" +
            "          \"type\": \"TEST\",\n" +
            "          \"bodyObjects\": [],\n" +
            "          \"weight\": 0,\n" +
            "          \"duration\": 0\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"\",\n" +
            "      \"weight\": 0,\n" +
            "      \"duration\": 0,\n" +
            "      \"instructions\": \"\",\n" +
            "      \"questions\": [\n" +
            "        {\n" +
            "          \"sections\": [],\n" +
            "          \"answeringSpace\": 1,\n" +
            "          \"title\": \"CIFRADO DE FLUJO:\",\n" +
            "          \"type\": \"ESSAY\",\n" +
            "          \"bodyObjects\": [],\n" +
            "          \"weight\": 0,\n" +
            "          \"duration\": 0\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    void testDocumentGeneration() {
        ExamParser exam = new ExamParser(examJson);
        DocumentGenerator documentGenerator = new DocumentGenerator(exam);
        String path = "test.docx";
        documentGenerator.generateDocument(path);
    }
}
