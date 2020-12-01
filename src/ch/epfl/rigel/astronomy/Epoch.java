package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.*;

/**
 * Epoch enumeration (time references)
 *
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum Epoch {

    /**
     * J2000 : first of January 2000 at 12h00 UTC
     *
     * J2010 : "0th" January 2010 (31st December 2009) at 00h00 UTC
     */

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.NOON,
            UTC)),

    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.MIDNIGHT,
                    UTC));

    /**
     * attribute of the enumeration
     */
    private final ZonedDateTime zonedDateTime;
    private static final int MILLI_SEC_IN_A_DAY = 1000 * 60 * 60 * 24;
    private static final int DAYS_IN_A_CENTURY = 36525;

    /**
     * private constructor
     * @param zonedDateTime1 - a ZonedDateTime
     */
    private Epoch(ZonedDateTime zonedDateTime1){ this.zonedDateTime = zonedDateTime1; }

    /**
     * return the number of days between the given instant and the instant to which it is applied (can be negative or not an integer)
     *
     * @param when - date to be compared with
     * @return number of days (double) between the given day parameter and one of the enumerations
     */
    public double daysUntil(ZonedDateTime when){
        long milliSecDiff = this.zonedDateTime.until(when, ChronoUnit.MILLIS);
        return (double)milliSecDiff/(MILLI_SEC_IN_A_DAY);
    }

    /**
     * gives the number of centuries (can be negative or not an integer) between the given date & time and the used epoch
     *
     * @param when - date to be compared with
     * @return number of julian centuries (double) between the given day parameter and one of the enumerations
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        double centuries = daysUntil(when);
        return centuries/(DAYS_IN_A_CENTURY);
    }
}