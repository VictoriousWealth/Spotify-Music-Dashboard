package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.util.Arrays;

/**
 * This class is designed to create SubQuery objects which make up parts of an individual query.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public class SubQuery
{
	public static final String[] VALID_OPERATORS = {"<", "<=", "=", ">=", ">", "!="};

	final SongProperty songProperty;
	final String operator;
	final double value;

	/**
	 * Constructor for subquery.
	 *
	 * @param songProperty the property involved in the query.
	 * @param operator the operator involved in the query.
	 * @param value the value involved in the query.
	 */
	public SubQuery(SongProperty songProperty, String operator, double value) {
		this.songProperty = songProperty;
		this.operator = operator;
		this.value = value;
	}

	public SongProperty getSongProperty()
	{
		return songProperty;
	}

	public String getOperator() {
		return operator;
	}

	public double getValue() {
		return value;
	}

	public String toString() {
		return this.getSongProperty() + " " +
				this.getOperator() + " " +
				this.getValue();
	}

	/**
	 * Check if a SongEntry satisfies the SubQuery.
	 *
	 * @param songEntry the SongEntry to check
	 * @return true if the song entry matches the SubQuery; false otherwise
	 */
	protected boolean songEntriesMatchesSubQuery(SongEntry songEntry) {
		SongProperty songProperty = getSongProperty();
		double propertyValue = getValue();

		switch (getOperator()) {

			case ">":
				if (songEntry.getSongProperty(songProperty) > propertyValue)
					return true;
				break;
			case ">=":
				if (songEntry.getSongProperty(songProperty) >= propertyValue)
					return true;
				break;
			case "<":
				if (songEntry.getSongProperty(songProperty) < propertyValue)
					return true;
				break;
			case "<=":
				if (songEntry.getSongProperty(songProperty) <= propertyValue)
					return true;
				break;
			case "=":
				if (songEntry.getSongProperty(songProperty) == propertyValue)
					return true;
				break;
			case "!=":
				if (songEntry.getSongProperty(songProperty) != propertyValue)
					return true;
				break;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((songProperty == null) ? 0 : songProperty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubQuery other = (SubQuery) obj;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return songProperty == other.songProperty;
	}

	/**
	 * Check if a String is a valid operator.
	 *
	 * @param operatorToCheck the String to check
	 * @return true if the String is a valid operator; false otherwise
	 */
	public static boolean isValidOperator(String operatorToCheck) {
		return Arrays.asList(VALID_OPERATORS).contains(operatorToCheck);
	}
}
