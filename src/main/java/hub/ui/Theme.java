package hub.ui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Theme {
    // Константы дизайна
    public static final Color ACCENT_COLOR = new Color(0, 122, 204);
    public static final Color SUCCESS_COLOR = new Color(46, 160, 67);
    public static final Color SECONDARY_COLOR = new Color(60, 60, 65);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    public static void apply() {
        FlatDarkLaf.setup();
        // Глобальные настройки FlatLaf
        UIManager.put("Button.arc", 16);
        UIManager.put("Component.arc", 16);
        UIManager.put("TextComponent.arc", 16);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
        UIManager.put("TableHeader.background", new Color(40, 40, 45));
    }
}
