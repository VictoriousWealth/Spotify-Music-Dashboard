package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;

/**
 * This class defines the main panel of a Spotify-like dashboard, managing song entries
 * and filters. It extends AbstractSpotifyDashboardPanel and provides GUI elements and functionality
 * for querying, filtering, and displaying song information.
 */
public class SpotifyDashboardPanel extends AbstractSpotifyDashboardPanel {

    private boolean updatingComboBoxes = false;

    /**
     * Constructor for SpotifyDashboardPanel.
     * @param songCatalog The song catalog that this panel will use to fetch and filter songs.
     */
    public SpotifyDashboardPanel(AbstractSongCatalog songCatalog) {
        super(songCatalog);
        clearFilters();
    }

    /**
     * Determines whether a SongEntry should be removed based on a list of SubQuery conditions.
     * Each SubQuery specifies a song property, a comparison operator, and a value. If any SubQuery condition
     * dictates that the SongEntry does not meet the criteria, the song should be removed.
     *
     * @param songEntry The SongEntry to evaluate against the provided subqueries.
     * @param subQueryList A list of SubQuery objects that define the removal criteria.
     * @return true if the SongEntry does not meet the criteria of all SubQueries; false otherwise.
     */
    private boolean shouldRemoveSong(SongEntry songEntry, List<SubQuery> subQueryList) {
        for (SubQuery subQuery : subQueryList) {
            double songPropertyValue = songEntry.getSongProperty(subQuery.getSongProperty());
            double subQueryValue = subQuery.getValue();
            switch (subQuery.getOperator()) {
                case ">":
                    if (songPropertyValue <= subQueryValue) return true;
                    break;
                case ">=":
                    if (songPropertyValue < subQueryValue) return true;
                    break;
                case "==":
                    if (songPropertyValue != subQueryValue) return true;
                    break;
                case "!=":
                    if (songPropertyValue == subQueryValue) return true;
                    break;
                case "<":
                    if (songPropertyValue >= subQueryValue) return true;
                    break;
                case "<=":
                    if (songPropertyValue > subQueryValue) return true;
                    break;
            }
        }
        return false; // No condition met to remove the song
    }

    /**
     * Executes a query on the song catalog using predefined sub-query conditions.
     * This method filters out songs that do not meet the criteria specified in {@code subQueryList}.
     * If the list of sub-queries is empty, no action is taken.
     * <p>
     * This process involves:
     * <ul>
     *     <li>Checking each song against the conditions specified in the sub-query list.</li>
     *     <li>Updating the GUI components to reflect the results of the query.</li>
     *     <li>Refreshing the display of filtered songs, statistics, and custom charts based on the updated song list.</li>
     * </ul>
     * The filtered songs are then displayed in the GUI, and relevant statistics and chart updates are triggered.
     */
    @Override
    public void executeQuery() {
        // If there are no sub-queries defined, exit the method early as there's nothing to filter.
        if (subQueryList.isEmpty()) {
            return;
        }

        // Create a new list from the currently filtered songs to work with during filtering.
        List<SongEntry> newFilteredSongEntriesList = new ArrayList<>(filteredSongEntriesList);

        // Use the removeIf method combined with the shouldRemoveSong method to filter the list.
        // This will remove any song that should be removed based on the sub-query conditions.
        newFilteredSongEntriesList.removeIf(songEntry -> shouldRemoveSong(songEntry, subQueryList));

        // Update the main list of filtered songs with the newly filtered list.
        filteredSongEntriesList = newFilteredSongEntriesList;

        // Update the GUI components to reflect the new set of filtered songs.
        populateFilteredSongEntriesTextArea();  // Update the text area that displays the song entries.
        updateStatistics();                     // Recalculate and display statistics based on the filtered songs.
        customChart.updateCustomChartContents(filteredSongEntriesList);  // Update the custom chart with new song data.
        repaint();                             // Request a repaint of the panel to reflect any visual updates.
    }

    /**
     * Clears all filter conditions from the sub-query list and updates the GUI components
     * to reflect a state with no active filters. This method resets the song catalog
     * display to its original, unfiltered state.
     * <p>
     * Operations performed by this method include:
     * <ul>
     *     <li>Clearing the sub-query list which holds the filtering conditions.</li>
     *     <li>Resetting the text area that displays the active sub-queries.</li>
     *     <li>Updating all combo boxes and other GUI components that might depend on the filter state.</li>
     *     <li>Refreshing the song entries display, statistics, and custom chart to show all songs.</li>
     * </ul>
     * These operations ensure that the user interface accurately reflects the removal of filters and shows
     * the complete list of songs without any filter constraints.
     * <p>
     * <b>THE <u>RESET DOES NOT APPLY</u> FOR FILTERS DUE TO WHAT WAS SELECTED IN THE <u>COMBO BOXES!</u></b>
     */
    @Override
    public void clearFilters() {
        // Clear the list that holds sub-queries for filtering songs.
        subQueryList.clear();

        // Clear the text in the sub-queries display area, indicating that no filters are currently applied.
        subQueriesTextArea.setText("");

        // Re-populate combo boxes and other dependent GUI components to reflect the full song catalog.
        populateComboBoxes();

        // Update the display area for filtered song entries to show all songs from the catalog.
        populateFilteredSongEntriesTextArea();

        // Recalculate and update the statistics to reflect the full song catalog without any filters.
        updateStatistics();

        // Update the contents of the custom chart to display data for all songs.
        customChart.updateCustomChartContents(filteredSongEntriesList);

        // Request a repaint of the panel to ensure all visual elements are updated correctly.
        repaint();
    }

    /**
     * Adds a new filter based on the selected properties, operator, and value from the GUI.
     * This filter is then used to narrow down the song entries according to the specified criteria.
     * If any part of the filter creation fails (e.g., due to an invalid operator or parsing error),
     * an error message is logged, and the GUI remains unchanged.
     * <p>
     * The method performs the following steps:
     * <ul>
     *     <li>Validates and retrieves user inputs from combo boxes and text field.</li>
     *     <li>Creates a new {@link SubQuery} object if the inputs are valid.</li>
     *     <li>Adds the new {@link SubQuery} to the list of sub-queries used for filtering.</li>
     *     <li>Updates the GUI to display the new list of active sub-queries.</li>
     * </ul>
     * Errors during the filter addition process are caught and logged without disrupting the user experience.
     */
    @Override
    public void addFilter() {
        try {
            // Ensure both property and operator selections are not null and the operator is valid before proceeding.
            if (comboQueryProperties.getSelectedItem() != null && comboOperators.getSelectedItem() != null &&
                    SubQuery.isValidOperator(comboOperators.getSelectedItem().toString())) {

                // Retrieve the selected song property, operator, and the value entered by the user.
                SongProperty songProperty = SongProperty.fromPropertyName(
                        comboQueryProperties.getSelectedItem().toString());
                String operator = comboOperators.getSelectedItem().toString();
                double value = Double.parseDouble(this.value.getText());  // May throw NumberFormatException

                // Create a new sub-query using the gathered details and add it to the list of filters.
                SubQuery subQuery = new SubQuery(songProperty, operator, value);
                subQueryList.add(subQuery);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format for filter value.");
        } catch (Exception e) {
            System.err.println("Something went wrong while adding a filter: " + e.getMessage());
        }

        // Clear the text area to avoid confusion as it's editable, and then repopulate with the updated filter list.
        subQueriesTextArea.setText("");
        subQueryList.forEach(subQuery -> subQueriesTextArea.append(subQuery.toString() + "; "));
    }

    /**
     * Filters the list of song entries based on the currently selected song name.
     * If a song name is selected (i.e., not empty), this method filters the list of
     * song entries to include only those that match the selected song name.
     * <p>
     * If no song name is selected, the method returns the original list of song entries
     * without any modifications.
     *
     * @return a {@code List<SongEntry>} that either contains song entries filtered by the selected
     *         song name or the original list if no song name is selected.
     */
    private List<SongEntry> filterSongEntriesBySelectedName() {
        // Check if a song name has been selected to filter by.
        if (!selectedSongName.isEmpty()) {
            // Create a copy of the currently filtered song entries list to work on.
            List<SongEntry> copyOfFilteredSongEntriesList = new ArrayList<>(filteredSongEntriesList);

            // Return a new list filtered by the selected song name using the song catalog's method.
            return songCatalog.getSongEntriesList(
                    copyOfFilteredSongEntriesList, SongDetail.NAME, selectedSongName
            );
        }

        // If no song name is selected, return the original list of filtered song entries.
        return filteredSongEntriesList;
    }

    /**
     * Filters the list of song entries based on the currently selected artist name.
     * If an artist name is selected (i.e., not empty), this method filters the list
     * to include only those song entries that match the selected artist name.
     * <p>
     * If no artist name is selected, the method returns the original list of song entries
     * without any modifications. This ensures that the filtering is conditional and only
     * applied when there is a specific artist to filter by.
     *
     * @return a {@code List<SongEntry>} that either contains song entries filtered by the selected
     *         artist name or the original list if no artist name is selected.
     */
    private List<SongEntry> filterSongEntriesBySelectedArtist() {
        // Check if an artist name has been selected to filter by.
        if (!selectedArtistName.isEmpty()) {
            // Create a copy of the currently filtered song entries list to avoid modifying the original.
            List<SongEntry> copyOfFilteredSongEntriesList = new ArrayList<>(filteredSongEntriesList);

            // Return a new list filtered by the selected artist name using the song catalog's functionality.
            return songCatalog.getSongEntriesList(
                    copyOfFilteredSongEntriesList, SongDetail.ARTIST, selectedArtistName
            );
        }

        // If no artist name is selected, return the unmodified original list of filtered song entries.
        return filteredSongEntriesList;
    }

    /**
     * Filters the list of song entries based on the currently selected album name.
     * This method applies a filter to include only those song entries that match
     * the selected album name, if an album name has been selected. If no album
     * name is selected, the method returns the original list of song entries
     * without any modifications.
     * <p>
     * This conditional filtering is helpful for dynamically adjusting the song
     * list based on user selection from the other combo boxes.
     *
     * @return a {@code List<SongEntry>} that contains either the filtered song entries
     *         by the selected album name or the original list if no album name is selected.
     */
    private List<SongEntry> filterSongEntriesBySelectedAlbum() {
        // Check if an album name has been selected for filtering.
        if (!selectedAlbumName.isEmpty()) {
            // Create a copy of the currently filtered song entries list to ensure the original is not modified.
            List<SongEntry> copyOfFilteredSongEntriesList = new ArrayList<>(filteredSongEntriesList);

            // Use the song catalog's method to return a new list filtered by the selected album name.
            return songCatalog.getSongEntriesList(
                    copyOfFilteredSongEntriesList, SongDetail.ALBUM_NAME, selectedAlbumName
            );
        }

        // If no album name is selected, return the original list to ensure no unintended filtering.
        return filteredSongEntriesList;
    }

    /**
     * Populates the artist names combo box with unique artist names extracted
     * from the currently filtered list of song entries. This method ensures that
     * the combo box reflects the latest available artist names and maintains the
     * selection.
     * <p>
     * The method does the following:
     * <ul>
     *     <li>Gathers unique artist names from the filtered song entries.</li>
     *     <li>Updates the combo box model with these names, preserving the current selection.</li>
     *     <li>Logs the state of the combo box selection before and after updating to facilitate debugging.</li>
     * </ul>
     */
    private void populateArtistComboBox() {
        // Gather unique artist names from the filtered song entries list.
        Set<String> uniqueArtists = new HashSet<>();
        for (SongEntry songEntry : filteredSongEntriesList) {
            uniqueArtists.add(songEntry.getSongArtist());
        }

        // Get the current combo box model or create a new one if it does not exist.
        DefaultComboBoxModel<String> model = (comboBoxArtistNames.getModel() == null) ?
                new DefaultComboBoxModel<>() : (DefaultComboBoxModel<String>) comboBoxArtistNames.getModel();

        // Update the combo box model with the new set of unique artists.
        model.removeAllElements();
        model.addAll(uniqueArtists);
        model.insertElementAt("", 0); // Add a blank option to allow deselecting.
        model.setSelectedItem(selectedArtistName); // Sets back the selected item to what it should be
    }

    /**
     * Populates the album names combo box with unique album names gathered
     * from the currently filtered list of song entries. This method updates
     * the combo box to reflect the latest album names available and tries to maintain
     * the previously selected album.
     * <p>
     * Key operations include:
     * <ul>
     *     <li>Collecting unique album names from the filtered song entries.</li>
     *     <li>Updating the combo box model with these names, ensuring that the
     *         current selection is preserved</li>
     * </ul>
     */
    private void populateAlbumComboBox() {
        // Collect unique album names from the filtered list of song entries.
        Set<String> uniqueAlbums = new HashSet<>();
        for (SongEntry songEntry : filteredSongEntriesList) {
            uniqueAlbums.add(songEntry.getSongAlbumName());
        }

        // Check if the combo box model exists or create a new one if it doesn't.
        DefaultComboBoxModel<String> model = (comboBoxAlbums.getModel() == null) ?
                new DefaultComboBoxModel<>() : (DefaultComboBoxModel<String>) comboBoxAlbums.getModel();

        // Update the combo box model with the new set of unique album names.
        model.removeAllElements();
        model.addAll(uniqueAlbums);
        model.insertElementAt("", 0);  // Add an empty option to allow for no selection.

        // Attempt to re-select the previously selected album, if still available.
        model.setSelectedItem(selectedAlbumName);  // Sets back the selected item to what it should be
    }

    /**
     * Populates the song names combo box with unique song names gathered from the
     * currently filtered list of song entries. This method ensures the combo box
     * reflects the latest song names available, and it maintains the previously
     * selected song name.
     * <p>
     * The operations performed by this method include:
     * <ul>
     *     <li>Gathering unique song names from the filtered song entries.</li>
     *     <li>Updating the combo box model with these names, ensuring that the current
     *         selection is preserved.</li>
     * </ul>
     */
    private void populateSongNameComboBox() {
        // Gather unique song names from the filtered song entries.
        Set<String> uniqueSongs = new HashSet<>();
        for (SongEntry songEntry : filteredSongEntriesList) {
            uniqueSongs.add(songEntry.getSongName());
        }

        // Retrieve or create the combo box model.
        DefaultComboBoxModel<String> model = (comboBoxSongs.getModel() == null) ?
                new DefaultComboBoxModel<>() : (DefaultComboBoxModel<String>) comboBoxSongs.getModel();

        // Update the combo box with the collected song names.
        model.removeAllElements();
        model.addAll(uniqueSongs);
        model.insertElementAt("", 0); // Add an empty option to allow for no selection.

        // Re-select the previously selected song, if it is still available in the updated list.
        model.setSelectedItem(selectedSongName);  // Sets back the selected item to what it should be
    }

    /**
     * Populates the filteredSongEntriesTextArea with song details in a structured tabular format.
     * Displays up to 1000 song entries along with their details, depending on the current filters.
     * Each entry is formatted to align artist, album, and song names in distinct columns for clarity.
     */
    private void populateFilteredSongEntriesTextArea() {
        // Clearing the existing text to refresh the content
        filteredSongEntriesTextArea.setText("");
        filteredSongEntriesTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Heading for the text area to indicate column headers
        StringBuilder header = getHeader();
        filteredSongEntriesTextArea.append(header.toString());

        // Iterating over each song entry to format and append it to the text area
        for (int index = 0; index < Math.min(1000, filteredSongEntriesList.size()); index++) {
            SongEntry entry = filteredSongEntriesList.get(index);
            String artist = entry.getSongArtist();
            String album = entry.getSongAlbumName();
            String song = entry.getSongName();

            // Create a formatted string for each entry, aligning all columns
            Object[] entries = {
                    artist, album, song,
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.DURATION),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.POPULARITY),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.DANCEABILITY),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.ENERGY),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.LOUDNESS),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.SPEECHINESS),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.ACOUSTICNESS),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.INSTRUMENTALNESS),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.LIVENESS),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.VALENCE),
                    filteredSongEntriesList.get(index).getSongProperty(SongProperty.TEMPO)
            };
            String formattedEntry = String.format("%-30s | %-140s | %-140s" +
                            (" | %-30.2f").repeat(10) +"\n", entries);
            filteredSongEntriesTextArea.append(formattedEntry);
        }

        // Footer to indicate the total number of displayed entries
        filteredSongEntriesTextArea.append("\n[Showing " + Math.min(1000, filteredSongEntriesList.size()) +
                " entries out of " + filteredSongEntriesList.size() + "]");
    }

    private static StringBuilder getHeader() {
        StringBuilder header = new StringBuilder();
        String[] properties = {
                SongProperty.DURATION.getName(),
                SongProperty.POPULARITY.getName(),
                SongProperty.DANCEABILITY.getName(),
                SongProperty.ENERGY.getName(),
                SongProperty.LOUDNESS.getName(),
                SongProperty.SPEECHINESS.getName(),
                SongProperty.INSTRUMENTALNESS.getName(),
                SongProperty.LIVENESS.getName(),
                SongProperty.VALENCE.getName(),
                SongProperty.TEMPO.getName(),
        };
        header.append(String.format("%-30s | %-140s | %-140s" + " | %-30s".repeat(10) + "\n",
                "Artist", "Album", "Song", properties[0], properties[1], properties[2], properties[3], properties[4],
                properties[5], properties[6], properties[7], properties[8], properties[9]));
        return header.append("-".repeat(750)).append("\n");
    }


    /**
     * Populates all combo boxes (artist, album, and song names) based on the current
     * selection state and the available song entries. This method is designed to prevent
     * <b>recursion</b> and ensure that the combo boxes are updated only when necessary.
     * <p>
     * This method first checks if updates are already in progress to avoid <b>recursion</b>.
     * It then retrieves the full list of song entries and selectively updates each
     * combo box based on whether their respective selections are empty (indicating a need for update).
     * If all selections are filled, it ensures that all combo boxes reflect the most current data.
     * <p>
     * The updates are performed in a manner that avoids unnecessary filtering operations
     * unless the respective selections are cleared, optimizing performance and responsiveness.
     */
    @Override
    public void populateComboBoxes() {
        // Prevent recursive updates which could lead to stack overflow.
        if (updatingComboBoxes) {
            return;
        }

        try {
            // Lock the updating process to prevent recursion from GUI feedback loops.
            updatingComboBoxes = true;

            // Get a fresh list of all song entries from the catalog.
            filteredSongEntriesList = songCatalog.getSongEntriesList();

            // Update the artist comboBox if the artist name selection is empty.
            if (selectedArtistName.isEmpty()) {
                filteredSongEntriesList = filterSongEntriesBySelectedName();
                filteredSongEntriesList = filterSongEntriesBySelectedAlbum();
                populateArtistComboBox();
            }

            // Update the album comboBox if the album name selection is empty.
            if (selectedAlbumName.isEmpty()) {
                filteredSongEntriesList = filterSongEntriesBySelectedName();
                filteredSongEntriesList = filterSongEntriesBySelectedArtist();
                populateAlbumComboBox();
            }

            // Update the song comboBox if the song name selection is empty.
            if (selectedSongName.isEmpty()) {
                filteredSongEntriesList = filterSongEntriesBySelectedArtist();
                filteredSongEntriesList = filterSongEntriesBySelectedAlbum();
                populateSongNameComboBox();
            }

            // Ensure all comboBoxes are up-to-date if all selections are filled.
            if (!(selectedSongName.isEmpty() || selectedAlbumName.isEmpty() || selectedArtistName.isEmpty())) {
                filteredSongEntriesList = filterSongEntriesBySelectedArtist();
                filteredSongEntriesList = filterSongEntriesBySelectedAlbum();
                filteredSongEntriesList = filterSongEntriesBySelectedName();
                populateArtistComboBox();
                populateAlbumComboBox();
                populateSongNameComboBox();
            }

        } finally {
            // Unlock the updating process.
            updatingComboBoxes = false;
        }
    }

    /**
     * Updates the GUI components of the dashboard to reflect the current state of the application.
     * This method is a central point for invoking updates that include:
     * <ul>
     *     <li>re-populating combo boxes,</li>
     *     <li>executing any defined queries,</li>
     *     <li>updating the display of filtered song entries,</li>
     *     <li> recalculating statistics,</li>
     *     <li>refreshing the custom chart,</li>
     *     <li>and repainting the GUI to ensure visual updates are rendered properly.</li>
     * </ul>
     * <p>
     * This method ensures that all parts of the GUI related to displaying song data are synchronized
     * and accurately represent the underlying data and user selections.
     */
    private void updateGUI() {
        // Populate all combo boxes based on the current filtering criteria or selections.
        populateComboBoxes();

        // Execute any queries based on the current state of the subQuery list to filter songs.
        executeQuery();

        // Populate the text area with song entries that match the current filters.
        populateFilteredSongEntriesTextArea();

        // Update the statistics display. This is necessary to reflect current data states,
        // especially when no new queries have been added but previous data states might have changed.
        updateStatistics();  // Ensures the statistics area is always accurate.

        // Update the contents of the custom chart to reflect the currently filtered songs.
        customChart.updateCustomChartContents(filteredSongEntriesList);

        // Repaint the component to ensure all visual updates are shown to the user.
        repaint();
    }

    /**
     * Configures and adds event listeners to the GUI components. This method sets up
     * action listeners for checkboxes and buttons, as well as item listeners for combo boxes.
     * These listeners handle user interactions and trigger updates to the application's state
     * and GUI in response to those interactions.
     * <p>
     * Each listener is designed to respond to specific actions:
     * <ul>
     *   <li>Checkbox listeners trigger a repaint of the GUI, though the one was meant to be target is the repaint
     *   of the customChartPanel.</li>
     *   <li>The "Add Filter" button applies a new filter and executes the query.</li>
     *   <li>The "Clear Filters" button resets all filters.</li>
     *   <li>Combo box listeners update the application state based on the selected item and refresh the GUI.</li>
     * </ul>
     */
    @Override
    public void addListeners() {
        // Repaint the GUI when any of the statistic checkboxes (average, max, min) are toggled.
        averageCheckBox.addActionListener(e -> repaint());
        maxCheckBox.addActionListener(e -> repaint());
        minCheckBox.addActionListener(e -> repaint());

        // Add a filter based on the current GUI inputs when the "Add Filter" button is clicked,
        // then execute the query to reflect the changes in the GUI.
        buttonAddFilter.addActionListener(e -> {
            addFilter();
            executeQuery();
        });

        // Clear all filters and update the GUI accordingly when the "Clear Filters" button is clicked.
        buttonClearFilters.addActionListener(e -> clearFilters());

        // Update the selected artist name and refresh the GUI whenever a new artist is selected.
        comboBoxArtistNames.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && comboBoxArtistNames.getSelectedItem() != null) {
                selectedArtistName = comboBoxArtistNames.getSelectedItem().toString().isEmpty() ? "" :
                        comboBoxArtistNames.getSelectedItem().toString();
                updateGUI();
            }
        });

        // Update the selected album name and refresh the GUI whenever a new album is selected.
        comboBoxAlbums.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && comboBoxAlbums.getSelectedItem() != null) {
                selectedAlbumName = comboBoxAlbums.getSelectedItem().toString().isEmpty() ? "" :
                        comboBoxAlbums.getSelectedItem().toString();
                updateGUI();
            }
        });

        // Update the selected song name and refresh the GUI whenever a new song is selected.
        comboBoxSongs.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && comboBoxSongs.getSelectedItem() != null) {
                selectedSongName = comboBoxSongs.getSelectedItem().toString().isEmpty() ? "" :
                        comboBoxSongs.getSelectedItem().toString();
                updateGUI();
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * Updates the statistics display in the GUI based on the currently filtered songs.
     * This method calculates and displays the minimum, maximum, and mean values for
     * each song property in the filtered list. If there are no songs in the filtered list,
     * it displays "n/a" for all statistics.
     * <p>
     * Steps include:
     * <ul>
     *     <li>Clearing the previous statistics.</li>
     *     <li>Displaying headers for song properties.</li>
     *     <li>Calculating statistics if there are filtered songs available.</li>
     *     <li>Displaying calculated statistics or placeholders if the list is empty.</li>
     * </ul>
     */
    @Override
    public void updateStatistics() {
        // Clear the previous statistics from the display area.
        statisticsTextArea.setText("");
        statisticsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Check if there are any songs to calculate statistics for.
        if (filteredSongEntriesList.isEmpty()) {
            // Build and display the header row to describe each song property column.
            StringBuilder headerBuilder = new StringBuilder();
            for (SongProperty songProperty : SongProperty.values()) {
                headerBuilder.append("-").append(songProperty.getName());
            }
            statisticsTextArea.append(headerBuilder.append("-\n").toString());

            // Display "n/a" for all statistics when there are no songs to analyze.
            String noData = " -n/a".repeat(SongProperty.values().length);
            statisticsTextArea.append("Minimum:" + noData + "-\n");
            statisticsTextArea.append("Maximum:" + noData + "-\n");
            statisticsTextArea.append("Mean:" + noData + "-\n");
            System.err.println("Song entries list is empty!");
            return;
        }

        // Calculate and store the minimum, maximum, and mean values for each song property.
        HashMap<SongProperty, Double> minimumValues = new HashMap<>();
        HashMap<SongProperty, Double> maximumValues = new HashMap<>();
        HashMap<SongProperty, Double> meanValues = new HashMap<>();
        for (SongProperty songProperty : SongProperty.values()) {
            minimumValues.put(songProperty, songCatalog.getMinimumValue(songProperty, filteredSongEntriesList));
            maximumValues.put(songProperty, songCatalog.getMaximumValue(songProperty, filteredSongEntriesList));
            meanValues.put(songProperty, songCatalog.getAverageValue(songProperty, filteredSongEntriesList));
        }

        // Append the calculated statistics to the text area.
        appendStatistics("Minimum:", minimumValues, statisticsTextArea);
        appendStatistics("Maximum:", maximumValues, statisticsTextArea);
        appendStatistics("Mean:", meanValues, statisticsTextArea);
    }

    /**
     * Appends statistical values for a given type (min, max, mean) to the text area.
     * @param label The label for the statistic type (e.g., "Minimum:").
     * @param values The calculated values for each song property.
     * @param textArea The JTextArea to append the results to.
     */
    private void appendStatistics(String label, HashMap<SongProperty, Double> values, JTextArea textArea) {
        // Starting the line with the label
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(String.format("%-8s", label));

        // Append each song property value, formatted with two decimal places
        for (SongProperty songProperty : SongProperty.values()) {
            double value = values.get(songProperty);
            // Use String.format to ensure consistent decimal places and add vertical bars for separation
            lineBuilder.append(String.format(" | %-16s: %9.2f", songProperty.getName(), value));
        }
        lineBuilder.append(" |\n"); // Close the line with a vertical bar

        // Append the constructed line to the text area
        textArea.append(lineBuilder.toString());
    }

    /**
     * Checks if the minimum value checkbox is selected.
     *
     * @return true if the minimum value checkbox is selected; false otherwise.
     */
    @Override
    public boolean isMinCheckBoxSelected() {
        // Return the selection state of the minimum checkbox.
        return minCheckBox.isSelected();
    }

    /**
     * Checks if the maximum value checkbox is selected.
     *
     * @return true if the maximum value checkbox is selected; false otherwise.
     */
    @Override
    public boolean isMaxCheckBoxSelected() {
        // Return the selection state of the maximum checkbox.
        return maxCheckBox.isSelected();
    }

    /**
     * Checks if the average value checkbox is selected.
     *
     * @return true if the average value checkbox is selected; false otherwise.
     */
    @Override
    public boolean isAverageCheckBoxSelected() {
        // Return the selection state of the average checkbox.
        return averageCheckBox.isSelected();
    }
}
