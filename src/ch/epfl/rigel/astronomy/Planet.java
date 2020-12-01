package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Planet
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Planet extends CelestialObject {

    /**
     * public constructor
     * @param name - name of the planet
     * @param equatorialPos - equatorial position of the planet
     * @param angularSize - planet's angular size
     * @param magnitude - planet's magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
