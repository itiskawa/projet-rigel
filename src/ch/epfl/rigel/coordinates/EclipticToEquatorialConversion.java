package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion from ecliptic to equatorial coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    /**
     * declaration of private attributes; epsilon, cosEpsilon & sinEpsilon are constants to be calculated at instantiation
     * obliqueFormula is a polynomial of known coefficients
     */
    private final double cosEpsilon, sinEpsilon;
    private static final Polynomial OBLIQUE_FORMULA = Polynomial.of(Angle.ofArcsec(0.00181),
            Angle.ofArcsec(-0.0006),
            Angle.ofArcsec(-46.815),
            Angle.ofDMS(23, 26, 21.45));



    /**
     * public construction method
     * sets the private attributes
     * @param when - a ZonedDateTime
     */
    public EclipticToEquatorialConversion(ZonedDateTime when){
        double epsilon = OBLIQUE_FORMULA.at(Epoch.J2000.julianCenturiesUntil(when));
        this.cosEpsilon = Math.cos(epsilon);
        this.sinEpsilon = Math.sin(epsilon);
    }



    /**
     * builds equatorial coordinates corresponding to the given ecliptic coordinates
     * @param ecl - ecliptic coordinates to be converted
     * @return corresponding EquatorialCoordinates to the given EclipticCoordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double rightAscension = Math.atan2((Math.sin(ecl.lon()) * cosEpsilon - (Math.tan(ecl.lat()) * sinEpsilon)), Math.cos(ecl.lon()));
        double declination = Math.asin(Math.sin(ecl.lat()) * cosEpsilon + Math.cos(ecl.lat()) * sinEpsilon * Math.sin(ecl.lon()));
        rightAscension = Angle.normalizePositive(rightAscension);

        return EquatorialCoordinates.of(rightAscension, declination);
    }

    /**
     * override of equals, is undefined
     * @throws UnsupportedOperationException - can't compare coordinates
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * override of hashCode, is undefined
     * @throws UnsupportedOperationException - can't compare coordinates
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}
