package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

class MySiderealTimeTest {

    private static double hmsToRad(int h, int m, double s) {
        assert 0 <= h && 0 <= m && m < 60 && 0 <= s && s < 60;
        var hr = h + (m / 60d) + (s / 3600d);
        return Math.toRadians(hr * 15d);
    }

    @Test
    void greenwichWorks() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670000000),
                UTC);
        double result = SiderealTime.greenwich(t1);
        assertEquals(Angle.ofHr(4.668120), result, 1e-6);

        ZonedDateTime t2 = ZonedDateTime.of(LocalDate.of(2004, Month.SEPTEMBER, 23),
                LocalTime.of(11, 0, 0), UTC);
        double res2 = SiderealTime.greenwich(t2);
        assertEquals(2.9257399567031235, res2, 1e-7);
    }

    @Test
    void localWorks() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000), ZoneOffset.UTC);
        double res = SiderealTime.local(t1, GeographicCoordinates.ofDeg(-64, 47));
        assertEquals(hmsToRad(0, 24, 5.23), res, hmsToRad(0, 0, 0.01 / 2));
    }


}