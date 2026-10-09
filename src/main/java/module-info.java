/**
 * Module of the "Escritura Rapida" (Fast Writing) game.
 */
module com.example.fastwriting {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fastwriting.controller to javafx.fxml;
    exports com.example.fastwriting;
    exports com.example.fastwriting.controller;
    exports com.example.fastwriting.model;
    exports com.example.fastwriting.view;
}
