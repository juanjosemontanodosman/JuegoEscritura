package com.example.fastwriting.model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Countdown timer of each level. It uses a JavaFX {@link Timeline} that
 * runs on the JavaFX Application Thread, so the listener can update the
 * user interface safely.
 *
 * @author Juan José Montaño Dosman
 * @author Anthony Alejandro Mora
 * @author Roiban Alirio Rosales Bastidas
 * @version 1.0
 */
public class GameTimer {

    /** Animation that fires once every second. */
    private final Timeline timeline;

    /** Object notified about the timer events. */
    private final ITimerListener listener;

    /** Remaining seconds of the countdown. */
    private int secondsLeft;

    /**
     * Creates a timer that notifies the given listener.
     *
     * @param listener object that receives the timer events
     */
    public GameTimer(ITimerListener listener) {
        this.listener = listener;
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> tick()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Starts (or restarts) the countdown.
     *
     * @param seconds seconds of the countdown
     */
    public void start(int seconds) {
        timeline.stop();
        secondsLeft = seconds;
        listener.onStart(seconds);
        timeline.playFromStart();
    }

    /**
     * Stops the countdown before it reaches zero.
     */
    public void stop() {
        if (isRunning()) {
            timeline.stop();
            listener.onStop(secondsLeft);
        }
    }

    /**
     * Indicates if the countdown is running.
     *
     * @return {@code true} if the timer is running
     */
    public boolean isRunning() {
        return timeline.getStatus() == Animation.Status.RUNNING;
    }

    /**
     * Returns the remaining seconds.
     *
     * @return remaining seconds
     */
    public int getSecondsLeft() {
        return secondsLeft;
    }

    /**
     * Subtracts one second and notifies the listener.
     * When the time reaches zero, the timer stops and notifies {@code onTimeUp}.
     */
    private void tick() {
        secondsLeft--;
        listener.onTick(secondsLeft);
        if (secondsLeft <= 0) {
            timeline.stop();
            listener.onTimeUp();
        }
    }
}
