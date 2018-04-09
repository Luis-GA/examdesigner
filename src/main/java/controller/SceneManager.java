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

    private Stack<SceneWrapper> scenes;
    private static SceneManager instance;
    private static Stage primaryStage;
    private MainApp mainApp;
    private static System.Logger logger = System.getLogger(SceneManager.class.getName());
    private BooleanProperty changes = new SimpleBooleanProperty(false);

    private class SceneWrapper {

        private Scene scene;
        private RootLayoutController controller;

        public SceneWrapper(Scene scene, RootLayoutController controller) {
            this.scene = scene;
            this.controller = controller;
        }

        public boolean changes() {
            if(controller != null) {
                return controller.changes();
            } else {
                return false;
            }
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    private SceneManager() {}

    public void setRootScene(Stage primaryStage, Scene rootScene, MainApp mainApp) {
        this.scenes = new Stack();
        this.scenes.push(new SceneWrapper(rootScene, null));
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(rootScene);
        this.mainApp = mainApp;
    }

    private Scene setNewMenuScene(AnchorPane pane, Exam exam, BooleanProperty changes) {
        Scene scene = null;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            BorderPane rootLayout = (BorderPane) loader.load();

            //Set MainApp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setExam(exam);

            rootLayout.setCenter(pane);
            rootLayout.setAlignment(pane, Pos.BOTTOM_RIGHT);

            scene = new Scene(rootLayout, primaryStage.getScene().getWidth(),primaryStage.getScene().getHeight());
            scenes.push(new SceneWrapper(scene, controller));
            return scene;
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Root Layout");
        } finally {
            if(scene == null) {
                scene = new Scene(pane, primaryStage.getScene().getWidth(),primaryStage.getScene().getHeight());
            }
            return scene;
        }
    }

    public Scene newExamOverviewScene(Exam exam) {
        Scene scene = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ExamOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle("languages/labels"));
            AnchorPane examOverview = (AnchorPane) loader.load();

            // Set MainApp and give the controller access to the exam
            ExamOverviewController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setExam(exam);

            scene = setNewMenuScene(examOverview, exam, this.changes);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Exam Overview");
        } finally {
            if(scene == null){
                scene = primaryStage.getScene();
            }
            return scene;
        }
    }

    public void setExamOverviewScene(Exam exam) {
        Scene scene = newExamOverviewScene(exam);
        primaryStage.setScene(scene);
    }

    public void changeExamOverviewScene(Exam exam) {
        scenes.pop();
        Scene scene = newExamOverviewScene(exam);
        primaryStage.setScene(scene);
    }

    public boolean changes() {
        return scenes.peek().changes();
    }

    public void back() {
        Scene sceneOLD = scenes.pop().scene;

        if(scenes.peek().controller != null) {
            primaryStage.setScene(scenes.peek().scene);
        } else {
            mainApp.showWelcomeOverview(sceneOLD);
        }
    }
}
