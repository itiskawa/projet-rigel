package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * time animator class
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean observedInstant;
    private final ObjectProperty<TimeAccelerator> timeAccelerator;
    private final SimpleBooleanProperty running;
    private boolean firstHandle;
    private long firstHandleTime;
    private ZonedDateTime firstZDT;

    /**
     * public constructor
     * @param observedInstant - the current time (dateTime), represented as a JavaFX bean
     * sets running parameter to false, as it isn't supposed to start at initialization
     */
    public TimeAnimator(DateTimeBean observedInstant) {
        this.observedInstant = observedInstant;
        this.timeAccelerator = new SimpleObjectProperty<>();
        running = new SimpleBooleanProperty(false);
    }

    /**
     * handle method, its function is to run the animation by modifying the bean.
     * sets the time to "now" by adjusting this' time accelerator
     * @param now - how many nanoseconds passed since start of animation
     */
    @Override
    public void handle(long now) {
        if (firstHandle) {
            firstHandle = false;
            firstHandleTime = now;
            firstZDT = observedInstant.getZonedDateTime();
        }
        else {
            long delta = now - firstHandleTime;
            observedInstant.setZonedDateTime(timeAccelerator.get().adjust(firstZDT, delta));
        }
    }

    /**
     * redefinition of AnimationTimer.start(), calls it first, then starts the animation by setting running attribute to true
     */
    @Override
    public void start() {
        super.start();
        firstHandle = true;
        running.set(true);
    }

    /**
     * redefinition of AnimationTimer.stop(), calls it first, then sets this.running to false
     * stops the animation
     */
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    /**
     * getter method for the property containing the value true if the animator & false otherwise
     * @return value of running
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }

    /**
     * getter method for the boolean value contained in the running property
     * @return - boolean value wrapped in running
     */
    public boolean getRunning() {
        return running.get();
    }

    /**
     * getter method for the property containing the TimeAccelerator
     * @return - the property containing the timeAccelerator
     */
    public ObjectProperty<TimeAccelerator> timeAcceleratorProperty() {
        return timeAccelerator;
    }

    /**
     * sets the TimeAccelerator we wish to use for this animation in this.timeAccelerator
     * @param timeAcc - the desired TimeAccelerator
     */
    public void setAccelerator(TimeAccelerator timeAcc) {
        this.timeAccelerator.set(timeAcc);
    }
}
