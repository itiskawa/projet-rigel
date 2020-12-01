package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Observed Sky
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class ObservedSky {

    /**
     * declaration of private final attributes
     */
    private final Sun observedSun;
    private final CartesianCoordinates sunPos;
    private final Moon observedMoon;
    private final CartesianCoordinates moonPos;

    private final List<Planet> allObservedPlanets;
    private final List<CartesianCoordinates> planetPos;

    private final Map<Integer, CartesianCoordinates> starPos;
    private final StarCatalogue starCatalogue;


    /**
     * public constructor
     * constructs all private final attributes
     * @param inst - a ZonedDateTime representing the desired moment and timezone
     * @param pos - in GeographicCoordinates
     * @param sterProj - used Stereographic Projection
     * @param starC - used StarCatalogue
     */
    public ObservedSky(ZonedDateTime inst, GeographicCoordinates pos, StereographicProjection sterProj, StarCatalogue starC){

        // initialise attributes used in coordinate conversions
        double daySinceJ2010 = Epoch.J2010.daysUntil(inst);
        EclipticToEquatorialConversion eclToEq = new EclipticToEquatorialConversion(inst);
        EquatorialToHorizontalConversion eqToHor = new EquatorialToHorizontalConversion(inst, pos);


        // compute sun, moon & planets
        observedSun = SunModel.SUN.at(daySinceJ2010, eclToEq);
        sunPos = sterProj.apply(eqToHor.apply(observedSun.equatorialPos()));

        observedMoon = MoonModel.MOON.at(daySinceJ2010, eclToEq);
        moonPos = sterProj.apply(eqToHor.apply(observedMoon.equatorialPos()));

        starCatalogue = starC;

        planetPos = new ArrayList<>();
        allObservedPlanets = new ArrayList<>();
        for(PlanetModel p : PlanetModel.values()){
            if(!(p.name().equals("EARTH"))) {
                Planet computedPlanet = p.at(daySinceJ2010, eclToEq);
                allObservedPlanets.add(computedPlanet);
                planetPos.add(sterProj.apply(eqToHor.apply(computedPlanet.equatorialPos())));
            }
        }

        // creates stars & stores their cartesian coords in a list
        starPos = new HashMap<>();
        for(Star s : starC.stars()){
            starPos.put(s.hipparcosId(), sterProj.apply(eqToHor.apply(s.equatorialPos())));
        }
    }

    /**
     * getter method for Sun at given time
     * @return corresponding Sun
     */
    public Sun sun(){
        return observedSun;
    }

    /**
     * getter method for position of Sun in Cartesian coordinates
     * @return cartesian coordinates of Sun
     */
    public CartesianCoordinates sunPosition(){
        return sunPos;
    }

    /**
     * getter method for Moon at given time
     * @return corresponding Moon
     */
    public Moon moon(){
        return observedMoon;
    }

    /**
     * getter method for position of Moon in Cartesian coordinates
     * @return cartesian coordinates of Moon
     */
    public CartesianCoordinates moonPosition(){
        return moonPos;
    }

    /**
     * getter method for the list of all extraterrestrial planets in our Solar system
     * @return List<> of all Planets
     */
    public List<Planet> planets(){
        return List.copyOf(allObservedPlanets);
    }

    /**
     * getter method for positions of all extraterrestrial planets in cartesian coordinates in the projection's plane in same order as planets()
     * x and y are consecutively put in the fixed array -> has 14 rows. For Mercury, index 0 is x coord, index 1 is y coord etc...
     * @return double[] representing all coordinates as described
     */
    public double[] planetPositions(){
        double[] positions = new double[14];
        int counter = 0;
        for (CartesianCoordinates coords : planetPos) {
            positions[counter] = coords.x();
            positions[counter + 1] = coords.y();
            counter += 2;
        }
        return positions;
    }

    /**
     * getter method for all stars in star catalogue
     * @return List of Stars
     */
    public List<Star> stars(){
        return List.copyOf(starCatalogue.stars());
    }

    /**
     * getter method position of a star in cartesian coordinates in the projection's plane
     * @param hipparcosId - to identify the star whose coordinates are to be fetched
     * @return corresponding cartesian coordinates
     */
    public CartesianCoordinates starPosition(int hipparcosId){
        Preconditions.checkArgument(starPos.containsKey((hipparcosId)));
        return starPos.get(hipparcosId);
    }

    /**
     * getter method for asterisms in star catalogue
     * @return a collection of Asterisms
     */
    public Collection<Asterism> asterisms(){
        return starCatalogue.asterisms();
    }

    /**
     * getter method for indices of stars in given asterism
     * indices correspond to the order of the stars in the star catalogue
     * @param asterism - the asterism whose list of star indices we want
     * @return corresponding indices (asterismIndices(...) in StarCatalogue)
     */
    public List<Integer> asterismIndices(Asterism asterism){
        return starCatalogue.asterismIndices(asterism);
    }

    /**
     * computes the closest celestial object to the given coordinates.
     * the search is constrained by a given maximal distance between the given coordinates
     * and its possible closest celestial object
     * @param here - cartesian coordinates in the projection's plane, reference point of distance
     * @param maxDistance - maximal distance
     * @return Optional containing the closest celestial object, or Optional.empty() if no celestial objects are "in range"
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates here, double maxDistance){
        Preconditions.checkArgument(maxDistance >= 0);

        double minX = here.x() - maxDistance;
        double maxX = here.x() + maxDistance;
        double minY = here.y() - maxDistance;
        double maxY = here.y() + maxDistance;
        TreeMap<Double, CelestialObject> distancesToObjectMap = new TreeMap<>();

        //check stars in square
        List<Star> starsInSquare = new ArrayList<>();
        for(Star s: starCatalogue.stars()){
            int h = s.hipparcosId();
            double xS = starPos.get(h).x();
            double yS = starPos.get(h).y();
            if((xS < maxX) && (xS > minX) && (yS < maxY) && (yS > minY)){
                starsInSquare.add(s);
            }
        }

        // check planets
        int i = 0;
        for(Planet p : allObservedPlanets) {
            double xP = planetPos.get(i).x();
            double yP = planetPos.get(i).y();
            double d = Math.hypot(xP - here.x(), yP - here.y());
            i += 1;
            if (d < maxDistance) {
                distancesToObjectMap.put(d, p);
            }
        }

        // truncate stars in circle
        for(Star s : starsInSquare){
            int h = s.hipparcosId();
            double x = starPos.get(h).x();
            double y = starPos.get(h).y();
            double d = Math.hypot(x - here.x(), y - here.y());
            if(d < maxDistance){
                distancesToObjectMap.put(d, s);
            }
        }

        // adding sun & moon if in range
        double sunDist = Math.hypot(sunPos.x() - here.x(), sunPos.y()- here.y());
        if(sunDist < maxDistance){
            distancesToObjectMap.put(sunDist, observedSun);
        }
        double moonDist = Math.hypot(moonPos.x() - here.x(), moonPos.y() - here.y());
        if(moonDist < maxDistance){
            distancesToObjectMap.put(moonDist, observedMoon);
        }

        // everything is in circle of r = maxDistance
        if(distancesToObjectMap.isEmpty()){
            return Optional.empty();
        }
        else{
            return Optional.of(distancesToObjectMap.get(distancesToObjectMap.firstKey()));
        }
    }
}