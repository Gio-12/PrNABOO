package com.pack.prnaboo;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.pack.prnaboo.Directory.directoryFeeds;
import static com.pack.prnaboo.Directory.sourceExist;

public class RSSreaderPanelController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb){

        sourceListView.setItems(sourceObservableList);
        sourceListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    public static ObservableList<String> sourceObservableList = FXCollections.observableList(directoryFeeds);{
        sourceObservableList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                sourceListView.refresh();
            }
        });
    }

    @FXML
    private ListView<String> sourceListView;


    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    void onListViewClicked(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addSource = new MenuItem("Add Source");
        MenuItem deleteSource = new MenuItem("Delete Source");
        contextMenu.getItems().add(addSource);
        contextMenu.getItems().add(deleteSource);
        sourceListView.setContextMenu(contextMenu);

        addSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String source = null;
                TextInputDialog userInput = new TextInputDialog();
                userInput.setTitle("Add Source");
                userInput.setContentText("New Source");
                userInput.setHeaderText(null);
                userInput.setGraphic(null);
                userInput.getDialogPane().setPrefSize(400,100);
                Optional<String> result = userInput.showAndWait();
                TextField input = userInput.getEditor();
                if(result.isPresent()){
                    source = input.getText();
                    if (!sourceExist(source)){
                        if (isValidURL(source)){
                            directoryFeeds.add(source);
                            //sourceObservableList.add(source);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Url non è valido");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Source già Esistente");
                        alert.showAndWait();
                    }
                }
            }
        });

        deleteSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String source = sourceListView.getSelectionModel().getSelectedItem();
                directoryFeeds.remove(source);
                sourceListView.refresh();
            }
        });
    }
}
