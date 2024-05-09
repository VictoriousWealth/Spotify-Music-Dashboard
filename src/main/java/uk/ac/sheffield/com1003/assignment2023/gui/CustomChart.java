package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChart;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * CustomChart extends AbstractCustomChart and manages the visualization of song data
 * through a chart that displays minimum, maximum, and average values of various song properties.
 * This class dynamically updates its data based on the provided list of filtered song entries.
 */
public class CustomChart extends AbstractCustomChart {

    /**
     * Constructs a CustomChart object with the specified song catalog and filtered song entries.
     * Initializes and updates chart contents upon creation.
     *
     * @param songCatalog The song catalog used as the data source.
     * @param filteredSongEntriesList The list of song entries that will be displayed on the chart.
     */
    public CustomChart(AbstractSongCatalog songCatalog, List<SongEntry> filteredSongEntriesList) {
        super(songCatalog, filteredSongEntriesList);
        updateCustomChartContents(filteredSongEntriesList);
    }

    /**
     * Updates the chart contents based on the current set of filtered song entries.
     * It calculates and updates the minimum, maximum, and average values for each song property.
     *
     * @param filteredSongEntriesList A list of song entries which is used to generate the custom chart.
     */
    @Override
    public void updateCustomChartContents(List<SongEntry> filteredSongEntriesList) {
        try {
            // Iterate over each song property to update its corresponding axis values in the chart.
            for (SongProperty songProperty : SongProperty.values()) {
                customChartAxesValues.put(songProperty,
                        new CustomChartAxisValues(
                                songCatalog.getMinimumValue(songProperty, filteredSongEntriesList),
                                songCatalog.getMaximumValue(songProperty, filteredSongEntriesList),
                                songCatalog.getAverageValue(songProperty, filteredSongEntriesList)
                        )
                );
            }
        } catch (NoSuchElementException e) {
            // Handle the case where the filtered song entries list is empty and log an error.
            System.err.println("Song entries list passed as a parameter is empty!");
        }
    }

    /**
     * Provides read-only access to the custom chart axes values which include minimum,
     * maximum, and average values for each song property.
     *
     * @return An immutable map of song properties to their corresponding chart axis values.
     */
    @Override
    public Map<SongProperty, CustomChartAxisValues> getCustomChartAxesValues() {
        return Map.copyOf(customChartAxesValues);
    }
}
