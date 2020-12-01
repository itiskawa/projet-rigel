package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Sun
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Sun extends CelestialObject {

    /**
     * declaration of private final attributes
     */
    private final EclipticCoordinates eclPos;
    private final float meanAnomaly;

    /**
     * public constructor
     * @param eclipticPos - of the sun
     * @param equatorialPos - of the sun
     * @param angularSize - of the sun
     * @param meanAnomaly - of the sun
     * @throws NullPointerException if ecliptic coordinates are empty
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        Objects.requireNonNull(eclipticPos, "empty ecliptic coordinates");
        this.eclPos = eclipticPos;
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * getter method for ecliptic coordinates
     * @return ecliptic position of .this
     */
    public EclipticCoordinates eclipticPos(){
        return this.eclPos;
    }

    /**
     * getter method for mean anomaly
     * @return mean anomaly of .this
     */
    public float meanAnomaly(){
        return this.meanAnomaly;
    }

}
