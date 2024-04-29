package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.util.*;

/**
 * This class is designed to be used to create Query objects from the Query List.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public class Query {

    final List<SubQuery> subQueryList;

    /**
     * Constructor.
     *
     * @param subQueryList - List of queries.
     */
    public Query(List<SubQuery> subQueryList) {
        this.subQueryList = subQueryList;
    }

    /**
     * Getter for the subQueryList.
     *
     * @return List of SubQueries
     */
    public List<SubQuery> getSubQueryList() {
        return subQueryList;
    }


    /**
     * Apply the Query to the songs of a SongCatalog, retrieve the songs which match.
     *
     * @param songCatalog the SongCatalog to query
     * @return List of filtered Song entries
     */
    public List<SongEntry> executeQuery(AbstractSongCatalog songCatalog) {
        // Start by adding all the song entries with the matching type
        List<SongEntry> filteredSongEntriesList =
                new ArrayList<>(songCatalog.getSongEntriesList());

        // Continuously filter the song entries according to each SubQuery
        for (SubQuery subQuery : subQueryList) {
            filteredSongEntriesList =
                    executeSubQuery(filteredSongEntriesList, subQuery);

        }
        // Return the filtered song entries
        return filteredSongEntriesList;
    }

    /**
     * Filter provided song entries according to a SubQuery.
     *
     * @param songEntries the Collection of relevant song entries
     * @param subQuery the SubQuery to use to filter song entries
     * @return List of all song entries which meet criteria
     */
    private List<SongEntry> executeSubQuery(Collection<SongEntry> songEntries, SubQuery subQuery) {
        List<SongEntry> filteredSongEntriesList = new ArrayList<>();

        for (SongEntry w : songEntries) {
            if(subQuery.songEntriesMatchesSubQuery(w))
                filteredSongEntriesList.add(w);
        }
        return filteredSongEntriesList;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subQueryList == null) ? 0 : subQueryList.hashCode());
		return result;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(subQueryList, query.subQueryList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Query: ").append("{");
        Iterator<SubQuery> subQueryIterator = subQueryList.iterator();
        while (subQueryIterator.hasNext()) {
            sb.append(subQueryIterator.next());
            if(subQueryIterator.hasNext())
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}