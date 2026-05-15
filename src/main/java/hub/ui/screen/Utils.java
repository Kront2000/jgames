package hub.ui.screen;

import hub.ui.Theme;
import org.example.Main;

import javax.swing.*;
import java.awt.*;

public class Utils {

    // ХЕЛПЕРЫ ДЛЯ UI
    public static JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.UI_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 45));

        // Магия FlatLaf для скругления (даже если мы меняем фон)
        btn.putClientProperty("JButton.buttonType", "roundRect");

        return btn;
    }



    @FunctionalInterface
    interface GameStarter { void start() throws Exception; }
}
