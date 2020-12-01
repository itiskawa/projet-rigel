package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Moon model
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    /**
     * single element of the enumeration
     */
    MOON;

    /**
     * declaration & initialisation of constants used in at method in order:
     * average longitude (lo)
     * average longitude at perigee (p0)
     * longitude at ascending node (No)
     * orbital inclination (i)
     * orbit eccentricity (e)
     */
    private final double l0 = Angle.ofDeg(91.929336);
    private final double p0 = Angle.ofDeg(130.143076);
    private final double n0 = Angle.ofDeg(291.682547);
    private final double i = Angle.ofDeg(5.145396);
    private final double e = 0.0549;


    /**
     * calculator method for a model of the Moon, at a given time
     *
     * @param daysSinceJ2010 - days since J2010 (days)
     * @param eclToEq - ecliptic to equatorial coordinate conversion (conversion of given time)
     * @return corresponding Moon (in time & space)
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclToEq) {


        /*
         * declaration & initialisation of variables (in order)
         * Sun respective to given parameters (s)
         * Sun's mean anomaly (sunMeanAnomaly)
         * Sun's ecliptic longitude (sunEclLon)
         */
       Sun s =  SunModel.SUN.at(daysSinceJ2010, eclToEq);
       double sunMeanAnomaly = s.meanAnomaly();
       double sunEclLon = s.eclipticPos().lon();


        /*
         * declaration & initialisation of variables to compute orbital longitude (in order):
         *
         * average orbital longitude (l)
         * mean anomaly (meanAnomaly)
         * eviction (ev)
         * correction of annual equation (ae)
         * correction 3 (a3)
         * corrected mean anomaly (corrMeanAnom)
         * center equation correction (ec)
         * correction 4 (a4)
         * corrected orbital longitude (corrL)
         * variation (v)
         * true orbital longitude (trueL)
         */
        double l = Angle.ofDeg(13.1763966) * daysSinceJ2010 + l0;
        double meanAnomaly = l - Angle.ofDeg(0.1114041) * daysSinceJ2010 - p0;
        double ev = Angle.ofDeg(1.2739) * Math.sin(2 * (l - sunEclLon) - meanAnomaly);
        double ae = Angle.ofDeg(0.1858) * Math.sin(sunMeanAnomaly);
        double a3 = Angle.ofDeg(0.37) * Math.sin(sunMeanAnomaly);
        double corrMeanANom = meanAnomaly + ev - ae - a3;
        double ec = Angle.ofDeg(6.2886) * Math.sin(corrMeanANom);
        double a4 = Angle.ofDeg(0.214) * Math.sin(2 * corrMeanANom);
        double corrL = l + ev + ec - ae + a4;
        double v = Angle.ofDeg(0.6583) * Math.sin(2 * (corrL - sunEclLon));
        double trueL = corrL + v;



        /*
         * declaration & initialisation of variables to calculate geocentric ecliptic coordinates (in order)
         *
         * ascending node longitude (n)
         * corrected ascending node longitude (corrN)
         * ecliptic longitude of Moon (eclLon)
         * ecliptic latitude of Moon (eclLat)
         *
         */
        double n = n0 - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double corrN = n - Angle.ofDeg(0.16) * Math.sin(sunMeanAnomaly);
        double eclLon = Math.atan2(Math.sin(trueL - corrN) * Math.cos(i), Math.cos(trueL - corrN)) + corrN;
        double eclLat = Math.asin(Math.sin(trueL - corrN) * Math.sin(i));


        /*
         * declaration & initialisation of phase variable (phaseF)
         */
        double phaseF = (1 - Math.cos(trueL - sunEclLon)) / 2.0;


        /*
         * declaration & initialisation of variables to compute angular size (in order)
         * distance Earth-Moon (rho)
         * angular size of Moon (angularSizeFromEarth)
         */
        double rho = (1 - e * e) / (1 + e * Math.cos(corrMeanANom+ ec));
        double angularSizeFromEarth = Angle.ofDeg(0.5181) / rho;


        /*
         * declaration & initialisation of variables to calculate the coordinate conversion (in order)
         * ecliptic coordinates from calculated lon & lat (ecl)
         * relative equatorial coordinates (eq) - obtained through conversion using eclToEq parameter
         */

        eclLon = Angle.normalizePositive(eclLon);

        EclipticCoordinates ecl = EclipticCoordinates.of(eclLon, eclLat);
        EquatorialCoordinates eq = eclToEq.apply(ecl);

        return new Moon(eq, (float)angularSizeFromEarth, 0, (float)phaseF);
    }
}
