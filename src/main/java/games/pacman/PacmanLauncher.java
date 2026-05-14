package games.pacman;

import javax.swing.*;

public class PacmanLauncher extends JFrame{

    public PacmanLauncher() {
        add(new model());
    }


    public static void start() {
        PacmanLauncher pac = new PacmanLauncher();
        pac.setVisible(true);
        pac.setTitle("Pacman");
        pac.setSize(380,420);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);

    }

}