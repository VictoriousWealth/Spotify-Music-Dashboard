package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.Query;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SubQuery;

import java.util.*;

/**
 * Provides an implementation for parsing textual query inputs into structured query objects.
 * This class extends {@link AbstractQueryParser} and specifically handles the transformation
 * of a list of string tokens, representing individual queries, into a structured list of {@link Query} objects.
 * Each {@link Query} object comprises multiple {@link SubQuery} objects, where each SubQuery represents a single
 * conditional statement within the query.
 * The parsing process involves validating and interpreting each component of the query to ensure
 * they conform to expected formats and logical constructs expected by the application. This involves
 * checking the validity of song properties, comparison operators, and value constraints. Malformed queries
 * are then identified and skipped to ensure that only valid queries are processed and returned.
 */
public class QueryParser extends AbstractQueryParser {
    
    /**
     * Parses a list of string tokens into a structured list of Query objects.
     * Malformed queries are skipped with an error message printed to the standard error output.
     *
     * @param queryTokens A list of string tokens to be used to build a list of queries.
     * @return a list of parsed and validated Query objects, empty if any token in valid or input is empty.
     */
    @Override
    public List<Query> buildQueries(List<String> queryTokens) {
        List<Query> queries = new ArrayList<>();

        // Combine all tokens into a single string for easier processing.
        String queryTokensUnited = queryTokens.toString()
                .replace("]", "")
                .replace(",", "")
                .replace("[select songs where ", "");

        // Split the combined tokens into individual query strings based on the pattern in the text file.
        List<String> queryList = Arrays.stream(queryTokensUnited.split(" select songs where ")).toList();

        // Loop through each individual query string to process further.
        label: for (String toBeValidatedQuery : queryList) {
            List<SubQuery> subQueries = new ArrayList<>();

            // Split the query into subqueries on the 'and' keyword.
            List<String> subQueryList = Arrays.stream(toBeValidatedQuery.split(" and ")).toList();
            for (String toBeValidatedSubQuery : subQueryList) {
                // Split each subquery into its components: property, operator, and value.
                List<String> parts = Arrays.stream(toBeValidatedSubQuery.split(" ")).toList();
                if (parts.size() != 3) {
                    System.err.println("Alert: Malformed query found!");
                    continue label;  // Skip to the next query if current is malformed.
                }

                try {
                    // Validate and parse the subquery parts.
                    SongProperty songProperty = SongProperty.fromName(parts.get(0));
                    String operator = parts.get(1);
                    double value = Double.parseDouble(parts.get(2));

                    if (!SubQuery.isValidOperator(operator)) break;  // Check if the operator is valid.

                    // Create and add the subquery to the list.
                    subQueries.add(new SubQuery(songProperty, operator, value));
                } catch (Exception e) {
                    System.err.println("Alert: Malformed query found!");
                    break;  // Break on parsing error, skip this malformed subquery.
                }
            }

            // Add the new Query object if it has valid subqueries.
            if (!subQueries.isEmpty()) {
                queries.add(new Query(subQueries));
            }
        }

        return queries;
    }
}
