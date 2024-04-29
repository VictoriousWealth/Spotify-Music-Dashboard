package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */
public class SongCatalog extends AbstractSongCatalog {
    public SongCatalog(String songFile)
            throws IllegalArgumentException, IOException {
        super(songFile);
    }

    @Override
    public SongPropertyMap parseSongEntryLine(String line) throws IllegalArgumentException {
        // TODO implement
        SongPropertyMap songPropertyMap = new SongPropertyMap();
        return songPropertyMap;
    }

    @Override
    public List<SongEntry> getSongEntriesList(List<SongEntry> songEntryList, SongDetail songDetail, String name) {
        // TODO implement
        return songEntryList;
    }

    @Override
    public double getMinimumValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws NoSuchElementException  {
        // TODO implement
        return -1;
    }

    @Override
    public double getMaximumValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws NoSuchElementException {
        // TODO implement
        return -1;
    }

    @Override
    public double getAverageValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws NoSuchElementException {
        // TODO implement
        return -1;
    }

    @Override
    public List<SongEntry> getFirstFiveSongEntries() {
        // TODO implement
        return new ArrayList<>();
    }

}
