package com.example.fastwritinggame.model;

/**
 * Contract for any class that can provide random words or phrases to the game.
 * <p>
 * Thanks to this interface, the word set does not depend on a specific
 * source: a different implementation (for example, words read from a file)
 * could be used without changing the game logic.
 * </p>
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public interface IWordProvider {

    /**
     * Returns a random word or phrase.
     *
     * @return a random word or phrase, never {@code null}
     */
    String getRandomWord();
}

