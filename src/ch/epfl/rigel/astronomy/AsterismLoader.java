package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader for asterisms from external document
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum AsterismLoader implements StarCatalogue.Loader {

    /**
     * single element of enumeration
     */
    INSTANCE;

    /**
     * redefinition of load method from Loader (super -) interface
     * reads a list of Hipparcos Indices & creates an asterism with corresponding stars
     * each "line" of the document represents a different asterism
     * adds to StarCatalogue.Builder parameter the created asterisms
     * @param inputstream - the document
     * @param builder - from StarCatalogue.Builder
     * @throws IOException - if acting upon the InputStream fails
     */
    @Override
    public void load(InputStream inputstream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.US_ASCII))) {
            String line;

            Map<Integer, Star> hipparcosToStar = new HashMap<>();

            for(Star s : builder.stars()){
                hipparcosToStar.putIfAbsent(s.hipparcosId(), s);
            }

            while((line = r.readLine()) != null){
                String[] parts = line.split(",");
                boolean missingStar = false;
                List<Star> starList = new ArrayList<>();


                for (String part : parts) {
                    int currentID = Integer.parseInt(part);
                    if (hipparcosToStar.containsKey(currentID)) {
                        starList.add(hipparcosToStar.get(currentID));
                    } else {
                        missingStar = true;
                        break;
                    }
                }
                if(!missingStar){
                    builder.addAsterism(new Asterism(starList));
                }
            }
        }
    }
}
