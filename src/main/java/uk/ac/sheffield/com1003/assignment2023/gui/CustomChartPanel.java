package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChart;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChartPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a custom panel for displaying the custom chart based on some of the song properties.
 * This class extends AbstractCustomChartPanel and manages the rendering of both
 * circular bar and radar charts, which visually represent data from a custom chart model.
 */
public class CustomChartPanel extends AbstractCustomChartPanel {

    private Arc2D sectorForPopularity;
    private Arc2D arc;
    private Arc2D sectorForTempo;
    private Arc2D sectorForLoudness;
    private HashMap<SongProperty, Point2D> maxPointByPropertyMap;
    private ArrayList<Point2D> valPointByPropertyArrayList;

    /**
     * Constructs a CustomChartPanel with a reference to the parent panel and the custom chart model.
     *
     * @param parentPanel The parent panel containing this chart panel.
     * @param customChart The custom chart model providing data and configurations.
     */
    public CustomChartPanel(AbstractSpotifyDashboardPanel parentPanel, AbstractCustomChart customChart) {
        super(parentPanel, customChart);
    }

    /**
     * Overridden paintComponent method to draw custom charts.
     * This method handles the rendering of the circular and radar charts,
     * including the application of antialiasing to smooth out the chart lines a bit.
     *
     * @param g The Graphics object used for drawing operations after being cast to a Graphics2D object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension d = getSize();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set border and background color
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, d.width, d.height);
        g2d.setColor(new Color(238, 241, 238));
        g2d.fillRect(0, 0, d.width, d.height);

        // Retrieve axis values for song properties
        Map<SongProperty, CustomChartAxisValues> customChartAxisValues = getCustomChart().getCustomChartAxesValues();
        CustomChartAxisValues[] songAttributesForCircularBarChart = {
                customChartAxisValues.get(SongProperty.POPULARITY),
                customChartAxisValues.get(SongProperty.TEMPO),
                customChartAxisValues.get(SongProperty.LOUDNESS)
        };

        // Draw charts
        drawCircularBarChart(g2d, d, songAttributesForCircularBarChart);
        drawRadarChart(g2d, d);
        if (getParentPanel().isMinCheckBoxSelected()) {
            showMinDataPlot(g2d, d);
        }
    }

    /**
     * Draws the radar chart based on the data provided by the custom chart model.
     *
     * @param g2d The Graphics2D object for drawing.
     * @param dimension The dimensions of the panel.
     */
    private void drawRadarChart(Graphics2D g2d, Dimension dimension) {
        maxPointByPropertyMap = new HashMap<>();
        double offset = 20d;
        double radius = Math.min(dimension.height, dimension.width) / 2d - offset;
        g2d.translate((dimension.height - dimension.width) / 2, 0); // Center the radar chart
        g2d.setColor(Color.BLACK);

        // Setup radar chart constraints and labels
        creationOfRadarChartConstraints(g2d, offset, radius, dimension, maxPointByPropertyMap);
        labelRadarChartAxis(g2d, maxPointByPropertyMap, (int) offset, dimension);

        // Plot data points based on chart selections
        if (getParentPanel().isMaxCheckBoxSelected()) {
            showMaxDataPlotInRadarChart(g2d);
        }
        if (getParentPanel().isAverageCheckBoxSelected()) {
            showAvgDataPlotInRadarChart(g2d, dimension, getCustomChart().getCustomChartAxesValues());
        }
    }

    /**
     * Displays the minimum data plot on the radar chart.
     * This method is responsible for highlighting the minimum values on the chart by placing
     * a marker(an ellipse) at the center of the radar chart when the minimum checkbox is selected.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param dimension The dimensions of the panel.
     */
    private void showMinDataPlot(Graphics2D g2d, Dimension dimension) {
        g2d.setColor(Color.RED);
        // Draw a filled ellipse at the center of the radar chart to represent the minimum data point
        g2d.fill(new Ellipse2D.Double(dimension.width / 2d - 5, dimension.height / 2d - 5, 10, 10));
    }

    /**
     * Plots average data points for the radar chart.
     * This method iterates through each applicable song property to plot average data,
     * connecting these points to form a coherent visualization on the radar chart.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param dimension The dimensions of the panel.
     * @param customChartAxisValuesMap A map containing axis values (min, max, avg) for each song property.
     */
    private void showAvgDataPlotInRadarChart(Graphics2D g2d, Dimension dimension,
                                             Map<SongProperty, CustomChartAxisValues> customChartAxisValuesMap) {
        g2d.setColor(Color.BLUE); // Set color for average data points
        valPointByPropertyArrayList = new ArrayList<>();

        for (SongProperty songProperty : SongProperty.values()) {
            // Exclude properties that are not plotted on the radar chart
            if (List.of(SongProperty.LOUDNESS, SongProperty.TEMPO, SongProperty.POPULARITY,
                    SongProperty.DURATION).contains(songProperty)) continue;

            plotRadarChartAvgData(customChartAxisValuesMap, songProperty, dimension, g2d);
        }

        // Draw connecting lines between plotted average points
        for (int index = 0; index < valPointByPropertyArrayList.size() - 1; index++) {
            Line2D connectingLine = new Line2D.Double(
                    valPointByPropertyArrayList.get(index).getX(),
                    valPointByPropertyArrayList.get(index).getY(),
                    valPointByPropertyArrayList.get(index + 1).getX(),
                    valPointByPropertyArrayList.get(index + 1).getY());
            g2d.draw(connectingLine);
        }
    }

    /**
     * Calculates and plots a single average data point for a given song property on the radar chart.
     * This method determines the plot position based on the average value relative to the maximum
     * and minimum values for that property.
     *
     * @param customChartAxisValuesMap The map containing axis values for each song property.
     * @param songProperty The song property to plot.
     * @param dimension The dimensions of the panel.
     * @param g2d The Graphics2D object used for drawing.
     */
    private void plotRadarChartAvgData(Map<SongProperty, CustomChartAxisValues> customChartAxisValuesMap,
                                       SongProperty songProperty, Dimension dimension, Graphics2D g2d) {
        double data = customChartAxisValuesMap.get(songProperty).getAverage();
        Point2D startingPoint = new Point2D.Double(dimension.width / 2d, dimension.height / 2d);
        Point2D endingPoint = maxPointByPropertyMap.get(songProperty);

        // Calculate the point to plot based on the average data
        Point2D pointToPlot = new Point2D.Double(
                startingPoint.getX() + data * (endingPoint.getX() - startingPoint.getX()),
                startingPoint.getY() + data * (endingPoint.getY() - startingPoint.getY()));

        valPointByPropertyArrayList.add(pointToPlot);
        // Draw the data point as a filled ellipse
        g2d.fill(new Ellipse2D.Double(pointToPlot.getX() - 5, pointToPlot.getY() - 5, 10, 10));
    }

    /**
     * Draws maximum data points on the radar chart for specific song properties.
     * This method plots a visual marker for the maximum value of each relevant song property,
     * excluding properties like popularity, duration, tempo, and loudness which are handled separately.
     * Additionally, it connects these data points with lines to visually represent the relationship
     * between different properties.
     *
     * @param g2d The Graphics2D object used for drawing.
     */
    private void showMaxDataPlotInRadarChart(Graphics2D g2d) {
        // Loop twice for enhancing the visibility of the plots
        for (int i = 0; i < 2; i++) {
            g2d.setColor(Color.GREEN); // Set color for maximum data points

            // Iterate over each song property to draw maximum data points where applicable
            for (SongProperty songProperty : SongProperty.values()) {
                // Skip properties not relevant for this plot based on predefined list
                if (List.of(SongProperty.POPULARITY, SongProperty.DURATION, SongProperty.TEMPO,
                        SongProperty.LOUDNESS).contains(songProperty)) continue;

                // Draw filled circles at the calculated maximum points for each property
                g2d.fill(new Ellipse2D.Double(
                        maxPointByPropertyMap.get(songProperty).getX() - 5,
                        maxPointByPropertyMap.get(songProperty).getY() - 5, 10, 10));
            }

            // Connect the plotted points with lines to show relationships between properties
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.DANCEABILITY),
                    maxPointByPropertyMap.get(SongProperty.ENERGY)));
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.ENERGY),
                    maxPointByPropertyMap.get(SongProperty.SPEECHINESS)));
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.SPEECHINESS),
                    maxPointByPropertyMap.get(SongProperty.ACOUSTICNESS)));
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.ACOUSTICNESS),
                    maxPointByPropertyMap.get(SongProperty.INSTRUMENTALNESS)));
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.INSTRUMENTALNESS),
                    maxPointByPropertyMap.get(SongProperty.LIVENESS)));
            g2d.draw(new Line2D.Double(maxPointByPropertyMap.get(SongProperty.LIVENESS),
                    maxPointByPropertyMap.get(SongProperty.VALENCE)));
        }
    }

    /**
     * Configures and draws the geometric constraints for the radar chart based on song properties.
     * This method sets up the axes for each song property represented on the radar chart,
     * calculating their endpoint positions based on the specified radius and offset from the center.
     * Each axis is drawn on the chart, and its endpoint is stored for later use in plotting data points.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param offset The initial offset from the center to the start of each axis.
     * @param radius The radius of the axes, determining how far out from the center they extend.
     * @param dimension The dimensions of the component on which the radar chart is being drawn.
     * @param endPointAxisValuesForProperties A map to store the endpoint of each axis for each song property.
     */
    private void creationOfRadarChartConstraints(Graphics2D g2d, double offset, double radius, Dimension dimension,
                                                 HashMap<SongProperty, Point2D> endPointAxisValuesForProperties) {
        // Initial setup for the central axis line from center to the offset point
        Line2D longLine = new Line2D.Double();
        longLine.setLine(dimension.width / 2d, dimension.height / 2d, dimension.width / 2d, offset);
        endPointAxisValuesForProperties.put(SongProperty.DANCEABILITY, longLine.getP2());
        g2d.draw(longLine);

        // Loop to create axes at different angles around the circle
        for (int j = 0; j < 2; j++) {  // Loop twice to ensure all lines are drawn in case of overlap
            longLine.setLine(dimension.width / 2d, dimension.height / 2d, dimension.width / 2d, offset);
            Line2D shortLine = new Line2D.Double(new Point2D.Double(), longLine.getP2());

            for (int i = -2; i <= 3; i++) {  // Adjust angle for each property
                double angle = i * Math.PI / 6;  // Calculate angle for each axis based on index
                double newX2 = longLine.getX1() + radius * Math.cos(angle);
                double newY2 = longLine.getY1() + radius * Math.sin(angle);
                shortLine.setLine(shortLine.getX2(), shortLine.getY2(), newX2, newY2);
                longLine.setLine(longLine.getX1(), longLine.getY1(), newX2, newY2);

                // Map each property to its corresponding axis using a switch statement
                SongProperty songProperty = switch (i) {
                    case -2 -> SongProperty.ENERGY;
                    case -1 -> SongProperty.SPEECHINESS;
                    case 0 -> SongProperty.ACOUSTICNESS;
                    case 1 -> SongProperty.INSTRUMENTALNESS;
                    case 2 -> SongProperty.LIVENESS;
                    default -> SongProperty.VALENCE;
                };
                endPointAxisValuesForProperties.put(songProperty, longLine.getP2());
                g2d.draw(shortLine);  // Draw each property line
                if ((j == 1 && i == -2) || (j == 1 && i == 3)) continue;  // Skip unnecessary overdraw
                g2d.draw(longLine);  // Draw or redraw the line for clarity
            }
        }
    }

    /**
     * Labels the axes of the radar chart with the names of the song properties.
     * This method places labels near the endpoint of each axis. Labels for certain properties
     * are adjusted to avoid overlap and enhance readability. Additionally, this method includes
     * explanatory text for the normalization used on the chart's data plotting.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param endPointAxisValuesForProperties A map storing the endpoints of each radar chart axis.
     * @param offset An offset value used to fine-tune label placement.
     * @param dimension The dimensions of the component where the radar chart is drawn.
     */
    private void labelRadarChartAxis(Graphics2D g2d, HashMap<SongProperty, Point2D> endPointAxisValuesForProperties,
                                     int offset, Dimension dimension) {
        // Iterate through each song property to place its label
        for (SongProperty songProperty : endPointAxisValuesForProperties.keySet()) {
            // Adjust the label placement for properties where overlap may occur
            int adjuster = (songProperty == SongProperty.LIVENESS ||
                    songProperty == SongProperty.VALENCE) ? offset / 2 : 0;

            // Draw the property name at the calculated position
            g2d.drawString("  " + songProperty.toString(), // Include padding for aesthetic spacing
                    ((int) endPointAxisValuesForProperties.get(songProperty).getX()),
                    ((int) endPointAxisValuesForProperties.get(songProperty).getY()) + adjuster);

            // Set color for additional explanatory text boxes
            g2d.setColor(Color.black);

            // Explain the data normalization context for the radar chart on the right side
            g2d.fillRect(((int) (dimension.width - 7.5d * offset)), 0, ((int) (7.5d * offset)), 4 * offset);
            g2d.setColor(Color.white);
            g2d.drawString("All plots on the right side ", dimension.width - 7 * offset, offset);
            g2d.drawString("of the chart are from ", dimension.width - 7 * offset, 2 * offset);
            g2d.drawString("0 (min) to 1 (max)", dimension.width - 7 * offset, 3 * offset);

            // Reset color to draw a similar explanatory box on the left side
            g2d.setColor(Color.black);
            g2d.fillRect(offset / 2, 0, ((int) (7.5d * offset)), 4 * offset);
            g2d.setColor(Color.white);
            g2d.drawString("All plots on the left side of ", offset, offset);
            g2d.drawString("the chart are normalized ", offset, 2 * offset);
            g2d.drawString("from 0 (min) to 100 (max).", offset, 3 * offset);

            // Restore default drawing color
            g2d.setColor(Color.black);
        }
    }

    /**
     * Draws a circular bar chart on the provided graphics context.
     * This method sets up the circular chart area and plots segments for specified song properties
     * such as popularity, tempo, and loudness. It adjusts for component size and applies transformations
     * to fit the chart within the available area. The method handles different data visualizations based on
     * selections like maximum or average values through interaction with the GUI controls.
     *
     * @param g2d The Graphics2D object used for rendering the chart.
     * @param dimension The size of the component on which the chart is drawn.
     * @param customChartAxisValues An array of CustomChartAxisValues which includes range and value data
     *                              for each song property to be visualized.
     */
    private void drawCircularBarChart(Graphics2D g2d, Dimension dimension,
                                      CustomChartAxisValues[] customChartAxisValues) {
        Color tempColor = g2d.getColor();  // Store the original color to restore later
        // Center the chart in the available space
        g2d.translate((dimension.width - dimension.height) / 2, 0);
        int offset = 20;  // Margin from the edges of the component
        int diameter = Math.min(dimension.width, dimension.height);  // Ensure the chart is circular

        g2d.setColor(new Color(34, 31, 31));  // Set the color for the chart

        arc = new Arc2D.Double();
        // Loop twice to ensure lines are sufficiently thick for visibility
        for (int i = 0; i < 2; i++) {
            // Set the arc dimensions and draw it to create the base of the circular bar chart
            arc.setArc(offset, offset, diameter - (2 * offset), diameter - (2 * offset), 90,
                    180, Arc2D.PIE);
            g2d.draw(arc);

            // Initialize sectors for different song properties
            initialisesSectorForPopularity(g2d);
            initialisesSectorForTempo(g2d);
            initialisesSectorForLoudness(g2d);
        }

        // Plot data on the chart based on selection in the GUI
        if (getParentPanel().isMaxCheckBoxSelected()) {
            showMaxDataPlotInCircularBarChart(g2d, diameter);  // Show maximum values if selected
        }
        if (getParentPanel().isAverageCheckBoxSelected()) {
            showAvgDataPlotInCircularBarChart(g2d, customChartAxisValues, diameter);  // Show average values if selected
        }

        g2d.setColor(tempColor);  // Restore the original color
    }

    /**
     * Renders maximum data points on a circular bar chart.
     * This method is specifically used to display the maximum values for selected song properties
     * such as popularity, tempo, and loudness, representing them as full segments (100%) on the chart.
     * The method sets the color and delegates to a utility method for the actual drawing of each segment.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param diameter The diameter of the circular chart, which affects the size of the segments.
     */
    private void showMaxDataPlotInCircularBarChart(Graphics2D g2d, int diameter) {
        g2d.setColor(Color.GREEN);  // Set the color for maximum value segments

        // Display the maximum data plot for popularity at full scale (100%)
        showDataPlot(g2d, diameter, 100d, SongProperty.POPULARITY);
        // Display the maximum data plot for tempo at full scale (100%)
        showDataPlot(g2d, diameter, 100d, SongProperty.TEMPO);
        // Display the maximum data plot for loudness at full scale (100%)
        showDataPlot(g2d, diameter, 100d, SongProperty.LOUDNESS);
    }

    /**
     * Renders average data points on a circular bar chart.
     * This method displays the average values for specific song properties such as popularity, tempo,
     * and loudness, scaled relative to their respective ranges. The visual representation uses segments
     * of a circle, with each segment's length proportional to how the average value compares to the
     * range (minimum to maximum) of that property.
     *
     * @param g2d The Graphics2D object used for drawing.
     * @param customChartAxisValues An array of CustomChartAxisValues containing the min, max, and average
     *                              values for each song property to be visualized.
     * @param diameter The diameter of the circular chart, affecting the scale of the segments.
     */
    private void showAvgDataPlotInCircularBarChart(Graphics2D g2d, CustomChartAxisValues[] customChartAxisValues,
                                                   double diameter) {
        g2d.setColor(Color.BLUE);  // Set the color for average value segments

        // Calculate the range and scale for the popularity property
        double rangeForPopularity = customChartAxisValues[0].getMax() - customChartAxisValues[0].getMin();
        double scaledValueForPopularity = (customChartAxisValues[0].getAverage() - customChartAxisValues[0].getMin())
                / rangeForPopularity * 100d;
        // Display the scaled average data plot for popularity
        showDataPlot(g2d, diameter, scaledValueForPopularity, SongProperty.POPULARITY);

        // Calculate the range and scale for the tempo property
        double rangeForTempo = customChartAxisValues[1].getMax() - customChartAxisValues[1].getMin();
        double scaledValueForTempo = (customChartAxisValues[1].getAverage() - customChartAxisValues[1].getMin())
                / rangeForTempo * 100d;
        // Display the scaled average data plot for tempo
        showDataPlot(g2d, diameter, scaledValueForTempo, SongProperty.TEMPO);

        // Calculate the range and scale for the loudness property
        double rangeForLoudness = customChartAxisValues[2].getMax() - customChartAxisValues[2].getMin();
        double scaledValueForLoudness = (customChartAxisValues[2].getAverage() - customChartAxisValues[2].getMin())
                / rangeForLoudness * 100d;
        // Display the scaled average data plot for loudness
        showDataPlot(g2d, diameter, scaledValueForLoudness, SongProperty.LOUDNESS);
    }

    /**
     * Renders data plots on the circular chart for specific song properties.
     * This method calculates the radius for each plot based on the normalized data provided and
     * draws sectors on the circular chart corresponding to the song properties like popularity,
     * tempo, and loudness. Each property is visualized as a pie segment with a radius proportional
     * to the normalized data value, effectively visualizing the data magnitude on the chart.
     *
     * @param g2d The Graphics2D object used for drawing on the graphical interface.
     * @param diameter The diameter of the circular chart which defines the scale of the plot.
     * @param normalizedDataForPopularity The normalized data value (0-100) representing the magnitude
     *                                    to be plotted for the given song property.
     * @param songProperty The specific song property (e.g., Popularity, Tempo, Loudness) to be plotted.
     */
    private void showDataPlot(Graphics2D g2d, double diameter, double normalizedDataForPopularity,
                              SongProperty songProperty) {
        // Calculate the effective radius as a fraction of the diameter
        double percentageOfReach = normalizedDataForPopularity / 100d;

        // Handle plotting based on the song property
        if (songProperty == SongProperty.POPULARITY) {
            // Calculate radius for the arc based on the normalized popularity data
            double radiusForArcDataPlot = sectorForPopularity.getWidth() / 2d * percentageOfReach;
            for (int i = 0; i < 2; i++) {  // Draw twice to enhance visibility
                sectorForPopularity.setArcByCenter(
                        diameter / 2d,
                        diameter / 2d,
                        radiusForArcDataPlot,
                        sectorForPopularity.getAngleStart(),
                        sectorForPopularity.getAngleExtent(),
                        Arc2D.PIE
                );
                g2d.draw(sectorForPopularity);
            }
        } else if (songProperty == SongProperty.TEMPO) {
            // Similar calculations and drawing logic for tempo
            double radiusForArcDataPlot = sectorForTempo.getWidth() / 2d * percentageOfReach;
            for (int i = 0; i < 2; i++) {
                sectorForTempo.setArcByCenter(
                        diameter / 2d,
                        diameter / 2d,
                        radiusForArcDataPlot,
                        sectorForTempo.getAngleStart(),
                        sectorForTempo.getAngleExtent(),
                        Arc2D.PIE
                );
                g2d.draw(sectorForTempo);
            }
        } else if (songProperty == SongProperty.LOUDNESS) {
            // Similar calculations and drawing logic for loudness
            double radiusForArcDataPlot = sectorForLoudness.getWidth() / 2d * percentageOfReach;
            for (int i = 0; i < 2; i++) {
                sectorForLoudness.setArcByCenter(
                        diameter / 2d,
                        diameter / 2d,
                        radiusForArcDataPlot,
                        sectorForLoudness.getAngleStart(),
                        sectorForLoudness.getAngleExtent(),
                        Arc2D.PIE
                );
                g2d.draw(sectorForLoudness);
            }
        }
    }

    /**
     * Initializes and draws the sector for the "Loudness" song property on the circular chart.
     * This method sets the parameters for an Arc2D object to represent the loudness sector in a pie chart.
     * It configures the sector's position and size based on the predefined arc dimensions and
     * calculates the label's placement to annotate the sector appropriately.
     *
     * @param g2d The Graphics2D object used for rendering shapes and text on the GUI.
     */
    private void initialisesSectorForLoudness(Graphics2D g2d) {
        // Create a new Arc2D.Double object to represent the loudness sector
        sectorForLoudness = new Arc2D.Double();
        // Set the arc's parameters to carve out the loudness sector from the main arc
        sectorForLoudness.setArc(
                arc.getX(),  // Use the x-coordinate of the main arc
                arc.getY(),  // Use the y-coordinate of the main arc
                arc.getWidth(),  // The width of the main arc
                arc.getHeight(),  // The height of the main arc
                arc.getAngleStart() + 2 * arc.getAngleExtent() / 3,  // Start at two-thirds along the main arc
                arc.getAngleExtent() / 3,  // Span one-third of the main arc's extent
                Arc2D.PIE  // Use the PIE closure type to create a filled sector
        );
        g2d.draw(sectorForLoudness);  // Draw the defined sector on the graphics context

        // Calculate the coordinates for placing the label
        Point2D stringCoordinate = new Point2D.Double(
                // Position the label horizontally centered within the sector
                (sectorForLoudness.getCenterX() - sectorForLoudness.getMinX()) / 2 + sectorForLoudness.getMinX(),
                // Position the label vertically centered within the sector
                (sectorForLoudness.getCenterY() - sectorForLoudness.getMinY()) / 2 + sectorForLoudness.getMinY() +
                        sectorForLoudness.getCenterY()
        );
        // Draw the "Loudness" label at the calculated position
        g2d.drawString(SongProperty.LOUDNESS.toString(), ((int) stringCoordinate.getX()),
                ((int) stringCoordinate.getY()));
    }

    /**
     * Initializes and draws the sector for the "Tempo" song property on the circular chart.
     * This method configures the arc for the tempo sector based on the main arc's dimensions and angles.
     * It positions this sector to occupy one-third of the main arc, starting from one-third of the way along the arc.
     * Additionally, it calculates and places a label appropriately within this sector to identify it visually.
     *
     * @param g2d The Graphics2D object used for rendering the sector and text on the graphical interface.
     */
    private void initialisesSectorForTempo(Graphics2D g2d) {
        // Create a new Arc2D.Double object for the tempo sector
        sectorForTempo = new Arc2D.Double();
        // Configure the arc for the tempo sector within the circular chart
        sectorForTempo.setArc(
                arc.getX(),  // Start at the main arc's x-coordinate
                arc.getY(),  // Start at the main arc's y-coordinate
                arc.getWidth(),  // Match the width of the main arc
                arc.getHeight(),  // Match the height of the main arc
                arc.getAngleStart() + arc.getAngleExtent() / 3, // Position starts after the first third of the main arc
                arc.getAngleExtent() / 3,  // This sector spans one-third of the main arc's total angle
                Arc2D.PIE  // Use PIE to create a filled sector
        );
        g2d.draw(sectorForTempo);  // Draw the tempo sector on the graphics context

        // Calculate the coordinates for the tempo label inside the sector
        Point2D stringCoordinate = new Point2D.Double(
                // Calculate x by finding a quarter point within the sector for better visibility
                (sectorForTempo.getCenterX() - sectorForTempo.getMinX()) / 4 + sectorForTempo.getMinX(),
                // Use the center y-coordinate of the sector for vertical alignment
                sectorForTempo.getCenterY()
        );
        // Draw the label "Tempo" at the calculated position
        g2d.drawString(SongProperty.TEMPO.toString(), ((int) stringCoordinate.getX()), ((int) stringCoordinate.getY()));
    }

    /**
     * Initializes and draws the sector for the "Popularity" song property on the circular chart.
     * This method sets the parameters for an Arc2D object to represent the popularity sector within a pie chart.
     * It determines the arc's position and size based on the parent arc's dimensions, starting at the beginning
     * and covering one-third of the arc's total angle.
     * <p>
     * Additionally, it computes the position and draws a label within this sector to visually identify it.
     *
     * @param g2d The Graphics2D object used for rendering shapes and text on the graphical interface.
     */
    private void initialisesSectorForPopularity(Graphics2D g2d) {
        // Initialize the Arc2D.Double object for the popularity sector
        sectorForPopularity = new Arc2D.Double();
        // Set the arc parameters to define the popularity sector
        sectorForPopularity.setArc(
                arc.getX(),  // Use the x-coordinate of the main arc
                arc.getY(),  // Use the y-coordinate of the main arc
                arc.getWidth(),  // Use the width of the main arc
                arc.getHeight(),  // Use the height of the main arc
                arc.getAngleStart(),  // Start angle aligned with the beginning of the main arc
                arc.getAngleExtent() / 3,  // Span one-third of the main arc's extent
                Arc2D.PIE  // Closure type PIE to create a filled sector
        );
        g2d.draw(sectorForPopularity);  // Draw the defined sector on the graphics context

        // Calculate the position for placing the "Popularity" label inside the sector
        Point2D stringCoordinate = new Point2D.Double(
                // Center the label horizontally within the sector
                (sectorForPopularity.getCenterX() - sectorForPopularity.getMinX()) / 2 + sectorForPopularity.getMinX(),
                // Center the label vertically within the sector
                (sectorForPopularity.getCenterY() - sectorForPopularity.getMinY()) / 2 + sectorForPopularity.getMinY()
        );
        // Draw the label at the calculated position
        g2d.drawString(SongProperty.POPULARITY.toString(), ((int) stringCoordinate.getX()),
                ((int) stringCoordinate.getY()));
    }
}
