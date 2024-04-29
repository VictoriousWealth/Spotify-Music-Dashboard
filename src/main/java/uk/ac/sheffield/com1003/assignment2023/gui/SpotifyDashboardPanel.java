package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */

public class SpotifyDashboardPanel extends AbstractSpotifyDashboardPanel {

    public SpotifyDashboardPanel(AbstractSongCatalog songCatalog) {
        super(songCatalog);
    }

    @Override
    public void executeQuery() {
       // TODO implement
    }
    @Override
    public void clearFilters() {
        // TODO implement
    }
    @Override
    public void addFilter() {
        // TODO implement

    }

    @Override
    public void populateComboBoxes() {
        // TODO remove ALL the code below and implement
        // TODO The comboboxes should be dynamically updated based on user actions

        // Populating the artists combobox. - CODE BELOW TO BE REMOVED
        List<String> listOfArtists = new ArrayList<>();
        listOfArtists.add("Artist 1");
        listOfArtists.add("Artist 2");
        listOfArtists.add("Artist 3");
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBoxArtistNames.getModel();
        listOfArtists.add(0,"");
        model.addAll(listOfArtists);

        // Populating the albums combobox. - CODE BELOW TO BE REMOVED
        List<String> listOfAlbums = new ArrayList<>();
        listOfAlbums.add("Album 1");
        listOfAlbums.add("Album 2");
        listOfAlbums.add("Album 3");
        model = (DefaultComboBoxModel<String>) comboBoxAlbums.getModel();
        listOfAlbums.add(0,"");
        model.addAll(listOfAlbums);

        // Populating the songs combobox. - CODE BELOW TO BE REMOVED
        List<String> listOfSongs = new ArrayList<>();
        listOfSongs.add("Song 1");
        listOfSongs.add("Song 2");
        listOfSongs.add("Song 3");
        model = (DefaultComboBoxModel<String>) comboBoxSongs.getModel();
        listOfSongs.add(0,"");
        model.addAll(listOfSongs);

    }

    @Override
    public void addListeners() {
        // TODO implement

    }

    /**
     * This method is called automatically by the Swing Framework whenever the component needs to be redrawn/repainted.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        // TODO implement
        super.paintComponent(g);

    }

    @Override
    public void updateStatistics() {
        // TODO implement
    }

    @Override
    public boolean isMinCheckBoxSelected() {
        // TODO implement - next line needs to be removed with the appropriate code
        return false;
    }

    @Override
    public boolean isMaxCheckBoxSelected() {
        // TODO implement - next line needs to be removed with the appropriate code
        return false;
    }

    @Override
    public boolean isAverageCheckBoxSelected() {
        // TODO implement - next line needs to be removed with the appropriate code
        return false;
    }
}
