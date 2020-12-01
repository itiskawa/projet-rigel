package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * An asterism
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Asterism {

    /**
     * declaration of private final attribute
     * list of stars (stars)
     */
    private final List<Star> stars;

    /**
     * public constructor method
     * @param stars - list of stars in said asterism
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(stars != null && !stars.isEmpty());
        this.stars = List.copyOf(stars);
    }

    /**
     * getter method for stars
     * @return immutable list of stars
     */
    public List<Star> stars(){
        return stars;
    }
}
