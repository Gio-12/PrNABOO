package com.pack.prnaboo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pack.prnaboo.Directory.*;

public class MainApp extends Application {
    private static File fileJSON;
    private static Directory directory;

    private static String pathAbs;
    private static String fileName;


    @Override
    public void start(Stage mainStage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle("NABOO");
            mainStage.setScene(scene);
            mainStage.show();
            MainTelegram.main(null);
            directory = new Directory();
            pathAbs = askDirectoryPath();
            createFile(pathAbs);
            readJson();
            directory.setNewsIndex(directoryNews.size());
            // GESTIONE CHIUSURA DA SISTEMARE LE ULTIME COS
            mainStage.setOnCloseRequest(e -> {
                for (User user : directoryUsers) {
                    user.setOffline();
                }
                try {
                    saveJson();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            });
        }  catch (IOException unexpectedShutdown) {
            saveJson();
            unexpectedShutdown.printStackTrace();
        }
    }
    public static String askDirectoryPath(){
        String path = null;
        while(true) {
            TextInputDialog userInput = new TextInputDialog();
            userInput.setTitle("Selezione folder della directory");
            userInput.getDialogPane().setContentText("path della directory");
            Optional<String> result = userInput.showAndWait();
            TextField input = userInput.getEditor();
            if (input.getText() != null && input.getText().toString().length() != 0) {
                path = input.getText();
                break;
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Path invalido Ritentare l'inserimento", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    alert.close();}
                if (alert.getResult() == ButtonType.CANCEL) {
                    Platform.exit();
                    System.exit(0);
                }
            }
        }
        return path;
    }

    public static boolean nameCheck(String fileName) {

        if (fileName.equals("") || fileName.equals(null)) {  // verifica degli spazi vuoti
            return true;
        } else if ((Character.toString(fileName.charAt(0)).equals(" "))) { // verifica degli spazi vuoti
            return true;
        } else {
            Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]"); // char illegali
            Matcher matcher = pattern.matcher(fileName);
            return matcher.find();
        }
    }

    public static File createFile(String pathAbs) throws IOException {
        fileName = "";
        while (true) {
            TextInputDialog userInput = new TextInputDialog();
            userInput.setTitle("Inserisci il nome del file json");
            userInput.getDialogPane().setContentText("Nome file Json");
            Optional<String> result = userInput.showAndWait();
            TextField input = userInput.getEditor();
            if (!nameCheck(input.getText())) {
                 fileName = input.getText();
                break;
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Nome non valido ritentare l'inserimento", ButtonType.YES);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    alert.close();
                }
            }
        }
        if (pathAbs.equals(".")) {
            fileJSON = new File(fileName +".json");
        } else {
            fileJSON = new File(pathAbs,fileName +".json");
        } if (!fileJSON.exists()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Il file è stato creato perchè inesistente", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert.close();
            }
            fileJSON.createNewFile();
            try (PrintWriter out = new PrintWriter(new FileWriter(fileJSON))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String usersJson = gson.toJson(directoryUsers);
                String newsJson = gson.toJson(directoryNews);
                String feedsJson = gson.toJson(directoryFeeds);
                String concat = "{" + "\n" + "\"directoryUsers\":" + usersJson  + "\n," + "\"directoryNews\":" + newsJson + "\n," + "\"directoryFeeds\":" + feedsJson + "}";
                out.write(concat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Il file è stato trovato", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert.close();
            }
            return fileJSON;
        }
        return fileJSON;
    }


    public void saveJson() throws IOException {
        JSONParser parser = new JSONParser();
        try (PrintWriter out = new PrintWriter(new FileWriter(fileJSON))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String usersJson = gson.toJson(directoryUsers);
            String newsJson = gson.toJson(directoryNews);
            String feedsJson = gson.toJson(directoryFeeds);
            String concat = "{" + "\n" + "\"directoryUsers\":" + usersJson  + "\n," + "\"directoryNews\":" + newsJson + "\n," + "\"directoryFeeds\":" + feedsJson + "}";
            out.write(concat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readJson() throws IOException {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(fileJSON));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray directoryUsers1 = (JSONArray) jsonObject.get("directoryUsers");
            for (Object s : directoryUsers1){
                String ss = s.toString();
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(ss, User.class);
                directoryUsers.add(user);
            }
            JSONArray directoryNews1 = (JSONArray) jsonObject.get("directoryNews");
            for (Object s : directoryNews1){
                String ss = s.toString();
                ObjectMapper mapper = new ObjectMapper();
                News news = mapper.readValue(ss, News.class);
                directoryNews.add(news);
            }
            JSONArray directoryFeeds1 = (JSONArray) jsonObject.get("directoryFeeds");
            for (Object s : directoryFeeds1){
                String feed = s.toString();
                directoryFeeds.add(feed);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


        public static void main(String[] args) {
            launch();
        }
}
