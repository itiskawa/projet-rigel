package ch.epfl.rigel.gui;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * enumeration of possible time accelerators
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public enum NamedTimeAccelerator {

    /**
     * elements of the enumeration, each with characteristics designated in self-descriptive names
     */
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));

    private final String name;
    private final TimeAccelerator accelerator;
    public final static List<NamedTimeAccelerator> ALL = List.of(values());

    /**
     * constructor of the enumeration elements, assigns to an elements a name & a time accelerator
     * @param name - given name
     * @param timeAcc - the desired time accelerator
     */
    NamedTimeAccelerator(String name, TimeAccelerator timeAcc){
        this.name = name;
        accelerator = timeAcc;
    }

    /**
     * getter method for name of enumeration element
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * getter method for time accelerator of enumeration element
     * @return time accelerator
     */
    public TimeAccelerator getAccelerator(){
        return this.accelerator;
    }

    /**
     * redefinition of toString method of Object class
     * @return text representation of enumeration element
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "%s",
        this.getName());
    }
}
