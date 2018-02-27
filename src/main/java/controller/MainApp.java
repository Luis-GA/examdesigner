package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    private static Stage primaryStage;
    private BorderPane rootLayout;

    @Override public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        this.primaryStage.setTitle(ResourceBundle.getBundle("languages/labels").getString("title.applicationName"));
        this.primaryStage.getIcons().add(new Image("images/exam_designer_256.png"));

        setPreferences();

        initEmptyRootLayout();
        showWelcomeOverview();
    }

    public void initEmptyRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/EmptyRootLayout.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** * Shows the welcome overview inside the root layout. */
    public void showWelcomeOverview() {
        try {
            // Load exam overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/WelcomeOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            AnchorPane examOverview = (AnchorPane) loader.load();

            // Set exam overview into the center of root layout.
            rootLayout.setCenter(examOverview);
            rootLayout.setAlignment(examOverview, Pos.BOTTOM_RIGHT);

            // Give the controller access to the main app.
            WelcomeOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPreferences(){

        // Retrieve the user preference node
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        // Get the value of the language preference
        String defaultValue = "unselected";
        String languageValue = prefs.get("language", defaultValue);

        if(languageValue == "unselected"){
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
