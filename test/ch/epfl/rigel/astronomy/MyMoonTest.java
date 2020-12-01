package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyMoonTest {

    @Test
    void constructorThrowsError(){

        assertThrows(IllegalArgumentException.class, () -> {
            Moon moonTest = new Moon(EquatorialCoordinates.of(0,0), 1.f, 1.f, -0.5f);
        });
    }


    @Test
    void toStringWorks(){
        Moon moonTest = new Moon(EquatorialCoordinates.of(0,0), 1.f, 1.f, 0.3752f);
        assertEquals("Lune (37.5%)", moonTest.info());
    }
}