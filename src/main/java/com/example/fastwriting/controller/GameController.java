package com.example.fastwriting.controller;

import com.example.fastwriting.model.Game;
import com.example.fastwriting.model.GameTimer;
import com.example.fastwriting.model.TimerAdapter;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Controller of the game view ({@code game-view.fxml}).
 * <p>
 * It connects the view with the model and handles all the events of the game:
 * </p>
 * <ul>
 *     <li><b>Keyboard events:</b> Enter validates the answer, Escape clears it,
 *     and every key released gives live feedback while typing.</li>
 *     <li><b>Mouse events:</b> clicks on the buttons and hover effects.</li>
 *     <li><b>Timer events:</b> received through an anonymous class that
 *     extends the adapter {@link TimerAdapter}.</li>
 * </ul>
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public class GameController {

    /** Seconds left from which the timer is shown as a warning. */
    private static final int WARNING_SECONDS = 5;

    /** Pause between a correct answer and the next level. */
    private static final Duration NEXT_LEVEL_DELAY = Duration.millis(900);

    /** Label that shows the current level. */
    @FXML
    private Label levelLabel;

    /** Label that shows the remaining seconds. */
    @FXML
    private Label timeLabel;

    /** Label that shows the best score of the session. */
    @FXML
    private Label bestScoreLabel;

    /** Bar that shows the remaining time visually. */
    @FXML
    private ProgressBar timeProgressBar;

    /** Label that shows the word the player must type. */
    @FXML
    private Label wordLabel;

    /** Text field where the player types the answer. */
    @FXML
    private TextField answerField;

    /** Label that shows feedback messages (success, error, warning). */
    @FXML
    private Label messageLabel;

    /** Button that validates the answer. */
    @FXML
    private Button validateButton;

    /** Pane shown over the game for the welcome screen and the final summary. */
    @FXML
    private StackPane overlayPane;

    /** Title of the overlay. */
    @FXML
    private Label overlayTitleLabel;

    /** Text of the overlay (instructions or summary). */
    @FXML
    private Label overlayContentLabel;

    /** Button of the overlay ("Comenzar" or "Reiniciar"). */
    @FXML
    private Button overlayButton;

    /** Game logic (model). */
    private final Game game = new Game();

    /** Countdown timer of each level. */
    private GameTimer gameTimer;

    /** Total seconds of the current level, used to update the progress bar. */
    private int totalSeconds;

    /** Indicates if a level is being played (the player can type and validate). */
    private boolean playing;

    /**
     * Creates the controller. The view components are injected later by
     * the {@code FXMLLoader}, and the setup is done in {@link #initialize()}.
     */
    public GameController() {
    }

    /**
     * Initializes the controller after the FXML file is loaded.
     * Creates the timer and registers the keyboard and mouse handlers.
     */
    @FXML
    public void initialize() {
        // Anonymous class that extends the adapter: only the needed methods are overridden
        gameTimer = new GameTimer(new TimerAdapter() {
            @Override
            public void onStart(int seconds) {
                totalSeconds = seconds;
                updateTimeDisplay(seconds);
            }

            @Override
            public void onTick(int secondsLeft) {
                updateTimeDisplay(secondsLeft);
            }

            @Override
            public void onTimeUp() {
                handleTimeUp();
            }
        });

        // Keyboard events (inner classes)
        answerField.setOnKeyPressed(new AnswerKeyHandler());
        answerField.setOnKeyReleased(new TypingFeedbackHandler());
        overlayPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                startGame();
            }
        });

        // Mouse events (one inner class reused by every button)
        HoverHandler hoverHandler = new HoverHandler();
        registerHover(validateButton, hoverHandler);
        registerHover(overlayButton, hoverHandler);

        showWelcome();
    }

    /**
     * Handles the click on the "Validar" button.
     */
    @FXML
    private void onValidateButtonClick() {
        validateAnswer();
    }

    /**
     * Handles the click on the overlay button ("Comenzar" or "Reiniciar").
     */
    @FXML
    private void onOverlayButtonClick() {
        startGame();
    }

    /**
     * Starts a new game from level 1.
     */
    private void startGame() {
        game.reset();
        overlayPane.setVisible(false);
        bestScoreLabel.setText("Récord: " + game.getBestScore());
        startLevel();
    }

    /**
     * Starts the current level: shows a new random word, clears the text
     * field and starts the countdown.
     */
    private void startLevel() {
        playing = true;
        levelLabel.setText("Nivel " + game.getLevel());
        wordLabel.setText(game.nextWord());

        answerField.clear();
        answerField.setDisable(false);
        setFieldState(null);
        validateButton.setDisable(false);
        answerField.requestFocus();

        if (game.isDifficultyIncreased()) {
            showMessage("¡Dificultad aumentada! Ahora tienes "
                    + game.getTimeForLevel() + " s por nivel", "message-warning");
        } else {
            showMessage("Escribe la palabra exactamente como aparece", "message-info");
        }

        gameTimer.start(game.getTimeForLevel());
    }

    /**
     * Validates the answer when the player presses Enter or the button.
     * A wrong answer does not lose the level: the player can try again
     * while there is time left.
     */
    private void validateAnswer() {
        if (!playing) {
            return;
        }
        String answer = answerField.getText();

        if (answer.isEmpty()) {
            showMessage("Escribe la palabra antes de validar", "message-error");
            shake(answerField);
        } else if (game.isCorrect(answer)) {
            handleLevelPassed();
        } else {
            showMessage("✘ Incorrecto. Revisa mayúsculas, tildes, espacios y puntuación",
                    "message-error");
            setFieldState("field-error");
            shake(answerField);
        }
    }

    /**
     * Called when the time is over: the answer is validated automatically.
     * If it is wrong or empty, the game ends.
     */
    private void handleTimeUp() {
        if (!playing) {
            return;
        }
        String answer = answerField.getText();

        if (game.isCorrect(answer)) {
            handleLevelPassed();
        } else if (answer.isEmpty()) {
            endGame(false, "¡Tiempo agotado! No escribiste ninguna respuesta.", 0);
        } else {
            endGame(false, "¡Tiempo agotado! La respuesta escrita era incorrecta.", 0);
        }
    }

    /**
     * Called when the player writes the word correctly: stops the timer,
     * shows a positive message and goes to the next level.
     */
    private void handleLevelPassed() {
        playing = false;
        int secondsLeft = gameTimer.getSecondsLeft();
        gameTimer.stop();
        game.levelUp();

        answerField.setDisable(true);
        validateButton.setDisable(true);
        setFieldState("field-success");
        showMessage("✔ ¡Correcto! Nivel superado", "message-success");
        bestScoreLabel.setText("Récord: " + game.getBestScore());

        if (game.isMaxLevelReached()) {
            endGame(true, "¡Completaste los " + Game.MAX_LEVEL + " niveles!", secondsLeft);
            return;
        }

        PauseTransition pause = new PauseTransition(NEXT_LEVEL_DELAY);
        pause.setOnFinished(event -> startLevel());
        pause.play();
    }

    /**
     * Ends the game and shows the summary with the option to restart.
     *
     * @param won         {@code true} if the player reached the maximum level
     * @param reason      reason why the game ended
     * @param secondsLeft remaining seconds when the game ended
     */
    private void endGame(boolean won, String reason, int secondsLeft) {
        playing = false;
        gameTimer.stop();
        answerField.setDisable(true);
        validateButton.setDisable(true);

        if (!won) {
            setFieldState("field-error");
            showMessage(reason, "message-error");
        }

        overlayTitleLabel.setText(won ? "¡Ganaste!" : "Fin de la partida");
        overlayContentLabel.setText(reason
                + "\n\nNiveles completados: " + game.getLevelsCompleted()
                + "\nTiempo restante: " + Math.max(0, secondsLeft) + " s"
                + "\nÚltima palabra: \"" + game.getCurrentWord() + "\""
                + "\nRécord de la sesión: " + game.getBestScore() + " niveles");
        overlayButton.setText("Reiniciar");
        showOverlay();
    }

    /**
     * Shows the welcome screen with the instructions of the game.
     */
    private void showWelcome() {
        playing = false;
        answerField.setDisable(true);
        validateButton.setDisable(true);
        levelLabel.setText("Nivel 1");
        timeLabel.setText(Game.INITIAL_TIME + " s");
        bestScoreLabel.setText("Récord: 0");
        wordLabel.setText("¿Listo?");

        overlayTitleLabel.setText("Escritura Rápida");
        overlayContentLabel.setText("Escribe la palabra que aparece en pantalla\n"
                + "antes de que se acabe el tiempo.\n\n"
                + "• Presiona Enter o el botón Validar para comprobar.\n"
                + "• Debe ser exacta: mayúsculas, tildes y puntuación.\n"
                + "• Cada 5 niveles tendrás 2 segundos menos.\n"
                + "• Si el tiempo llega a cero con un error, pierdes.");
        overlayButton.setText("Comenzar");
        showOverlay();
    }

    /**
     * Shows the overlay and gives the focus to its button, so the
     * player can continue by pressing Enter.
     */
    private void showOverlay() {
        overlayPane.setVisible(true);
        overlayButton.requestFocus();
    }

    /**
     * Updates the time label and the progress bar.
     *
     * @param secondsLeft remaining seconds
     */
    private void updateTimeDisplay(int secondsLeft) {
        timeLabel.setText(Math.max(0, secondsLeft) + " s");
        timeProgressBar.setProgress(totalSeconds == 0 ? 0 : (double) secondsLeft / totalSeconds);

        boolean warning = secondsLeft <= WARNING_SECONDS;
        toggleStyleClass(timeLabel, "time-warning", warning);
        toggleStyleClass(timeProgressBar, "progress-warning", warning);
    }

    /**
     * Shows a message to the player with the given style.
     *
     * @param text       message to show
     * @param styleClass CSS class of the message (success, error, warning, info)
     */
    private void showMessage(String text, String styleClass) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll(
                "message-success", "message-error", "message-warning", "message-info");
        messageLabel.getStyleClass().add(styleClass);
    }

    /**
     * Changes the visual state (border color) of the text field.
     *
     * @param styleClass CSS class to apply, or {@code null} for the normal state
     */
    private void setFieldState(String styleClass) {
        answerField.getStyleClass().removeAll("field-success", "field-error", "field-typing");
        if (styleClass != null) {
            answerField.getStyleClass().add(styleClass);
        }
    }

    /**
     * Adds or removes a CSS class from a node.
     *
     * @param node       node to change
     * @param styleClass CSS class
     * @param add        {@code true} to add the class, {@code false} to remove it
     */
    private void toggleStyleClass(Node node, String styleClass, boolean add) {
        node.getStyleClass().remove(styleClass);
        if (add) {
            node.getStyleClass().add(styleClass);
        }
    }

    /**
     * Plays a short shake animation, used when the answer is wrong.
     *
     * @param node node to shake
     */
    private void shake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0);
        shake.setByX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setOnFinished(event -> node.setTranslateX(0));
        shake.play();
    }

    /**
     * Registers the hover handler on a node for the mouse entered and
     * mouse exited events.
     *
     * @param node    node that receives the events
     * @param handler handler of the mouse events
     */
    private void registerHover(Node node, EventHandler<MouseEvent> handler) {
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, handler);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, handler);
    }

    /**
     * Inner class that handles the keys pressed in the answer field:
     * Enter validates the answer and Escape clears it.
     */
    private class AnswerKeyHandler implements EventHandler<KeyEvent> {

        /** Creates the handler. */
        AnswerKeyHandler() {
        }

        /**
         * Handles the key pressed event.
         *
         * @param event the keyboard event
         */
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                validateAnswer();
            } else if (event.getCode() == KeyCode.ESCAPE && playing) {
                answerField.clear();
                setFieldState(null);
            }
        }
    }

    /**
     * Inner class that gives live feedback while the player types:
     * the border of the field is blue while the text matches the beginning
     * of the word, and red as soon as there is a mistake.
     */
    private class TypingFeedbackHandler implements EventHandler<KeyEvent> {

        /** Creates the handler. */
        TypingFeedbackHandler() {
        }

        /**
         * Handles the key released event.
         *
         * @param event the keyboard event
         */
        @Override
        public void handle(KeyEvent event) {
            if (!playing || event.getCode() == KeyCode.ENTER) {
                return;
            }
            String text = answerField.getText();
            if (text.isEmpty()) {
                setFieldState(null);
            } else if (game.isCorrectPrefix(text)) {
                setFieldState("field-typing");
            } else {
                setFieldState("field-error");
            }
        }
    }

    /**
     * Inner class that handles the mouse hover on the buttons:
     * the button grows a little and the cursor changes to a hand.
     */
    private static class HoverHandler implements EventHandler<MouseEvent> {

        /** Scale applied to the node when the mouse is over it. */
        private static final double HOVER_SCALE = 1.06;

        /** Creates the handler. */
        HoverHandler() {
        }

        /**
         * Handles the mouse entered and mouse exited events.
         *
         * @param event the mouse event
         */
        @Override
        public void handle(MouseEvent event) {
            Node node = (Node) event.getSource();
            boolean entered = event.getEventType() == MouseEvent.MOUSE_ENTERED;
            node.setScaleX(entered ? HOVER_SCALE : 1);
            node.setScaleY(entered ? HOVER_SCALE : 1);
            node.setCursor(entered ? Cursor.HAND : Cursor.DEFAULT);
        }
    }
}
