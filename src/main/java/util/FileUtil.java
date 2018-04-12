package util;

import controller.Dialogs;
import controller.MainApp;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class FileUtil {

    private static System.Logger logger = System.getLogger(FileUtil.class.getName());

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String readFile(Path path) {
        String content = "";
        try {
            byte[] encoded = Files.readAllBytes(path);
            content = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to read string from file");
        }

        return content;
    }

    public static String readJsonFile(Stage stage) {

        Path path = Dialogs.showOpenExamDialog(stage);

        String jsonString = null;
        if (path != null) {
            jsonString = FileUtil.readFile(path);
        }

        return jsonString;
    }

    public static void writeJsonFile(Stage stage, String jsonString) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ResourceBundle.getBundle(MainApp.LABELS).getString("lbl.jsonFiles"), "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {

            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(jsonString);
            } catch (IOException e) {
                logger.log(System.Logger.Level.ERROR, "Error trying to save exam as JSON file");
            }
        }
    }
}
