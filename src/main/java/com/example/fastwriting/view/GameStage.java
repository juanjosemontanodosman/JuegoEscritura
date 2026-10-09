package com.example.fastwriting.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window (stage) of the game. It loads the {@code game-view.fxml} file,
 * creates the scene and shows it.
 *
 * @author Integrante 1
 * @author Integrante 2
 * @author Integrante 3
 * @version 1.0
 */
public class GameStage extends Stage {

    /** Width of the window in pixels. */
    private static final int WIDTH = 720;

    /** Height of the window in pixels. */
    private static final int HEIGHT = 520;

    /**
     * Creates and shows the game window.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/fastwriting/game-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        setTitle("Escritura Rápida");
        setScene(scene);
        setResizable(false);
        show();
    }
}
