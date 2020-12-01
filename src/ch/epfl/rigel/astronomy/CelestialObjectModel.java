package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Celestial Object Model
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public interface CelestialObjectModel<O> {

    /**
     * public abstract method for "model" of Object O
     * @param daysSinceJ2010 days (can be negative) since J2010
     * @param eclToEq ecliptic to equatorial coordinates conversion
     * @return Object of type O at given time
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclToEq);
}