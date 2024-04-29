package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import javax.swing.*;

/**
 * This class instantiates the GUI.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2023
 */

public class SpotifyDashboard extends JFrame {
    public SpotifyDashboard(AbstractSpotifyDashboardPanel panel){
        setTitle("Spotify Dashboard");
        add(panel);
        //maximize the JFrame to fit the entire screen.
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
