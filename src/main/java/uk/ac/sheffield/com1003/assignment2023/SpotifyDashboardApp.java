package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.SpotifyDashboard;
import uk.ac.sheffield.com1003.assignment2023.gui.SpotifyDashboardPanel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the main class used to run the assignment's GUI.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * <p>
 * Copyright (c) University of Sheffield 2023
 */
public class SpotifyDashboardApp {
    private final AbstractSongCatalog songCatalog;
    private final List<Query> listOfQueries;

    /**
     * Constructs a new SpotifyDashboardApp with data loaded from queries.txt and song_spotify.tsv
     * This constructor initializes the song catalog with data from the song file and builds
     * a list of queries from the query file. If any errors occur during file reading or data parsing,
     * the application prints the error to the standard error stream and may exit.
     */
    public SpotifyDashboardApp(String songFile, String queryFile) {
        AbstractSongCatalog songCatalog = null;
        List<Query> listOfQueries = null;

        try {
            // Attempt to load the song catalog from the provided file.
            songCatalog = new SongCatalog(songFile);

            // Read and parse query tokens from the file.
            List<String> queryTokens = new ArrayList<>(AbstractQueryParser.readQueryTokensFromFile(queryFile));

            try {
                // Build queries from the parsed tokens.
                listOfQueries = new ArrayList<>(new QueryParser().buildQueries(queryTokens));
            } catch (IllegalArgumentException e) {
                // Handle exceptions related to query parsing specifically.
                System.err.println("Failed to parse queries: " + e.getMessage());
            }
        } catch (IllegalArgumentException | IOException e) {
            // Handle exceptions related to file reading or song catalog construction.
            System.err.println("Failed to initialize song catalog or read queries: " + e.getMessage());
            System.exit(-1);
        }

        // Assign loaded data to the instance variables.
        this.songCatalog = songCatalog;
        this.listOfQueries = listOfQueries;
    }

    /**
     * The main entry point of the SpotifyDashboardApp.
     * This method initializes the application using song data and query specifications provided via command-line arguments.
     * If no arguments are provided, it defaults to pre-defined file paths for the song and query data.
     * The application starts both a command-line interface (CLI) and a graphical user interface (GUI) to interact with the song data.
     *
     * @param args Command-line arguments expected to contain:
     *             - args[0]: Path to the song data file (.tsv format).
     *             - args[1]: Path to the query file (text format).
     *             If no arguments are provided, default paths are used.
     */
    public static void main(String[] args) {
        // Check if any command-line arguments are provided; if not, use default file paths.
        if (args.length == 0) {
            args = new String[]{
                    "./src/main/resources/spotify_songs.tsv", // Default song data file path.
                    "./src/main/resources/queries.txt"       // Default query file path.
            };
        }

        // Initialize the SpotifyDashboardApp with the specified or default file paths.
        SpotifyDashboardApp spotifyDashboardApp = new SpotifyDashboardApp(args[0], args[1]);

        // Start the command-line interface.
        spotifyDashboardApp.startCLI();

        // Start the graphical user interface.
        spotifyDashboardApp.startGUI();
    }

    /**
     * Starts the Command Line Interface (CLI) version of the SpotifyDashboardApp.
     * This method displays the basic song catalog information and processes
     * a series of predefined queries against this catalog, displaying the results.
     * It organizes the display into sections with headers and footers for clarity.
     */
    public void startCLI() {
        // Print header for the song catalog section
        System.out.println("##########################################################################################");
        // Display questions and their answers (presumably about the song catalog)
        printQuestionAnswers();
        // Print footer for the song catalog section
        System.out.println("##########################################################################################");

        // Add a newline for better separation in CLI output
        System.out.println("\n");

        // Print header for the queries section
        System.out.println("##########################################################################################");
        // Print the number of queries to be executed
        printNumberQueries();
        // Execute predefined queries and print the results
        executeQueries();
        // Print footer for the queries section
        System.out.println("##########################################################################################\n");
    }

    /**
     * Executes a list of predefined queries against the song catalog and prints the results.
     * Each query is printed along with its corresponding result set, which includes details
     * of song entries that match the query criteria.
     */
    private void executeQueries() {
        // Announce the start of query execution
        System.out.println("Executing queries...");

        // Iterate over each query in the list of queries
        for (Query query : listOfQueries) {
            // Print the query description
            System.out.println(query.toString() + ":");
            // Execute the query and retrieve the results
            List<SongEntry> queryResults = query.executeQuery(songCatalog);
            // Print the song entries returned by the query
            printSongEntries(queryResults);
            // Print a newline for separation between query results
            System.out.println();
        }
    }

    /**
     * Prints the total number of queries that will be executed.
     * This helps provide a clear expectation to the user about the number of operations to be performed.
     */
    private void printNumberQueries() {
        System.out.printf("The number of queries to be executed is [%s]\n", listOfQueries.size());
    }

    /**
     * Displays key statistics and answers about the song catalog.
     * This includes counts of unique songs and artists, as well as calculated average and extreme values
     * for various song properties such as duration, tempo, and loudness.
     */
    private void printQuestionAnswers() {
        // Print the number of unique songs and artists
        printNumberUniqueSongs();
        printNumberUniqueArtists();

        try {
            // Display various computed statistics from the song catalog
            System.out.printf("The average duration of a song in the dataset is: [%s].\n",
                    songCatalog.getAverageValue(SongProperty.DURATION, songCatalog.getSongEntriesList()));
            System.out.printf("The average tempo of a song in the dataset is: [%s].\n",
                    songCatalog.getAverageValue(SongProperty.TEMPO, songCatalog.getSongEntriesList()));
            System.out.printf("The maximum loudness in the dataset is [%s].\n",
                    songCatalog.getMaximumValue(SongProperty.LOUDNESS, songCatalog.getSongEntriesList()));
            System.out.printf("The minimum tempo in the dataset [%s].\n",
                    songCatalog.getMinimumValue(SongProperty.TEMPO, songCatalog.getSongEntriesList()));
        } catch (NoSuchElementException e) {
            // Handle cases where the song catalog is empty and statistics cannot be computed
            System.err.println("Song entries list is empty!");
        }
    }

    /**
     * Prints the number of unique artists found in the song catalog. This method considers an artist
     * unique if their name (and other artists' names) has not appeared previously in the dataset in
     * the same order.
     */
    private void printNumberUniqueArtists() {
        // Retrieve all song entries from the catalog.
        List<SongEntry> songEntries = songCatalog.getSongEntriesList();
        // Use a HashSet to store unique artist names, ensuring each artist is counted only once.
        Set<String> listOfUniqueArtists = new HashSet<>();
        for (SongEntry songEntry : songEntries) {
            // Add the artist name to the set of unique artists.
            listOfUniqueArtists.add(songEntry.getSongArtist());
        }
        // Output the total count of unique artists to the console.
        System.out.printf("The total number of different artists in the dataset is: %s.\n", listOfUniqueArtists.size());
    }

    /**
     * Prints the total number of unique songs in the song catalog. A song is considered unique based on a combination
     * of its title, artist, album name, and various song properties (duration, popularity, danceability, energy, loudness,
     * speechiness, acousticness, instrumentalness, liveness, valence, tempo).
     */
    private void printNumberUniqueSongs() {
        // Retrieve all song entries from the catalog.
        List<SongEntry> songEntries = songCatalog.getSongEntriesList();
        // Use a HashSet to store unique song descriptions.
        Set<String> uniqueSongEntries = new HashSet<>();
        for (SongEntry songEntry : songEntries) {
            // Construct a unique identifier for each song based on its properties.
            String uniqueIdentifier = songEntry.getSongName() + " " +
                    songEntry.getSongArtist() + " " +
                    songEntry.getSongAlbumName() + " " +
                    Arrays.stream(SongProperty.values())
                            .map(property -> songEntry.getSongProperty(property)+"")
                            .collect(Collectors.joining(" "));
            // Add the unique identifier to the set.
            uniqueSongEntries.add(uniqueIdentifier);
        }
        // Output the total count of unique songs to the console.
        System.out.printf("The total number of unique songs in the dataset is: %s out of %s.\n",
                uniqueSongEntries.size(), songEntries.size());
    }

    /**
     * Prints the first five song entries from the song catalog to the console.
     * This method provides a quick preview of the data contained in the song catalog,
     * formatted within a clearly marked section for easy identification.
     */
    private void printFirstFiveSongEntries() {
        // Print a header to visually separate the song entry section in the console output.
        System.out.println("#########################################################################");
        System.out.println("Printing first five song entries");
        // Retrieve and print the first five song entries from the song catalog.
        System.out.println(songCatalog.getFirstFiveSongEntries());
        // Print a footer to end the section.
        System.out.println("#########################################################################");
    }

    /**
     * Prints a specified list of song entries to the console, limiting the output to the first five entries.
     * This method is used to avoid excessive console output and to focus on a manageable number of entries.
     *
     * @param songEntriesList The list of song entries to be printed.
     */
    private void printSongEntries(List<SongEntry> songEntriesList) {
        // Counter to keep track of the number of printed entries.
        int count = 1;
        for (SongEntry song : songEntriesList) {
            System.out.println(song);
            // Stop printing after five entries to keep the console output concise.
            if (++count > 5)
                break;
        }
    }

    /**
     * Initializes and displays the Graphical User Interface (GUI) of the SpotifyDashboardApp.
     * This method sets up the main dashboard panel and makes the GUI visible to the user,
     * providing an interactive way to explore the song catalog.
     */
    public void startGUI() {
        // Initialize the main dashboard panel with the song catalog.
        AbstractSpotifyDashboardPanel spotifyDashboardPanel = new SpotifyDashboardPanel(songCatalog);
        // Create the main application window and pass the dashboard panel to it.
        SpotifyDashboard songDashboard = new SpotifyDashboard(spotifyDashboardPanel);
        // Make the application window visible to the user.
        songDashboard.setVisible(true);
    }
}
