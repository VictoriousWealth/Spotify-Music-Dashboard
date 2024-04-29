package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class provided a simplified Map wrapper to be used by a SongEntry, storing the values for each
 * of its properties.
 * This wrapper stores a HashMap rather than extending to de-expose the unneeded methods.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public class SongPropertyMap {

    // Consult the Javadocs for Map & HashMap for more information
    private final Map<SongProperty, Double> propertyToValuesMap = new HashMap<>();

    // Consult the Javadocs for Map & HashMap for more information
    private final Map<SongDetail, String> detailsMap = new HashMap<>();


    /**
     * Add a SongProperty and value pair. See HashMap.put() for more technical details.
     * @param songProperty the property to store
     * @param value the value associated with the property
     */
    public void putProperty(SongProperty songProperty, double value) {
        propertyToValuesMap.put(songProperty, value);
    }

    /**
     * Add a SongDetail and value pair. See HashMap.put() for more technical details.
     * @param songDetail the detail to store
     * @param detail the value associated with the property
     */
    public void putDetail(SongDetail songDetail, String detail) {
        detailsMap.put(songDetail, detail);
    }


    /**
     * Retrieve a value associated with a given SongProperty. See HashMap.get() for more technical details.
     * @param songProperty the SongProperty to retrieve the value of
     * @return the retrieved value
     */
    public double getProperty(SongProperty songProperty) {
        return propertyToValuesMap.get(songProperty);
    }

    /**
     * Retrieve a detail associated with a given SongDetail. See HashMap.get() for more technical details.
     * @param songDetail the SongDetail to retrieve the value of
     * @return the retrieved value
     */
    public String getDetail(SongDetail songDetail) {
        return detailsMap.get(songDetail);
    }

    /**
     * Get the properties stored in the map. See HashMap.keySet() for more technical details.
     * @return the properties stored in the map
     */
    public Set<SongProperty> propertySet() {
        return propertyToValuesMap.keySet();
    }
}
