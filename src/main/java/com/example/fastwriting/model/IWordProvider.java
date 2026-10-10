package com.example.fastwriting.model;

/**
 * Contract for any class that can provide random words or phrases to the game.
 * <p>
 * Thanks to this interface, {@link Game} does not depend on a specific word
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
