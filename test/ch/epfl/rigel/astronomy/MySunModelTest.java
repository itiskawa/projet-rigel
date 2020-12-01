package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MySunModelTest {

    @Test
    void atWorksWithKnownValues(){
        double d = SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,
                Month.FEBRUARY, 27),
                LocalTime.of(0,0),
                ZoneOffset.UTC))).equatorialPos().ra();
        double d2 = SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY,
                27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        double d3 =  SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY,
                27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();

        assertEquals(d, 5.9325494700300885, 1e-7);
        assertEquals(d2, 8.392682808297807, 1e-7);
        assertEquals(d3, 19.35288373097352, 1e-7);


    }


    @Test
    void sunAngSizeWorks(){
        ZonedDateTime zone1988 = ZonedDateTime.of(
                LocalDate.of(1988,Month.JULY,27),
                LocalTime.of(0,0),ZoneOffset.UTC);
        double aS = SunModel.SUN.at(Epoch.J2010.daysUntil(zone1988),new EclipticToEquatorialConversion(zone1988)).angularSize();
        assertEquals(0.009162353351712227 , aS, 1e-7);
    }
}