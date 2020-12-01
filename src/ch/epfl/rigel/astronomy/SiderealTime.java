package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Sidereal Time
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class SiderealTime {

    private final static Polynomial S0_FORMAT = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private final static Polynomial S1_FORMAT = Polynomial.of(1.002737909, 0);
    private final static int MILLIS_IN_AN_HOUR = 3600000;

    /**
     * private default constructor
     */
    private SiderealTime(){}

    /**
     * gives the greenwich sidereal time in radians corresponding to the given zoned date & time
     * @param when - time & timezone in any given timezone
     * @return corresponding sidereal time at greenwich in radians
     */
    public static double greenwich(ZonedDateTime when){
        ZonedDateTime inGreenwichZone = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime atMidnight = inGreenwichZone.truncatedTo(ChronoUnit.DAYS);

        double T = Epoch.J2000.julianCenturiesUntil(atMidnight);
        double t = (double)(atMidnight.until(when, ChronoUnit.MILLIS))/ MILLIS_IN_AN_HOUR;

        double s0 = S0_FORMAT.at(T);
        double s1 = S1_FORMAT.at(t);

        double sg = s0 + s1;
        sg = Angle.ofHr(sg);

        return Angle.normalizePositive(sg);
    }

    /**
     * gives the sidereal time at the specified location in radians, at the given
     * instant specified by the zoned date & time
     * @param when - time requested & its timezone
     * @param where - location wanted for the requested moment
     * @return corresponding sidereal time at the specified location in radians
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        double localRad = (greenwich(when) + where.lon());
        return Angle.normalizePositive(localRad);
    }
}
