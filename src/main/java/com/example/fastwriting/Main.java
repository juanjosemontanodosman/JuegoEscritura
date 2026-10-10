package com.example.fastwriting;

import com.example.fastwriting.view.GameStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point of the "Escritura Rapida" (Fast Writing) game.
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Creates the application. It is called by the JavaFX runtime.
     */
    public Main() {
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Opens the game window.
     *
     * @param primaryStage the primary stage provided by JavaFX (not used,
     *                     the game uses its own {@link GameStage})
     * @throws IOException if the FXML file of the game cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        new GameStage();
    }
}
