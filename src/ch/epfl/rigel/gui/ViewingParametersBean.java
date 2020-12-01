package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.*;

/**
 * bean for the sky-viewing parameters : center of the stereographic projection used
 * and the field of view in degrees
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class ViewingParametersBean{

    private final DoubleProperty fOVDeg;
    private final ObjectProperty<HorizontalCoordinates> center;
    private final BooleanProperty toggleAsterism;

    /**
     * default constructor
     */
    public ViewingParametersBean(){
        fOVDeg = new SimpleDoubleProperty();
        center = new SimpleObjectProperty<>();
        toggleAsterism = new SimpleBooleanProperty();
    }

    /**
     * getter method for the property containing the HorizontalCoordinates
     * @return the property containing the HorizontalCoordinates
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return center;
    }

    /**
     * getter method for the center's value
     * @return center.get()
     */
    public HorizontalCoordinates getCenter(){
        return center.get();
    }

    /**
     * getter method for azimuth of center
     * @return azimuth of center (horizontal coordinates)
     */
    public double getCenterAz(){
        return this.getCenter().az();
    }

    /**
     * getter method for altitude of center
     * @return altitude of center (horizontal coordinates)
     */
    public double getCenterAlt(){
        return this.getCenter().alt();
    }

    /**
     * setter method for center
     * @param newCenter - the new center to be stored
     */
    public void setCenter(HorizontalCoordinates newCenter){
        this.center.set(newCenter);
    }


    /**
     * getter method for the property containing the double representing the angle of field of view
     * @return the property containing the double representing the angle of field of view
     */
    public DoubleProperty fOVDegProperty(){
        return this.fOVDeg;
    }

    /**
     * getter method for the field of view angle's value (degrees)
     * @return fOVDeg.getValue()
     */
    public double getFOVDeg(){
        return fOVDeg.get();
    }

    /**
     * setter method for the field of view angle
     * @param newFOVDeg - the new field of view angle (degrees) to be stored
     */
    public void setFOVDeg(double newFOVDeg){
        this.fOVDeg.set(newFOVDeg);
    }

    /**
     * getter method for the property containing the boolean determining if the lines forming
     * asterisms show or not
     * @return toggleAsterism
     */
    public ReadOnlyBooleanProperty toggleAsterismProperty() {
        return toggleAsterism;
    }

    /**
     * getter method for the boolean determining if the lines forming
     * asterisms show or not
     * @return toggleAsterisms.get()
     */
    public boolean getToggleAsterism(){
        return this.toggleAsterism.get();
    }

    /**
     * setter method for the boolean determining if the lines forming
     * the asterisms show or not
     * @param b -  the new boolean value
     */
    public void setToggleAsterism(boolean b){
        this.toggleAsterism.set(b);
    }
}
