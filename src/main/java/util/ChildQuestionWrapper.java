package util;

import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ResourceBundle;

public class ChildQuestionWrapper {
    Node auxNode;
    Object controller;

    public ChildQuestionWrapper(String view) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(view));
            loader.setResources(ResourceBundle.getBundle(MainApp.LABELS));
            auxNode = loader.load();
            controller = loader.getController();
        }  catch (IOException e) {
            System.Logger logger = System.getLogger(ChildQuestionWrapper.class.getName());
            logger.log(System.Logger.Level.ERROR, "Error trying to load resources while initializing specific questionOverview");
            auxNode = new Pane();
        }
    }

    public Node getNode() {
        return this.auxNode;
    }

    public Object getController() {
        return this.controller;
    }
}