package com.example.fastwritinggame.model;

/**
 * Listener of the events produced by the timer.
 * <p>
 * Any class that wants to react to the timer must implement this interface,
 * or extend {@link TimerAdapter} to override only the methods it needs.
 * </p>
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public interface ITimerListener {

    /**
     * Called when the timer starts.
     *
     * @param totalSeconds total seconds of the countdown
     */
    void onStart(int totalSeconds);

    /**
     * Called every second while the timer is running.
     *
     * @param secondsLeft remaining seconds
     */
    void onTick(int secondsLeft);

    /**
     * Called when the timer is stopped before reaching zero.
     *
     * @param secondsLeft remaining seconds when it was stopped
     */
    void onStop(int secondsLeft);

    /**
     * Called when the countdown reaches zero.
     */
    void onTimeUp();
}
