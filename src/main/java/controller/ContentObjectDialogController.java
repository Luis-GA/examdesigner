package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.ContentObject;

import java.util.ArrayList;
import java.util.List;

public class ContentObjectDialogController extends DialogController{

    @FXML
    private ListView<ContentObjectHBox> contentList;
    private List<ContentObject> contentObjects;
    private boolean saved = false;

    @FXML
    public void initialize() {
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), null));
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), null));
    }

    @FXML
    public void addContentObjectHBox() {
        contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), null));
    }

    @FXML
    public void handleCancel() {
        saved = false;
        dialogStage.close();
    }

    @FXML
    public void handleOk() {
        saved = true;
        dialogStage.close();
    }

    public List<ContentObject> getContentObjectList() {
        if(saved == true) {
            ArrayList<ContentObject> aux = new ArrayList<>();
            for (ContentObjectHBox contentObjectHBox : contentList.getItems()) {
                aux.add(contentObjectHBox.getContentObject());
            }
            return aux;
        } else {
            return this.contentObjects;
        }
    }

    @Override
    public void setContentObjects(List<ContentObject> contentObjects) {
        if(contentObjects != null) {
            contentList.getItems().clear();
            for(ContentObject contentObject : contentObjects) {
                contentList.getItems().add(new ContentObjectHBox(contentList.getItems(), contentObject));
            }
        }
        this.contentObjects = contentObjects;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }
}
