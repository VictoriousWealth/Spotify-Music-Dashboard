package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is an Abstract class in charge of parsing the text file containing the queries
 * and building them using the class Query.
 *
 * Should be implemented as uk.ac.sheffield.assignment2023.QueryParser
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */

public abstract class AbstractQueryParser {

    /**
     * This method gets a text file containing a list of queries and returns a list
     * of tokens that will be used to build the queries that need to be executed.
     *
     * @param queryFileLocation provides the file name containing the queries.
     * @return a list of Strings representing query tokens
     *
     */
    public static List<String> readQueryTokensFromFile(String queryFileLocation){
        List<String> queryTokens = new ArrayList<>();
        String split = " ";

        try (BufferedReader br = new BufferedReader(new FileReader(queryFileLocation))){
            String line = br.readLine();
            if (line == null) {
                System.err.println("Queries file is empty. No queries will be executed.");
            }
            while (line != null) {
                String[] query = line.toLowerCase().split(split);
                queryTokens.addAll(Arrays.asList(query));
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println(queryFileLocation + " could not be found. No queries will be executed.");
        } catch (IOException e) {
            System.err.println("File could not be handled. No queries will be executed.");
        }
        return queryTokens;
    }

    /**
     * This abstract method builds a list of Query objects representing the queries provided
     * in the list of tokens provided as an argument.
     *
     * @param queryTokens provides the list of tokens to be used to build a list of queries
     * @return a list of queries
     *
     */
    public abstract List<Query> buildQueries(List<String> queryTokens);
}
