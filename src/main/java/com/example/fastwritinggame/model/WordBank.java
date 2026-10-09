package com.example.fastwritinggame.model;


import java.util.List;
import java.util.Random;

/**
 * Bank of words and phrases used in the game. It includes phrases with
 * capital letters, accents, spaces and punctuation, because the validation
 * of the game is exact.
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public class    WordBank implements IWordProvider {

    /** Words and phrases that can appear in the game. */
    private static final List<String> WORDS = List.of(
            "JavaFX", "Evento", "Teclado", "Ratón", "Escenario",
            "Controlador", "Interfaz", "Adaptador", "Nodo", "Escena",
            "Programación", "Univalle", "Algoritmo", "Variable", "Botón",
            "Hola, mundo!", "Escritura Rápida", "Clase interna",
            "Universidad del Valle", "El tiempo vuela.", "Java es genial",
            "¿Listo para jugar?", "Modelo Vista Controlador", "Scene Builder",
            "Manejador de eventos", "Presiona Enter", "¡Sigue así!"
    );

    /** Random number generator used to pick words. */
    private final Random random = new Random();

    /** Last word returned, used to avoid repeating the same word twice in a row. */
    private String lastWord;

    /**
     * Creates a word bank with the default words and phrases.
     */
    public WordBank() {
    }

    /**
     * Returns a random word or phrase, different from the previous one.
     *
     * @return a random word or phrase
     */
    @Override
    public String getRandomWord() {
        String word;
        do {
            word = WORDS.get(random.nextInt(WORDS.size()));
        } while (word.equals(lastWord));
        lastWord = word;
        return word;
    }
}
