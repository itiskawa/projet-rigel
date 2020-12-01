package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyPlanetModelTest {

    @Test
    void atPositionsWorksWithBookJupiterValues(){
        double h = PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        double d = PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();
        assertEquals(6.356635506685756, d , 1e-7);
        assertEquals(11.187154934709678, h ,  1e-7);
    }

    @Test
    void atPositionsWorksWithBookMercuryValues() {
        double ra = PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        double dec = PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();

        var eclip = EclipticCoordinates.of(Angle.ofDeg(253.929758), Angle.ofDeg(-2.044057));
        var converter = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));
        var equat = converter.apply(eclip);

        assertEquals(-24.500872462861143, dec, 1e-7);
        assertEquals(16.820074565897194, ra, 1e-7);
    }

    @Test
    void atGivesCorrectAngSizeForKnownValues(){
        double expectedSize = Angle.toDeg(PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER,
                        22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).angularSize())*3600;
        assertEquals(35.11141185362771, expectedSize, 1e-7);
    }

    @Test
    void atGivesCorrectMagnitudeWithKnownValue(){
        double expectedMag = PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER,
                        22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).magnitude();
        assertEquals(-1.9885659217834473, expectedMag, 1e-7);
    }

    @Test
    void atBookJupiterExampleWithConversion() {
        EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(
                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));

        double h = PlanetModel.JUPITER.at(-2231.0,
                conv).equatorialPos().ra();
        double d = PlanetModel.JUPITER.at(-2231.0,
                conv).equatorialPos().dec();

        var eclLon = Angle.ofDeg(166.310510);
        var eclLat = Angle.ofDeg(1.036466);
        var eclCoords = EclipticCoordinates.of(eclLon, eclLat);
        var equat = conv.apply(eclCoords);
        assertEquals(equat.ra(), h, 1e-8);
        assertEquals(equat.dec(), d, 1e-8);

    }

    @Test
    void atBookMercuryExampleWithConversion() {
        EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(
                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));

        var actualEquat = PlanetModel.MERCURY.at(-2231.0, conv).equatorialPos();

        double ra = actualEquat.ra();
        double dec = actualEquat.dec();

        //expected
        var eclLon = Angle.ofDeg(253.929758);
        var eclLat = Angle.ofDeg(-2.044057);
        var eclCoords = EclipticCoordinates.of(eclLon, eclLat);
        var expectedEquat = conv.apply(eclCoords);

        assertEquals(expectedEquat.ra(), ra, 1e-8);
        assertEquals(expectedEquat.dec(), dec, 1e-8);
    }
}