This document provides a comprehensive explanation of the **Spotify Music Dashboard** codebase, a Java application for analyzing and visualizing song data.

### Project Overview 🎵

This is a data-driven desktop application built with **Java** and the **Swing GUI framework**. It's a **Gradle** project designed to parse a dataset of Spotify songs, allow users to filter this data through a sophisticated query system, and visualize the results in both tabular and graphical formats.

The architecture is a key feature of this project. It's built upon a framework of **`abstract` classes** (`codeprovided` package) that define the structure and required methods. The student's task is to create **concrete implementations** of these classes to provide the actual application logic. This design pattern enforces a clear separation of concerns and demonstrates a strong understanding of **inheritance** and **polymorphism**.



---
### Codebase Structure

The project uses a standard Gradle layout, separating the provided framework, student implementation, resources, and tests.

* **`src/main/java/uk/ac/sheffield/com1003/assignment2023/`**: The root for all main application code.
    * **`codeprovided/`**: This package contains the "skeleton" of the application. It consists of abstract classes, data models (like `SongEntry`, `Query`), and GUI components that the student must extend and implement.
    * **`Student Implementation`**: The root package (`assignment2023/`) and its `gui/` sub-package contain the concrete classes (`SongCatalog`, `QueryParser`, `SpotifyDashboardPanel`, etc.) that provide the working logic by implementing the abstract methods defined in the `codeprovided` framework.
* **`src/main/resources/`**: Contains the data files:
    * `spotify_songs.csv` / `spotify_songs.tsv`: The primary dataset containing song properties.
    * `queries.txt`: A text file with predefined queries to be parsed and executed.
* **`src/test/java/`**: A suite of **JUnit 5** tests designed to verify the correctness of the student's implementation against the requirements of the abstract framework.

---
### Core Components Breakdown

The system is logically divided into data management, querying, and the graphical user interface.

#### 1. Data Loading and Management

* **`AbstractSongCatalog` & `SongCatalog.java`**
    * `AbstractSongCatalog` provides the framework for reading a song file.
    * The concrete **`SongCatalog`** class implements the core logic. Its most important method is `parseSongEntryLine(String line)`, which takes a single line from the `.tsv` file, splits it, and populates a `SongPropertyMap` with the song's attributes. It also implements methods to calculate statistics (min, max, average) on lists of songs.

#### 2. Querying Engine

* **`AbstractQueryParser` & `QueryParser.java`**
    * `AbstractQueryParser` reads the `queries.txt` file and tokenizes its contents.
    * The concrete **`QueryParser`** implements `buildQueries()`, which intelligently parses these tokens into a structured list of `Query` objects.
* **`Query.java` & `SubQuery.java`**
    * These are data model classes. A **`SubQuery`** represents a single filter condition (e.g., `popularity > 60`).
    * A **`Query`** is a collection of one or more `SubQuery` objects. Its `executeQuery()` method filters a list of songs by applying each of its sub-queries sequentially.

#### 3. Data Models

* **`SongEntry.java`**: Represents a single song. Each song has an ID and a `SongPropertyMap` containing its data.
* **`SongPropertyMap.java`**: A wrapper around two `HashMap`s. It cleanly separates numeric **`SongProperty`** (e.g., `ENERGY`, `DANCEABILITY`) from string-based **`SongDetail`** (e.g., `NAME`, `ARTIST`).
* **`SongProperty.java` & `SongDetail.java`**: `enum` types that define all possible song attributes, making the code type-safe and readable.

#### 4. GUI Framework and Implementation (`gui` packages)

* **`SpotifyDashboard.java`**: The main application window (`JFrame`).
* **`AbstractSpotifyDashboardPanel` & `SpotifyDashboardPanel.java`**
    * The abstract panel sets up the entire GUI layout, creating all the Swing components (combo boxes for artists/albums/songs, buttons, text areas for results, and placeholders for charts).
    * The concrete **`SpotifyDashboardPanel`** is the main controller for the GUI. It implements all the **event handling logic** (`addListeners()`) and methods that react to user input, such as `addFilter()`, `clearFilters()`, and `populateComboBoxes()`.
* **`AbstractCustomChart` & `CustomChart.java`**
    * This is the **data model** for the custom visualization.
    * The concrete **`CustomChart`** implements `updateCustomChartContents()`, which takes a list of filtered songs and calculates the required statistics (min, max, average) for the chart to display.
* **`AbstractCustomChartPanel` & `CustomChartPanel.java`**
    * This is the `JPanel` responsible for **drawing the visualization**.
    * The concrete **`CustomChartPanel`** contains the `paintComponent(Graphics g)` method. This method holds all the **Java 2D drawing logic** to render the circular bar chart and the radar chart based on the data held in the `CustomChart` model.

---
### Execution Flow

1.  **Initialization**: `SpotifyDashboardApp.main()` starts the application. It creates an instance of `SongCatalog` to load `spotify_songs.tsv` and an instance of `QueryParser` to parse `queries.txt`.
2.  **CLI**: A command-line interface runs first, printing basic statistics and the results of the parsed queries to the console.
3.  **GUI Launch**: The `SpotifyDashboard` (`JFrame`) is created, containing the main `SpotifyDashboardPanel`.
4.  **User Interaction**: The `SpotifyDashboardPanel` listens for user actions:
    * **Selecting an item** from a combo box (e.g., an artist) triggers an event.
    * **Clicking "Add by Property Filter"** triggers an event.
5.  **Event Handling**: The listeners in `SpotifyDashboardPanel` respond:
    * A new `SubQuery` is created and added to a list.
    * The `executeQuery()` method is called, which filters the master song list based on all active `SubQuery` objects and combo box selections.
    * The panel then updates all its child components: the text areas are populated with filtered songs and new statistics, and the `CustomChart` data model is updated.
6.  **Rendering**: The `repaint()` method is called on the panel. This automatically invokes the `paintComponent()` method in `CustomChartPanel`, which redraws the custom visualization using the newly updated data.

---
### Summary

This is a well-structured Java application that separates a provided abstract framework from the student's concrete implementation. It effectively models real-world data processing: loading a dataset (`SongCatalog`), defining a way to filter it (`QueryParser`, `Query`), and presenting it to the user through a complex, interactive GUI (`SpotifyDashboardPanel`) with custom data visualizations (`CustomChartPanel`).
