package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.*;
import java.util.prefs.Preferences;

public class SettingsDialogController {

    @FXML
    private ComboBox languageComboBox;

    @FXML
    private Button okButton;

    private Stage dialogStage;

    private ObservableList<String> languagesList;
    private HashMap<String, String> languagesMap;
    private boolean languageChanged;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {

        languageChanged = false;
        okButton.setDisable(true);

        languagesMap = new HashMap<String, String>();
        languagesList = FXCollections.observableArrayList();

        languagesMap.put("English", "en");
        languagesMap.put("Español", "es");

        languagesMap.put("en", "English");
        languagesMap.put("es", "Español");

        languagesList.add("English");
        languagesList.add("Español");

        languageComboBox.setItems(languagesList);
        languageComboBox.getSelectionModel().select(languagesMap.get(Locale.getDefault().toString()));
    }

    /** Method called when language is changed **/
    @FXML
    public void handleLanguageChange() {
        languageChanged = true;
        handleChange();
    }

    /** Default method always called when one setting is changed **/
    @FXML
    public void handleChange() {
        okButton.setDisable(false);
    }

    @FXML
    public void handleSetSettings() {

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        int selectedIndex = languageComboBox.getSelectionModel().getSelectedIndex();
        String selectedLanguage = (String) languageComboBox.getItems().get(selectedIndex);
        prefs.put("language", languagesMap.get(selectedLanguage));
        Locale.setDefault(new Locale(languagesMap.get(selectedLanguage)));

        dialogStage.close();

        if(languageChanged){
            showLanguageChangedDialog();
        }
    }

    public void showLanguageChangedDialog(){
        Alert languageChangedDialog = new Alert(Alert.AlertType.INFORMATION);
        languageChangedDialog.setHeaderText(null);
        languageChangedDialog.setContentText(ResourceBundle.getBundle("languages/labels").getString("txt.languageChanged"));

        Stage stage = (Stage) languageChangedDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/exam_designer_256.png"));

        languageChangedDialog.showAndWait();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
