package com.pack.prnaboo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.pack.prnaboo.ControllerTableviewUsers.*;
import static com.pack.prnaboo.Directory.*;


public class UserDialogPaneController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmationButton;

    @FXML
    private TextField idTelegramTextField;

    @FXML
    private TextField idTexteField;

    @FXML
    private TextField passwordTextField;

    @FXML
    void useCancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    void useOkButton(ActionEvent event) {
        User user = userSelectedTo;
        if (idTexteField.getText() != null && passwordTextField.getText() != null && idTelegramTextField.getText() != null){
            User newUser = new User (idTexteField.getText(),passwordTextField.getText(),idTelegramTextField.getText());
            if (toAdd){
                directoryUsers.add(newUser);
                users.add(newUser);
                toAdd = false;
                Stage stage = (Stage) confirmationButton.getScene().getWindow();
                stage.close();
            } if (toEdit){
                if (checkUser(newUser)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User esiste già", ButtonType.YES);
                    alert.showAndWait();
                } else {
                    user.setId(newUser.getId());
                    user.setPassword(newUser.getPassword());
                    user.setIdTelegram(newUser.getIdTelegram());
                    User userDirectory = getUser(user);

                    userDirectory.setId(newUser.getId());
                    userDirectory.setPassword(newUser.getPassword());
                    userDirectory.setIdTelegram(newUser.getIdTelegram());

                    toEdit = false;
                    Stage stage = (Stage) confirmationButton.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }
}