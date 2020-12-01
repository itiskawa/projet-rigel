package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

class MyEclipticToEquatorialConversionTest {

    @Test
    void obliqueComputationWorks() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 9),
                LocalTime.of(00, 00),
                UTC);
        double julianDatet1 = 2455018.5;
        julianDatet1 -= 2451545;
        double T = julianDatet1 / 36525.0;
        double res = 46.815*T + 0.0006*T*T - 0.00181*T*T*T;
        res /= 3600;
        double epsExpected = (23 + 26/60.0 + 21.45/3600.0) - res;

        EclipticToEquatorialConversion eceq = new
                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 9),
                LocalTime.of(00, 00),
                UTC));
       // assertEquals(Angle.ofDeg(epsExpected), eceq.getEpsilon(), 1e-7);
    }

    @Test
    void applyWorks() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6),
                LocalTime.of(00, 00),
                UTC);
        double lambda = Angle.ofDeg(139.686111);
        double beta = Angle.ofDeg(4.875278);
        EclipticToEquatorialConversion eceq = new EclipticToEquatorialConversion(t1);
        var before = EclipticCoordinates.of(lambda, beta);
        var converted = eceq.apply(before);
        assertEquals(Angle.ofHr(9.581478), converted.lon(), 1e-7);
        System.out.println(converted.lon() + ", " + converted.lat());
        assertEquals(Angle.ofDMS(19, 32, 6.01), converted.lat(), 1e-7);
    }

}