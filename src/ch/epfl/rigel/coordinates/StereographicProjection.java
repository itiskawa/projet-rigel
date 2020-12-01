package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.function.Function;

/**
 * Stereographic projection
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    /**
     * declaration of private attributes to speed up calculations
     */
    private final  double cosPhi;
    private final double sinPhi;
    private final double lambda0;
    private final double phi1;

    /**
     * public constructor
     * @param center - of the projected object in horizontal coordinates
     */
    public StereographicProjection(HorizontalCoordinates center){
        phi1 = center.alt();
        cosPhi = Math.cos(phi1);
        sinPhi = Math.sin(phi1);
        lambda0 = center.az();
    }

    /**
     * applies the conversion from horizontal coordinates to cartesian coordinates
     * @param azAlt - horizontal coordinates
     * @return corresponding cartesian coordinates
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double lambdaDelta = azAlt.az() - lambda0;
        double sinAlt = Math.sin(azAlt.alt());
        double cosAlt = Math.cos(azAlt.alt());
        double cosLambdaDelta = Math.cos(lambdaDelta);
        double d = 1.0 / (1 + sinAlt * sinPhi + cosAlt * cosPhi * cosLambdaDelta);
        double x = d * cosAlt * Math.sin(lambdaDelta);
        double y = d * (sinAlt * cosPhi - cosAlt * sinPhi * cosLambdaDelta);
        return CartesianCoordinates.of(x, y);
    }

    /**
     * computes the center of the circle corresponding to the parallel passing by given horizontal coordinates
     * @param hor - horizontal coordinates
     * @return above-described circle's center in cartesian coordinates
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double y = (cosPhi) / (Math.sin(hor.alt()) + sinPhi);
        return CartesianCoordinates.of(0, y);
    }

    /**
     * computes the radius of the circle corresponding to the parallel passing by given horizontal coordinates
     * @param parallel - horizontal coordinates
     * @return the above-described circle's radius
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return ( Math.cos(parallel.alt()) ) / (sinPhi + Math.sin(parallel.alt()));
    }

    /**
     * computes the diameter of a projected sphere of angular size rad
     * @param rad - radians
     * @return projected diameter of sphere of given angular size
     */
    public double applyToAngle(double rad){
        return 2 * Math.tan(rad / 4.0);
    }


    /**
     * applies conversion from cartesian coordinates back to horizontal coordinates
     * if (x,y) = (0,0) -> return lambda = lambda0, phi = phi1
     * @param xy - cartesian coordinates
     * @return corresponding horizontal coordinates
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double lambda = lambda0;
        double phi = phi1;

        // x !=0 or y != 0 same as (x,y) ≠ (0,0)
        if((xy.x() != 0) || (xy.y() != 0)){
            double rho = Math.sqrt(Math.pow(xy.x(), 2) + Math.pow(xy.y(), 2));
            double rhoSquared = rho * rho;
            double sinC = (2 * rho) / (Math.pow(rho, 2) + 1);
            double cosC = (1 - rhoSquared) / (rhoSquared + 1);

            // affects new values to lambda & phi
            lambda = Math.atan2(xy.x() * sinC, rho * cosPhi * cosC - xy.y() * sinPhi * sinC) + lambda0;
            phi = Math.asin(cosC * sinPhi + (xy.y() * sinC * cosPhi)/rho);
            lambda = Angle.normalizePositive(lambda);
        }
        return HorizontalCoordinates.of(lambda, phi);

    }

    /**
     * override of equals, is undefined
     * @throws UnsupportedOperationException - can't compare horizontal coordinates
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * override of hashCode, is undefined
     * @throws UnsupportedOperationException - can't compare horizontal coordinates
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * redefinition of toString() of the Object class
     * @return text representation of StereographicProjection
     */
    @Override
    public String toString() {
        return "StereographicProjection{" +
                "azimut du centre:" + lambda0 +
                ", altitude> du centre:" + Math.asin(sinPhi) +
                '}';
    }
}

