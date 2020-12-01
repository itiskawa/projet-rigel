package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * functional interface representing a time accelerator
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * lambda function defining this.adjust
     * @param accelFactor - factor of how fast time will be accelerated
     * @return a continuous time accelerator with desired parameters
     */
    static TimeAccelerator continuous(int accelFactor) {
        return (ZonedDateTime initialTime, long timeSinceStart) ->
                initialTime.plus(accelFactor * timeSinceStart, ChronoUnit.NANOS);
    }

    /**
     * lambda function defining this.adjust
     * @param advanceFreq - frequency of steps (number of steps per real second)
     * @param discreteStep - discrete step
     * @return a discrete time accelerator with desired parameters
     */
    static TimeAccelerator discrete(double advanceFreq, Duration discreteStep) {
        return (ZonedDateTime initialTime, long timeSinceStart) ->
                initialTime.plus(((int) (advanceFreq * 1e-9 * timeSinceStart )) * discreteStep.toNanos(), ChronoUnit.NANOS);
    }

    /**
     * single abstract method of .this
     * @param initialTime    time at beginning of the animation
     * @param timeSinceStart time elapsed since beginning of animation !(in ns)!
     * @return adjusted ZonedDateTime given the parameters
     */
    ZonedDateTime adjust(ZonedDateTime initialTime, long timeSinceStart);
}
