package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEclipticCoordinatesTest {

    @Test
    void ofAndGettersWorkWithValidRad() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az = rng.nextDouble(0, Angle.ofDeg(360));
            var alt = rng.nextDouble(Angle.ofDeg(-90),Angle.ofDeg(90));
            var eclipt = EclipticCoordinates.of(az, alt);
            assertEquals(az, eclipt.lon(), 1e-6);
            assertEquals(alt, eclipt.lat(), 1e-6);
            assertEquals(Angle.toDeg(az), eclipt.lonDeg(), 1e-6);
            assertEquals(Angle.toDeg(alt), eclipt.latDeg(), 1e-6);
        }
        var alt = Angle.ofDeg(90);
        var eclipt = EclipticCoordinates.of(0, alt);
        assertEquals(alt, eclipt.lat(), 1e-6);
    }

    @Test
    void gettersReturnCorrectValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(Angle.ofDeg(0), Angle.ofDeg(360));
            var lat = rng.nextDouble(Angle.ofDeg(-90), Angle.ofDeg(90));
            var coord = EclipticCoordinates.of(lon, lat);
            assertEquals(lon, coord.lon());
            assertEquals(lat, coord.lat());

            assertEquals(Angle.toDeg(lat), coord.latDeg());
            assertEquals(Angle.toDeg(lon), coord.lonDeg());
        }
        var eclipt = EclipticCoordinates.of(0, Angle.ofDeg(90));
        assertEquals(Math.PI/2, eclipt.lat());
    }

    @Test
    void toStringWorks() {
        var eclipt = EclipticCoordinates.of(Angle.ofDeg(15), Angle.ofDeg(7.2));
        assertEquals("(λ=15.0000°, β=7.2000°)", eclipt.toString());
    }
}