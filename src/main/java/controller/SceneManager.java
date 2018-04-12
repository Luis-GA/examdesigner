package controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import model.Exam;

import java.io.IOException;
import java.util.*;

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

    public Scene newExamOverviewScene(Exam exam) {
        Scene scene = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ExamOverview.fxml"));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            AnchorPane examOverview = loader.load();

            // Set MainApp and give the controller access to the exam
            ExamOverviewController controller = loader.getController();
            controller.setMainApp(this.mainApp);
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

    public void setExamOverviewScene(Exam exam) {
        Scene scene = newExamOverviewScene(exam);
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

    public boolean deleteConfirmation() {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.delete"));
        closeConfirmation.setHeaderText(null);
        closeConfirmation.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString("txt.deleteConfirmation"));
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(MainApp.getPrimaryStage());

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        return ButtonType.OK.equals(closeResponse.get());
    }
}
