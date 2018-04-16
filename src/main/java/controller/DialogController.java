package controller;

import javafx.stage.Stage;
import model.ContentObject;
import java.util.List;

public abstract class DialogController {

    protected Stage dialogStage;

    public abstract void setDialogStage(Stage dialogStage);

    public abstract Stage getDialogStage();

    public void setContentObjects(List<ContentObject> contentObjects) {}
}
