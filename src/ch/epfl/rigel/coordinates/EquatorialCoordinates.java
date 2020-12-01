package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 *
 * Equatorial Coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class EquatorialCoordinates extends SphericalCoordinates{

    private final static RightOpenInterval RA_LIMIT = RightOpenInterval.of(0, Angle.ofDeg(360));
    private final static ClosedInterval DEC_LIMIT = ClosedInterval.symmetric(Angle.ofDeg(180));

    /**
     * private constructor
     * @param lon - right ascension
     * @param lat - declination
     */
    private EquatorialCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * static constructor method, in radians
     * @param ra - right ascension in radians
     * @param dec - declination in radians
     * @return corresponding EquatorialCoordinates in radians
     * @throws IllegalArgumentException if ra (resp. dec) is not in [0, 2PI] (resp. [-PI, PI[)
     */
    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkInInterval(RA_LIMIT, ra);
        Preconditions.checkInInterval(DEC_LIMIT, dec);
        return new EquatorialCoordinates(ra, dec);
    }

    /**
     * getter method for ra in radians
     * @return super.lon() (equivalent to ra)
     */
    public double ra(){
        return super.lon();
    }

    /**
     * getter method for raDeg
     * @return super.lonDeg() (equivalent to raDeg)
     */
    public double raDeg(){
        return super.lonDeg();
    }

    /**
     * getter method for ra in hours
     * @return super.lon() in hours using Angle.toHr() method
     */
    public double raHr(){
        return Angle.toHr(super.lon());
    }

    /**
     * getter method for dec in radians
     * @return super.lat() (equivalent to dec)
     */
    public double dec(){
        return super.lat();
    }

    /**
     * getter method for decDeg
     * @return super.latDeg() (equivalent to decDeg)
     */
    public double decDeg(){
        return super.latDeg();
    }

    /**
     * redefinition of toString method of Object class
     * @return text representation of EquatorialCoordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)",
                this.raHr(), this.decDeg());
    }
}
