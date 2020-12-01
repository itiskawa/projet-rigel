package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEquatorialCoordinatesTest {

    @Test
    void ofAndGettersWorkWithValidRad() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, Angle.ofDeg(360));
            var dec = rng.nextDouble(Angle.ofDeg(-90),Angle.ofDeg(90));
            var equat = EquatorialCoordinates.of(ra, dec);
            assertEquals(ra, equat.ra(), 1e-6);
            assertEquals(dec, equat.dec(), 1e-6);
            assertEquals(Angle.toDeg(ra), equat.raDeg(), 1e-6);
            assertEquals(Angle.toDeg(dec), equat.decDeg(), 1e-6);
        }
        var alt = Angle.ofDeg(90);
        var horiz = EquatorialCoordinates.of(0, alt);
        assertEquals(alt, horiz.dec(), 1e-6);
    }

    @Test
    void gettersReturnCorrectValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(Angle.ofDeg(0), Angle.ofDeg(360));
            var dec = rng.nextDouble(Angle.ofDeg(-90), Angle.ofDeg(90));
            var coord = EquatorialCoordinates.of(ra, dec);
            assertEquals(ra, coord.ra());
            assertEquals(dec, coord.dec());

            assertEquals(Angle.toHr(ra), coord.raHr());
            assertEquals(Angle.toDeg(dec), coord.decDeg());

            assertEquals(Angle.toDeg(ra), coord.raDeg());
        }
        var equat = EquatorialCoordinates.of(0, Angle.ofDeg(90));
        assertEquals(Math.PI/2, equat.dec());
    }

    @Test
    void toStringWorks() {
        var equat = EquatorialCoordinates.of(Angle.ofDeg(15), Angle.ofDeg(7.2));
        assertEquals("(ra=1.0000h, dec=7.2000°)", equat.toString());
    }
}