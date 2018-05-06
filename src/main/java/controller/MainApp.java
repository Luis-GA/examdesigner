package controller;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.DialogUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    public static final String LABELS = "languages/labels";
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setMinWidth(900);
        MainApp.primaryStage.setMinHeight(600);
        MainApp.primaryStage.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString("title.applicationName"));
        MainApp.primaryStage.getIcons().add(new Image(MainApp.class.getResource("/images/exam_designer_256.png").toString()));
        MainApp.primaryStage.setOnCloseRequest(confirmCloseEventHandler);

        setPreferences();

        showWelcomeOverview(null);
    }

    /**
     * Set welcome overview as primary scene and show.
     */
    public void showWelcomeOverview(Scene sceneOLD) {
        try {
            // Load exam overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/WelcomeOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            AnchorPane welcomeOverview = loader.load();

            Scene scene;
            if (sceneOLD != null) {
                scene = new Scene(welcomeOverview, sceneOLD.getWidth(), sceneOLD.getHeight());
            } else {
                scene = new Scene(welcomeOverview);
            }

            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.setRootScene(scene, this);
            primaryStage.show();
        } catch (IOException e) {
            System.Logger logger = System.getLogger(MainApp.class.getName());
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Welcome Overview");
        }
    }

    private void setPreferences() {

        // Retrieve the user preference node
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        // Get the value of the language preference
        String defaultValue = "unselected";
        String languageValue = prefs.get("language", defaultValue);

        if (languageValue.equals("unselected")) {
            languageValue = Locale.getDefault().toString().substring(0, 2);
            prefs.put("language", defaultValue);
            Locale.setDefault(new Locale(languageValue));
        } else {
            Locale.setDefault(new Locale(languageValue));
        }
    }

    private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {

        if (!DialogUtil.showCloseConfirmationDialog()) {
            event.consume();
        }
    };

    /**
     * Returns the main stage. * @return
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
