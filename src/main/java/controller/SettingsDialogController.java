package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.FileUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SettingsDialogController extends DialogController {

    @FXML
    private ComboBox languageComboBox;

    @FXML
    private Button okButton;

    private ObservableList<String> languagesList;
    private HashMap<String, String> languagesMap;
    private boolean languageChanged;

    private static final String ENGLISH = "English";
    private static final String SPANISH = "Español";
    private static final String EN = "en";
    private static final String ES = "es";

    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    @FXML
    private void initialize() {

        languageChanged = false;
        okButton.setDisable(true);

        languagesMap = new HashMap<>();
        languagesList = FXCollections.observableArrayList();

        languagesMap.put(ENGLISH, EN);
        languagesMap.put(SPANISH, ES);

        languagesMap.put(EN, ENGLISH);
        languagesMap.put(ES, SPANISH);

        languagesList.add(ENGLISH);
        languagesList.add(SPANISH);

        languageComboBox.setItems(languagesList);
        languageComboBox.getSelectionModel().select(languagesMap.get(Locale.getDefault().toString()));
    }

    /**
     * Method called when language is changed
     **/
    @FXML
    public void handleLanguageChange() {
        languageChanged = true;
        handleChange();
    }

    /**
     * Default method always called when one setting is changed
     **/
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

        if (languageChanged) {
            showLanguageChangedDialog();
        }
    }

    public void showLanguageChangedDialog() {
        Alert languageChangedDialog = new Alert(Alert.AlertType.INFORMATION);
        languageChangedDialog.setHeaderText(null);
        languageChangedDialog.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString("txt.languageChanged"));

        Stage stage = (Stage) languageChangedDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/exam_designer_256.png"));

        languageChangedDialog.showAndWait();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleImport() {
        handleChange();
        String questionsJson = FileUtil.readJsonFile(dialogStage);
        if(questionsJson != null) {
            try {
                databaseManager.importQuestions(questionsJson);
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(MainApp.getPrimaryStage());
                alert.setTitle(ResourceBundle.getBundle(MainApp.LABELS).getString("title.jsonError"));
                alert.setHeaderText(null);
                alert.setContentText(ResourceBundle.getBundle(MainApp.LABELS).getString("txt.jsonError"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleExport() {
        handleChange();
        databaseManager.exportQuestions(dialogStage);
    }
}
