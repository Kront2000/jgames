package hub.ui.screen;

import games.breakout.BreakoutLauncher;
import games.flappybird.FlappyBirdLauncher;
import games.minesweeper.MinesweeperLauncher;
import games.pacman.PacmanLauncher;
import games.snake.SnakeLauncher;
import hub.ui.Theme;
import hub.utils.TokenManager;
import org.example.Main;
import org.example.Navigation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuScreen {
    public static JPanel menuScreen() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("Мои игры", SwingConstants.LEFT);
        title.setFont(Theme.TITLE_FONT);
        mainPanel.add(title, BorderLayout.NORTH);

        // Список игр
        JPanel list = new JPanel(new GridLayout(0, 1, 0, 12));
        list.setBorder(new EmptyBorder(20, 0, 20, 0));

        list.add(createGameCard("Breakout", "Аркада", () -> BreakoutLauncher.start()));
        list.add(createGameCard("FlappyBird", "Хардкор", () -> FlappyBirdLauncher.start()));
        list.add(createGameCard("Snake", "Классика", () -> SnakeLauncher.start()));
        list.add(createGameCard("Pacman", "Лабиринт", () -> PacmanLauncher.start()));
        list.add(createGameCard("Minesweeper", "Логика", () -> MinesweeperLauncher.start()));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Кнопка выхода
        JButton logoutBtn = Utils.createBtn("Выйти из системы", new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> {
            TokenManager.deleteToken();
            Navigation.show(RegistrationScreen.registrationScreen());
        });
        mainPanel.add(logoutBtn, BorderLayout.SOUTH);

        return mainPanel;
    }

    private static JButton createGameCard(String name, String category, Navigation.GameStarter starter) {
        JButton btn = new JButton("<html><div style='text-align: left;'><b>" + name + "</b><br><font color='#888888'>" + category + "</font></div></html>");
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(10, 20, 10, 20));
        btn.setBackground(Theme.SECONDARY_COLOR);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> Navigation.show(DetailsScreen.showGameDetails(name, category, starter)));
        return btn;
    }

}

