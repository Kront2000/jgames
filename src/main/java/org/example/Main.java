package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import games.breakout.BreakoutLauncher;
import games.flappybird.FlappyBirdLauncher;
import games.minesweeper.MinesweeperLauncher;
import games.pacman.PacmanLauncher;
import games.snake.SnakeLauncher;

public class Main {
    public static void main(String[] args) {
        // Запуск интерфейса в потоке обработки событий Swing
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Создание основного окна
        JFrame frame = new JFrame("Game Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new GridLayout(6, 1, 10, 10)); // Сетка для кнопок
        frame.setLocationRelativeTo(null); // Центрирование на экране

        // Заголовок
        JLabel label = new JLabel("Выберите игру", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(label);

        // Создание кнопок для каждой игры
        frame.add(createGameButton("Breakout", e -> launch(() -> BreakoutLauncher.start())));
        frame.add(createGameButton("Flappy Bird", e -> launch(() -> FlappyBirdLauncher.start())));
        frame.add(createGameButton("Snake", e -> launch(() -> SnakeLauncher.start())));
        frame.add(createGameButton("Pacman", e -> launch(() -> PacmanLauncher.start())));
        frame.add(createGameButton("Minesweeper", e -> launch(() -> MinesweeperLauncher.start())));

        frame.setVisible(true);
    }

    // Утилитный метод для создания стилизованных кнопок
    private static JButton createGameButton(String name, ActionListener action) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    // Метод-обертка для обработки исключений при запуске
    private static void launch(GameStarter starter) {
        try {
            starter.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка запуска игры: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Функциональный интерфейс для удобного запуска
    @FunctionalInterface
    interface GameStarter {
        void start() throws Exception;
    }
}