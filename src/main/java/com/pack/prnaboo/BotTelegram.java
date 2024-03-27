package com.pack.prnaboo;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

import static com.pack.prnaboo.Directory.*;

class MyBot extends TelegramLongPollingBot {

    public Directory directory;
    private ArrayList<Reply> waitingForReply = new ArrayList<>();

    public class Reply{

        // string idTelegram invece di userID normale
        private final String userId;
        private final int newsUniqueId;
        boolean waitingReply;

        public void setWaitingReplyTrue() {
            this.waitingReply = true;
        }
        public void setWaitingReplyFalse() {
            this.waitingReply = false;
        }

        public int getNewsUniqueId() {
            return newsUniqueId;
        }

        public Reply(String userId, int newsUniqueId){
            this.newsUniqueId = newsUniqueId;
            this.userId = userId;
            this.waitingReply = false;
        }
    }

    public int isUserWaitingNewsId(String userId){
        int i = -1;
        for (Reply reply : waitingForReply){
            if (reply.userId.equals(userId)){
                i = reply.getNewsUniqueId();
            }
        }
        return i;
    }

    public boolean isUserWaiting(String userId){
        boolean check = false;
        for (Reply reply : waitingForReply){
            if (reply.userId.equals(userId)){
                if (reply.waitingReply){
                    check = true;
                    reply.setWaitingReplyFalse();
                }
            }
        }
        return check;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String responseMessage = "";
            String userId = Long.toString(update.getMessage().getFrom().getId());
            Long updateId = update.getMessage().getChatId();
            String receivedMessage = update.getMessage().getText();
            if (isUserWaiting(userId)){
                int newsId = isUserWaitingNewsId(userId);
                String newsIdParsed = String.valueOf(newsId);
                News news = searchNewsUniqueId(newsIdParsed);
                String id = getUserIdTelegram(userId).getId(); // CAMBIAMENTO
                news.addComment("Utente: " + id + "\n" + receivedMessage);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(updateId);
                responseMessage = "Commento aggiunto con successo";
                sendMessage.setText(responseMessage);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                // user non esiste
                if (!checkUserIdTelegram(userId)){
                    // request
                    String[] requestCommands;
                    requestCommands = receivedMessage.split(" ", 3);
                    // comand infoLog
                    if (requestCommands[0].equals("/infoLog")) {
                        responseMessage = "Per poter usare le funzionalità del bot è necessario registrarsi al comando, nel caso non siate registrati, ogni comando vi restituirà un messaggio d'errore" + "\n" +
                                "/registration <Username> <Password> " + "\n" +
                                "Prima di poter usare tutte le funzionalità del programma è necessario loggare, successivamente registrati vi sarà permesso usare il comando /log" + "\n" +
                                "/log <Username> <Password>";
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(updateId);
                        sendMessage.setText(responseMessage);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    // comand register
                    if (requestCommands[0].equals("/registration")) {
                        User user = new User(requestCommands[1], requestCommands[2], userId);
                        if (!checkUser(user)) {
                            responseMessage = "Utente è stato registrato, ora è possibile utilizzare le altre funzionalità";
                            directoryUsers.add(user);
                        } else {
                            responseMessage = "utente già registrato";
                        }
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(updateId);
                        sendMessage.setText(responseMessage);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                                e.printStackTrace();
                        }
                    }
                    // FINO A QUI IL CODICE FUNZIONA
                } else if (checkUserIdTelegram(userId)) {
                    // user esiste estraggo l'user provvisorio
                    User userProv = getUserIdTelegram(userId);
                    if (!userProv.isLogged()) {
                        // request
                        String[] requestCommands;
                        requestCommands = receivedMessage.split(" ", 3);
                        // comand infoLog
                        if (requestCommands[0].equals("/infoLog")) {
                            responseMessage = "Per poter usare le funzionalità del bot è necessario registrarsi al comando, nel caso non siate registrati, ogni comando vi restituirà un messaggio d'errore" + "\n" +
                                    "/registration <Username> <Password> " + "\n" +
                                    "Prima di poter usare tutte le funzionalità del programma è necessario loggare, successivamente registrati vi sarà permesso usare il comando /log" + "\n" +
                                    "/log <Username> <Password>";
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        // comand login
                        if (requestCommands[0].equals("/log")) {
                            //User user = new User(requestCommands[1], requestCommands[2], userId);
                            if (userProv.id.equals(requestCommands[1]) && userProv.password.equals(requestCommands[2]) && userProv.idTelegram.equals(userId)){
                                userProv.setLogged();
                                responseMessage = "Benvenuto, utente loggato";
                            } else {
                                responseMessage = "Utente non esiste, tentare la registrazione";
                            }
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (userProv.isLogged() == true){

                        // request
                        String[] requestCommandsLog;
                        requestCommandsLog = receivedMessage.split(" ", 3);
                        // all commands
                        if (requestCommandsLog[0].equals("/information")) {
                            responseMessage = "Benvenuto nel News to Dump" + "\n" +
                                    "Per poter usare le funzionalità del bot è necessario registrarsi al comando, nel caso non siate registrati, ogni comando vi restituirà un messaggio d'errore"
                                    + "\n" +
                                    "/registration <Username> <Password> " + "\n" +
                                    "Prima di poter usare tutte le funzionalità del programma è necessario loggare"
                                    + "\n" +
                                    "/log <Username> <Password>" + "\n" +
                                    "Appena vi sarete registrati vi sarà possibile usare i seguenti comandi, seguite le rispettive istruzioni."
                                    + "\n" +
                                    "Ogni notizia ha una ID unico, da utilizzare in determinati comandi." + "\n" +
                                    "Il comando seguente vi restituirà la grandezza della directory." + "\n" +
                                    "/getDirectorySize" + "\n" +
                                    "Il comando seguente vi permetterà di filtrare le news in base agli indici di registro, nel caso indice y superi la grandezza del registro esso verrà automaticamente reimpostato."
                                    + "\n" +
                                    "/getNews <Index i> <Index y>" + "\n" +
                                    "Il comando seguente vi restituirà i tipi di filtri accettati." + "\n" +
                                    "/getTypeFilter" + "\n" +
                                    "Il comando vi permetterà di filtrare le news a seconda dei tipi dei filtri." + "\n"
                                    +
                                    "/getFilteredNews <Type of filter> <filter>" + "\n" +
                                    "Il comando seguente vi restituirà i commenti di una determinata notizia." + "\n" +
                                    "/seeComments <Unique Id>" + "\n" +
                                    "Il comando seguente vi restituirà una conferma, successivamente il vostro prossimo messaggio verrà riconosciuto come commento"
                                    + "\n" +
                                    "/addComment <Unique Id>" + "\n" +
                                    "Il comando seguente vi permetterà di aggiungere il voto ad una determinata news"
                                    + "\n" +
                                    "/addVote <Unique Id> <vote>";

                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);

                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        if (requestCommandsLog[0].equals("/getTypeFilter")) {
                            responseMessage = "title || author || sources || date || vote || categories";
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        if (requestCommandsLog[0].equals("/getDirectorySize")) {
                            responseMessage = getDirectorySize();
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                        if (requestCommandsLog[0].equals("/getNews")) {
                            String indexA = (requestCommandsLog[1]);
                            String indexB = (requestCommandsLog[2]);
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            responseMessage = getListByIndex(indexA, indexB);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                        if (requestCommandsLog[0].equals("/getFilteredNews")) {
                            String typeFilter = requestCommandsLog[1];
                            String filter = requestCommandsLog[2];
                            responseMessage = searchNewsByFilters(typeFilter, filter);
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                        // DA RISOLVERE IL PROBLEMA CON L'ID UNICO DELLE NOTIZIE

                        if (requestCommandsLog[0].equals("/seeComments")) {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            String newsId = requestCommandsLog[1];
                            News news = searchNewsUniqueId(newsId);
                            responseMessage = news.toStringComments();
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        if (requestCommandsLog[0].equals("/addComment")) {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            String newsId = requestCommandsLog[1];
                            News news = searchNewsUniqueId(newsId);
                            if (news != null) {
                                int newsIdParsed = Integer.parseInt(newsId);
                                Reply reply = new Reply(userId, newsIdParsed);
                                reply.setWaitingReplyTrue();
                                waitingForReply.add(reply);
                                responseMessage = "Scrivi un commento";
                            } else {
                                responseMessage = "News non trovata";
                            }
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        if (requestCommandsLog[0].equals("/addVote")) {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(updateId);
                            String newsId = requestCommandsLog[1];
                            String vote = requestCommandsLog[2];
                            News news = searchNewsUniqueId(newsId);
                            news.addVote(vote);
                            responseMessage = "voto inviato";
                            sendMessage.setText(responseMessage);
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "NabooPP01_Bot";
    }

    @Override
    public String getBotToken() {
        return "5850641295:AAHPB_4VdrqCp2q85qIHuU65yNSek83U048";
    }
}

  /*
                    infoLog - informazioni per la registrazione
                    registration - <username> <password> per potersi registrare al bot
                    log - <username> <password> per accedere al bot
                    information - informazioni generali
                    getTypeFilter - mostra i filtri che si possono usare
                    getDirectorySize - mostra la grandezza della directory
                    getNews - <Index i> <Index y> ricerca per indici
                    getFilteredNews - <Type of filter> <filter>
                    addComment - <Unique Id> aggiunge il vostro commento
                    seeComments - <Unique Id> mostra i commenti
                    addVote - <Unique Id> <vote> permette di aggiunger il voto
                    */