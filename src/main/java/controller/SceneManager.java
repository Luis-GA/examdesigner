package controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Exam;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.function.Function;

public class SceneManager {

    private Deque<SceneWrapper> scenes;
    private static SceneManager instance;
    private MainApp mainApp;
    private static System.Logger logger = System.getLogger(SceneManager.class.getName());

    private class SceneWrapper {

        private Scene scene;
        private RootLayoutController controller;

        public SceneWrapper(Scene scene, RootLayoutController controller) {
            this.scene = scene;
            this.controller = controller;
        }

        public boolean changes() {
            if (controller != null) {
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

    private SceneManager() {
    }

    public void setRootScene(Scene rootScene, MainApp mainApp) {
        this.scenes = new LinkedList<>();
        this.scenes.push(new SceneWrapper(rootScene, null));
        MainApp.getPrimaryStage().setScene(rootScene);
        this.mainApp = mainApp;
    }

    private Scene setNewMenuScene(AnchorPane pane, Exam exam) {
        Scene scene = null;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            BorderPane rootLayout = loader.load();

            //Set MainApp
            RootLayoutController controller = loader.getController();
            controller.setExam(exam);

            rootLayout.setCenter(pane);
            rootLayout.setAlignment(pane, Pos.BOTTOM_RIGHT);

            scene = new Scene(rootLayout, MainApp.getPrimaryStage().getScene().getWidth(), MainApp.getPrimaryStage().getScene().getHeight());
            scenes.push(new SceneWrapper(scene, controller));
            return scene;
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Root Layout");
        } finally {
            if (scene == null) {
                scene = new Scene(pane, MainApp.getPrimaryStage().getScene().getWidth(), MainApp.getPrimaryStage().getScene().getHeight());
            }
        }
        return scene;
    }

    private Scene newExamOverviewScene(Exam exam) {
        Scene scene = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ExamOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            AnchorPane examOverview = loader.load();

            // Give the controller access to the exam
            ExamOverviewController controller = loader.getController();
            controller.setExam(exam);

            scene = setNewMenuScene(examOverview, exam);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Exam Overview");
        } finally {
            if (scene == null) {
                scene = MainApp.getPrimaryStage().getScene();
            }
        }
        return scene;
    }

    private Scene newAutomaticGenerationScene(Exam exam) {
        Scene scene = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/AutomaticGeneration.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            AnchorPane automaticGeneration = loader.load();

            // Give the controller access to the exam
            AutomaticGenerationController controller = loader.getController();
            controller.setExam(exam);

            scene = setNewMenuScene(automaticGeneration, exam);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Automatic Generation");
        } finally {
            if (scene == null) {
                scene = MainApp.getPrimaryStage().getScene();
            }
        }
        return scene;
    }

    public void setExamOverviewScene(Exam exam) {
        Scene scene = newExamOverviewScene(exam);
        MainApp.getPrimaryStage().setScene(scene);
    }

    public void setAutomaticGenerationScene(Exam exam) {
        Scene scene = newAutomaticGenerationScene(exam);
        MainApp.getPrimaryStage().setScene(scene);
    }

    public void changeExamOverviewScene(Exam exam) {
        scenes.pop();
        Scene scene = newExamOverviewScene(exam);
        MainApp.getPrimaryStage().setScene(scene);
    }

    public boolean changes() {
        return scenes.peek().changes();
    }

    public void back() {
        Scene sceneOLD = scenes.pop().scene;

        if (scenes.peek().controller != null) {
            MainApp.getPrimaryStage().setScene(scenes.peek().scene);
        } else {
            mainApp.showWelcomeOverview(sceneOLD);
        }
    }

    public void reloadWelcomeOverview() {
        mainApp.showWelcomeOverview(scenes.peek().scene);
    }

    public void showWorkIndicator(Exam exam, Function function) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/WorkIndicator.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            HBox workIndicator = loader.load();

            WorkIndicatorController controller = loader.getController();
            controller.setExam(exam);

            Scene scene = new Scene(workIndicator, MainApp.getPrimaryStage().getScene().getWidth(), MainApp.getPrimaryStage().getScene().getHeight());
            scenes.push(new SceneWrapper(scene, controller));
            MainApp.getPrimaryStage().setScene(scene);
            controller.runFunction(exam, function);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing Root Layout");
        }
    }
}
