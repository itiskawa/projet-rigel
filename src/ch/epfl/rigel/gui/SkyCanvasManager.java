package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Sky Canvas Manager - manager for the sky ; creates everything (notably links between beans & the painter, observed sky & stereographic projection)
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class SkyCanvasManager {

    private final Canvas canvas;
    private final SkyCanvasPainter skyCanvasPainter;
    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<ObservedSky> observedSky;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObservableValue<Double> mouseAzDeg;
    private final ObservableValue<Double> mouseAltDeg;
    private final ObservableValue<CelestialObject> objectUnderMouse;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;

    private final static double MAX_DISTANCE = 10;
    private final static ClosedInterval FOV_LIMIT = ClosedInterval.of(Angle.ofDeg(30), Angle.ofDeg(150));
    private final static ClosedInterval ALT_LIMIT = ClosedInterval.of(Angle.ofDeg(5), Angle.ofDeg(90));

    /*
    bonus drag and drop attributes
     */
    private Point2D dragInitCanvasCoords = new Point2D(0, 0);
    private Point2D dragEndCanvasCoords = new Point2D(0, 0);
    private HorizontalCoordinates dragInitCoord = HorizontalCoordinates.of(0, 0);
    private final SimpleBooleanProperty canvasIsPressed = new SimpleBooleanProperty();

    /**
     * constructor
     * @param starCatalogue - star catalogue to be loaded
     * @param dtb - DateTimeBean
     * @param olb - ObserverLocationBean
     * @param vpb -  ViewingParametersBean
     */
    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dtb, ObserverLocationBean olb, ViewingParametersBean vpb) {

        /*
         * creating the canvas, painter
         */
        canvas = new Canvas(400, 300);
        skyCanvasPainter = new SkyCanvasPainter(canvas);

        /*
         * stereographic projections, observed sky and transformation
         */
        projection = Bindings.createObjectBinding(() ->
                new StereographicProjection(vpb.getCenter()), vpb.centerProperty());


        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatationFactor = (canvas.getWidth() / projection.getValue().applyToAngle(Angle.ofDeg(vpb.getFOVDeg())));
                    return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor,
                            canvas.getWidth() / 2, canvas.getHeight() / 2);
                }, canvas.heightProperty(), canvas.widthProperty(), projection, vpb.fOVDegProperty());


        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dtb.getZonedDateTime(), olb.getCoordinates(),
                        projection.getValue(), starCatalogue),
                dtb.dateProperty(), dtb.timeProperty(), dtb.zoneProperty(), projection, olb.coordinatesProperty());


        /*
         * mouse events - in order: position, position in Hor, object under mouse, scroll event
         */
        CartesianCoordinates startingPosOnPlane = projection.getValue().apply(vpb.getCenter());
        Point2D startingPos = planeToCanvas.getValue().transform(startingPosOnPlane.x(), startingPosOnPlane.y());
        mousePosition = new SimpleObjectProperty<>(new Point2D(startingPos.getX(), startingPos.getY()));


        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
            try {
                Point2D inversed = planeToCanvas.getValue().inverseTransform(mousePosition.get());
                return projection.getValue().inverseApply(CartesianCoordinates.of(inversed.getX(), inversed.getY()));
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, planeToCanvas, mousePosition, projection);

        objectUnderMouse = Bindings.createObjectBinding(() -> {
            try {
                Point2D inversed = planeToCanvas.getValue().inverseTransform(mousePosition.get());
                Optional<CelestialObject> optionalCelestialObject = observedSky.getValue().objectClosestTo(CartesianCoordinates.of(inversed.getX(), inversed.getY()),
                        MAX_DISTANCE / planeToCanvas.getValue().getMxx());
                return (optionalCelestialObject.orElse(null));
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, observedSky, mousePosition, planeToCanvas);

        mouseAzDeg = Bindings.createObjectBinding(() -> {
            if (mouseHorizontalPosition.getValue() != null) {
                return mouseHorizontalPosition.getValue().azDeg();
            } else
                return Double.NaN;
        }, mouseHorizontalPosition);

        mouseAltDeg = Bindings.createObjectBinding(() -> {
            if (mouseHorizontalPosition.getValue() != null)
                return mouseHorizontalPosition.getValue().altDeg();
            else
                return Double.NaN;
        }, mouseHorizontalPosition);

        canvas.setOnMouseMoved(event -> mousePosition.set(new Point2D(event.getX(), event.getY())));

        canvas.setOnScroll(scrollEvent -> {
            if (!canvasIsPressed.get()) {
                double deltaX = scrollEvent.getDeltaX();
                double deltaY = scrollEvent.getDeltaY();

                double delta = (Math.abs(deltaX) > Math.abs(deltaY)) ? deltaX : deltaY;
                double nextFOVRad = FOV_LIMIT.clip(Angle.ofDeg(vpb.getFOVDeg() + delta));
                vpb.setFOVDeg(Angle.toDeg(nextFOVRad));
            }
        });

        canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                canvas.requestFocus();

                //drag and drop started
                canvasIsPressed.set(true);
                dragInitCanvasCoords = new Point2D(event.getX(), event.getY());
                try {
                    Point2D inversed = planeToCanvas.getValue().inverseTransform(dragInitCanvasCoords);
                    dragInitCoord = projection.getValue().inverseApply(CartesianCoordinates.of(inversed.getX(), inversed.getY()));
                } catch (NonInvertibleTransformException e) {}
            }
        });

        /*
        drag and drop listeners
         */
        canvas.setOnMouseReleased(event -> {
            canvasIsPressed.set(false);
            dragEndCanvasCoords = new Point2D(event.getX(), event.getY());
            updateProjCenter(vpb);
        });

        canvas.setOnMouseDragged(event -> {
            dragEndCanvasCoords = new Point2D(event.getX(), event.getY());
            updateProjCenter(vpb);
        });

        /*
         * keyboard events
         */
        {
            canvas.setOnKeyPressed(keyEvent -> {
                if (!canvasIsPressed.get()) {
                    double az = vpb.getCenterAz();
                    double alt = vpb.getCenterAlt();
                    switch (keyEvent.getCode()) {
                        case LEFT:
                            vpb.setCenter(HorizontalCoordinates.of(Angle.normalizePositive(az - Angle.ofDeg(10)), alt));
                            break;
                        case UP:
                            vpb.setCenter(HorizontalCoordinates.of(az, ALT_LIMIT.clip(alt + Angle.ofDeg(5))));
                            break;
                        case RIGHT:
                            vpb.setCenter(HorizontalCoordinates.of(Angle.normalizePositive(az + Angle.ofDeg(10)), alt));
                            break;
                        case DOWN:
                            vpb.setCenter(HorizontalCoordinates.of(az, ALT_LIMIT.clip(alt - Angle.ofDeg(5))));
                            break;
                    }
                }
                keyEvent.consume();
            });

            List<Observable> drawTriggers = new ArrayList<>();
            drawTriggers.add(observedSky);
            drawTriggers.add(planeToCanvas);
            drawTriggers.add(vpb.toggleAsterismProperty());
            for (Observable o : drawTriggers) {
                o.addListener((observable) -> {
                    ObservedSky sky = observedSky.getValue();
                    StereographicProjection proj = projection.getValue();
                    Transform trans = planeToCanvas.getValue();
                    skyCanvasPainter.clear();
                    skyCanvasPainter.drawStars(sky, proj, trans, vpb.getToggleAsterism());
                    skyCanvasPainter.drawPlanets(sky, proj, trans);
                    skyCanvasPainter.drawSun(sky, proj, trans);
                    skyCanvasPainter.drawMoon(sky, proj, trans);
                    skyCanvasPainter.drawHorizon(sky, proj, trans);
                });
            }

        }
    }

    // update the proj center when drag and dropped
    private void updateProjCenter(ViewingParametersBean vpb) {
        try {
            Point2D endP = planeToCanvas.getValue().inverseTransform(dragEndCanvasCoords);
            HorizontalCoordinates dragEndCoords = projection.getValue()
                    .inverseApply(CartesianCoordinates.of(endP.getX(), endP.getY()));

            double deltaAz = dragEndCoords.az() - dragInitCoord.az();
            double deltaAlt = dragEndCoords.alt() - dragInitCoord.alt();


            double az = vpb.getCenterAz();
            double alt = vpb.getCenterAlt();
            vpb.setCenter(HorizontalCoordinates.of(Angle.normalizePositive(az - deltaAz), ALT_LIMIT.clip(alt - deltaAlt)));
        } catch (NonInvertibleTransformException e) {}
    }

    /**
     * getter method for canvas
     * @return the canvas
     */
    public Canvas canvas(){
        return canvas;
    }

    /**
     * getter method for the object under the mouse
     * @return the object closest to the cursor (in a radius of MAX_DISTANCE)
     */
    public ObservableValue<CelestialObject> objectUnderMouseProperty(){
        return objectUnderMouse;
    }


    /**
     * getter for the property containing the azimuth of the mouse in horizontal coordinates (observer's view)
     * @return the mouse's azimuth
     */
    public ObservableValue<Double> getMouseAzDegProperty(){
        return mouseAzDeg;
    }

    /**
     * getter for the property containing the altitude of the mouse in horizontal coordinates (observer's view)
     * @return the mouse's altitude
     */
    public ObservableValue<Double> getMouseAltDegProperty(){
        return mouseAltDeg;
    }
}
