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

import model.Exam;
import model.ExamParser;

import java.time.LocalDate;
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

        testExamJson();
    }

    private void initEmptyRootLayout() {
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
    private void showWelcomeOverview() {
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

    //TODO delete this test method
    private void testExamJson(){

        Exam aux = new Exam();

        aux.examDate.set(LocalDate.of(2017, 10, 25));
        aux.numQuestions.set(5);
        aux.title.set("Algoritmos Voraces - Octubre 2017");
        aux.duration.set(60);
        aux.groupField.set(true);
        aux.idNumberField.set(true);
        aux.instructionDetails.set("Se permite usar calculadora.");
        aux.logo = null;
        aux.modality.set("Convocatoria Ordinaria");
        aux.nameField.set(true);
        aux.publicationDate.set(LocalDate.of(2017, 10, 30));
        aux.reviewDate.set(LocalDate.of(2017, 11, 4));
        aux.subject.set("Algorítmica y Complejidad");
        aux.surnameField.set(true);
        aux.weigh.set(40);

        System.out.print(new ExamParser(aux).toJson());
    }

    /** * Returns the main stage. * @return */
    public Stage getPrimaryStage() {
        return primaryStage;
        }

    public static void main(String[] args) {
        launch(args);
    }
}
