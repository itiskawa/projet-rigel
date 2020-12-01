package ch.epfl.rigel.astronomy;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

class MyEpochTest {

    @Test
    void daysUntilWorks() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 01),
                LocalTime.of(12, 00),
                UTC);
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            t1 = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 01),
                    LocalTime.of(12, 00),
                    UTC);
            long days = rng.nextLong(0, 500);
            long hours = rng.nextLong(0, 24);
            long minut = rng.nextLong(0, 60);
            t1 = t1.plusDays(days).plusHours(hours).plusMinutes(minut);

            assertEquals(days+(hours/24.0)+(minut/(60.0*24.0)), Epoch.J2000.daysUntil(t1), 1e-7);
        }
        ZonedDateTime t2 = ZonedDateTime.of(LocalDate.of(2010, Month.DECEMBER, 31),
                LocalTime.of(00, 00),
                UTC);
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            t2 = ZonedDateTime.of(LocalDate.of(2009, Month.DECEMBER, 31),
                    LocalTime.of(00, 00),
                    UTC);
            long days = rng.nextLong(0, 500);
            long hours = rng.nextLong(0, 24);
            long minut = rng.nextLong(0, 60);
            t2 = t2.plusDays(days).plusHours(hours).plusMinutes(minut);

            assertEquals(days+(hours/24.0)+(minut/(60.0*24.0)), Epoch.J2010.daysUntil(t2), 1e-7);
        }

    }

    @Test
    void julianCenturiesUntil() {
        ZonedDateTime t1 = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 01),
                LocalTime.of(12, 00),
                UTC);
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            t1 = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 01),
                    LocalTime.of(12, 00),
                    UTC);
            long days = rng.nextLong(-500, 500);

            t1 = t1.plusDays(days);

            assertEquals(days/36525.0, Epoch.J2000.julianCenturiesUntil(t1), 1e-7);
        }
        assertEquals(0, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 01),
                LocalTime.of(12, 00),
                UTC)));
    }
}