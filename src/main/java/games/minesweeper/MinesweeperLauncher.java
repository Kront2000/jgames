package games.minesweeper;

import javax.swing.*;
import java.awt.*;

public class MinesweeperLauncher extends JFrame {
    private JLabel status;

    public MinesweeperLauncher() {
    	status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new Board(status));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void start() {
     	MinesweeperLauncher ms = new MinesweeperLauncher();
    	ms.setVisible(true);
    }
}
