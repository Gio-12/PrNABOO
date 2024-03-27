module com.pack.prnaboo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires json.simple;
    requires rome;
    requires telegrambots.meta;
    requires telegrambots;
    requires com.google.gson;


    opens com.pack.prnaboo to javafx.fxml, com.google.gson;
    exports com.pack.prnaboo;
}