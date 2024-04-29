package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SubQuery;
import uk.ac.sheffield.com1003.assignment2023.gui.CustomChart;
import uk.ac.sheffield.com1003.assignment2023.gui.CustomChartPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * This class provides a basic implementation of the main elements in the GUI as discussed in the handout.
 * You would need to consider implementing the abstract methods in this class by overriding them.
 * <p>
 * Should be implemented as uk.ac.sheffield.assignment2023.gui.SpotifyDashboardPanel
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public abstract class AbstractSpotifyDashboardPanel extends JPanel {

    protected final AbstractSongCatalog songCatalog;

    protected List<SongEntry> filteredSongEntriesList;

    protected List<SubQuery> subQueryList = new ArrayList<>();

    protected String selectedArtistName = "";

    protected String selectedAlbumName = "";

    protected String selectedSongName = "";

    protected AbstractCustomChart customChart;

    // starting the definition of buttons
    // buttonAddFilter will add a new query condition to queryConditionList when clicked
    // clicking this button implies calling the method addFilter(...)
    protected final JButton buttonAddFilter = new JButton("Add by Property Filter");

    // buttonClearFilters will remove all query conditions in queryConditionList when clicked
    // clicking this button implies calling the method clearFilters(...)
    protected final JButton buttonClearFilters = new JButton("Clear All by Property Filters");

    protected JComboBox<String> comboBoxArtistNames = new JComboBox<>();
    protected JComboBox<String> comboBoxAlbums = new JComboBox<>();
    protected JComboBox<String> comboBoxSongs = new JComboBox<>();

    Vector<String> propertyValues = new Vector<>(Arrays.stream(SongProperty.values())
            .map(SongProperty::getName).collect(Collectors.toList()));

    protected JComboBox<String> comboQueryProperties = new JComboBox<>(propertyValues);

    // defining the combobox used to select the operator that will be used to build the filter (or SubQuery object)
    // than will be applied
    protected String[] operators = {">", ">=", "<", "<=", "==", "!="};
    protected JComboBox<String> comboOperators = new JComboBox<>(operators);

    // defining the textfield where the value of the SubQuery (or filter)
    protected JTextField value = new JTextField(5);

    // defining all the labels to facilitate the what goes where in the GUI
    protected JLabel filterByLabel = new JLabel("Filter by :", SwingConstants.LEFT);
    protected JLabel artistNameSelectorLabel = new JLabel("Artist:", SwingConstants.LEFT);
    protected JLabel albumSelectorLabel = new JLabel("Album:", SwingConstants.LEFT);
    protected JLabel songSelectorLabel = new JLabel("Song:", SwingConstants.LEFT);

    protected JLabel subQueryLabel = new JLabel("Filter by property:", SwingConstants.LEFT);
    protected JLabel operatorLabel = new JLabel("Operator:", SwingConstants.LEFT);
    protected JLabel operatorValueLabel = new JLabel("Value:", SwingConstants.LEFT);
    protected JLabel subQueryListLabel = new JLabel("List of filters by property (or subqueries):", SwingConstants.LEFT);

    // defining all the checkboxes to control what is shown in custom chart
    protected JCheckBox minCheckBox = new JCheckBox("Minimum");
    protected JCheckBox maxCheckBox = new JCheckBox("Maximum");
    protected JCheckBox averageCheckBox = new JCheckBox("Average");

    // defining the three JTextAreas that will need to be updated every time the buttons
    // buttonAddFilter and buttonClearFilters are clicked
    // subQueriesTextArea will show the contents of subQueryList
    protected JTextArea subQueriesTextArea = new JTextArea(1, 50);
    // statisticsTextArea will show basic summary statistics for the filteredSongEntriesList
    // (which contains the results after executing the filters or SubQuery in subQueryList)
    protected JTextArea statisticsTextArea = new JTextArea(10, 70);
    // filteredSongEntriesTextArea will show the results contained in the filteredSongEntries object
    protected JTextArea filteredSongEntriesTextArea = new JTextArea(28, 70);

    // titles for TitleBorders used to name the three main GUI areas
    protected String statisticsTitle = "SONG CATALOG STATISTICS";
    protected String songEntriesTitle = "SONG ENTRIES";
    protected String customChartTitle = "CUSTOM CHART";

    public AbstractSpotifyDashboardPanel(AbstractSongCatalog songCatalog){
        Border blackline = BorderFactory.createLineBorder(Color.black);

        this.songCatalog = songCatalog;

        this.filteredSongEntriesList = songCatalog.getSongEntriesList();

        subQueriesTextArea.setName("subQueries");
        comboQueryProperties.setName("songProperties");
        value.setName("filterValue");
        filteredSongEntriesTextArea.setName("filteredSongEntriesTextArea");
        statisticsTextArea.setName("songCatalogStats");
        comboOperators.setName("operators");
        comboBoxArtistNames.setName("artistNames");
        buttonAddFilter.setName("addFilter");
        buttonClearFilters.setName("clearFilters");

        // building the GUI using a combination of JPanels and a range of LayoutManagers
        // to get a structured GUI
        this.setLayout(new BorderLayout());

        // Query panel
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(4, 1));

        JPanel typeSelectorPanel = new JPanel();
        typeSelectorPanel.setLayout(new FlowLayout());
        typeSelectorPanel.add(artistNameSelectorLabel);
        typeSelectorPanel.add(albumSelectorLabel);

        comboBoxArtistNames.setPreferredSize(new Dimension(250,30));
        comboBoxAlbums.setPreferredSize(new Dimension(250,30));
        comboBoxSongs.setPreferredSize(new Dimension(250,30));

        typeSelectorPanel.add(filterByLabel);
        typeSelectorPanel.add(artistNameSelectorLabel);
        typeSelectorPanel.add(comboBoxArtistNames);
        typeSelectorPanel.add(albumSelectorLabel);
        typeSelectorPanel.add(comboBoxAlbums);
        typeSelectorPanel.add(songSelectorLabel);
        typeSelectorPanel.add(comboBoxSongs);

        JPanel filterBuilderPanel = new JPanel();
        filterBuilderPanel.setLayout(new FlowLayout());
        filterBuilderPanel.add(subQueryLabel);
        filterBuilderPanel.add(comboQueryProperties);
        filterBuilderPanel.add(operatorLabel);
        filterBuilderPanel.add(comboOperators);
        filterBuilderPanel.add(operatorValueLabel);
        filterBuilderPanel.add(value);
        filterBuilderPanel.add(buttonAddFilter);
        filterBuilderPanel.add(buttonClearFilters);

        queryPanel.add(typeSelectorPanel);
        queryPanel.add(filterBuilderPanel);
        queryPanel.add(subQueryListLabel);

        JScrollPane jscQueries = new JScrollPane(subQueriesTextArea);
        queryPanel.add(jscQueries);
        queryPanel.setBorder(blackline);

        // Custom chart panel
        JPanel customChartContainer = new JPanel();
        JPanel controlCustomChartContainer = new JPanel();

        customChartContainer.setLayout(new BorderLayout());
        controlCustomChartContainer.add(minCheckBox);
        controlCustomChartContainer.add(maxCheckBox);
        controlCustomChartContainer.add(averageCheckBox);

        customChart = new CustomChart(songCatalog, filteredSongEntriesList);
        AbstractCustomChartPanel customChartPanel = new CustomChartPanel(this, customChart);
        customChartContainer.add(customChartPanel, BorderLayout.CENTER);
        customChartContainer.add(controlCustomChartContainer, BorderLayout.SOUTH);

        TitledBorder tbCustomChart = BorderFactory.createTitledBorder(
                blackline, customChartTitle);
        tbCustomChart.setTitleJustification(TitledBorder.CENTER);
        customChartContainer.setBorder(tbCustomChart);

        // Statistics panel
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BorderLayout());
        JScrollPane statisticsScrollPane = new JScrollPane(statisticsTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statisticsPanel.add(statisticsScrollPane, BorderLayout.CENTER);
        statisticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        TitledBorder tbStatistics = BorderFactory.createTitledBorder(
                blackline, statisticsTitle);
        tbStatistics.setTitleJustification(TitledBorder.CENTER);
        statisticsPanel.setBorder(tbStatistics);

        // Filtered song entries panel
        JPanel songEntriesPanel = new JPanel();
        songEntriesPanel.setLayout(new BorderLayout());
        JScrollPane songEntriesScrollPane = new JScrollPane(filteredSongEntriesTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        songEntriesPanel.add(songEntriesScrollPane, BorderLayout.CENTER);
        songEntriesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        TitledBorder tbSongEntries = BorderFactory.createTitledBorder(
                blackline, songEntriesTitle);
        tbSongEntries.setTitleJustification(TitledBorder.CENTER);
        songEntriesPanel.setBorder(tbSongEntries);

        JPanel songCatalogPanel = new JPanel();
        songCatalogPanel.setLayout(new BorderLayout());
        songCatalogPanel.add(statisticsPanel, BorderLayout.NORTH);
        songCatalogPanel.add(songEntriesPanel, BorderLayout.CENTER);

        this.add(queryPanel, BorderLayout.NORTH);
        this.add(customChartContainer, BorderLayout.CENTER);
        this.add(songCatalogPanel, BorderLayout.EAST);

        // dynamically populate the comboboxes with song details
        this.populateComboBoxes();

        // adding the listeners, you will need to implement this method to register the events generated
        // by the GUI components that will be expecting a change in the results being displayed by the GUI
        this.addListeners();
    }

    /**
     * addFilter method -
     * 1- this method is called when the JButton buttonAddFilter is clicked
     * 2- adds a new filter (a SubQuery object) to subQueryList ArrayList
     * 3- updates the GUI results accordingly, i.e. updates the three JTextAreas as follows:
     *    3a- subQueriesTextArea will show the new SubQuery
     *    3b- statisticsTextArea will show the updated statistics for the results after applying this filter
     *    3c- filteredSongEntriesTextArea will show the contents of filteredSongEntriesList
     *    (the results after applying this filter)
     *    3d- the custom chart is updated to display the newly filtered song entries
     *    (Note: this can alternatively be done in another method)
     */
    public abstract void addFilter();

    /**
     * clearFilters method - clears all filters from the subQueryList ArrayList and updates
     * the relevant GUI components when the button buttonClearFilters is clicked
     */
    public abstract void clearFilters();

    /**
     * executeQuery method - executes the complete query to the relevant song list
     */
    public abstract void executeQuery();

    /**
     * populateSongDetailsComboBoxes method - dynamically populates the song detail comboboxes:
     * comboArtistNames, comboAlbumNames and comboSongNames.
     */
    public abstract void populateComboBoxes();

    /**
     * addListeners method - adds relevant actionListeners to the GUI components
     * You will need to listen (at least) to the following:
     * - buttonAddFilter
     * - buttonClearFilters
     * - comboArtistNames, comboAlbumNames and comboSongNames,
     *              if you want the filteredSongEntriesTextArea to be updated
     *              to show only the song entries specified by these comboboxes
     */
    public abstract void addListeners();

    /**
     * updateStatistics method - updates the statistics to be displayed in the
     * statisticsTextArea when the results being shown in the GUI need to be updated,
     * recalculates the average, minimum and maximum values for each song property.
     */
    public abstract void updateStatistics();

    /**
     * isMinCheckBoxSelected method - executes the complete query to the relevant song list
     */
    public abstract boolean isMinCheckBoxSelected();

    /**
     * isMaxCheckBoxSelected method - executes the complete query to the relevant song list
     */
    public abstract boolean isMaxCheckBoxSelected();

    /**
     * isAverageCheckBoxSelected method - checks if average
     */
    public abstract boolean isAverageCheckBoxSelected();
}
