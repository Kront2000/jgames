package org.example;

import javax.swing.*;

public class Navigation {
    private static JFrame mainFrame;

    public static void setMainFrame(JFrame frame) {
        mainFrame = frame;
    }

    public Navigation(JPanel mainFrame){
    }

    public static void show(JPanel panel){
        switchContent(panel);
    }

    private static void switchContent(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    //Функция для запуска игр
    public static void launch(GameStarter starter) {
        try {
            starter.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Ошибка: " + ex.getMessage());
        }
    }

    @FunctionalInterface
    public interface GameStarter { void start() throws Exception; }
}
