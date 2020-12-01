package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static org.junit.jupiter.api.Assertions.*;

class MyMoonModelTest {

    @Test
    void atWorksForKnownValue() {
        double ra = MoonModel.MOON.at(-2313,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,
                        Month.SEPTEMBER, 1),
                        LocalTime.of(0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        double dec = MoonModel.MOON.at(-2313,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,
                        Month.SEPTEMBER, 1),
                        LocalTime.of(0, 0), ZoneOffset.UTC))).equatorialPos().dec();
        assertEquals(14.211456457836277, ra, 1e-10);
        assertEquals(-0.20114171346019355, dec, 1e-10);
    }

    @Test
    void atWorksForProfExample() {
        double eclLon = Angle.ofDeg(214.862515);
        double eclLat = Angle.ofDeg(1.716257);
        var ecliptic = EclipticCoordinates.of(eclLon, eclLat);

        var zdt = ZonedDateTime.of(LocalDate.of(2003,
                Month.SEPTEMBER, 1),
                LocalTime.of(0, 0), ZoneOffset.UTC);

        var eclToEquat = new EclipticToEquatorialConversion(zdt);
        var expectedEquat = eclToEquat.apply(ecliptic);

        double actualRa = MoonModel.MOON.at(-2313, eclToEquat).equatorialPos().ra();
        double actualDec = MoonModel.MOON.at(-2313, eclToEquat).equatorialPos().dec();

        assertEquals(expectedEquat.ra(), actualRa, 1e-8);
        assertEquals(expectedEquat.dec(), actualDec, 1e-8);
    }

    @Test
    void atGivesCorrectAngSizeKnownValue() {
        double aS = MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize();

        assertEquals(0.009225908666849136, aS, 1e-10);
    }

    @Test
    void atGivesCorrectPhaseKnownValue() {
        String info = MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
                LocalTime.of(0, 0),ZoneOffset.UTC))).
                info();

        assertEquals("Lune (22.5%)", info);
    }
}