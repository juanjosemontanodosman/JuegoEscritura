/**
 * Module of the "Escritura Rapida" (Fast Writing) game.
 */
module com.example.fastwritinggame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fastwritinggame.controller to javafx.fxml;
    exports com.example.fastwritinggame;
    exports com.example.fastwritinggame.controller;
    exports com.example.fastwritinggame.model;
    exports com.example.fastwritinggame.view;
}