package com.pack.prnaboo;

public class User {
        protected String id;
        protected String password;
        protected String idTelegram;
        private boolean logged;

        public User(){}


        public User(String id, String password) {

        }
        public void setLogged(boolean logged) {
        this.logged = logged;}
    public User(String id, String password, String idTelegram) {
            this.id = id;
            this.password = password;
            this.idTelegram = idTelegram;
            this.logged = false;
        }

        public String getId() {
            return id;
        }

        public String getPassword() {
            return password;
        }

        public String getIdTelegram() {
            return idTelegram;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setIdTelegram(String idTelegram) {
            this.idTelegram = idTelegram;
        }

        // gestione accesso all'app telegram

        public boolean isLogged() {
            return logged;
        }
        public void setLogged() {
            this.logged = true;
        }

        public void setOffline(){
            this.logged = false;
        }
}
