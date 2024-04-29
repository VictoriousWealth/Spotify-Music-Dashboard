package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChart;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChartPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * SKELETON IMPLEMENTATION
 */

public class CustomChartPanel extends AbstractCustomChartPanel {

    public CustomChartPanel(AbstractSpotifyDashboardPanel parentPanel, AbstractCustomChart customChart) {
        super(parentPanel, customChart);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO implement - the following code is just for illustrative purposes
        super.paintComponent(g);

        Dimension d = getSize();
        Graphics2D g2d = (Graphics2D) g;

        // drawing background
        g2d.setColor(new Color(180, 182, 204));
        Rectangle2D rectangle = new Rectangle2D.Double(0, 0, d.width, d.height);
        g2d.draw(rectangle);

    }

}
