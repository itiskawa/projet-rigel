package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCelestialObjectTest {

    @Test
    void ConstructorThrowsIfNameEmpty(){
        assertThrows(NullPointerException.class, () -> {
            new Planet(null, EquatorialCoordinates.of(0,0), 0f, 0f);
        });
    }

    @Test
    void ConstructorThrowsWhenPosEmpty(){
        assertThrows(NullPointerException.class, () ->{
            new Planet("Venus", null, 0f, 0f);
        });
    }

    @Test
    void ConstructorThrowsIAE(){
        assertThrows(IllegalArgumentException.class, () ->{
            new Planet("Venus", EquatorialCoordinates.of(0,0), -1f, 0f);
                });
    }

}