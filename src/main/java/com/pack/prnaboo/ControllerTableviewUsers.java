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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.pack.prnaboo.Directory.directoryUsers;
//import static com.pack.prnaboo.MainController.useDatabaseUsersButton;


public class ControllerTableviewUsers implements Initializable {
    static User userSelectedTo;
    static boolean toEdit = false;
    static boolean toAdd = false;
    static ObservableList<User> users;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        idColumn.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
        telegramIdColumn.setCellValueFactory(new PropertyValueFactory<User,String>("idTelegram"));
        logColumn.setCellValueFactory(new PropertyValueFactory<User,Boolean>("logged"));
        usersTableview.setItems(getUsers());
    }

    public ObservableList<User> getUsers(){

        users = FXCollections.observableArrayList();
        users.addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                usersTableview.refresh();
            }
        });
        users.addAll(directoryUsers);
        return users;
    }

    @FXML
    void onUsersTableViewClicked(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addUser = new MenuItem("Add User");
        MenuItem deleteUser = new MenuItem("Delete User");
        MenuItem editUser = new MenuItem("Edit User");
        contextMenu.getItems().add(addUser);
        contextMenu.getItems().add(deleteUser);
        contextMenu.getItems().add(editUser);
        usersTableview.setContextMenu(contextMenu);

        addUser.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                userSelectedTo = usersTableview.getSelectionModel().getSelectedItem();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("UserDialogPane.fxml")));
                    Stage stage = new Stage();
                    stage.setTitle("Add User");
                    stage.setScene(scene);
                    toAdd = true;
                    stage.show();
                    stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                        if(!stage.isFocused())
                            Platform.runLater(() -> stage.close());
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        deleteUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                 User user = usersTableview.getSelectionModel().getSelectedItem();
                 directoryUsers.remove(user);
                 users.remove(user);
            }
        });
        editUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userSelectedTo = usersTableview.getSelectionModel().getSelectedItem();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("UserDialogPane.fxml")));
                    Stage stage = new Stage();
                    stage.setTitle("Edit user");
                    stage.setScene(scene);
                    toEdit = true;
                    stage.show();
                    stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                        if(!stage.isFocused())
                            Platform.runLater(() -> stage.close());
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @FXML
    private TableView<User> usersTableview;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> telegramIdColumn;

    @FXML
    private TableColumn<User, Boolean> logColumn;

}

