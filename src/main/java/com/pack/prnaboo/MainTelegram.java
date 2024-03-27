package com.pack.prnaboo;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainTelegram {
    public static void main(String[] args) {
        TelegramBotsApi botsApi;

        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}