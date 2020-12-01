package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Star Catalogue
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class StarCatalogue {

    /**
     * declaration of private final attributes (in order)
     * list of possible stars (stars)
     * map associating asterisms to star indices (asterismsToIndexes)
     */
    private final List<Star> stars;
    private final Map<Asterism, List<Integer>> asterismsToIndexesMap;

    /**
     * public constructor
     * @param stars - list of stars
     * @param asterisms - list of asterisms
     * @throws IllegalArgumentException - if an asterism contains a star that is not in the given list of stars
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for(Asterism a : asterisms){
            Preconditions.checkArgument(stars.containsAll(a.stars()));
        }

        this.stars = List.copyOf(stars);
        Map<Star, Integer> starToIndexMap = new HashMap<>();
        for (int i = 0; i < this.stars.size(); ++i) {
            starToIndexMap.put(this.stars.get(i), i);
        }

        Map<Asterism, List<Integer>> modifAsterismIndexMap = new HashMap<>();
        for(Asterism a: asterisms){
            List<Integer> listOfIndices = new ArrayList<>();
            for(Star s : a.stars()){
                listOfIndices.add(starToIndexMap.get(s));
            }
            modifAsterismIndexMap.put(a, listOfIndices);
        }
        asterismsToIndexesMap = Map.copyOf(modifAsterismIndexMap);
    }

    /**
     * getter method for stars
     * @return an immutable List<> of stars
     */
    public List<Star> stars(){
        return this.stars;
    }

    /**
     * getter method for asterisms
     * @return Set of asterisms
     */
    public Set<Asterism> asterisms(){

        return Set.copyOf(asterismsToIndexesMap.keySet());
    }

    /**
     * getter method for indices of stars in given asterism
     * indices correspond to the order of the stars in the catalogue
     * @param asterism - the asterism whose list of star indices we want
     * @return List of integers
     * @throws IllegalArgumentException if parameter is not in list of asterisms of .this
     */
    public List<Integer> asterismIndices(Asterism asterism){
        Preconditions.checkArgument(asterismsToIndexesMap.containsKey(asterism));
        return List.copyOf(asterismsToIndexesMap.get(asterism));
    }


    /**
     * (nested static class)
     * Star Catalogue Builder
     * @author Victor Borruat (300666)
     * @author Raphaël Selz (302980)
     */
    public static final class Builder{

        /**
         * declaration of private final attributes (in order)
         * list of possible stars (stars)
         * list of asterisms (asterisms)
         */
        private final List<Star> stars;
        private final List<Asterism> asterisms;

        /**
         * default constructor method
         * sets all parameters to empty lists
         */
        public Builder(){
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }

        /**
         * public method adding a star to current list of stars in .this
         * @param star - to be added
         * @return modified .this
         */
        public Builder addStar(Star star){
            this.stars.add(star);
            return this;
        }

        /**
         * public method adding an asterism to current list of asterisms in .this
         * @param asterism - to be added
         * @return modified .this
         */
        public Builder addAsterism(Asterism asterism){
            this.asterisms.add(asterism);
            return this;
        }

        /**
         * public getter method for stars in .this
         * @return unmodifiable list of stars, but not immutable
         */
        public List<Star> stars(){
            return Collections.unmodifiableList(this.stars);
        }

        /**
         * public getter method for asterisms in .this
         * @return unmodifiable list of asterisms
         */
        public List<Asterism> asterisms(){
            return Collections.unmodifiableList(this.asterisms);
        }


        /**
         * public method to load from external documents using loader parameter's load method
         * May read either CSV documents for stars or asterisms using a Hipparcos Indice list
         *
         * @param inputStream - external document
         * @param loader - instance of Loader interface
         * @return .this with loaded document
         * @throws IOException if acting upon InputStream fails
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }
        /**
         * builder method
         * @return OuterClass of .this, using .this attributes
         */
        public StarCatalogue build(){
            return new StarCatalogue(this.stars, this.asterisms);
        }
    }

    /**
     * (nested interface)
     * Loader for external documents
     * @author Victor Borruat (300666)
     * @author Raphaël Selz (302980)
     */
    public interface Loader{

        /**
         * public abstract method to load from external documents
         * @param inputstream - external document
         * @param builder - from StarCatalogue.Builder
         * @throws IOException if acting upon InputStream fails
         */
        void load(InputStream inputstream, Builder builder) throws IOException;
    }

}
