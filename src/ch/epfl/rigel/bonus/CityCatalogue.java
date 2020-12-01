package ch.epfl.rigel.bonus;


import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ** BONUS **
 * city catalogue - very analog to StarCatalogue
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class CityCatalogue {

    private final List<City> cities;
    private final List<String> cityNames;
    private final Map<String, GeographicCoordinates> cityCoords;


    /**
     * public constructor
     * @param cities - list to be stored in the catalogue
     */
    public CityCatalogue(List<City> cities){

        this.cities = cities;

        cityCoords = new HashMap<>();
        cityNames = new ArrayList<>();
        for(City c : cities){
            cityCoords.put(c.getName().substring(1, c.getName().length()-1).toUpperCase(), c.coordinates());
            cityNames.add(c.getName().substring(1, c.getName().length()-1).toUpperCase());
        }
        Collections.sort(cityNames);
    }

    /**
     * getter method for the cities
     * @return immutable list of cities
     * help me
     * help me
     */
    public List<City> cities(){
        return List.copyOf(cities);
    }

    /**
     *  getter method for the cities' names
     * @return immutable list of the cities' names
     */
    public List<String> cityNames() {
        return List.copyOf(cityNames);
    }

    /**
     * getter method for the cities' coordinates (in GeographicCoordinates)
     * @return an immutable list of the cities' coordinates
     */
    public Map<String, GeographicCoordinates> cityCoords(){
        return Map.copyOf(cityCoords);
    }


    /**
     * computes the city closest to the given GeographicCoordinates, within a radius <= maxDist
     * @param coords - the center of the "scanning circle"
     * @param maxDist - the radius of the "scanning circle"
     * @return
     */
    public String cityClosestTo(GeographicCoordinates coords, double maxDist){ // maxDist is in degrees

        double minX = coords.lonDeg() - maxDist;
        double maxX = coords.lonDeg() + maxDist;
        double minY = coords.latDeg() - maxDist;
        double maxY = coords.latDeg() + maxDist;
        TreeMap<Double, String> distances = new TreeMap<>();

        List<City> citiesInRange = new ArrayList<>();
        for(City c : this.cities()) { // makes list of cities in square
            double xS = c.getLonDeg();
            double yS = c.getLatDeg();
            if ((xS < maxX) && (xS > minX) && (yS < maxY) && (yS > minY)) {
                citiesInRange.add(c);
            }
        }

        /**
        seet
         
         */

        for(City c : citiesInRange){
            double x = c.getLonDeg();
            double y = c.getLatDeg();
            double d = Math.hypot(x - coords.lonDeg(), y - coords.latDeg());
            if(d < maxDist){
                distances.put(d, c.getName());
            }
        }

        if(distances.isEmpty()){
            return "nothing";
        }
        else{
            String result = distances.get(distances.firstKey());
            return result.substring(1, result.length()-1);
        }

    }

    /**
     * static nested class , builds the CityCatalogue - analog to StarCatalogue.Builder
     */
    public static final class Builder{
        private final List<City> cities;

        /**
         * public default constructor
         * sets both attributes to empty lists
         */
        public Builder(){
            this.cities = new ArrayList<>();
        }


        /**
         * adds the city to the current list of cities
         * @param c - the city to be added
         */
        public void addCity(City c){
            this.cities.add(c);
        }

        /**
         * getter method for the list of cities
         * @return list of cities in the catalogue
         */
        public List<City> cities(){
            return Collections.unmodifiableList(cities);
        }

        /**
         * public construction method to load from external documents
         * @param inputStream -  the document to be read
         * @param loader - the loader
         * @return .this from the loaded document
         * @throws IOException - if acting upon the InputStream fails
         */
        public Builder loadFrom(InputStream inputStream, CityLoader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * build method, creates a CityCatalogue from .this
         * @return OuterClass of .this, using .this' attributes
         */
        public CityCatalogue build(){
            return new CityCatalogue(cities);
        }
    }

}
