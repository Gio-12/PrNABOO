package com.pack.prnaboo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static com.pack.prnaboo.ControllerTableviewNews.newsSelected;
import static com.pack.prnaboo.ControllerTableviewNews.selectedNewsComments;
import static com.pack.prnaboo.Directory.getNews;

public class CommentsPanelController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb){
        commentsListView.setItems(commentsObservableList);
        commentsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    public static ObservableList<String> commentsObservableList = FXCollections.observableList(selectedNewsComments);{
        commentsObservableList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                commentsListView.refresh();
            }
        });
    }

    @FXML
    private ListView<String> commentsListView;

    @FXML
    void onCommentsListViewClicked(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteComment = new MenuItem("Delete Comment");
        contextMenu.getItems().add(deleteComment);
        commentsListView.setContextMenu(contextMenu);
        deleteComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String comment = commentsListView.getSelectionModel().getSelectedItem();
                // modificare il comment della news selected tableview
                newsSelected.getComments().remove(comment);
                getNews(newsSelected).getComments().remove(comment);
                //da modificare anche il comment nella directory originale
                commentsListView.refresh();
            }
        });
    }
}
