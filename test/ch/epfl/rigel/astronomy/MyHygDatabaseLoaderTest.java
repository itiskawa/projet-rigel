package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyHygDatabaseLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String ASTERISM_DOC_NAME =
            "/asterisms.txt";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
        }
    }

    @Test
    void hygLoaderGivesCorrectDataSpecificStar() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            StarCatalogue cata = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();

            Star rigel = null;
            for (Star s : cata.stars()) {
                if (s.name().equalsIgnoreCase("rigel")) {
                    rigel = s;
                }
            }

            assertEquals(1.3724303693276385, rigel.equatorialPos().ra());
            assertEquals(-0.143145630755865, rigel.equatorialPos().dec());
            assertEquals(24436, rigel.hipparcosId());
            //assertEquals(0.180, rigel.magnitude());
        }
    }

    @Test
    void hygLoaderGivesRigelAndBetelgeuse() throws IOException{
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            try (InputStream asterStream = getClass()
                    .getResourceAsStream(ASTERISM_DOC_NAME)) {

                StarCatalogue.Builder catalogueBuild = new StarCatalogue.Builder()
                        .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                        .loadFrom(asterStream, AsterismLoader.INSTANCE);

                int rigelCount = 0;
                List<Asterism> asterismsWithRigel = new ArrayList<>();
                for (Asterism a : catalogueBuild.asterisms()) {
                    for (Star s : a.stars()) {
                        if (s.name().equalsIgnoreCase("rigel")) {
                            rigelCount += 1;
                            asterismsWithRigel.add(a);
                        }
                    }
                }

                assertEquals(2, rigelCount);

                Star betel = null;

                for (Asterism a : asterismsWithRigel) {
                    for (Star s : a.stars()) {
                        if (s.name().equalsIgnoreCase("betelgeuse")) {
                            betel = s;
                        }
                    }
                }

                assertNotNull(betel);
            }


        }
    }
}