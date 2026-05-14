package games.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeLauncher extends Application {
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

    public void start(Stage primaryStage) {
        try {
            VBox root = new VBox();

            int windowWidth = width * cornersize + PAD * 2;
            int windowHeight = height * cornersize + TOP_UI + PAD;

            Canvas c = new Canvas(windowWidth, windowHeight);
            GraphicsContext gc = c.getGraphicsContext2D();
            root.getChildren().add(c);

            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc);
                    }
                }
            }.start();

            Scene scene = new Scene(root, windowWidth, windowHeight);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (gameOver) {
                    if (key.getCode() == KeyCode.R) {
                        resetGame();
                    }
                    return;
                }

                if (key.getCode() == KeyCode.W && currentDirection != Dir.down) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.A && currentDirection != Dir.right) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.S && currentDirection != Dir.up) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.D && currentDirection != Dir.left) {
                    direction = Dir.right;
                }
            });

            resetGame();

            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        newFood();
    }

    public static void tick(GraphicsContext gc) {
        int windowWidth = width * cornersize + PAD * 2;
        int windowHeight = height * cornersize + TOP_UI + PAD;

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Roboto", FontWeight.BOLD, 50));
            // Центрируем надпись относительно нового размера окна
            gc.fillText("GAME OVER", windowWidth / 2 - 145, windowHeight / 2 - 20);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
            gc.fillText("Press 'R' to Restart", windowWidth / 2 - 95, windowHeight / 2 + 20);
            return;
        }

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

        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        gc.setFill(Color.GREY);
        gc.fillRect(0, 0, windowWidth, windowHeight);

        gc.setFill(Color.BLACK);
        gc.fillRect(PAD, TOP_UI, width * cornersize, height * cornersize);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(PAD - 1, TOP_UI - 1, width * cornersize + 2, height * cornersize + 2);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Roboto", FontWeight.BOLD, 26));
        gc.fillText("Score: " + (speed - 6), PAD, 35);

        // 5. Выбираем цвет еды
        Color cc = Color.WHITE;
        switch (foodcolor) {
            case 0: cc = Color.PURPLE; break;
            case 1: cc = Color.LIGHTBLUE; break;
            case 2: cc = Color.YELLOW; break;
            case 3: cc = Color.PINK; break;
            case 4: cc = Color.ORANGE; break;
        }
        gc.setFill(cc);
        gc.fillOval(PAD + foodX * cornersize, TOP_UI + foodY * cornersize, cornersize, cornersize);


        for (Corner c : snake) {
            gc.setFill(Color.PALEVIOLETRED);
            gc.fillRect(PAD + c.x * cornersize, TOP_UI + c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.BLUEVIOLET);
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

    public static void start() {
        launch();
    }
}