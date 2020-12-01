package ch.epfl.rigel.coordinates;

/**
 * Cartesian Coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class CartesianCoordinates{

    /**
     * private attributes
     * represent the x & y coordinates of a plane
     */
    private final double x;
    private final double y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * public construction method, parameters are purposely self-descriptive
     * @param x - coordinate on X axis
     * @param y - coordinate on Y axis
     * @return corresponding cartesian coordinates
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x, y);
    }

    /**
     * public getter method for x coordinate
     * @return x
     */
    public double x(){
        return x;
    }

    /**
     * public getter method for y coordinate
     * @return y
     */
    public double y(){
        return y;
    }

    /**
     * override of equals, is undefined
     * @throws UnsupportedOperationException - can't compare real values
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * override of hashCode, is undefined
     * @throws UnsupportedOperationException - can't compare real values
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * redefinition of toString method of Object class
     * @return formatted display of the coordinates
     */
    public String toString(){
        return "x : " + this.x() + ", y : " + this.y();
    }
}
