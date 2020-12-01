package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Geographic coordinates
 *
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class GeographicCoordinates extends SphericalCoordinates{

    private final static RightOpenInterval LON_LIMIT = RightOpenInterval.symmetric(360);
    private final static ClosedInterval LAT_LIMIT = ClosedInterval.symmetric(180);
    /**
     * private constructor
     * @param lon - longitude (radians)
     * @param lat - latitude (radians)
     */
    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * static constructor method, uses degrees
     * @param lonDeg - in degrees
     * @param latDeg - in degrees
     * @return corresponding GeographicCoordinates in degrees
     * @throws IllegalArgumentException if lonDeg (resp. latDeg) is not in [-180, 180[ (resp. [-90,90])
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkInInterval(LAT_LIMIT, latDeg);
        Preconditions.checkInInterval(LON_LIMIT, lonDeg);
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * checks if a given value in degrees is a valid longitude
     * @param lonDeg - longitude in degrees
     * @return true iff the parameter is between -180° & 180°(not included)
     */
    public static boolean isValidLonDeg(double lonDeg){
        return LON_LIMIT.contains(lonDeg);
    }

    /**
     * checks if a given value in degrees is a valid latitude
     * @param latDeg -  latitude in degrees
     * @return true iff the parameter is in [-90°, 90°]
     */
    public static boolean isValidLatDeg(double latDeg){
        return LAT_LIMIT.contains(latDeg);
    }

    /**
     * getter for longitude in radians
     * @return super.lon()
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * getter for longitude in degrees
     * @return super.lonDeg()
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * getter for latitude in radians
     * @return super.lat()
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * getter for latitude in degrees
     * @return super.latDeg()
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * redefinition of toString method of Object class
     * @return text representation of GeographicCoordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)",
                this.lonDeg(), this.latDeg());
    }
}