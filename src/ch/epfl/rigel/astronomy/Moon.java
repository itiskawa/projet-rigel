package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * Moon
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Moon extends CelestialObject{

    /**
     * declaration of private attribute representing the phase
     */
    private final float phase;
    private final static ClosedInterval PHASE_LIMIT = ClosedInterval.of(0,1);

    /**
     * public constructor
     * @param equatorialPos - equatorial position of the moon
     * @param angularSize - angular size of the moon
     * @param magnitude - magnitude of the moon
     * @param phase - phase of the moon
     * @throws IllegalArgumentException if phase is not in [0,1]
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float) Preconditions.checkInInterval(PHASE_LIMIT, phase);
    }

    /**
     * redefinition of info() method of CelestialObject class
     * @return text representation of the Moon (name + phase in %)
     */
    @Override
    public String info(){
        return String.format(Locale.ROOT,
                "%s (%.1f%%)",
                super.name(),
                100 * phase);
    }
}
