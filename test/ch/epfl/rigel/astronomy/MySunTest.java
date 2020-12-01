package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySunTest {

    @Test
    void constructorThrowNPE(){
        assertThrows(NullPointerException.class, () ->{
            new Sun(null, EquatorialCoordinates.of(0, 0), 0f, 0f);
        });
    }
}