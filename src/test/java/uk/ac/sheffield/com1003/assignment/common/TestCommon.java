package uk.ac.sheffield.com1003.assignment.common;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongDetail;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongPropertyMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TestCommon
{

    // File input, you should _not_ modify these files or create new ones
    public static final String SONG_FILE = "src/main/resources/spotify_songs.tsv";


    public static final List<SongEntry> DUMMY_SONG_ENTRIES = Arrays.asList(
            new SongEntry(0, new SongPropertyMapBuilder()
                    .with(SongProperty.DURATION, 200000)
                    .with(SongProperty.ENERGY, 7.4)
                    .with(SongProperty.ACOUSTICNESS, 0.2)
                    .with(SongProperty.DANCEABILITY, 0.7)
                    .with(SongProperty.INSTRUMENTALNESS, 0.1)
                    .with(SongProperty.LIVENESS, 0.1)
                    .with(SongProperty.LOUDNESS, -7.4)
                    .with(SongProperty.TEMPO, 120)
                    .with(SongProperty.VALENCE, 0.5)
                    .with(SongProperty.SPEECHINESS, 0.1)
                .build())
    );

    /**
     * Helper method to tokenize a String, by splitting a String by the presence of
     * whitespace
     *
     * @param toTokenize the String to tokenize.
     * @return A list of token Strings
     */
    public static List<String> tokenizeString(String toTokenize) {
        List<String> tokens;
        toTokenize = toTokenize.replaceAll(Pattern.compile("\\s+").pattern(), " ");
        tokens = Arrays.asList(toTokenize.split(Pattern.compile(" ").pattern()));
        return tokens;
    }

    /**
     * Helper method to split a multi-line String into a List of Strings by the
     * newline character (\n).
     *
     * @param toSplit the String to split into a series of lines.
     * @return A list of individual lines.
     */
    @SuppressWarnings("unused")
    public static List<String> getLines(String toSplit) {
        List<String> lines;
        lines = Arrays.asList(toSplit.split(Pattern.compile("\n").pattern()));
        return lines;
    }

    /**
     * Class to manually create a SongPropertyMap for testing purposes
     * Not necessarily recommended for normal use, but it can be useful for tests
     */
    private static class SongPropertyMapBuilder {

        final SongPropertyMap map = new SongPropertyMap();

        SongPropertyMapBuilder with(@SuppressWarnings("SameParameterValue") SongProperty songProperty, double value) {
            this.map.putProperty(songProperty, value);
            return this;
        }

        SongPropertyMap build() {
            return map;
        }
    }

}
