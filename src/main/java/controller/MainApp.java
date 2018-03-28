package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private static System.Logger logger = System.getLogger(MainApp.class.getName());

    private static Stage primaryStage;

    @Override public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setMinWidth(900);
        this.primaryStage.setMinHeight(600);
        this.primaryStage.setTitle(ResourceBundle.getBundle("languages/labels").getString("title.applicationName"));
        this.primaryStage.getIcons().add(new Image("images/exam_designer_256.png"));

        setPreferences();

        showWelcomeOverview();
    }

    /** * Set welcome overview as primary scene and show. */
    private void showWelcomeOverview() {
        try {
            // Load exam overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/WelcomeOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            AnchorPane welcomeOverview = (AnchorPane) loader.load();

            // Give the controller access to the main app.
            WelcomeOverviewController controller = loader.getController();
            controller.setMainApp(this);
            Scene scene = new Scene(welcomeOverview);

            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.setRootScene(primaryStage, scene, this);
            primaryStage.show();
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Welcome Overview");
        }
    }

    private void setPreferences(){

        // Retrieve the user preference node
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        // Get the value of the language preference
        String defaultValue = "unselected";
        String languageValue = prefs.get("language", defaultValue);

        if(languageValue.equals("unselected")){
            languageValue = Locale.getDefault().toString().substring(0, 2);
            prefs.put("language", defaultValue);
            Locale.setDefault(new Locale(languageValue));
        } else {
            Locale.setDefault(new Locale(languageValue));
        }
    }

    /** * Returns the main stage. * @return */
    public Stage getPrimaryStage() {
        return primaryStage;
        }

    public static void main(String[] args) {
        launch(args);
    }
}
