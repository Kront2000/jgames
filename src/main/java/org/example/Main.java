package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

import java.awt.*;

import hub.ui.screen.MenuScreen;
import hub.ui.screen.RegistrationScreen;
import hub.utils.TokenManager;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static JFrame mainFrame;


    public static void main(String[] args) {
        setupTheme();

        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Game Hub");

            Navigation.setMainFrame(mainFrame);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(450, 650);
            mainFrame.setLocationRelativeTo(null);

            // Проверка токена и запуск нужного экрана
            if (TokenManager.hasToken()) {
                Navigation.show(MenuScreen.menuScreen());
            } else {
                Navigation.show(RegistrationScreen.registrationScreen());
            }

            mainFrame.setVisible(true);
        });
    }

    private static void setupTheme() {
        FlatDarkLaf.setup();
        UIManager.put("Button.arc", 16);
        UIManager.put("Component.arc", 16);
        UIManager.put("TextComponent.arc", 16);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
        UIManager.put("TableHeader.background", new Color(40, 40, 45));
    }

    public static void message(String text){
        JOptionPane.showMessageDialog(mainFrame, text);
    }

    @FunctionalInterface
    interface GameStarter { void start() throws Exception; }
}