package hub.ui.screen;

import hub.entities.Stat;
import hub.ui.Theme;
import hub.utils.Server;
import hub.utils.TokenManager;
import org.example.Navigation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DetailsScreen {
    public static JPanel showGameDetails(String name, String genre, Navigation.GameStarter starter) {
        Stat[] dataFromServer = Server.getTop10(name);
        int rowCount = (dataFromServer != null) ? Math.min(dataFromServer.length, 10) : 0;
        Object[][] data = new Object[rowCount][3];
        for (int i = 0; i < rowCount; i++) {
            Stat s = dataFromServer[i];
            data[i][0] = i + 1;
            data[i][1] = (s.player != null) ? s.player.name : "Аноним";
            data[i][2] = s.stat;
        }

        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(new EmptyBorder(30, 35, 30, 35));

        // Шапка
        JPanel header = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel(name);
        title.setFont(Theme.TITLE_FONT);
        JLabel sub = new JLabel("Жанр: " + genre);
        sub.setForeground(Color.GRAY);
        header.add(title);
        header.add(sub);
        panel.add(header, BorderLayout.NORTH);

        // Таблица лидеров
        String[] cols = {"Место", "Игрок", "Очки"};
        JTable table = new JTable(data, cols);
        table.setRowHeight(35);
        table.setEnabled(false);
        table.setShowVerticalLines(false);

        // Центрирование данных в таблице
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Топ игроков недели"));
        panel.add(scroll, BorderLayout.CENTER);

        // Кнопки управления
        JPanel footer = new JPanel(new GridLayout(1, 2, 15, 0));
        JButton backBtn = Utils.createBtn("Назад", Theme.SECONDARY_COLOR);
        backBtn.addActionListener(e -> Navigation.show(MenuScreen.menuScreen()));

        JButton playBtn = Utils.createBtn("Играть", Theme.SUCCESS_COLOR);
        playBtn.addActionListener(e -> Navigation.launch((Navigation.GameStarter) starter));

        JButton refreshBtn = Utils.createBtn("Обновить", Theme.SECONDARY_COLOR);
        refreshBtn.addActionListener(e -> {
            Navigation.show(DetailsScreen.showGameDetails(name, genre, starter));
        });
        footer.add(backBtn);
        footer.add(playBtn);
        footer.add(refreshBtn);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

}
