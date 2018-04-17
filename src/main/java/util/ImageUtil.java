package util;

import controller.WelcomeOverviewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {

    private static System.Logger logger = System.getLogger(WelcomeOverviewController.class.getName());

    private ImageUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getBase64(Image image) {
        String base64 = "";
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

        try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
            ImageIO.write(bImage, "png", s);
            byte[] res = s.toByteArray();
            base64 = Base64.getEncoder().encodeToString(res);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to write image to buffer while generating base64 string");
        }

        return base64;
    }

    public static Image getImage(String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        InputStream in = new ByteArrayInputStream(bytes);
        return new Image(in);
    }

    public static InputStream getInputStream(String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new ByteArrayInputStream(bytes);
    }
}
