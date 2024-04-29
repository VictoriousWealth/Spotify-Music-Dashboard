package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChart;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SKELETON
 */

public class CustomChart extends AbstractCustomChart {
    public CustomChart(AbstractSongCatalog songCatalog, List<SongEntry> filteredSongEntriesList) {
        super(songCatalog, filteredSongEntriesList);
    }

    /**
     * This method is called whenever the filtered song entries change.
     * The minimum, maximum and average value for each song property is calculated.
     *
     * @param filteredSongEntriesList - A list of song entries which is used to generate the custom chart.
     */
    @Override
    public void updateCustomChartContents(List<SongEntry> filteredSongEntriesList) {
        // TODO implement
    }

    @Override
    public Map<SongProperty, CustomChartAxisValues> getCustomChartAxesValues() {
        // TODO implement
        return new HashMap<>();
    }

}
