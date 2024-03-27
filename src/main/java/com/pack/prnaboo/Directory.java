package com.pack.prnaboo;

import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Directory {
    public static ArrayList<News> directoryNews;
    public static ArrayList<User> directoryUsers;
    public static ArrayList<String> directoryFeeds;

    private static int newsIndex = 0;

    public Directory() {
        this.directoryNews = new ArrayList<News>();
        this.directoryUsers = new ArrayList<User>();
        this.directoryFeeds = new ArrayList<String>();
    }

    static void addNews(News newsToAdd) {
        directoryNews.add(newsToAdd);
        newsToAdd.setId(newsIndex);
        newsIndex++;

    }
    static void setNewsIndex(int i){
        newsIndex = i;
    }

    // COMANDI LEGATI ALLA DIRECOTORY

    //APPROVATO
    public static String getDirectorySize() {
        if (directoryNews.isEmpty()) {
            return "La directory è vuota";
        }
        String size = Integer.toString(directoryNews.size());
        return size;
    }

    public static String getListByIndex(String indexa, String indexb) {
        int indexA = Integer.parseInt(indexa);
        int indexB = Integer.parseInt(indexb);
        String list = "";
        if (directoryNews.isEmpty()) {
            return "Directory vuota nessuna notizia attualmente disponibile";
        } else {
            if (indexA < 0 || indexA >= directoryNews.size()) {
                list = "Valori immessi sono errati, indici non corrispondenti";
            } else {
                if (indexB >= directoryNews.size()) {
                    for (int i = indexA; i < directoryNews.size(); i++) {
                        list += directoryNews.get(i).toStringNews() + "\n";
                    }
                } else {
                    for (int i = indexA; i < indexB; i++) {
                        list += directoryNews.get(i).toStringNews() + "\n";
                    }
                }
            }
        }
        return list;
    }

    public static boolean newsExist(String titolo, String timestamp) {
        boolean check = false;
        for (News news : directoryNews) {
            if (news.getTitle().equals(titolo) && news.getTimestamp().equals(timestamp)) {
                check = true;
            }
        }
        return check;
    }
    public static News getNews(News news){
        News uToCopy = null;
        for(News u : directoryNews){
            if (u.equals(news)){
                uToCopy = u;
            }
        } return uToCopy;
    }

    public static News searchNewsUniqueId(String id) {
        int idParsed = Integer.parseInt(id);
        News newsId = null;
        for (News news : directoryNews) {
            if (news.getId() == idParsed) {
                newsId = news;
            }
        }
        return newsId;
    }

    public static String searchNewsByFilters(String filter, String filterName) {
        String selectedNews = "";
        if (filter.equals("title")) {
            selectedNews = "";
            for (News news : directoryNews) {
                if (Pattern.compile(Pattern.quote(filterName), Pattern.CASE_INSENSITIVE).matcher(news.getTitle()).find()) {
                    //if (news.getTitle().contains(filterName.toLowerCase()) || news.getTitle().contains(filterName.toUpperCase())) {
                    selectedNews += news.toStringNews();
                }
            }
            if (selectedNews.isEmpty()) {
                selectedNews = "Errore nell'inserimento del filterName";
            }
            return selectedNews;
        } else if (filter.equals("author")) {
            for (News news : directoryNews) {
                if (Pattern.compile(Pattern.quote(filterName), Pattern.CASE_INSENSITIVE).matcher(news.getAuthor()).find()) {
                    selectedNews += news.toStringNews();
                }
            }
            if (selectedNews.isEmpty()) {
                selectedNews = "Errore nell'inserimento del filterName";
            }
            return selectedNews;
        } else if (filter.equals("sources")) {
            for (News news : directoryNews) {
                if (Pattern.compile(Pattern.quote(filterName), Pattern.CASE_INSENSITIVE).matcher(news.getSources()).find()) {
                    selectedNews += news.toStringNews();
                }
            }
            if (selectedNews.isEmpty()) {
                selectedNews = "Errore nell'inserimento del filterName";
            }
            return selectedNews;
        } else if (filter.equals("categories")) {
            for (News news : directoryNews) {
                if (Pattern.compile(Pattern.quote(filterName), Pattern.CASE_INSENSITIVE).matcher(news.getCategories()).find()) {
                    selectedNews += news.toStringNews();
                }
            }
            if (selectedNews.isEmpty()) {
                selectedNews = "Errore nell'inserimento del filterName";
            }
            return selectedNews;
        }
        // DA SISTEMARE LA LETTURA DELLA DATA PROBABILMENTE NON RICONOSCERà IL FILTER
        else if (filter.equals("date")) {
            for (News news : directoryNews) {
                if (Pattern.compile(Pattern.quote(filterName), Pattern.CASE_INSENSITIVE).matcher(news.getTimestamp()).find()) {
                    selectedNews += news.toStringNews();
                }
            }
            if (selectedNews.equals("")) {
                selectedNews = "Errore nell'inserimento del filterName";
            }
            return selectedNews;
        } else if (filter.equals("vote")) {
            try {
                int filterVote = Integer.parseInt(filter);
                for (News news : directoryNews) {
                    if (news.getVote() == filterVote) {
                        selectedNews += news.toStringNews();
                    }
                }
                if (selectedNews.equals("")) {
                    selectedNews = "Errore nell'inserimento del filterName";
                }
                return selectedNews;
            } catch (NumberFormatException e) {
                return "Errore nell'assegnazione del voto variabile non numerica";
            }
        } else {
            selectedNews = "Errore nell'inserimento del filtro";
        }
        return selectedNews;
    }

    // SEZIONE METHOD PER LA DIRECTORY DEGLI UTENTI
    public static User getUser(User user){
        User uToCopy = null;
        for(User u : directoryUsers){
            if (u.equals(user)){
                uToCopy = u;
            }
        } return uToCopy;
    }

    public static boolean checkUser(User user) {
        if (directoryUsers.isEmpty()) {
            return false;
        }
        for (User u : directoryUsers) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    // CONTROLLO USER CON ID UNIVOCO TELEGRAM
    public static boolean checkUserIdTelegram(String idTelegram) {
        boolean check = false;
        if (directoryUsers.isEmpty()) {
            check = false;
        }
        for (User u : directoryUsers) {
            if (u.idTelegram.equals(idTelegram)) {
                check = true;
            }
        }
        return check;
    }

    // ESTRAZIONE USER ATTRAVERSO L'ID UNIVOCO TELEGRAM
    public static User getUserIdTelegram(String idTelegram) {
        User user = null;
        for (User u : directoryUsers) {
            if (u.idTelegram.equals(idTelegram)) {
                user = u;
            }
        }
        return user;
    }


    // gestione della feed list

    public static void updateFeed() throws URISyntaxException, FeedException, IOException {
        for (String url : directoryFeeds) {
            RSSreaderController.read(url);
        }
    }

    public static boolean sourceExist(String source) {
        boolean check = false;
        for (String s : directoryFeeds) {
            if (s.equals(source)) {
                check = true;
            }
        }
        return check;
    }
}

