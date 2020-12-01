package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyObservedSkyTest {

@Test
    void closestToWorksOnFramapadExample() throws IOException {
            String HYG_CATALOGUE_NAME ="/hygdata_v3.csv";
            String AST_CATALOGUE_NAME ="/asterisms.txt";
            StarCatalogue catalogue;
            ObservedSky sky;
            StereographicProjection stereo;
            GeographicCoordinates geoCoords;
            ZonedDateTime time;
            EquatorialToHorizontalConversion convEquToHor;
            EclipticToEquatorialConversion convEcltoEqu;
            StarCatalogue.Builder builder;
            try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
                builder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
            }
            InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME);
            try {
                catalogue = builder.loadFrom(astStream, AsterismLoader.INSTANCE).build();
            } finally {
                astStream.close();
            }
            time = ZonedDateTime.of(LocalDate.of(2020, Month.APRIL, 4), LocalTime.of(0, 0), ZoneOffset.UTC);
            geoCoords = GeographicCoordinates.ofDeg(30, 45);
            stereo = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
            convEquToHor = new EquatorialToHorizontalConversion(time, geoCoords);
            convEcltoEqu = new EclipticToEquatorialConversion(time);
            sky = new ObservedSky(time, geoCoords, stereo, catalogue);
            assertEquals("Tau Phe", sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time, geoCoords)
            .apply(EquatorialCoordinates.of(0.004696959812148989,-0.861893035343076))),0.1).get().name());
}
}