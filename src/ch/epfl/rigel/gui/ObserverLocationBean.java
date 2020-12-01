package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

/**
 * bean for the observer's location on the Earth
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class ObserverLocationBean {

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObservableValue<GeographicCoordinates> coordinates;

    /**
     * public default constructor, initialization of properties
     * the content of the Geographic coordinates properties is linked so that
     * it is updated when lonDeg or latDeg are modified
     */
    ObserverLocationBean(){
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();
        coordinates = Bindings.createObjectBinding(()->
                GeographicCoordinates.ofDeg(this.lonDeg.get(), this.latDeg.get()), lonDeg, latDeg);
    }

    /**
     * getter method for the property containing the GeographicCoordinates
     * @return the property containing the GeographicCoordinates
     */
    public ObservableValue<GeographicCoordinates> coordinatesProperty(){
        return coordinates;
    }

    /**
     * getter method for the GeographicCoordinates
     * @return - the stored GeographicCoordinates
     */
    public GeographicCoordinates getCoordinates(){
        return this.coordinates.getValue();
    }

    /**
     * setter method to modify the contained GeographicCoordinates
     * @param geographicCoordinates - the new coordinates to be stored
     */
    public void setCoordinates(GeographicCoordinates geographicCoordinates){
        this.setLatDeg(geographicCoordinates.latDeg());
        this.setLonDeg(geographicCoordinates.lonDeg());
    }

    /**
     * getter method for the property containing the longitude
     * @return - the property containing the longitude
     */
    public DoubleProperty lonDegProperty(){
        return lonDeg;
    }

    /**
     * getter method for the longitude (double)
     * @return - the stored longitude
     */
    public double getLonDeg(){
        return lonDeg.get();
    }

    /**
     * setter method to modify the contained longitude
     * @param l - the new longitude to be stored
     */
    public void setLonDeg(double l){
        this.lonDeg.set(l);
    }

    /**
     * getter method for the property containing the latitude
     * @return - the property containing the latitude
     */
    public DoubleProperty latDegProperty(){
        return latDeg;
    }

    /**
     * getter method for the latitude (double)
     * @return - the stored latitude
     */
    public double getLatDeg(){
        return this.latDeg.get();
    }

    /**
     * setter method to modify the contained latitude
     * @param l - the new latitude to be stored
     */
    public void setLatDeg(double l){
        this.latDeg.set(l);
    }



}
