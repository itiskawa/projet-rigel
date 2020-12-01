package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * A star
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Star extends CelestialObject {

    /**
     * declaration of private and final attributes, names are purposely self-descriptive
     */
    private final int hipparcosId;
    private final int colorTemperature;
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    /**
     * public constructor
     * @param hipparcosId - the hipparcos index
     * @param name - of the star
     * @param equatorialPos - equatorial position
     * @param magnitude - of the star
     * @param colorIndex - float
     * @throws IllegalArgumentException if colorIndex is not in [-0.5, 5.5] or if hipparcosId is negative
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0f, magnitude);

        Preconditions.checkInInterval(COLOR_INTERVAL, colorIndex);
        Preconditions.checkArgument((hipparcosId >= 0));

        this.hipparcosId = hipparcosId;

        //compute the color temperature
        colorTemperature = (int) (4600 * (1.0 / ((0.92 * colorIndex) + 1.7) + 1.0 / (0.92 * colorIndex + 0.62)));
    }

    /**
     * getter for hippparcos id
     * @return the hipparcos id
     */
    public int hipparcosId(){
        return this.hipparcosId;
    }


    /**
     * gives the color temperature using the colorIndex of the star
     * @return the color temperature
     */
    public int colorTemperature(){
        return colorTemperature;
    }
}
