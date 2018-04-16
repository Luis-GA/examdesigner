package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import model.ContentObject;
import util.DialogUtil;

import java.nio.file.Path;
import java.util.ResourceBundle;

public class ContentObjectHBox extends GridPane {
    private TextArea title = new TextArea();
    private ImageView image = new ImageView();
    private Button deleteButton = new Button();
    private Button openImageButton = new Button();
    private ObservableList<ContentObjectHBox> list;
    private BooleanProperty disableButton;
    private Boolean imageLoaded = false;
    private ComboBox typesComboBox = new ComboBox();
    private static final String IMAGE = ResourceBundle.getBundle(MainApp.LABELS).getString("lbl.image");
    private static final String TEXT = ResourceBundle.getBundle(MainApp.LABELS).getString("lbl.text");

    public ContentObjectHBox(ObservableList<ContentObjectHBox> list, BooleanProperty disableButton, ContentObject contentObject) {
        super();

        this.list = list;
        this.disableButton = disableButton;

        openImageButton.setText(ResourceBundle.getBundle(MainApp.LABELS).getString("btn.openFile"));
        openImageButton.setVisible(false);
        openImageButton.setOnAction(event -> openImageFile());

        title.setText("");
        title.setFont(Font.font(12));
        title.setMaxHeight(100);
        title.setWrapText(true);

        image.setVisible(false);
        image.setFitHeight(100);
        image.setPreserveRatio(true);

        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add(TEXT);
        typeList.add(IMAGE);
        typesComboBox.setItems(typeList);
        typesComboBox.getSelectionModel().select(TEXT);
        typesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(newValue == IMAGE) {
                    title.setVisible(false);
                    image.setVisible(true);
                    if(!imageLoaded)
                        openImageButton.setVisible(true);
                } else {
                    image.setVisible(false);
                    title.setVisible(true);
                    openImageButton.setVisible(false);
                }
            }
        });

        if(contentObject != null) {
            if(contentObject.getType() == ContentObject.Type.IMAGE) {
                typesComboBox.getSelectionModel().select(IMAGE);
                imageLoaded = true;
                image = contentObject.getImageProperty();
            } else {
                title.setText(contentObject.getText());
            }
        }

        ImageView deleteImage = new ImageView(new Image("images/ic_delete_forever_black.png"));

        deleteImage.setFitHeight(15);
        deleteImage.setFitWidth(15);

        deleteButton.setGraphic(deleteImage);
        deleteButton.setOnAction(event -> deleteContentObject());
        deleteButton.disableProperty().bindBidirectional(this.disableButton);

        ColumnConstraints contentColumn = new ColumnConstraints();
        contentColumn.setHgrow(Priority.ALWAYS);
        contentColumn.setHalignment(HPos.CENTER);
        this.getColumnConstraints().add(new ColumnConstraints(100));
        this.getColumnConstraints().add(new ColumnConstraints(10));
        this.getColumnConstraints().add(contentColumn);
        this.getColumnConstraints().add(new ColumnConstraints(10));
        this.getColumnConstraints().add(new ColumnConstraints(40));
        RowConstraints singleRow = new RowConstraints();
        this.getRowConstraints().add(singleRow);

        this.add(typesComboBox, 0, 0);
        this.add(title, 2, 0);
        this.add(image, 2, 0);
        this.add(openImageButton, 2, 0);
        this.add(deleteButton, 4, 0);
    }

    private void deleteContentObject() {
        this.list.remove(this);
        if(this.list.size() < 3)
            this.disableButton.setValue(true);
    }

    private void openImageFile() {
        Path path = DialogUtil.showOpenImageDialog(MainApp.getPrimaryStage());

        if(path != null) {
            try {
                Image image = new Image("file:///" + path.toString());
                this.image.setImage(image);
                this.imageLoaded = true;
                this.openImageButton.setVisible(false);
            } catch (Exception e) {
                DialogUtil.showInfoDialog("txt.imageError");
            }
        }
    }


    public ContentObject getContentObject() {
        ContentObject aux = new ContentObject();
        if(this.typesComboBox.getSelectionModel().equals(IMAGE)) {
            aux.setContent(this.image);
        } else {
            aux.setContent(this.title.getText());
        }

        return aux;
    }
}
