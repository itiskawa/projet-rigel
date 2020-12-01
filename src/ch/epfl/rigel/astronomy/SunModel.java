package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Sun Model
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum SunModel implements CelestialObjectModel<Sun> {

    /**
     * single element of the enumeration
     */
    SUN;

    /**
     * declaration & initialisation of constants (in order):
     * longitude at J2010 (eplisonG)
     * longitude at perigee (omegaG)
     * eccentricity of orbit (e)
     */
    private final double epsilonG = Angle.ofDeg(279.557208);
    private final double omegaG = Angle.ofDeg(283.112438);
    private final double e = 0.016705;
    private final static double DAYS_PER_YEAR = 365.242191;
    private final static double THETA_ZERO = 0.533128;

    /**
     * calculator method for model of the Sun at given time
     * @param daysSinceJ2010 - days since J2010 (days)
     * @param eclToEq - ecliptic to equatorial coordinates conversion
     * @return corresponding Sun (in time & space)
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclToEq) {

        /*
         * declaration & initialisation of variables to compute position of Sun (in order)
         * mean anomaly of the Sun in radians (meanAnomaly)
         * true anomaly of the Sun in radians (trueAnomaly)
         * geocentric ecliptic longitude of Sun in radians (l0nEcl)
         */
        double meanAnomaly = (Angle.TAU / DAYS_PER_YEAR) * daysSinceJ2010 + epsilonG - omegaG;
        double trueAnomaly = meanAnomaly + 2 * e * Math.sin(meanAnomaly);
        double lonEcl = trueAnomaly + omegaG;

        lonEcl = Angle.normalizePositive(lonEcl);

        /*
         * declaration & initialisation of variables to compute coordinates conversion (in order)
         * corresponding ecliptic coordinates (ecl)
         * corresponding equatorial coordinates (eq)
         */
        EclipticCoordinates ecl = EclipticCoordinates.of(lonEcl, 0);
        EquatorialCoordinates eq = eclToEq.apply(ecl);
        float angularSize = (float)(Angle.ofDeg(THETA_ZERO) * (((1 + e * Math.cos(trueAnomaly) )/ (1 - Math.pow(e, 2)))));

        return new Sun(ecl, eq, angularSize, (float)meanAnomaly);
    }
}
