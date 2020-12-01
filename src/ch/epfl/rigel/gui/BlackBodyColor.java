package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class (non-instantiable) to get colours corresponding to given temperatures
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public class BlackBodyColor {
    private BlackBodyColor() {}


    private static Map<Integer, String> setColorTable() {
        try (InputStream bbrStream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt")) {
            BufferedReader r = new BufferedReader(new InputStreamReader(bbrStream, StandardCharsets.US_ASCII));

            String line;
            Map<Integer, String> table = new HashMap<>();
            while ((line = r.readLine()) != null) {
                if (line.charAt(0) != '#' && line.contains("10deg")) {
                    String[] splitLine = line.split(" ");
                    for (int i = 0; i < splitLine.length; ++i) {
                        if (splitLine[i].equals("K")) {
                            table.put(Integer.parseInt(splitLine[i-1]), splitLine[splitLine.length - 1]);
                            break;
                        }
                    }
                }
            }
            return table;
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * maps temperatures (in int, hundred by hundred), to colours (as hexadecimal strings)
     */
    private static final Map<Integer, String> tempToColorTable = setColorTable();


    /**
     * converter method, gives JavaFX color corresponding to the given temperature
     * @param kelvinTemp - read temperature, in Kelvins
     * @return corresponding JavaFX color
     * @throws IllegalArgumentException if the temperature is under 1000 kelvin
     */
    public static Color colorForTemperature(double kelvinTemp) {
        Preconditions.checkArgument(kelvinTemp >= 1000);

        kelvinTemp = Math.round(kelvinTemp / 100.0);
        int closestHundredTemp = ((int)kelvinTemp) * 100;

        String hexaColor = BlackBodyColor.tempToColorTable.get(closestHundredTemp);

        return Color.web(hexaColor);
    }
}