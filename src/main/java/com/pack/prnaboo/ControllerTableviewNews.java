package com.pack.prnaboo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.pack.prnaboo.Directory.directoryNews;

public class ControllerTableviewNews implements Initializable {
    static ArrayList<String> selectedNewsComments;

    static News newsSelected;
    static ObservableList<News> newsCollection;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        idNewsColumn.setCellValueFactory(new PropertyValueFactory<News, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<News,String>("title"));
        timeStampColumn.setCellValueFactory(new PropertyValueFactory<News,String>("timestamp"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<News,String>("description"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<News,String>("author"));
        sourcesColumn.setCellValueFactory(new PropertyValueFactory<News,String>("sources"));
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<News,String>("categories"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<News,ArrayList>("comments")); // PROBLEMA DELLA VISUALIZZAZIONE DEI COMMENTS NELLA TABLEVIEW
        votesColumn.setCellValueFactory(new PropertyValueFactory<News,Double>("vote"));
        tableViewNews.setItems(getNews());
    }

    public ObservableList<News> getNews(){

        newsCollection = FXCollections.observableArrayList();
        newsCollection.addListener(new ListChangeListener<News>() {
            @Override
            public void onChanged(Change<? extends News> c) {
                tableViewNews.refresh();
            }
        });
        newsCollection.addAll(directoryNews);
        return newsCollection;
    }


    public void onNewsTableViewClicked(javafx.scene.input.MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteNews = new MenuItem("Delete News");
        MenuItem showComments = new MenuItem("Show comments");
        contextMenu.getItems().add(deleteNews);
        contextMenu.getItems().add(showComments);
        tableViewNews.setContextMenu(contextMenu);

        deleteNews.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                News news = tableViewNews.getSelectionModel().getSelectedItem();
                directoryNews.remove(news);
                newsCollection.remove(news);
            }
        });

        showComments.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newsSelected = tableViewNews.getSelectionModel().getSelectedItem();
                selectedNewsComments = new ArrayList<String>();
                selectedNewsComments = newsSelected.getComments();
                Scene scene;
                if (newsSelected.getComments().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Comments Section is Empty", ButtonType.YES);
                    alert.showAndWait();
                } else {
                    try {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("CommentsPanel.fxml")));
                        Stage stage = new Stage();
                        stage.setTitle("Comments Panel");
                        stage.setScene(scene);
                        stage.show();
                        stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                            if(!stage.isFocused())
                                Platform.runLater(() -> stage.close());
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    private TableView<News> tableViewNews;

    @FXML
    private TableColumn<News, Integer> idNewsColumn;

    @FXML
    private TableColumn<News, String> authorColumn;

    @FXML
    private TableColumn<News, String> categoriesColumn;


    @FXML
    private TableColumn<News, String> descriptionColumn;

    @FXML
    private TableColumn<News, String> sourcesColumn;

    @FXML
    private TableColumn<News, String> timeStampColumn;

    @FXML
    private TableColumn<News, String> titleColumn;

    @FXML
    private TableColumn<News, Double> votesColumn; // Da modificare in media dei voti direi

    @FXML
    private TableColumn<News, ArrayList> commentsColumn; // Da modificare in numero di commenti direi

}
