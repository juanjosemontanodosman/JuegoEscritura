package com.example.fastwriting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests of the game rules ({@link Game}).
 *
 * @author Integrante 1
 * @author Integrante 2
 * @author Integrante 3
 * @version 1.0
 */
class GameTest {

    /**
     * Creates a game that always returns the given word.
     *
     * @param word fixed word
     * @return a game for testing
     */
    private Game gameWithWord(String word) {
        Game game = new Game(() -> word);
        game.nextWord();
        return game;
    }

    @Test
    void validationIsExact() {
        Game game = gameWithWord("Hola, mundo!");
        assertTrue(game.isCorrect("Hola, mundo!"));
        assertFalse(game.isCorrect("hola, mundo!"));
        assertFalse(game.isCorrect("Hola mundo!"));
        assertFalse(game.isCorrect("Hola, mundo! "));
        assertFalse(game.isCorrect(""));
    }

    @Test
    void timeStartsAtTwentySeconds() {
        Game game = new Game();
        assertEquals(20, game.getTimeForLevel());
        assertEquals(1, game.getLevel());
    }

    @Test
    void timeDecreasesEveryFiveLevelsUntilTwoSeconds() {
        Game game = gameWithWord("JavaFX");
        for (int completed = 0; completed < 60; completed++) {
            int value = Math.max(2, 20 - (completed / 5) * 2);
            assertEquals(value, game.getTimeForLevel(), "completed levels: " + completed);
            game.levelUp();
        }
    }

    @Test
    void difficultyIncreaseIsDetected() {
        Game game = gameWithWord("JavaFX");
        for (int i = 0; i < 4; i++) {
            game.levelUp();
            assertFalse(game.isDifficultyIncreased());
        }
        game.levelUp();
        assertTrue(game.isDifficultyIncreased());
        assertEquals(18, game.getTimeForLevel());
    }

    @Test
    void resetGoesBackToLevelOneAndKeepsBestScore() {
        Game game = gameWithWord("JavaFX");
        game.levelUp();
        game.levelUp();
        game.reset();
        assertEquals(1, game.getLevel());
        assertEquals(20, game.getTimeForLevel());
        assertEquals(2, game.getBestScore());
    }

    @Test
    void wordBankNeverRepeatsTheSameWordTwiceInARow() {
        WordBank bank = new WordBank();
        String previous = bank.getRandomWord();
        for (int i = 0; i < 200; i++) {
            String current = bank.getRandomWord();
            assertFalse(current.equals(previous));
            previous = current;
        }
    }
}
