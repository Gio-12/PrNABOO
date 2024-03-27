package com.pack.prnaboo;

import com.sun.syndication.io.FeedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.pack.prnaboo.Directory.*;

public class MainController implements Initializable{

    @FXML
    private Button databaseNewsButton;

    @FXML
    private Button databaseUsersButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button manageFeedButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuButton;

    @FXML
    private Button newsCollectorButton;

    @FXML
    private AnchorPane paneDisplayTableview;

    @FXML
    private static ImageView notify;

    public void useDatabaseNewsButton(javafx.event.ActionEvent actionEvent) throws IOException {
        if (directoryUsers == null || directoryNews.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Directory News vuota o nulla, impostare Feed e poi usare NewsCollector", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert.close();
            }
        } else {
            AnchorPane view = FXMLLoader.load(getClass().getResource("TableviewNews.fxml"));
            paneDisplayTableview.getChildren().add(view);
        }

    }

    public void useDatabaseUsersButton(javafx.event.ActionEvent actionEvent) throws IOException {
        if (directoryUsers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Directory Users vuota o nulla", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert.close();
            }
        } else {
                AnchorPane view = FXMLLoader.load(getClass().getResource("TableviewUsers.fxml"));
                paneDisplayTableview.getChildren().add(view);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void useNewsCollectorButton(javafx.event.ActionEvent actionEvent) throws FeedException, URISyntaxException, IOException, InterruptedException {
        if (directoryFeeds.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Directory Feeds vuota o nulla", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert.close();
            }
        } else {
            updateFeed();
        }
    }

    public void useManageFeedController(javafx.event.ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("RssReaderPanel.fxml")));
        Stage stage = new Stage();
        stage.setTitle("Manage Feed");
        stage.setScene(scene);
        stage.show();
        stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
            if(!stage.isFocused())
                Platform.runLater(() -> stage.close());
        });

    }

    public void useHelpButton(javafx.event.ActionEvent actionEvent) {
        var alert = new Alert(Alert.AlertType.INFORMATION, "INFORMATION", ButtonType.YES);
        alert.setTitle("Help");
        alert.setHeaderText("INFORMATION");
        alert.setContentText("•\t“Database News”\n" +
                "Il pulsante database news restituisce sullo schermo la tableview delle news, se appositamente è stato attivato prima il news collector, senno vi resistuirà un messaggio di warning.\n" +
                "Caricata la tableview delle news, essa apparirà sul lato destro dello schermo restituendo le news raccolte suddivise in colonne rappresentanti: ID, Titolo, Timestamp, Description, Author, Sources, Categories, Comments e Votes.\n" +
                "La tabella al vostro click destro vi restituirà un context menu con le opzioni: Delete News e Show comments, nel caso della prima opzione vi permetterà di cancellare la news selezionata, se invece sceglierete show comments verrà aperta una nuova finestra che caricherà nel caso ci siano i comments della new selezionata precedentemente.\n" +
                "•\t“Database Users”\n" +
                "Il pulsante database users, come il news, restituisce sullo schermo la tableview degli users, nel caso non ci siano users registrati, verrà restituito un messaggio di warning;\n" +
                "sempre come nel primo caso al vostro click destro vi verrà restituito un menu con le opzioni : add user, delete user, edit user. \n" +
                "Come suggeriscono i nomi le funzionalità sono equivalenti, nel caso di add e edit user, il programma vi restituirà una finestra dove inserire le informazioni dell’user. \n" +
                "•\t“News Collector”\n" +
                "Il news collector come da istruzione andrà ad aggiornare la tabella delle news scaricando i contenuti rss dei siti inseriti nel Manage Feed Rss.\n" +
                "•\t“ManageFeedRss”\n" +
                "Il pulsante vi restituirà una ListView contenente le sorgenti dei feed rss, al vostro click destro vi aprirà un context menu con le opzioni: Add Feed e Delete Feed. \n" +
                "•\t“Help”\n" +
                "Il pulsante vi restituirà un riassunto delle funzionalità.\n");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            alert.close();

        }
    }
}
