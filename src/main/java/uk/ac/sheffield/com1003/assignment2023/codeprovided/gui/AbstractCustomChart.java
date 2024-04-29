package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an Abstract class providing some functionality to store and access a Custom Chart.
 * The custom chart is made up of a circular bar chart on the left half of a circle
 * and a Radar chart on the other half.
 *
 * Should be implemented as uk.ac.sheffield.assignment2023.gui.CustomChart
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public abstract class AbstractCustomChart {

    protected Map<SongProperty, CustomChartAxisValues> customChartAxesValues;

    protected final AbstractSongCatalog songCatalog;

    protected List<SongEntry> filteredSongEntries;

    /**
     * The constructor is called by AbstractSpotifyDashboardPanel.
     *
     * @param songCatalog - Used to calculate min / max / average values.
     * @param filteredSongEntriesList - A list of song entries which is used to generate the custom chart.
     */
    public AbstractCustomChart(AbstractSongCatalog songCatalog,
                               List<SongEntry> filteredSongEntriesList) {
        this.songCatalog = songCatalog;
        this.filteredSongEntries = filteredSongEntriesList;
        this.customChartAxesValues = new HashMap<>();
        //updateCustomChartContents(filteredSongEntriesList);
    }

    /**
     * This method should completely update/reset the custom chart based on a new list of song entries.
     *
     * @param filteredSongEntriesList - A list of song entries used to generate the custom chart.
     */
    public abstract void updateCustomChartContents(List<SongEntry> filteredSongEntriesList);

    /**
     * This is a get method used to access the custom chart axes values.
     *
     * @return customChartAxesValues - Min / Max / Avg for each Song Property.
     */
    public abstract Map<SongProperty, CustomChartAxisValues> getCustomChartAxesValues();

}
