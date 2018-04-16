package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.ContentObject;

import java.util.ArrayList;
import java.util.List;

public class ContentObjectDialogController extends DialogController{

    @FXML
    ListView<ContentObjectHBox> contentList;

    BooleanProperty disableButtons;

    @FXML
    public void initialize() {
        disableButtons = new SimpleBooleanProperty(true);
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), disableButtons, null));
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), disableButtons, null));
    }

    @FXML
    public void addContentObjectHBox() {
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), disableButtons, null));
        if(contentList.getItems().size() > 2)
            disableButtons.setValue(false);
    }

    private List<ContentObject> getContentObjectList() {
        ArrayList<ContentObject> aux = new ArrayList<>();
        for(ContentObjectHBox contentObjectHBox : contentList.getItems()) {
            aux.add(contentObjectHBox.getContentObject());
        }
        return aux;
    }

    @Override
    public void setContentObjects(List<ContentObject> contentObjects) {
        if(contentObjects != null) {
            contentList.getItems().clear();
            for(ContentObject contentObject : contentObjects) {
                contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), disableButtons, contentObject));
            }
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }
}
