package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provided basic reading functionalities of the dataset with song entries.
 * <p>
 * This class is designed to be extended.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public abstract class AbstractSongCatalog {

    protected final List<SongEntry> songEntriesList;

    /**
     * Constructor - reads datasets with song catalogue (song entries
     * and initialises the songEntries list.
     */
    public AbstractSongCatalog(String songFile)
            throws IllegalArgumentException, IOException {
        this.songEntriesList = new ArrayList<>();
        loadSongCatalogData(songFile);
    }

    /**
     * Reads the TSV file passed by main. It then reads the contents of the files
     * and creates the relevant SongEntry objects and returns them into a list.
     * Catches exception errors should they occur and it delegates handling of other exceptions
     *
     * @param songFile This will be the dataset providing the song dataset.
     * @return List of SongEntry objects
     */
    private List<SongEntry> readDataFromFile(String songFile)
            throws IllegalArgumentException, IOException {
        List<SongEntry> songEntriesList = new ArrayList<>();
        int count = 1;

        songFile = songFile.replaceAll(" ", "");

        BufferedReader br = new BufferedReader(new FileReader(songFile));
        String line = br.readLine();
        if (line == null) {
            throw new IllegalArgumentException("File is empty. Please run the programme again and provide a valid dataset.");
        }
        while ((line = br.readLine()) != null) {
            try {
                // The song entry ID is created by this reader; it is not provided in the original files
                // The ID should _not_ be modified later
                int id = count;
                SongEntry songEntry = new SongEntry(id, parseSongEntryLine(line));
                songEntriesList.add(songEntry);
                count++;

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("File format is incorrect; only double values are allowed. " +
                        "See line: " + (count + 1));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Malformed song entry line: " + line +
                        "\nSee line: " + (count + 1));
            }
        }
        return songEntriesList;
    }

    /**
     * Read the contents of filename and stores it.
     *
     * @param songFile file with the songs.
     */
    private void loadSongCatalogData(String songFile)
            throws IllegalArgumentException, IOException {
        List<SongEntry> entriesFromFile = readDataFromFile(songFile);
        songEntriesList.addAll(entriesFromFile);
    }

    /**
     * Returns the list of song entries.
     *
     * @return List<SongEntry>, a list song entries.
     */
    public List<SongEntry> getSongEntriesList() {
        return songEntriesList;
    }


    /**
     * Returns the list of song entries after filtering by SongDetail.
     *
     * @param filteredSongEntriesList the list of song entries used as input for this filtering by SongDetail.
     * @param songDetail the SongDetail to retrieve
     * @param name the name of the SongDetail to retrieve
     * @return List<SongEntry>, a </SongEntry> list with the relevant Song entries
     */
    public abstract List<SongEntry> getSongEntriesList(List<SongEntry> filteredSongEntriesList,
                                                       SongDetail songDetail,String name);

    /**
     * Parse the properties from a given line from a song catalog file.
     * You can expect that each value appears in the same order as the columns in the file,
     * and that this order will not change.
     *
     * @param line the line to parse
     * @return a SongPropertyMap constructed from the parsed row, containing values for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not include every property
     * for a single song, or contains undefined properties)
     */
    public abstract SongPropertyMap parseSongEntryLine(String line) throws IllegalArgumentException;

    /**
     * Get the minimum value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested minimum value
     * @return the minimum value of the property
     */
    public abstract double getMinimumValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    /**
     * Get the maximum value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested maximum value
     * @return the maximum value of the property
     */
    public abstract double getMaximumValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    /**
     * Get the average value of the given property for song entries in this song catalog
     * @param songProperty the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested average value
     * @return the average value of the property
     */
    public abstract double getAverageValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    public abstract List<SongEntry> getFirstFiveSongEntries();
}
