package com.example.fastwritinggame.model;

/**
 * Adapter class of {@link ITimerListener}.
 * <p>
 * It provides empty implementations for all the methods of the interface,
 * so a class that extends it only needs to override the methods it is
 * interested in (the same idea as {@code MouseAdapter} in Java).
 * </p>
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public abstract class TimerAdapter implements ITimerListener {

    /**
     * Creates the adapter. Used by the subclasses.
     */
    protected TimerAdapter() {
    }

    /**
     * Empty implementation.
     *
     * @param totalSeconds total seconds of the countdown
     */
    @Override
    public void onStart(int totalSeconds) {
    }

    /**
     * Empty implementation.
     *
     * @param secondsLeft remaining seconds
     */
    @Override
    public void onTick(int secondsLeft) {
    }

    /**
     * Empty implementation.
     *
     * @param secondsLeft remaining seconds when it was stopped
     */
    @Override
    public void onStop(int secondsLeft) {
    }

    /**
     * Empty implementation.
     */
    @Override
    public void onTimeUp() {
    }
}
