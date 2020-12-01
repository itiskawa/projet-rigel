package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Horizontal Coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 *
 */
public final class HorizontalCoordinates extends SphericalCoordinates{

    private final static RightOpenInterval AZ_LIMIT = RightOpenInterval.of(0, Angle.ofDeg(360));
    private final static ClosedInterval ALT_LIMIT = ClosedInterval.symmetric(Angle.ofDeg(180));

    /**
     * private constructor
     * @param lon - corresponds to azimuth
     * @param lat - corresponds to altitude
     */
    private HorizontalCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     *
     * static constructor method, uses radians
     * @param az - azimuth in radians
     * @param alt - altitude in radians
     * @return corresponding HorizontalCoordinates in radians
     * @throws IllegalArgumentException - if az (resp. alt) is not in [0,2PI[ (resp.[-PI,PI])
     */
    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkInInterval(ALT_LIMIT, alt);
        Preconditions.checkInInterval(AZ_LIMIT, az);
        return new HorizontalCoordinates(az, alt);
    }

    /**
     *
     * static constructor method, uses degrees as parameters
     * @param azDeg - azimuth in degrees
     * @param altDeg - altitude in degrees
     * @return corresponding HorizontalCoordinates
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        return HorizontalCoordinates.of(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * getter method for az in radians
     * @return super.lon() (equivalent to az)
     */
    public double az(){
        return super.lon();
    }

    /**
     * getter method for azDeg
     * @return super.lonDeg() (equivalent to azDeg)
     */
    public double azDeg(){
        return super.lonDeg();
    }

    /**
     * getter method for alt in radians
     * @return super.lat() (equivalent to alt)
     */
    public double alt(){
        return super.lat();
    }

    /**
     * getter method for altDeg
     * @return super.latDeg() (equivalent to altDeg)
     */
    public double altDeg(){
        return super.latDeg();
    }

    /**
     * returns the corresponding cardinal (as a String) to .this
     * @param n - string representing north
     * @param e - string representing east
     * @param s - string representing south
     * @param w - string representing west
     * @return one (or concatenation of two) of the parameters
     */
    public String azOctantName(String n, String e, String s, String w){
        double frameNumber = this.lonDeg() + 22.5;
        frameNumber /= 45.0;
        int octant = (int)(frameNumber);

        switch(octant){
            case(1):
                return n+e;
            case(2):
                return e;
            case(3):
                return s+e;
            case(4):
                return s;
            case(5):
                return s+w;
            case(6):
                return w;
            case(7):
                return n+w;
            default:
                return n;
        }
    }

    /**
     * calculates the angular distances between the given coordinates (that) and current coordinates (.this)
     * @param that - horizontal coordinates to be compared to
     * @return angular distance (double)
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        double thatAz = that.az();
        double thatAlt = that.alt();

        return (Math.acos(Math.sin(this.alt()) * Math.sin(thatAlt)
                + Math.cos(thatAlt) * Math.cos(this.alt()) * Math.cos(thatAz - this.az())));
    }

    /**
     * redefinition of toString method of Object class
     * @return text representation of HorizontalCoordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)",
                this.azDeg(), this.altDeg());
    }
}