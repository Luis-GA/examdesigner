package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
}
