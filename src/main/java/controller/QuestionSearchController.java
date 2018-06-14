package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.EssayQuestion;
import model.Question;
import model.TestQuestion;
import util.DialogUtil;

import java.util.Map;

public class QuestionSearchController extends DialogController{

    @FXML
    private Pagination questionsPagination;
    @FXML
    private Button searchQuestionsButton;
    @FXML
    private TextField title;
    final private int rowsPerPage = 10;

    private ObservableList<QuestionWrapper> questionsList;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }

    @FXML
    private void initialize() {
        questionsList = FXCollections.observableArrayList();

        title.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    searchQuestions();
                }
            }
        });

        ImageView searchImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_search_black.png").toString()));

        searchImage.setFitHeight(15);
        searchImage.setFitWidth(15);

        searchQuestionsButton.setGraphic(searchImage);

        reloadPagination();
    }

    private void reloadPagination() {
        int numPages = questionsList.size() / rowsPerPage;
        if(questionsList.size() % rowsPerPage != 0)  {
            numPages = numPages + 1;
        }
        questionsPagination.setPageCount(numPages);
        questionsPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > questionsList.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            }
        });
    }

    private ListView createPage(int pageIndex) {

        ListView<QuestionTitleHBox> listView = new ListView<>();
        ObservableList<QuestionTitleHBox> aux = FXCollections.observableArrayList();

        for (int i = pageIndex * 10; i < pageIndex * 10 + 10 && i < questionsList.size(); i++) {
            aux.add(new QuestionTitleHBox(aux, questionsList.get(i)));
        }
        listView.setItems(aux);
        return listView;
    }

    public class QuestionTitleHBox extends GridPane {
        private Label title = new Label();
        private Button deleteButton = new Button();
        private Button openButton = new Button();
        private ObservableList<QuestionTitleHBox> list;
        private QuestionWrapper question;

        public QuestionTitleHBox(ObservableList<QuestionTitleHBox> list, QuestionWrapper question) {
            super();

            this.list = list;
            this.question = question;

            ImageView openImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_open_in_new_black.png").toString()));
            openImage.setFitHeight(15);
            openImage.setFitWidth(15);
            openButton.setGraphic(openImage);
            openButton.setOnAction(event -> openQuestion());

            ImageView deleteImage = new ImageView(new Image(MainApp.class.getResource("/images/ic_delete_forever_black.png").toString()));
            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            deleteButton.setGraphic(deleteImage);
            deleteButton.setOnAction(event -> deleteQuestion());

            title.setFont(Font.font(12));
            title.setMaxHeight(100);
            title.setMaxWidth(500);
            title.setText(this.question.getTitle());

            RowConstraints singleRow = new RowConstraints();
            this.getRowConstraints().add(singleRow);

            ColumnConstraints titleColumn = new ColumnConstraints();
            titleColumn.setHgrow(Priority.SOMETIMES);
            this.getColumnConstraints().add(titleColumn);
            this.getColumnConstraints().add(new ColumnConstraints(10));
            this.getColumnConstraints().add(new ColumnConstraints(40));
            this.getColumnConstraints().add(new ColumnConstraints(40));

            this.add(title, 0, 0);
            this.add(deleteButton, 2, 0);
            this.add(openButton, 3, 0);
        }

        private void deleteQuestion() {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            if(DialogUtil.showDeleteConfirmationDialog("txt.deleteQuestionConfirmation")) {
                questionsList.remove(this.list.indexOf(this));
                this.list.remove(this);
                int currentPage = questionsPagination.getCurrentPageIndex();
                reloadPagination();
                questionsPagination.setCurrentPageIndex(currentPage);
                databaseManager.deleteQuestion(this.question.idQuestion);
            }
        }

        private void openQuestion() {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            Question question = databaseManager.getQuestion(this.question.idQuestion);
            TestQuestion testQuestion;
            EssayQuestion essayQuestion;
            Boolean isTest = true;
            if(question.isTest()) {
                testQuestion = (TestQuestion) question;
                essayQuestion = new EssayQuestion();
                testQuestion.copyBaseData(essayQuestion);
            } else {
                isTest = false;
                essayQuestion = (EssayQuestion) question;
                testQuestion = new TestQuestion();
                essayQuestion.copyBaseData(testQuestion);
            }
            DialogUtil.showQuestionOverviewDialog(testQuestion, essayQuestion, getDialogStage(), isTest);
        }
    }

    public class QuestionWrapper {
        private String title;
        private int idQuestion;

        public QuestionWrapper(String title, int idQuestion) {
            this.title = title;
            this.idQuestion = idQuestion;
        }

        public int getIdQuestion() {
            return idQuestion;
        }

        public String getTitle() {
            return this.title;
        }
    }

    @FXML
    private void searchQuestions() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Map<Integer, String> questionsMap = databaseManager.searchQuestions(title.getText());
        questionsList.clear();
        questionsMap.forEach((k, v) -> questionsList.add(new QuestionWrapper(v, k)));
        reloadPagination();
    }
}
