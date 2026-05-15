package games.breakout;

import javax.swing.*;
import java.awt.*;

public class BreakoutLauncher extends JFrame {

	public BreakoutLauncher() {
		initUI();
	}

	public void initUI() {
		add(new Board());
		setTitle("Breakout");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
	}

	public static void start() {
		EventQueue.invokeLater(() -> {
			var game = new BreakoutLauncher();
			game.setVisible(true);
		});
	}

}
