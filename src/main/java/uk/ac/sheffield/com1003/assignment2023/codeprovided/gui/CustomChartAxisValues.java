package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

/**
 * Used to store the Minimum, Maximum and Average values for each Song Property.
 *
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */

public class CustomChartAxisValues
{
    private final double min;
    private final double max;
    private final double average;

    public CustomChartAxisValues(double min, double max, double average)
    {
        this.min = min;
        this.max = max;
        this.average = average;
    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }

    public double getAverage()
    {
        return average;
    }

    @Override
    public String toString() {
        return "CustomChartAxisValues{" +
                "min=" + min +
                ", max=" + max +
                ", average=" + average +
                '}';
    }
}

