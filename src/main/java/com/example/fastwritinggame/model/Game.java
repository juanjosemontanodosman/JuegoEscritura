package com.example.fastwritinggame.model;

/**
 * Logic of the "Escritura Rapida" game: current level, current word,
 * exact validation of the answer and time available per level.
 * <p>
 * Rules:
 * </p>
 * <ul>
 *     <li>The game starts at level 1 with 20 seconds per level.</li>
 *     <li>Every 5 completed levels the time decreases by 2 seconds,
 *     with a minimum of 2 seconds per level.</li>
 *     <li>The answer must be exactly equal to the word (letters, spaces,
 *     capital letters and punctuation).</li>
 *     <li>The game is won when {@link #MAX_LEVEL} levels are completed.</li>
 * </ul>
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public class Game {

    /** Initial time at the first level, in seconds. */
    public static final int INITIAL_TIME = 20;

    /** Minimum time at any level, in seconds. */
    public static final int MIN_TIME = 2;

    /** Seconds subtracted each time the difficulty increases. */
    public static final int TIME_DECREASE = 2;

    /** Number of completed levels needed to increase the difficulty. */
    public static final int LEVELS_PER_DECREASE = 5;

    /** Number of levels needed to win the game. */
    public static final int MAX_LEVEL = 50;

    /** Source of the random words. */
    private final IWordProvider wordProvider;

    /** Current level (starts at 1). */
    private int level;

    /** Word the player must type in the current level. */
    private String currentWord;

    /** Highest number of levels completed in this session. */
    private int bestScore;

    /**
     * Creates a game that uses the given word provider.
     *
     * @param wordProvider source of the random words
     */
    public Game(IWordProvider wordProvider) {
        this.wordProvider = wordProvider;
        this.level = 1;
    }

    /**
     * Creates a game that uses the default {@link WordBank}.
     */
    public Game() {
        this(new WordBank());
    }

    /**
     * Picks a new random word for the current level.
     *
     * @return the new word the player must type
     */
    public String nextWord() {
        currentWord = wordProvider.getRandomWord();
        return currentWord;
    }

    /**
     * Checks if the answer is exactly equal to the current word.
     * The comparison takes into account letters, spaces, capital letters
     * and punctuation (no trim, no ignore case).
     *
     * @param answer text typed by the player
     * @return {@code true} if the answer is exactly equal to the current word
     */
    public boolean isCorrect(String answer) {
        return currentWord != null && currentWord.equals(answer);
    }

    /**
     * Checks if the answer is a correct beginning of the current word.
     * Used to give live visual feedback while the player types.
     *
     * @param answer text typed so far
     * @return {@code true} if the current word starts with the answer
     */
    public boolean isCorrectPrefix(String answer) {
        return currentWord != null && answer != null && currentWord.startsWith(answer);
    }

    /**
     * Advances to the next level and updates the best score.
     */
    public void levelUp() {
        level++;
        bestScore = Math.max(bestScore, getLevelsCompleted());
    }

    /**
     * Calculates the time available for the current level:
     * 20 seconds minus 2 seconds every 5 completed levels, minimum 2 seconds.
     *
     * @return time for the current level, in seconds
     */
    public int getTimeForLevel() {
        return calculateTime(getLevelsCompleted());
    }

    /**
     * Indicates if the difficulty has just increased, that is, if the
     * current level has less time than the previous level.
     *
     * @return {@code true} if the time per level has just decreased
     */
    public boolean isDifficultyIncreased() {
        int completed = getLevelsCompleted();
        return completed > 0 && calculateTime(completed) < calculateTime(completed - 1);
    }

    /**
     * Calculates the time per level for a given number of completed levels.
     *
     * @param levelsCompleted number of levels completed
     * @return time in seconds, never less than {@link #MIN_TIME}
     */
    private static int calculateTime(int levelsCompleted) {
        int decreases = levelsCompleted / LEVELS_PER_DECREASE;
        return Math.max(MIN_TIME, INITIAL_TIME - decreases * TIME_DECREASE);
    }

    /**
     * Indicates if the player completed all the levels.
     *
     * @return {@code true} if the maximum level was reached
     */
    public boolean isMaxLevelReached() {
        return getLevelsCompleted() >= MAX_LEVEL;
    }

    /**
     * Restarts the game from level 1. The best score is kept.
     */
    public void reset() {
        level = 1;
        currentWord = null;
    }

    /**
     * Returns the current level.
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the number of levels completed (correct answers in a row).
     *
     * @return number of levels completed
     */
    public int getLevelsCompleted() {
        return level - 1;
    }

    /**
     * Returns the word of the current level.
     *
     * @return the current word, or {@code null} if no word was picked yet
     */
    public String getCurrentWord() {
        return currentWord;
    }

    /**
     * Returns the highest number of levels completed in this session.
     *
     * @return the best score
     */
    public int getBestScore() {
        return bestScore;
    }
}
