package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;


/**
 * Conversion from equatorial to horizontal coordinates
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    /**
     * private attributes stored at construction
     */
    private final double siderealTimeAt;
    private final double cosPhi;
    private final double sinPhi;

    /**
     * public constructor
     * @param when a ZonedDateTime
     * @param where GeographicCoordinates
     * builds the sidereal time with the given parameters, along with the cosine & sine of the observer's latitude
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        this.siderealTimeAt = SiderealTime.local(when, where);
        this.cosPhi = Math.cos(where.lat());
        this.sinPhi = Math.sin(where.lat());
    }

    /**
     * converts the given equatorial coordinates into Horizontal coordinates
     * @param eq equatorial coordinates to be converted
     * @return HorizontalCoordinates corresponding to the given EquatorialCoordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates eq) {
        double angleH = siderealTimeAt - eq.ra();
        double hauteur = Math.asin(Math.sin(eq.dec()) * sinPhi + Math.cos(eq.dec()) * cosPhi * Math.cos(angleH));
        double azimuth = Math.atan2(-Math.cos(eq.dec()) * cosPhi * Math.sin(angleH), Math.sin(eq.dec()) - sinPhi * Math.sin(hauteur));

        azimuth = Angle.normalizePositive(azimuth);

        return HorizontalCoordinates.of(azimuth, hauteur);
    }

    /**
     * override of equals, is undefined
     * @throws UnsupportedOperationException - can't compare coordinates
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * override of hashCode, is undefined
     * @throws UnsupportedOperationException - can't compare coordinates
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}