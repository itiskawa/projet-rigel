package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Ecliptic Coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class EclipticCoordinates extends SphericalCoordinates{

    private final static RightOpenInterval LON_LIMIT= RightOpenInterval.of(0, Angle.ofDeg(360));
    private final static ClosedInterval LAT_LIMIT = ClosedInterval.symmetric(Angle.ofDeg(180));

    /**
     * private constructor
     * @param lon - longitude
     * @param lat - latitude
     */
    private EclipticCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * static constructor method, in radians
     * @param lon - longitude in radians
     * @param lat - latitude in radians
     * @return corresponding EclipticCoordinates in radians
     * @throws IllegalArgumentException if lon (resp. lat) is not in [0, 2*PI] (resp. [-PI, PI[)
     */
    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkInInterval(LON_LIMIT, lon);
        Preconditions.checkInInterval(LAT_LIMIT, lat);
        return new EclipticCoordinates(lon, lat);
    }

    /**
     * getter method for lon in radians
     * @return super.lon()
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * getter method for lonDeg (lon in degrees)
     * @return super.lonDeg()
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * getter method for lat in radians
     * @return super.lat()
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * getter method for latDeg (lat in degrees)
     * @return super.latDeg()
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * redefinition of toString method of Object class
     * @return text representation of EclipticCoordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", this.lonDeg(), this.latDeg());
    }
}
