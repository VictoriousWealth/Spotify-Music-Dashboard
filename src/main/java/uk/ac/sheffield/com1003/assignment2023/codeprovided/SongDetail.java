package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.util.NoSuchElementException;

/**
 * This is a helper enum with constants representing the details of a song entry.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public enum SongDetail {
    NAME("Name of the track"),
    ARTIST("Name of the artist"),
    ALBUM_NAME("Name of the album of the track");

    private final String detailName;

    SongDetail(String dName) { detailName = dName; }

    public String getName() { return this.detailName; }

    public static SongDetail fromName(String name) throws NoSuchElementException {
        String pName = name.toUpperCase();
        SongDetail songDetail = null;
        try {
            songDetail = SongDetail.valueOf(pName);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("No such detail (" + name + ")!");
        }
        return songDetail;
    }
}
