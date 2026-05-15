package hub.ui.screen;

import hub.ui.Theme;
import hub.utils.TokenManager;
import org.example.Navigation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginScreen {
    public static JPanel loginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;

        JLabel title = new JLabel("Логин", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        panel.add(title, gbc);
        panel.add(Box.createVerticalStrut(30), gbc);

        JTextField loginField = new JTextField();
        loginField.putClientProperty("JTextField.placeholderText", "Введите ваш никнейм");
        loginField.setPreferredSize(new Dimension(0, 45));
        loginField.setFont(Theme.UI_FONT);
        panel.add(loginField, gbc);
        panel.add(Box.createVerticalStrut(15), gbc);

        JTextField passwordField = new JTextField();
        passwordField.putClientProperty("JTextField.placeholderText", "Введите ваш пароль");
        passwordField.setPreferredSize(new Dimension(0, 45));
        passwordField.setFont(Theme.UI_FONT);
        panel.add(passwordField, gbc);
        panel.add(Box.createVerticalStrut(15), gbc);

        JButton registrationBtn = Utils.createBtn("Войти", Theme.ACCENT_COLOR);
        registrationBtn.addActionListener(e -> {
            if (!loginField.getText().trim().isEmpty() && !passwordField.getText().trim().isEmpty()) {
                String response = TokenManager.login(loginField.getText(), passwordField.getText());
                if(response == "SUCCESS"){
                    Navigation.show(MenuScreen.menuScreen());
                }
            }
        });
        panel.add(registrationBtn, gbc);
        panel.add(Box.createVerticalStrut(30), gbc);

        JLabel toLogin = new JLabel("Нету аккаунта?", SwingConstants.CENTER);
        toLogin.setFont(Theme.UI_FONT);
        panel.add(toLogin, gbc);

        panel.add(Box.createVerticalStrut(15), gbc);
        JButton loginBtn = Utils.createBtn("Зарегистрироваться", Theme.ACCENT_COLOR);
        loginBtn.addActionListener(e -> Navigation.show(RegistrationScreen.registrationScreen()));
        panel.add(loginBtn, gbc);


        return panel;
    }
}
