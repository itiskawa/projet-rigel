package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MyStereographicProjectionTest {

    @Test
    void applyWorksOnKnownValue(){
        double y = new StereographicProjection(HorizontalCoordinates.ofDeg(45, 45)).apply(HorizontalCoordinates.ofDeg(45, 30)).y();
        assertEquals(-0.13165249758739583, y, 1e-7);
    }

    @Test
    void applyWorksOnTrivialValues() {
        var horiz = HorizontalCoordinates.ofDeg(34.4, 53.011101);
        StereographicProjection sP = new StereographicProjection((horiz));
        assertEquals(0, sP.apply(horiz).y());
        assertEquals(0, sP.apply(horiz).x());
    }

    @Test
    void inverseApplyWorksOnKnowValue(){
        double az = new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).inverseApply(CartesianCoordinates.of(10, 0)).az();
        assertEquals(3.648704634091, az, 1e-7);
    }

    @Test
    void applyToAngleWorksOnKnownValue(){
        double ang = new StereographicProjection(HorizontalCoordinates.ofDeg(23, 45)).applyToAngle(Angle.ofDeg(1/2.0));
        assertEquals(0.00436333005, ang, 1e-7);
    }

    @Test
    void circleCenterWorksOnKnownValue(){
        double y = new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).circleCenterForParallel(HorizontalCoordinates.ofDeg(0, 27)).y();
        assertEquals(0.6089987400, y, 1e-7);
    }

    @Test
    void circleCenterInfty() {
        var horiz = HorizontalCoordinates.ofDeg(0, 0);
        var sP = new StereographicProjection(horiz);
        System.out.println(sP.circleCenterForParallel(horiz));
    }

    @Test
    void circleRadiusWorksOnKnownValue(){
        double ang = new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).circleRadiusForParallel(HorizontalCoordinates.ofDeg(0, 27));
        assertEquals(0.7673831803, ang, 1e-7);
    }
}