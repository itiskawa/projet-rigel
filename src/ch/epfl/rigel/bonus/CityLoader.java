package ch.epfl.rigel.bonus;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * ** BONUS **
 * Loader for the list of cities - analogue (almost parallel) in form to HygDatabaseLoader & AsterismLoader
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum CityLoader {

    /**
     * unique element of the enumeration
     */
    INSTANCE;

    /**
     * loads the name, country, and coordinates of each city present in the read document
     * @param inputStream - the document
     * @param builder - from CityCatalogue.Builder
     * @throws IOException - if acting upon the InputStream fails
     */
    public void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {

            String line = r.readLine();
            while ((line = r.readLine()) != null) {

                String[] parts = line.split(",");
                GeographicCoordinates coordinates =
                        GeographicCoordinates.ofDeg(Double.parseDouble(parts[3].substring(1, parts[3].length() - 1)),
                                Double.parseDouble(parts[2].substring(1, parts[2].length() - 1)));

                City c = new City(parts[1].substring(0, parts[1].length() - 1) + "  - " + parts[4].substring(1), coordinates);

                builder.addCity(c);
            }

        }
    }
}
