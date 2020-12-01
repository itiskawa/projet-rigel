package ch.epfl.rigel.bonus;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.property.ObjectProperty;


/**
 * ** BONUS **
 * City
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class City {

    private final String name;
    private final GeographicCoordinates coordinates;

    /**
     * public constructor
     * @param name -  the city's name
     * @param coords - it's coordinates (in GeographicCoordinates)
     */
    public City(String name, GeographicCoordinates coords){
        this.name = name;
        this.coordinates = coords;
    }

    /**
     * getter method for the name
     * @return - the city's name
     */
    public String getName(){
        return this.name;
    }


    /**
     * getter method for the coordinates
     * @return - the city's coordinates
     */
    public GeographicCoordinates coordinates(){
        return coordinates;
    }

    /**
     * getter method for the city's longitude in radians
     * @return - coordinates.lon()
     */
    public double getLon(){
        return coordinates.lon();
    }

    /**
     * getter method for the city's latitude in radians
     * @return - coordinates.lat()
     */
    public double getLat(){
        return coordinates.lat();
    }

    /**
     * getter method for the city's longitude in degrees
     * @return coordinates.lonDeg()
     */
    public double getLonDeg(){
        return coordinates.lonDeg();
    }

    /**
     * getter method for the city's latitude in degrees
     * @return coordinates.latDeg()
     */
    public double getLatDeg() {
        return coordinates.latDeg();
    }
}

