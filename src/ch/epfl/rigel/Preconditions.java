package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * argument validation methods
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Preconditions {

    /**
     * private default constructor
     */
    private Preconditions() {}

    /**
     * validates argument if it is true
     * @param isTrue - boolean value
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException("Argument does not comply to with the requirements");
        }
    }

    /**
     * validates if the value is contained in the interval
     *
     * @param interval - interval searched
     * @param value - value to be checked
     * @return value if contained
     * @throws IllegalArgumentException if value is not in interval
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}
