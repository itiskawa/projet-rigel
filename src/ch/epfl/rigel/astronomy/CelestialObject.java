package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Celestial Object
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public abstract class CelestialObject {

    /**
     * declaration of private attributes
     */
    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * package private constructor
     *
     * @param name - of the object
     * @param equatorialPos - equatorial coordinates of the object
     * @param angularSize - of the object
     * @param magnitude - of the object
     * @throws NullPointerException if the given equatorial position is null
     * @throws IllegalArgumentException if the angular size is strictly negative
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        Objects.requireNonNull(name, "no name");
        Objects.requireNonNull(equatorialPos, "empty coordinates");
        Preconditions.checkArgument(angularSize >= 0);
        this.name = name;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * getter method for name
     * @return name
     */
    public String name(){
        return name;
    }

    /**
     * getter method for the angular size
     * @return angularSize
     */
    public double angularSize(){
        return angularSize;
    }

    /**
     * getter for magnitude
     * @return magnitude
     */
    public double magnitude(){
        return magnitude;
    }

    /**
     * getter for Equatorial Position
     * @return equatorialPos
     */
    public EquatorialCoordinates equatorialPos() { return equatorialPos; }

    /**
     * gives text information about .this
     * @return short text
     */
    public String info(){
        return name();
    }

    /**
     * redefinition of toString() from Object class
     * @return text representation of CelestialObject
     */
    @Override
    public String toString() {
        return info();
    }
}
