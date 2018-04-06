package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Exam;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Stack;

public class SceneManager {

    private Stack<Scene> scenes;
    private static SceneManager instance;
    private static Stage primaryStage;
    private MainApp mainApp;
    private static System.Logger logger = System.getLogger(SceneManager.class.getName());

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    private SceneManager() {}

    public void setRootScene(Stage primaryStage, Scene rootScene, MainApp mainApp) {
        this.scenes = new Stack();
        this.scenes.push(rootScene);
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(rootScene);
        this.mainApp = mainApp;
    }

    private Scene setNewMenuScene(AnchorPane pane, Exam exam, BooleanProperty changes) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            BorderPane rootLayout = (BorderPane) loader.load();

            //Set MainApp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setExam(exam);
            controller.setChanges(changes);

            rootLayout.setCenter(pane);
            rootLayout.setAlignment(pane, Pos.BOTTOM_RIGHT);

            return new Scene(rootLayout, primaryStage.getScene().getWidth(),primaryStage.getScene().getHeight());
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Root Layout");
        }
        return new Scene(pane, primaryStage.getScene().getWidth(),primaryStage.getScene().getHeight());
    }

    public void setExamOverviewScene(Exam exam) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ExamOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            AnchorPane examOverview = (AnchorPane) loader.load();

            // Set MainApp and give the controller access to the exam
            ExamOverviewController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setExam(exam);

            Scene scene = setNewMenuScene(examOverview, exam, new SimpleBooleanProperty(false));

            primaryStage.setScene(scene);
            scenes.push(scene);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Exam Overview");
        }
    }
}
