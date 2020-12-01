package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHorizontalCoordinatesTest {

    private HorizontalCoordinates newHorizontalCoordinates(double az, double alt) {
        return HorizontalCoordinates.of(az, alt);
    }

    @Test
    void ofAndGettersWorkWithValidRad() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az = rng.nextDouble(0, Angle.ofDeg(360));
            var alt = rng.nextDouble(Angle.ofDeg(-90),Angle.ofDeg(90));
            var horiz = HorizontalCoordinates.of(az, alt);
            assertEquals(az, horiz.az(), 1e-6);
            assertEquals(alt, horiz.alt(), 1e-6);
            assertEquals(Angle.toDeg(az), horiz.azDeg(), 1e-6);
            assertEquals(Angle.toDeg(alt), horiz.altDeg(), 1e-6);
        }
        var alt = Angle.ofDeg(90);
        var horiz = HorizontalCoordinates.of(0, alt);
        assertEquals(alt, horiz.alt(), 1e-6);
    }

    @Test
    void ofFailsOutofBounds() {
        var az = Angle.ofDeg(360);
        assertThrows(IllegalArgumentException.class, () -> {
            var coords = HorizontalCoordinates.of(az, 0);
        });
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az2 = rng.nextDouble(Angle.ofDeg(360), Angle.ofDeg(1000));
            var alt2 = rng.nextDouble(Angle.ofDeg(90+Double.MIN_VALUE), Angle.ofDeg(1000));
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = HorizontalCoordinates.of(az2, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = HorizontalCoordinates.of(0, alt2);
            });
            var az3 = rng.nextDouble(-Angle.ofDeg(1000), -Double.MIN_VALUE);
            var alt3 = rng.nextDouble(-Angle.ofDeg(1000), Angle.ofDeg(-90));
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = HorizontalCoordinates.of(az3, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                var coords = HorizontalCoordinates.of(0, alt3);
            });
        }
    }

    @Test
    void azOctantNameWorksOnKnownValues() {
        assertEquals("n", newHorizontalCoordinates(0, 0).azOctantName("n", "e", "s", "w"));
        assertEquals("ne", newHorizontalCoordinates(Angle.ofDeg(45), Angle.ofDeg(45)).azOctantName("n", "e", "s", "w"));
        assertEquals("e", newHorizontalCoordinates(Angle.ofDeg(90), 1).azOctantName("n", "e", "s", "w"));
        assertEquals("se", newHorizontalCoordinates(Angle.ofDeg(135), Angle.ofDeg(2)).azOctantName("n", "e", "s", "w"));
        assertEquals("s", newHorizontalCoordinates(Angle.ofDeg(180), Angle.ofDeg(60)).azOctantName("n", "e", "s", "w"));
        assertEquals("sw", newHorizontalCoordinates(Angle.ofDeg(225), 0).azOctantName("n", "e", "s", "w"));
        assertEquals("w", newHorizontalCoordinates(Angle.ofDeg(270), Angle.ofDeg(89)).azOctantName("n", "e", "s", "w"));
        assertEquals("nw", newHorizontalCoordinates(Angle.ofDeg(315), Angle.ofDeg(90)).azOctantName("n", "e", "s", "w"));
    }

    @Test
    void angularDistanceToWorksOnEasyValues() {
        var reference = newHorizontalCoordinates(0, Angle.ofDeg(90));
        assertEquals(0, reference.angularDistanceTo(reference));
        assertEquals(Math.PI/2, newHorizontalCoordinates(0, 0).angularDistanceTo(reference));
        assertEquals(Math.PI/2, reference.angularDistanceTo(newHorizontalCoordinates(0, 0)));
        assertEquals(Math.PI, reference.angularDistanceTo(newHorizontalCoordinates(0, Angle.ofDeg(-90))));
        assertEquals(Math.PI/4, reference.angularDistanceTo(newHorizontalCoordinates(0, Angle.ofDeg(45))), 1e-8);
        var az1 = Angle.ofDeg(0);
        var alt1 = Angle.ofDeg(45);
        var az2 = 0;
        var alt2 = Angle.ofDeg(90);
        for (int i = 0; i < 10; ++i) {
            az1 += Angle.ofDeg(i);
            az2 += Angle.ofDeg(i);
            var horiz1 = newHorizontalCoordinates(az1, alt1);
            var horiz2 = newHorizontalCoordinates(az2, alt2);
            assertEquals(Math.PI/4.0, horiz1.angularDistanceTo(horiz2), 1e-8);
        }
    }

    @Test
    void toStringWorks() {
        var horiz = HorizontalCoordinates.ofDeg(350, 7.2);
        assertEquals("(az=350.0000°, alt=7.2000°)", horiz.toString());
    }
}