package com.example.fastwriting.model;

/**
 * Listener of the events produced by {@link GameTimer}.
 * <p>
 * Any class that wants to react to the timer must implement this interface,
 * or extend {@link TimerAdapter} to override only the methods it needs.
 * </p>
 *
 * @author Integrante 1
 * @author Integrante 2
 * @author Integrante 3
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
