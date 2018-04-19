package controller;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ContentObject;
import model.EssayQuestion;
import model.TestQuestion;

import java.util.List;

public abstract class DialogController {

    protected Stage dialogStage;

    public abstract void setDialogStage(Stage dialogStage);

    public abstract Stage getDialogStage();

    public void setContentObjects(List<ContentObject> contentObjects) {}

}
