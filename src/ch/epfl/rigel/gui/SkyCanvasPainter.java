package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.Iterator;
import java.util.List;

/**
 * Painter of the sky on a canvas
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class SkyCanvasPainter {
    private final Canvas canvas;
    private static final ClosedInterval MAG_LIMIT = ClosedInterval.of(- 2, 5);
    private static final double SUN_HALO_OPACITY_FACTOR = 0.25;
    private static final double SUN_HALO_DIAMETER_FACTOR = 2.2;

    /**
     * public constructor
     * @param canvas - canvas to paint on
     */
    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
    }

    private double computeStarAndPlanetDiameter(double magnitude, StereographicProjection sterProj) {
        double newMag = MAG_LIMIT.clip(magnitude);
        return (99 - 17 * newMag) / 140 * sterProj.applyToAngle(Angle.ofDeg(0.5));
    }


    /**
     * painting method to clear the canvas and go back to a "black screen"
     */
    public void clear(){
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * painting method to draw the moon according to its position
     * @param o - the whole observed sky
     * @param str - the projection used
     * @param t - the transformation from projection to the canvas
     */
    public void drawMoon(ObservedSky o, StereographicProjection str, Transform t) {
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();

        double x = o.moonPosition().x();
        double y = o.moonPosition().y();
        Point2D p = t.transform(x, y);

        double diameter = str.applyToAngle(o.moon().angularSize()) * t.getMxx();

        ctx.setFill(Color.WHITE);
        ctx.fillOval(p.getX() - diameter / 2,p.getY() - diameter / 2, diameter, diameter);
    }

    /**
     * painting method to draw the sun according to its position
     * @param o - the whole observed sky
     * @param str - the projection used
     * @param t - the transformation from projection to the canvas
     */
    public void drawSun(ObservedSky o, StereographicProjection str, Transform t) {
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();

        double x = o.sunPosition().x();
        double y = o.sunPosition().y();

        double diameter = str.applyToAngle(o.sun().angularSize()) * t.getMxx();

        Point2D sunPoint = t.transform(x, y);
        x = sunPoint.getX();
        y = sunPoint.getY();

        ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, SUN_HALO_OPACITY_FACTOR));
        double thirdDiscDiameter = diameter * SUN_HALO_DIAMETER_FACTOR;
        ctx.fillOval(x - thirdDiscDiameter / 2, y - thirdDiscDiameter / 2, thirdDiscDiameter, thirdDiscDiameter);

        ctx.setFill(Color.YELLOW);
        double secondDiscDiameter = diameter + 2;
        ctx.fillOval(x - secondDiscDiameter / 2, y - secondDiscDiameter / 2, secondDiscDiameter, secondDiscDiameter);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
    }

    /**
     * painting method to draw the planets according to their position
     * @param o - the whole observed sky
     * @param str - the projection used
     * @param t - the transformation from projection to the canvas
     */
    public void drawPlanets(ObservedSky o, StereographicProjection str, Transform t) {
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();

        int i = 0;
        for (Planet p : o.planets()) {
            double x =  o.planetPositions()[2 * i];
            double y = o.planetPositions()[2 * i + 1];

            Point2D planetPoint = t.transform(new Point2D(x, y));
            double diameter = computeStarAndPlanetDiameter(p.magnitude(), str) * t.getMxx();

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(planetPoint.getX() - diameter / 2, planetPoint.getY() - diameter / 2, diameter, diameter);

            ++i;
        }
    }

    /**
     * painting method to draw the stars according to their position and the asterisms
     * @param o - the whole observed sky
     * @param str - the projection used
     * @param t - the transformation from projection to the canvas
     */
    public void drawStars(ObservedSky o, StereographicProjection str, Transform t, boolean toggleAsterisms){
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();

        Bounds canvasBounds = canvas.getBoundsInLocal();
        List<Star> starList = o.stars();
        if (toggleAsterisms) {
            for (Asterism a : o.asterisms()) {
                List<Integer> indices = o.asterismIndices(a);
                ctx.beginPath();

                Iterator<Integer> iter = indices.iterator();
                Point2D lastPoint = null;
                while (iter.hasNext()) {
                    int hipparcosFirst = starList.get(iter.next()).hipparcosId();
                    CartesianCoordinates firstCoords = o.starPosition(hipparcosFirst);
                    Point2D p1 = t.transform(firstCoords.x(), firstCoords.y());

                    if (lastPoint != null) {
                        if (canvasBounds.contains(p1) || canvasBounds.contains(lastPoint)) {
                            ctx.moveTo(p1.getX(), p1.getY());
                            ctx.lineTo(lastPoint.getX(), lastPoint.getY());
                        }
                    }

                    lastPoint = p1;
                }
                ctx.setLineWidth(1);
                ctx.setStroke(Color.BLUE);
                ctx.stroke();
            }
        }

        for (Star s : starList){
            double x =  o.starPosition(s.hipparcosId()).x();
            double y = o.starPosition(s.hipparcosId()).y();

            double size = computeStarAndPlanetDiameter(s.magnitude(), str) * t.getMxx();

            Point2D p = t.transform(x, y);

            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));
            ctx.fillOval(p.getX() - size/2, p.getY() - size/2, size, size);
        }

    }

    /**
     * painting method to draw the horizon and cardinal points
     * @param o - the whole observed sky
     * @param str - the projection used
     * @param t - the transformation from projection to the canvas
     */
    public void drawHorizon(ObservedSky o, StereographicProjection str, Transform t) {
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();


        HorizontalCoordinates pointOnHorizon = HorizontalCoordinates.ofDeg(0, 0);

        CartesianCoordinates horizCenter = str.circleCenterForParallel(pointOnHorizon);
        double horizonRadius = Math.abs(str.circleRadiusForParallel(pointOnHorizon)) * t.getMxx();

        Point2D transfCenter = t.transform(horizCenter.x(), horizCenter.y());

        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(transfCenter.getX() - horizonRadius,
                transfCenter.getY() - horizonRadius,
                horizonRadius * 2,
                horizonRadius * 2);


        for (int i = 0; i < 8; ++i) {
            HorizontalCoordinates h = HorizontalCoordinates.ofDeg(45 * i, - 0.5);
            CartesianCoordinates projected = str.apply(h);
            Point2D transformed = t.transform(projected.x(), projected.y());
            Text cardinalText = new Text(h.azOctantName("N", "E", "S", "O"));
            cardinalText.setFill(Color.RED);
            ctx.setFill(Color.RED);
            ctx.setTextBaseline(VPos.TOP);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.fillText(cardinalText.getText(), transformed.getX(), transformed.getY());
        }
    }
}
