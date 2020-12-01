package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStarTest {

    @Test
    void colorTemperatureWorksOnKnownValue(){
        double rigelColor = new Star(24436, "Rigel",
                EquatorialCoordinates.of(0, 0), 0, -0.03f)
                .colorTemperature();
        double betelgeuseColor = new Star(27989, "Betelgeuse",
                EquatorialCoordinates.of(0, 0), 0, 1.50f)
                .colorTemperature();
        assertEquals(rigelColor, 10515, 1e-7);
        assertEquals(betelgeuseColor, 3793);
    }
}