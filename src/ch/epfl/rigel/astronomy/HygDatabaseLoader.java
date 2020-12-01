package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Loader for Stars from an external CSV text document
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {

    /**
     * single element of the enumeration
     */
    INSTANCE;

    /**
     * redefinition of load method from Loader (super -) interface
     * reads the 37 columns of formatted CSV document
     * retains only name, hipparcosId, magnitude, coordinates & color index
     *
     * creates corresponding stars, which will be added to the StarCatalogue.Builder (parameter)
     * @param inputstream - the document
     * @param builder - from StarCatalogue.Builder
     * @throws IOException received from "try with resources" bloc, in which we declare & initialise the Reader
     */
    @Override
    public void load(InputStream inputstream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.US_ASCII))) {

            String line = r.readLine();
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");

                int hipparcosId = 0;
                String name = "";
                EquatorialCoordinates eq = EquatorialCoordinates.of(Double.parseDouble(parts[23]), Double.parseDouble(parts[24]));
                float magnitude = 0;
                float colorIndex = 0;

                if (!(parts[1].isBlank())){
                    hipparcosId = Integer.parseInt(parts[1]);
                }

                if(!(parts[6].isBlank())){
                    name += parts[6];
                }
                else if(!(parts[27].isBlank())){
                    name += parts[27] + " " + parts[29];
                }
                else{
                    name += "?" + " " + parts[29];
                }

                if(!(parts[13].isBlank())){
                   magnitude = Float.parseFloat(parts[13]);
                }

                if(!(parts[16].isBlank())){
                    colorIndex = (float)Double.parseDouble(parts[16]);
                }

                Star s = new Star(hipparcosId, name, eq, magnitude, colorIndex);
                builder.addStar(s);
            }
        }
    }
}