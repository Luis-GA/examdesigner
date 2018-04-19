package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.EssayQuestion;
import model.TestQuestion;
import util.DialogUtil;
import util.FileUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.prefs.Preferences;

public class SettingsDialogController extends DialogController {

    @FXML
    private ComboBox languageComboBox;
    @FXML
    private Button okButton;

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

        ObservableList<String> languagesList;

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
            DialogUtil.showInfoDialog("txt.languageChanged");
        }
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
                DialogUtil.showInfoDialog("txt.questionsImported");
            } catch (Exception e) {
                DialogUtil.showInfoDialog("txt.jsonError");
            }
        }
    }

    @FXML
    private void handleExport() {
        handleChange();
        databaseManager.exportQuestions(dialogStage);
    }

    @FXML
    private void addQuestion() {
        DialogUtil.showQuestionOverviewDialog(new TestQuestion(), new EssayQuestion(), this.dialogStage);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }
}
