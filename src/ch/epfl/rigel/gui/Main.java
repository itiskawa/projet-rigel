package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.bonus.CityCatalogue;
import ch.epfl.rigel.bonus.CityLoader;
import ch.epfl.rigel.bonus.PredictiveTextField;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Main class of the program
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class Main extends Application {

    private final StarCatalogue catalogue;
    private final CityCatalogue cityCatalogue;
    private final Font fontAwesome;

    private final static GeographicCoordinates INIT_POSITION = GeographicCoordinates.ofDeg(6.57, 46.52);
    private final static HorizontalCoordinates INIT_PROJ_CENTER = HorizontalCoordinates.ofDeg(180.000000000001, 15);

    /**
     * main constructor class, used to instantiate the StarCatalogue
     * @throws IOException - if any sort of Exception occurs
     */
    public Main() throws IOException{
        try (InputStream hs = getClass()
                .getResourceAsStream("/hygdata_v3.csv")) {
            try (InputStream asterStream = getClass()
                    .getResourceAsStream("/asterisms.txt")) {
                catalogue = new StarCatalogue.Builder()
                        .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                        .loadFrom(asterStream, AsterismLoader.INSTANCE)
                        .build();
            }
        }
        try(InputStream worldcities = getClass()
                .getResourceAsStream("/worldcities.csv")) {

            cityCatalogue = new CityCatalogue.Builder()
                    .loadFrom(worldcities, CityLoader.INSTANCE)
                    .build();
        }
        try (InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            fontAwesome = Font.loadFont(fontStream, 15);
        }
    }

    /**
     * main method : launches the program
     * @param args - arguments to be launched
     */
    public static void main(String[] args) {launch(args);}

    /**
     * start method
     * build the main window (and secondary window)
     * @param primaryStage - the main window that will upon launching the program
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Rigel");

        /*
        creating the beans
         */
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZone(ZoneId.systemDefault());
        dateTimeBean.setDate(LocalDate.now());
        dateTimeBean.setTime(LocalTime.now());

        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(INIT_POSITION);

        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(INIT_PROJ_CENTER);
        viewingParametersBean.setFOVDeg(100);
        viewingParametersBean.setToggleAsterism(true);

        /*
         * setting sky canvas manager, canvas, and animator
         */
        SkyCanvasManager skyCanvasManager = new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);

        Canvas skyCanvas = skyCanvasManager.canvas();

        Pane middle = new Pane(skyCanvas);
        middle.setMinHeight(skyCanvas.getHeight());
        middle.setMinWidth(skyCanvas.getWidth());

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

        /*
         * top bar
         */
        Stage citySelection = new Stage();
        HBox controlBar = new HBox();
        controlBar.getChildren().addAll(makeTopLeftBox(observerLocationBean, citySelection), new Separator(Orientation.VERTICAL),
                makeTopCenterBox(dateTimeBean, timeAnimator), new Separator(Orientation.VERTICAL), makeTopRightBox(dateTimeBean, timeAnimator, skyCanvas),
                new Separator(Orientation.VERTICAL), asterismToggleChoice(viewingParametersBean));
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        /*
         * making bottom info
         */
        BorderPane infoBox = new BorderPane(objectUnderMouse(skyCanvasManager), null, mousePos(skyCanvasManager), null, fov(viewingParametersBean));
        infoBox.setStyle("-fx-padding: 4; -fx-background-color: white;");

        /*
         * creating new stage to put city selection
         */

        citySelection.setTitle("Sélection de la position");
        BorderPane root2 = new BorderPane(makePredictiveTextField(observerLocationBean), new Label("Choix de la position d'observation par ville : "),
                null, null, null);
        citySelection.setMinWidth(430);
        citySelection.setMinHeight(80);
        citySelection.setScene(new Scene(root2));

        /*
         * arranging the children onto the root
         */
        BorderPane root = new BorderPane();
        root.setCenter(middle);
        root.setTop(controlBar);
        root.setBottom(infoBox);

        /*
         * final touches for visual aspects
         */
        primaryStage.setMinWidth(1232);
        primaryStage.setMinHeight(600);

        skyCanvas.widthProperty().bind(middle.widthProperty());
        skyCanvas.heightProperty().bind(middle.heightProperty());



        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        skyCanvas.requestFocus();
    }

    /*
     * all building methods
     */
    private HBox makeTopLeftBox(ObserverLocationBean observerLocationBean, Stage citySelection) {

        TextField lonTextField = createTextField(true, observerLocationBean);
        TextField latTextField = createTextField(false, observerLocationBean);

        Label longitudeLabel = new Label("Longitude (˚) :");
        Label latitudeLabel = new Label("Latitude (˚) :");

        HBox topLeftBox = new HBox(citySelectionToggle(citySelection), longitudeLabel, lonTextField, latitudeLabel, latTextField);
        topLeftBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return topLeftBox;
    }

    private TextField createTextField(boolean createLongitude, ObserverLocationBean observerLocationBean) {
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newCoordValue =
                        stringConverter.fromString(newText).doubleValue();
                boolean validCoord = createLongitude ? GeographicCoordinates.isValidLonDeg(newCoordValue)
                        : GeographicCoordinates.isValidLatDeg(newCoordValue);
                return validCoord
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> coordextFormatter =
                new TextFormatter<>(stringConverter, 0, filter);

        TextField coordTextField =
                new TextField();
        coordTextField.setTextFormatter(coordextFormatter);

        coordTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        if (createLongitude) {
            coordextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());
        }
        else {
            coordextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());
        }
        return coordTextField;
    }

    private HBox makeTopCenterBox(DateTimeBean dateTimeBean, TimeAnimator timeAnimator){

        Label date = new Label("Date :");
        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
        datePicker.setStyle("-fx-pref-width: 120;");

        Label heure = new Label("Heure :");
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(stringConverter);
        TextField timeTextField = new TextField();
        timeTextField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());

        timeTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");


        Set<String> sortedZonedIds = new TreeSet<>(ZoneId.getAvailableZoneIds());
        List<ZoneId> zoneIdList = new ArrayList<>();
        for(String s : sortedZonedIds){ zoneIdList.add(ZoneId.of(s)); }

        ComboBox<ZoneId> zoneMenu = new ComboBox<>(FXCollections.observableArrayList(zoneIdList));
        zoneMenu.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        zoneMenu.setStyle("-fx-pref-width: 180;");

        datePicker.disableProperty().bind(timeAnimator.runningProperty());
        timeTextField.disableProperty().bind(timeAnimator.runningProperty());
        zoneMenu.disableProperty().bind(timeAnimator.runningProperty());

        HBox middleBox = new HBox(date, datePicker, heure, timeTextField, zoneMenu);
        middleBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return middleBox;
    }

    private HBox makeTopRightBox(DateTimeBean dateTimeBean, TimeAnimator timeAnimator, Canvas skyCanvas) {
        ChoiceBox<NamedTimeAccelerator> acceleratorChoiceBox = new ChoiceBox<>();

        acceleratorChoiceBox.setItems(FXCollections.observableList(NamedTimeAccelerator.ALL));
        acceleratorChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.timeAcceleratorProperty().bind(Bindings.select(acceleratorChoiceBox.valueProperty(), "accelerator"));

        String resetUnicode = "\uf0e2";
        String playUnicode = "\uf04b";
        String pauseUnicode = "\uf04c";

        Button resetButton = new Button(resetUnicode);
        resetButton.setFont(fontAwesome);

        resetButton.setOnAction(event -> {
            dateTimeBean.setZone(ZoneId.systemDefault());
            dateTimeBean.setTime(LocalTime.now());
            dateTimeBean.setDate(LocalDate.now());
        });

        Button playPauseButton = new Button(playUnicode);
        playPauseButton.setFont(fontAwesome);

        playPauseButton.setOnAction(event -> pausePlayAction(playPauseButton, playUnicode, pauseUnicode, timeAnimator));

        skyCanvas.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.SPACE){
                pausePlayAction(playPauseButton, playUnicode, pauseUnicode, timeAnimator);
            }
        });

        resetButton.disableProperty().bind(timeAnimator.runningProperty());
        acceleratorChoiceBox.disableProperty().bind(timeAnimator.runningProperty());

        HBox topRightBox = new HBox(acceleratorChoiceBox, resetButton, playPauseButton);
        topRightBox.setStyle("-fx-spacing: inherit;");

        return topRightBox;
    }

    private void pausePlayAction(Button playPauseButton, String playString, String pauseString, TimeAnimator timeAnimator) {
        if (timeAnimator.getRunning()) {
            timeAnimator.stop();
            playPauseButton.setText(playString);
        }
        else {
            timeAnimator.start();
            playPauseButton.setText(pauseString);
        }
    }

    private Text fov(ViewingParametersBean viewingParametersBean){
        Text fovText = new Text();
        fovText.setText(String.format(Locale.ROOT, "Champ de vue : %.1f°", viewingParametersBean.getFOVDeg()));
        viewingParametersBean.fOVDegProperty().addListener((observable, oldValue, newValue) ->
                fovText.setText(String.format(Locale.ROOT, "Champ de vue : %.1f°", newValue.doubleValue())));
        return fovText;
    }

    private Text objectUnderMouse(SkyCanvasManager skyCanvasManager){
        Text t = new Text();
        skyCanvasManager.objectUnderMouseProperty().addListener(
                (p, o, n) -> {
                    if (n != null){
                        t.setText(n.info());
                    } else {
                        t.setText("");
                    }
                });
        return t;
    }

    private Text mousePos(SkyCanvasManager skyCanvasManager){
        Text pos = new Text();
        skyCanvasManager.getMouseAltDegProperty().addListener((observable, oldValue, newValue) ->
                pos.setText(String.format(Locale.ROOT, "Azimut : %.2f, hauteur : %.2f", skyCanvasManager.getMouseAzDegProperty().getValue(), newValue)));
        skyCanvasManager.getMouseAzDegProperty().addListener((observable, oldValue, newValue) ->
                pos.setText(String.format(Locale.ROOT, "Azimut : %.2f, hauteur : %.2f", newValue, skyCanvasManager.getMouseAltDegProperty().getValue())));
        return pos;
    }

    private HBox asterismToggleChoice(ViewingParametersBean viewingParametersBean){
        Label asterisms = new Label("Asterismes :");
        String toggleOnUniCode = "\uf205";
        String toggleOffUniCode = "\uf204";
        Button toggle = new Button(toggleOnUniCode);
        toggle.setFont(fontAwesome);
        toggle.setOnAction( e-> {
            if(viewingParametersBean.getToggleAsterism()){
                toggle.setText(toggleOffUniCode);
                viewingParametersBean.setToggleAsterism(false);
            }
            else{
                toggle.setText(toggleOnUniCode);
                viewingParametersBean.setToggleAsterism(true);
            }
        });
        HBox toggleChoice = new HBox(asterisms, toggle);
        toggleChoice.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return toggleChoice;
    }

    private HBox citySelectionToggle(Stage citySelection){
        Button chooseCity = new Button("Choisir la ville");
        chooseCity.setOnAction( e -> {
            citySelection.show();
            citySelection.requestFocus();

        });
        HBox cityBox = new HBox(chooseCity);
        cityBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return cityBox;
    }

    private PredictiveTextField makePredictiveTextField(ObserverLocationBean observerLocationBean) {
        List<String> cityList = new FilteredList<>(FXCollections.observableList(cityCatalogue.cityNames()), p -> true);
        PredictiveTextField pred = new PredictiveTextField(Set.copyOf(cityList));

        pred.textProperty().setValue("EPFL - SWITZERLAND");
        ObservableValue<GeographicCoordinates> geographicCoordsLink = Bindings.createObjectBinding(() -> {
            double lonActual = cityCatalogue.cityCoords().get(pred.textProperty().getValue()).lonDeg();
            double latActual = cityCatalogue.cityCoords().get(pred.textProperty().getValue()).latDeg();
            return GeographicCoordinates.ofDeg(lonActual, latActual);
        }, pred.textProperty());

        pred.textProperty().addListener((observable, oldValue, newValue) ->{
            if(cityList.contains(pred.getText())) {
                observerLocationBean.setCoordinates(geographicCoordsLink.getValue());
            }
        });

        return pred;
    }

}
