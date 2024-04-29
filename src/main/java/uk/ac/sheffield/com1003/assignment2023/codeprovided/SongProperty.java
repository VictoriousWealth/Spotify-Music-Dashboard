package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.util.NoSuchElementException;

/**
 * This is a helper enum with constants representing the properties of a song entry.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public enum SongProperty {
    DURATION("Duration of the track"),
    POPULARITY("Popularity of the track"),
    DANCEABILITY("Danceability of the track"),
    ENERGY("Energy of the track"),
    LOUDNESS("Loudness of the track"),
    SPEECHINESS("Speechiness of the track"),
    ACOUSTICNESS("Acousticness of the track"),
    INSTRUMENTALNESS("Instrumentalness of the track"),
    LIVENESS("Liveness of the track"),
    VALENCE("Valence of the track"),
    TEMPO("Tempo of the track");

    private final String propertyName;

    SongProperty(String pName) { propertyName = pName; }

    public String getName() { return this.propertyName; }

    /**
     * Convert a name String (e.g. "Matches") to the matching SongProperty
     * @param name the String to convert
     * @return the matching SongProperty
     * @throws NoSuchElementException if the String does not match any SongProperty
     */
    public static SongProperty fromName(String name) throws NoSuchElementException {
        String pName = name.toUpperCase();
        SongProperty songProperty = null;
        try {
            songProperty = SongProperty.valueOf(pName);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("No such property (" + name + ")!");
        }
        return songProperty;
    }
    /**
     * Convert a property name String to the matching SongProperty
     * @param name the String to convert
     * @return the matching SongProperty
     * @throws NoSuchElementException if the String does not match any SongProperty
     */
    public static SongProperty fromPropertyName(String name) throws NoSuchElementException {
        for (SongProperty p : SongProperty.values()) {
            if (p.getName().equals(name))
                return p;
        }
        throw new NoSuchElementException("No such property (" + name + ")!");
    }
}