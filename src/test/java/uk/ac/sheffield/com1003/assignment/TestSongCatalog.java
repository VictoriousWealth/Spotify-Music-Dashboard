package uk.ac.sheffield.com1003.assignment;

import org.junit.jupiter.api.Test;
import uk.ac.sheffield.com1003.assignment2023.SongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongPropertyMap;
import uk.ac.sheffield.com1003.assignment.common.TestCommon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSongCatalog {

    @Test
    public void testUpdateCatalog() {
        SongCatalog catalog = null;
        try {
            catalog = new SongCatalog(TestCommon.SONG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(32833, catalog.getSongEntriesList().size());

    }

    @Test
    public void testGetMeanAverageValue() {
        SongCatalog songCatalog = null;
        try {
            songCatalog = new SongCatalog(TestCommon.SONG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SongPropertyMap ap = new SongPropertyMap();
        SongPropertyMap bp = new SongPropertyMap();

        ap.putProperty(SongProperty.ENERGY, 4);
        bp.putProperty(SongProperty.ENERGY, 6);

        SongEntry a = new SongEntry(1, ap);
        SongEntry b = new SongEntry(1, bp);

        List<SongEntry> list = new ArrayList<>();
        list.add(a);
        list.add(b);

        assertEquals(5, songCatalog.getAverageValue(SongProperty.ENERGY, list));
    }

}
