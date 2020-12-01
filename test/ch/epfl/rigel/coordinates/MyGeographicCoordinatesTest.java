package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.test.TestRandomizer;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyGeographicCoordinatesTest {

    @Test
    void ofAndGettersWorkWithValidValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lonDeg = rng.nextDouble(-180, 180);
            var latDeg = rng.nextDouble(-90,90);
            var geogCoords = GeographicCoordinates.ofDeg(lonDeg, latDeg);
            assertEquals(lonDeg, geogCoords.lonDeg(), 1e-6);
            assertEquals(latDeg, geogCoords.latDeg(), 1e-6);
            var lon = Angle.ofDeg(lonDeg);
            var lat = Angle.ofDeg(latDeg);
            assertEquals(lon, geogCoords.lon(), 1e-6);
            assertEquals(lat, geogCoords.lat(), 1e-6);

        }
        var maxLat = 90;
        var geogCoords = GeographicCoordinates.ofDeg(0, maxLat);
        assertEquals(maxLat, geogCoords.latDeg());
    }

    @Test
    void ofFailsOutOfBounds() {
        var lon = 180;
        assertThrows(IllegalArgumentException.class, () -> {
            var coords = GeographicCoordinates.ofDeg(lon, 0);
        });
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon2 = rng.nextDouble(180, 500);
            var lat2 = rng.nextDouble(90+Double.MIN_VALUE, 500);
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = GeographicCoordinates.ofDeg(lon2, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = GeographicCoordinates.ofDeg(0, lat2);
            });
            var lon3 = rng.nextDouble(-500, -180);
            var lat3 = rng.nextDouble(-500, -90);
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = GeographicCoordinates.ofDeg(lon3, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = GeographicCoordinates.ofDeg(0, lat3);
            });
        }
    }

    @Test
    void isValidLonDegWorksOnValid() {
        assertTrue(GeographicCoordinates.isValidLonDeg(-180));
        assertTrue(GeographicCoordinates.isValidLonDeg(0));
        assertTrue(GeographicCoordinates.isValidLonDeg(-179.999999));
        assertTrue(GeographicCoordinates.isValidLonDeg(179.999999));
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(-180, 180);
            assertTrue(GeographicCoordinates.isValidLonDeg(lon));
        }
    }

    @Test
    void isValidLonDegFailsOutOfBounds() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS/2; i++) {
            var lon = rng.nextDouble(180, 500);
            assertTrue(!GeographicCoordinates.isValidLonDeg(lon));
            lon = rng.nextDouble(-500, -180);
            assertTrue((!GeographicCoordinates.isValidLonDeg(lon)));
        }
        assertTrue(!GeographicCoordinates.isValidLonDeg((180)));
    }

    @Test
    void isValidLatDegWorksonValid() {
        assertTrue(GeographicCoordinates.isValidLatDeg(0));
        assertTrue(GeographicCoordinates.isValidLatDeg(90));
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(-90, 90);
            assertTrue(GeographicCoordinates.isValidLonDeg(lon));
        }
    }

    @Test
    void isValidLatDegFailsOutOfBounds() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS/2; i++) {
            var lat = rng.nextDouble(90 + Double.MIN_VALUE, 500);
            assertTrue(!GeographicCoordinates.isValidLatDeg(lat));
        }
        var lat = rng.nextDouble(-500, -90);
        assertTrue((!GeographicCoordinates.isValidLatDeg(lat)));
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var geogCoords = GeographicCoordinates.ofDeg(0, 0);
            geogCoords.equals(geogCoords);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var geogCoords = GeographicCoordinates.ofDeg(0, 0);
            geogCoords.hashCode();
        });
    }

    @Test
    void toStringWorks() {
        var horiz = GeographicCoordinates.ofDeg(6.57, 7.2);
        assertEquals("(lon=6.5700°, lat=7.2000°)", horiz.toString());
    }
}