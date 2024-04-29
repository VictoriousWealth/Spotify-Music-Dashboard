package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import javax.swing.*;

/**
 * This is an Abstract class used for displaying a Custom Chart Panel.
 *
 * Should be implemented as uk.ac.sheffield.assignment2023.gui.CustomChartPanel.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */
public class AbstractCustomChartPanel extends JPanel {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AbstractSpotifyDashboardPanel parentPanel;

    private final AbstractCustomChart customChart;

    public AbstractCustomChartPanel(AbstractSpotifyDashboardPanel parentPanel, AbstractCustomChart customChart) {
        super();
        this.parentPanel = parentPanel;
        this.customChart = customChart;
    }

    public AbstractSpotifyDashboardPanel getParentPanel() {
        return parentPanel;
    }

    public AbstractCustomChart getCustomChart() {
        return customChart;
    }
}
