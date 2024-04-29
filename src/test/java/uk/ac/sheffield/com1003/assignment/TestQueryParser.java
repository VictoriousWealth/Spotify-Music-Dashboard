package uk.ac.sheffield.com1003.assignment;

import org.junit.jupiter.api.Test;
import uk.ac.sheffield.com1003.assignment.common.TestCommon;
import uk.ac.sheffield.com1003.assignment2023.QueryParser;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueryParser {

    @Test
    public void testReadSingleQuery() {
        QueryParser parser = new QueryParser();
        List<Query> queries = parser.buildQueries(TestCommon.tokenizeString("SELECT SONGS WHERE POPULARITY > 60".toLowerCase()));
        assertEquals(1, queries.size());
        assertEquals(1, queries.get(0).getSubQueryList().size());
    }

}
