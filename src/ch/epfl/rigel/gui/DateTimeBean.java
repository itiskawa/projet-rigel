package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * bean for a zoneDateTime
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class DateTimeBean {

    private final ObjectProperty<LocalDate> dateProperty;
    private final ObjectProperty<LocalTime> timeProperty;
    private final ObjectProperty<ZoneId> zoneProperty;

    /**
     * public default constructor, initialization of properties
     */
    public DateTimeBean(){
        this.dateProperty = new SimpleObjectProperty<>();
        this.timeProperty = new SimpleObjectProperty<>();
        this.zoneProperty = new SimpleObjectProperty<>();
    }

    /**
     * getter for the property containing the LocalDate
     * @return dateProperty
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return dateProperty;
    }

    /**
     * getter method for the LocalDate
     * @return - the stored LocalDate
     */
    public LocalDate getDate() {
        return dateProperty.get();
    }

    /**
     * setter method to modify the contained LocalDate
     * @param localDate - the new LocalDate to be stored
     */
    public void setDate(LocalDate localDate) {
        this.dateProperty.set(localDate);
    }

    /**
     * getter method for the property containing the LocalTime
     * @return the property containing the LocalTime
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return timeProperty;
    }

    /**
     * getter method for the LocalTime
     * @return the stored LocalTime
     */
    public LocalTime getTime() {
        return timeProperty.get();
    }

    /**
     * setter method to modify the contained LocalTime
     * @param localTime - the new LocalTime to be stored
     */
    public void setTime(LocalTime localTime) {
        this.timeProperty.set(localTime);
    }

    /**
     * getter method for the property containing the ZoneId
     * @return the stored ZoneId
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zoneProperty;
    }

    /**
     * getter method for the ZoneId
     * @return the stored ZoneId
     */
    public ZoneId getZone() {
        return zoneProperty.get();
    }

    /**
     * setter method to modify the contained ZoneId
     * @param zone - the new ZoneId to be stored
     */
    public void setZone(ZoneId zone) {
        this.zoneProperty.set(zone);
    }

    /**
     * getter method for the stored ZonedDateTime, obtained through the other three properties
     * @return the stored zonedDateTime
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(this.getDate(), this.getTime(), this.getZone());
    }

    /**
     * setter method for the stored ZonedDateTime, obtained through the other three properties
     * @param z - the new ZonedDateTime to be stored through the different properties
     */
    public void setZonedDateTime(ZonedDateTime z) {
        this.setDate(z.toLocalDate());
        this.setTime(z.toLocalTime());
        this.setZone(z.getZone());
    }

}