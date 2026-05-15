package games.snake;

import games.flappybird.FlappyBirdLauncher;
import hub.ui.screen.DetailsScreen;
import hub.utils.Server;
import org.example.Navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeLauncher {
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;

    static final int TOP_UI = 50;
    static final int PAD = 10;

    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static Dir currentDirection = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    static Timer timer;
    static GamePanel gamePanel;

    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Внутренний класс для отрисовки графики (аналог Canvas)
    static class GamePanel extends JPanel {
        public GamePanel() {
            int windowWidth = width * cornersize + PAD * 2;
            int windowHeight = height * cornersize + TOP_UI + PAD;
            setPreferredSize(new Dimension(windowWidth, windowHeight));
            setFocusable(true);

            // Обработка ввода
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (gameOver) {
                        if (e.getKeyCode() == KeyEvent.VK_R) {
                            resetGame();
                            repaint();
                        }
                        return;
                    }

                    if (e.getKeyCode() == KeyEvent.VK_W && currentDirection != Dir.down) {
                        direction = Dir.up;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A && currentDirection != Dir.right) {
                        direction = Dir.left;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S && currentDirection != Dir.up) {
                        direction = Dir.down;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D && currentDirection != Dir.left) {
                        direction = Dir.right;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Чтобы графика была более сглаженной (по желанию)
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            draw(g2d);
        }
    }

    public static void start() {
        JFrame frame = new JFrame("SNAKE");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // или EXIT_ON_CLOSE
        frame.setResizable(false);

        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Центрируем окно по экрану
        frame.setVisible(true);

        resetGame();

        // Игровой цикл на базе Timer
        timer = new Timer(1000 / speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    tick();
                    gamePanel.repaint();
                }
            }
        });
        timer.start();
    }

    public static void resetGame() {
        snake.clear();
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));

        direction = Dir.left;
        currentDirection = Dir.left;
        speed = 5;
        gameOver = false;

        if (timer != null) {
            timer.setDelay(1000 / speed);
        }

        newFood();
    }


    public static void tick() {
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        currentDirection = direction;

        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) gameOver = true;
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y >= height) gameOver = true;
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) gameOver = true;
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x >= width) gameOver = true;
                break;
        }

        // Поедание еды
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
            timer.setDelay(1000 / speed); // Обновляем скорость в таймере
        }

        // Столкновение с самим собой
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {

                gameOver = true;
            }
        }
    }

    // Метод отрисовки графики
    public static void draw(Graphics2D gc) {
        int windowWidth = width * cornersize + PAD * 2;
        int windowHeight = height * cornersize + TOP_UI + PAD;

        // Фон всего окна
        gc.setColor(Color.GRAY);
        gc.fillRect(0, 0, windowWidth, windowHeight);

        // Игровое поле
        gc.setColor(Color.BLACK);
        gc.fillRect(PAD, TOP_UI, width * cornersize, height * cornersize);

        // Рамка вокруг поля
        gc.setColor(Color.WHITE);
        gc.setStroke(new BasicStroke(2));
        gc.drawRect(PAD - 1, TOP_UI - 1, width * cornersize + 2, height * cornersize + 2);

        if (gameOver) {
            Server.SaveStat("Snake", (long) speed - 6);
            Navigation.show(DetailsScreen.showGameDetails("Snake", "Классика", () -> SnakeLauncher.start()));
            gc.setColor(Color.RED);
            gc.setFont(new Font("Roboto", Font.BOLD, 50));
            // Центрируем надпись
            FontMetrics fm = gc.getFontMetrics();
            String goText = "GAME OVER";
            int goWidth = fm.stringWidth(goText);
            gc.drawString(goText, (windowWidth - goWidth) / 2, windowHeight / 2 - 10);

            gc.setColor(Color.WHITE);
            gc.setFont(new Font("Roboto", Font.BOLD, 20));
            FontMetrics fmSmall = gc.getFontMetrics();
            String restartText = "Press 'R' to Restart";
            int resWidth = fmSmall.stringWidth(restartText);
            gc.drawString(restartText, (windowWidth - resWidth) / 2, windowHeight / 2 + 30);
            String scoreText = "score: " + (speed - 6);
            int scoWidth = fmSmall.stringWidth(scoreText);
            gc.drawString(scoreText, (windowWidth - resWidth) / 2, windowHeight / 2 + 50);
            return;
        }

        // Счет
        gc.setColor(Color.WHITE);
        gc.setFont(new Font("Roboto", Font.BOLD, 26));
        gc.drawString("Score: " + (speed - 6), PAD, 35);

        // Выбираем цвет еды
        Color cc = Color.WHITE;
        switch (foodcolor) {
            case 0: cc = new Color(128, 0, 128); break; // PURPLE
            case 1: cc = new Color(173, 216, 230); break; // LIGHTBLUE
            case 2: cc = Color.YELLOW; break;
            case 3: cc = Color.PINK; break;
            case 4: cc = Color.ORANGE; break;
        }
        gc.setColor(cc);
        gc.fillOval(PAD + foodX * cornersize, TOP_UI + foodY * cornersize, cornersize, cornersize);

        // Отрисовка змейки
        for (Corner c : snake) {
            if (c.x == -1 && c.y == -1) continue; // Пропускаем хвост, если он еще за границами

            gc.setColor(new Color(219, 112, 147)); // PALEVIOLETRED
            gc.fillRect(PAD + c.x * cornersize, TOP_UI + c.y * cornersize, cornersize - 1, cornersize - 1);

            gc.setColor(new Color(138, 43, 226)); // BLUEVIOLET
            gc.fillRect(PAD + c.x * cornersize, TOP_UI + c.y * cornersize, cornersize - 2, cornersize - 2);
        }
    }

    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;
        }
    }

    // Метод main для проверки в изолированном окружении
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
    }
}