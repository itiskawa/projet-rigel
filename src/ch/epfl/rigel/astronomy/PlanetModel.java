package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * Model of a planet
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */

public enum PlanetModel implements CelestialObjectModel<Planet> {

    /**
     * all planets in our solar system (in ascending order, starting from closest to Sun)
     * instantiated with all assumed-to-be-correct values, given by Mr. Schinz
     */
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);


    /**
     * private final attributes for all planets, names are purposely self-descriptive
     */
    private final String frenchName;
    private final double tropicalYear;
    private final double j2010Longitude;
    private final double perigeeLongitude;
    private final double orbitEccentricity;
    private final double orbitHalfGreatAxis;
    private final double orbitInclinationAtEcl;
    private final double sinOrbitInclinationAtEcl;
    private final double cosOrbitInclinationAtEcl;
    private final double orbitalNodeLongitude;
    private final double angSizeAt1UA;
    private final double magnitudeAt1UA;

    private final static double DAYS_PER_YEAR = 365.242191;

    /**
     * static attribute representing an immutable list of all planets
     */
    public final static List<PlanetModel> ALL = List.of(PlanetModel.values());


    /**
     * private constructor for PlanetModel enumerated objects
     *
     * @param frenchName            - name of the planet in french
     * @param tropicalYear          - the tropical year
     * @param j2010Longitude        - longitude J2010 (in degrees)
     * @param perigeeLongitude      - longitude at perigee (in degrees, stored in radins)
     * @param orbitEccentricity     - eccentricity of the orbit
     * @param orbitHalfGreatAxis    - half great axis of the orbit (in UA)
     * @param orbitInclinationAtEcl - in degrees
     * @param orbitalNodeLongitude  - in degrees
     * @param angSizeAt1UA          - in arcsecs
     * @param magnitudeAt1UA        - magnitude from 1 UA away
     */
    private PlanetModel(String frenchName, double tropicalYear, double j2010Longitude, double perigeeLongitude, double orbitEccentricity, double orbitHalfGreatAxis, double orbitInclinationAtEcl, double orbitalNodeLongitude, double angSizeAt1UA, double magnitudeAt1UA) {
        this.frenchName = frenchName;
        this.tropicalYear = tropicalYear;
        this.j2010Longitude = Angle.ofDeg(j2010Longitude);
        this.perigeeLongitude = Angle.ofDeg(perigeeLongitude);
        this.orbitEccentricity = orbitEccentricity;
        this.orbitHalfGreatAxis = orbitHalfGreatAxis;
        this.orbitInclinationAtEcl = Angle.ofDeg(orbitInclinationAtEcl);
        this.sinOrbitInclinationAtEcl = Math.sin(orbitInclinationAtEcl);
        this.cosOrbitInclinationAtEcl = Math.cos(orbitInclinationAtEcl);
        this.orbitalNodeLongitude = Angle.ofDeg(orbitalNodeLongitude);
        this.angSizeAt1UA = Angle.ofArcsec(angSizeAt1UA);
        this.magnitudeAt1UA = magnitudeAt1UA;
    }

    /**
     * calculator method for model of planets for a given time
     *
     * @param daysSinceJ2010 - days since J2010 (days)
     * @param eclToEq        - ecliptic to equatorial coordinate conversion at the given time
     * @return corresponding Planet (in time & space)
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclToEq) {

        /*
         * 1) computing planet's position on its own orbit
         */

        double meanAnomaly = (Angle.TAU / (DAYS_PER_YEAR)) * (daysSinceJ2010 / tropicalYear) + j2010Longitude
                - perigeeLongitude;
        double trueAnomaly = meanAnomaly + 2 * orbitEccentricity * Math.sin(meanAnomaly);

        double ownRadius = orbitHalfGreatAxis * (1 - Math.pow(orbitEccentricity, 2)) / (1 + orbitEccentricity * Math.cos(trueAnomaly));
        double ownLongitude = trueAnomaly + perigeeLongitude;

        /*
         * 2) planet's position is projected on ecliptic plane
         * then the coordinates are converted to ecliptic heliocentric ( plane change )
         */

        double sinLongitudeDiff = Math.sin(ownLongitude - orbitalNodeLongitude);
        double latEclHeliocentric = Math.asin(sinLongitudeDiff
                * Math.sin(orbitInclinationAtEcl));

        double projectedR = ownRadius * Math.cos(latEclHeliocentric);
        double projectedL = Math.atan2(sinLongitudeDiff * Math.cos(orbitInclinationAtEcl),
                Math.cos(ownLongitude - orbitalNodeLongitude)) + orbitalNodeLongitude;


        /*
         * 3) get earth's position in ecliptic heliocentric coordinates
         */

        double earthMeanAnom = (Angle.TAU / (DAYS_PER_YEAR)) * (daysSinceJ2010 / EARTH.tropicalYear) + EARTH.j2010Longitude
                - EARTH.perigeeLongitude;
        double earthTrueAnom = earthMeanAnom + 2 * EARTH.orbitEccentricity * Math.sin(earthMeanAnom);
        double earthR = EARTH.orbitHalfGreatAxis * (1 - Math.pow(EARTH.orbitEccentricity, 2)) / (1 + EARTH.orbitEccentricity * Math.cos(earthTrueAnom));
        double earthL = earthTrueAnom + EARTH.perigeeLongitude;

        /*
         * 4) use earth coordinates and planet coordinates to get planet's position in ecliptic geocentric coordinates
         * also computes planet's angular size and magnitude
         */

        double dist = Math.sqrt(Math.pow(earthR, 2) + Math.pow(ownRadius, 2) -
                2 * earthR * ownRadius * Math.cos(ownLongitude - earthL) * Math.cos(latEclHeliocentric));
        double angSize = angSizeAt1UA / dist;

        double formula1 = earthR * Math.sin(projectedL - earthL);

        double eclLon;
        double eclLat;

        switch (this) {
            case MERCURY:
            case VENUS:
                /*
                 * computing lon and lat for inferior planets
                 */

                eclLon = Math.PI + earthL + Math.atan2(projectedR * Math.sin(earthL - projectedL),
                        earthR - projectedR * Math.cos(earthL - projectedL));
                eclLon = Angle.normalizePositive(eclLon);
                break;
            case EARTH:
                return null;
            default:
                /*
                 * computing coordinates for superior planets
                 */
                eclLon = projectedL + Math.atan2(formula1,
                        projectedR - earthR * Math.cos(projectedL - earthL));
                eclLon = Angle.normalizePositive(eclLon);
        }

        eclLat = Math.atan((projectedR * Math.tan(latEclHeliocentric) * Math.sin(eclLon - projectedL)) /
                (formula1));
        var eclPos = EclipticCoordinates.of(eclLon, eclLat);

        /*
         * computing magnitude
         */
        double phase = (1 + Math.cos(eclLon - ownLongitude)) / 2;
        double magnitude = magnitudeAt1UA + 5 * Math.log10(ownRadius * dist / Math.sqrt(phase));

        return new Planet(frenchName, eclToEq.apply(eclPos), (float) angSize, (float) magnitude);
    }
}