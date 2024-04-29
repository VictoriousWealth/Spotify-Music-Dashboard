package uk.ac.sheffield.com1003.assignment;

import org.junit.jupiter.api.Test;
import uk.ac.sheffield.com1003.assignment2023.SongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongPropertyMap;
import uk.ac.sheffield.com1003.assignment.common.TestCommon;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileParsing
{
    // TODO Make a test that validates IDs, types and data are correct for the example dataset

    private static final double DELTA = 0.0001;

    @Test
    public void testParseFileLine() {
        SongCatalog songCatalog = null;
        try {
            songCatalog = new SongCatalog(TestCommon.SONG_FILE);
        } catch (IOException e) {
            e. printStackTrace();
        }
        String sampleLine = "Black Or White\tMichael Jackson\t254933\tDangerous\t0\t0.518\t0.9\t-3.748" +
                "\t0.0933\t0.172\t0.0315\t0.0713\t0.872\t115.029";
        SongPropertyMap map = songCatalog.parseSongEntryLine(sampleLine);

        assertEquals(0.172, map.getProperty(SongProperty.ACOUSTICNESS));
        assertEquals(0.518, map.getProperty(SongProperty.DANCEABILITY));
        assertEquals(0.9, map.getProperty(SongProperty.ENERGY));
    }

    @Test
    public void testParseFileLineTooManyColumns() {
        SongCatalog catalog = null;
        try {
            catalog = new SongCatalog(TestCommon.SONG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sampleLine = "Black Or White\tMichael Jackson\t254933\tDangerous\t0\t0.518\t0.9\t-3.748" +
                "\t0.0933\t0.172\t0.0315\t0.0713\t0.872\t115.029\t0.0";

        SongCatalog songCatalog = catalog;
        assertThrows(IllegalArgumentException.class, () -> songCatalog.parseSongEntryLine(sampleLine));
    }

    @Test
    public void testParseFileLineTooFewColumns() {
        SongCatalog catalog = null;
        try {
            catalog = new SongCatalog(TestCommon.SONG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sampleLine = "Black Or White\tMichael Jackson\t254933\tDangerous\t0\t0.518\t0.9\t-3.748" +
                "\t0.0933\t0.172\t0.0315\t0.0713\t0.872";

        SongCatalog songCatalog = catalog;
        assertThrows(IllegalArgumentException.class, () -> songCatalog.parseSongEntryLine(sampleLine));
    }

}
