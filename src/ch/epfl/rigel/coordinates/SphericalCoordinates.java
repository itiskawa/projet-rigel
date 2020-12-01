package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * represents spherical coordinates as a mathematical concept, thus it is abstract
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
abstract class SphericalCoordinates {

    private final double longitude;
    private final double latitude;

    /**
     * package private constructor, by convention takes radians as parameters
     * @param lon - longitude
     * @param lat - latitude
     */
    SphericalCoordinates(double lon, double lat){
        this.longitude = lon;
        this.latitude = lat;
    }

    /**
     * getter for lon
     * @return private parameter lon
     */
    double lon(){
        return this.longitude;
    }

    /**
     * getter for lon in degrees
     * @return private parameter lon in degrees
     */
    double lonDeg(){
       return Angle.toDeg(this.longitude);
    }

    /**
     * getter for lat
     * @return private parameter lat
     */
    double lat(){
        return this.latitude;
    }

    /**
     * getter for lat in degrees
     * @return private parameter lat in degrees
     */
    double latDeg(){
        return Angle.toDeg(this.latitude);
    }

    /**
     * override of equals, is undefined
     * @throws UnsupportedOperationException - can't compare real values
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * override of hashCode, is undefined
     * @throws UnsupportedOperationException - can't compare real values
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}
