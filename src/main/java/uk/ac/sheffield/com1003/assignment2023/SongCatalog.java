package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;

import java.io.IOException;
import java.util.*;

/**
 * This class provided basic reading functionalities of the dataset with song entries.
 * */
public class SongCatalog extends AbstractSongCatalog {
    /**
     * Constructs a new SongCatalog instance by loading song data from the spotify_songs.tsv file.
     * This constructor invokes the superclass's constructor to handle the loading of song entries
     * from the file.
     *
     * @param songFile the file path from which to load the songs.
     * @throws IllegalArgumentException if the file path or format is incorrect.
     * @throws IOException if an I/O error occurs during file reading.
     */
    public SongCatalog(String songFile) throws IllegalArgumentException, IOException {
        // Call to the superclass constructor to handle the actual file reading and object initialization.
        super(songFile);
    }


    /**
     * Parse the properties from a given line from a song catalog file.
     * Assumes that each value appears in the same order as the columns in the file,
     * and that this order will not change.
     *
     * @param line the line to parse
     * @return a SongPropertyMap constructed from the parsed row, containing values for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not include every property
     * for a single song, or contains undefined properties)
     */
    @Override
    public SongPropertyMap parseSongEntryLine(String line) throws IllegalArgumentException {
        SongPropertyMap songPropertyMap = new SongPropertyMap();
        List<String> songDetailsAndProperties = Arrays.stream(line.split("\t")).toList();

        if (songDetailsAndProperties.size()!=14) throw new IllegalArgumentException();

        songPropertyMap.putDetail(SongDetail.NAME, songDetailsAndProperties.get(0));
        songPropertyMap.putDetail(SongDetail.ARTIST, songDetailsAndProperties.get(1));
        songPropertyMap.putProperty(SongProperty.DURATION, Double.parseDouble(songDetailsAndProperties.get(2)));
        songPropertyMap.putDetail(SongDetail.ALBUM_NAME, songDetailsAndProperties.get(3));
        songPropertyMap.putProperty(SongProperty.POPULARITY, Double.parseDouble(songDetailsAndProperties.get(4)));
        songPropertyMap.putProperty(SongProperty.DANCEABILITY, Double.parseDouble(songDetailsAndProperties.get(5)));
        songPropertyMap.putProperty(SongProperty.ENERGY, Double.parseDouble(songDetailsAndProperties.get(6)));
        songPropertyMap.putProperty(SongProperty.LOUDNESS, Double.parseDouble(songDetailsAndProperties.get(7)));
        songPropertyMap.putProperty(SongProperty.SPEECHINESS, Double.parseDouble(songDetailsAndProperties.get(8)));
        songPropertyMap.putProperty(SongProperty.ACOUSTICNESS, Double.parseDouble(songDetailsAndProperties.get(9)));
        songPropertyMap.putProperty(SongProperty.INSTRUMENTALNESS, Double.parseDouble(songDetailsAndProperties.get(10)));
        songPropertyMap.putProperty(SongProperty.LIVENESS, Double.parseDouble(songDetailsAndProperties.get(11)));
        songPropertyMap.putProperty(SongProperty.VALENCE, Double.parseDouble(songDetailsAndProperties.get(12)));
        songPropertyMap.putProperty(SongProperty.TEMPO, Double.parseDouble(songDetailsAndProperties.get(13)));

        return songPropertyMap;
    }
    /**
     * Returns the pured list of song entries after filtering by SongDetail.
     *
     * @param filteredSongEntriesList the list of song entries used as input for this filtering by SongDetail.
     * @param songDetail the SongDetail to retrieve
     * @param name the name of the SongDetail to retrieve
     * @return List<SongEntry>, a </SongEntry> list with the relevant Song entries
     */
    @Override
    public List<SongEntry> getSongEntriesList(List<SongEntry> filteredSongEntriesList, SongDetail songDetail,
                                              String name) {
        List<SongEntry> puredSongEntriesList = new ArrayList<>();
        for (SongEntry songEntry : filteredSongEntriesList) {
            if (songEntry.getSongDetail(songDetail).equalsIgnoreCase(name)) {
                puredSongEntriesList.add(songEntry);
            }
        }
        filteredSongEntriesList.clear();
        filteredSongEntriesList.addAll(puredSongEntriesList);
        return filteredSongEntriesList;
    }

    /**
     * Get the minimum value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested minimum value
     * @return the minimum value of the property
     */
    @Override
    public double getMinimumValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws
            NoSuchElementException  {
        // making sure the list ain't empty first
        if (songEntriesList.isEmpty()) throw new NoSuchElementException();

        double minimumVal = songEntriesList.get(0).getSongProperty(songProperty);
        for (SongEntry songEntry:songEntriesList) {
            minimumVal = Math.min(minimumVal, songEntry.getSongProperty(songProperty));
        }
        return Math.round(minimumVal*100)/100d;
    }

    /**
     * Get the maximum value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested maximum value
     * @return the maximum value of the property
     */
    @Override
    public double getMaximumValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws
            NoSuchElementException {
        // making sure the list ain't empty first
        if (songEntriesList.isEmpty()) throw new NoSuchElementException();

        double maximumVal = songEntriesList.get(0).getSongProperty(songProperty);
        for (SongEntry songEntry:songEntriesList) {
            maximumVal = Math.max(maximumVal, songEntry.getSongProperty(songProperty));
        }
        return Math.round(maximumVal*100)/100d;
    }

    /**
     * Get the average value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested average value
     * @return the average value of the property
     */
    @Override
    public double getAverageValue(SongProperty songProperty, List<SongEntry> songEntriesList) throws
            NoSuchElementException {
        // making sure the list ain't empty
        if (songEntriesList.isEmpty()) throw new NoSuchElementException();

        double average = 0;
        for (SongEntry songEntry:songEntriesList) {
            average+=songEntry.getSongProperty(songProperty);
        }
        return Math.round((average/songEntriesList.size())*100.00d)/100.00d;
    }

    /**
     * Retrieves the first five song entries from the list of all song entries.
     * This method is useful for displaying a limited set of entries, such as for a quick preview.
     * If there are fewer than five songs in the list, it returns as many as are available.
     *
     * @return A list containing the first five or fewer song entries from the full list.
     */
    @Override
    public List<SongEntry> getFirstFiveSongEntries() {
        // Check if the list contains fewer than five entries to prevent IndexOutOfBoundsException
        int endIndex = Math.min(songEntriesList.size(), 5);
        // Return a sublist of the first up to five entries from the song entries list
        return songEntriesList.subList(0, endIndex);
    }


}
