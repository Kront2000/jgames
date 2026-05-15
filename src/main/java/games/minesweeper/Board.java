package games.minesweeper;

import games.flappybird.FlappyBirdLauncher;
import hub.ui.screen.DetailsScreen;
import hub.utils.Server;
import org.example.Navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Board extends JPanel {

    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private final int N_MINES = 40;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;

    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;

    private int allCells;
    private final JLabel status;

    private int score;
    private boolean resultSent;

    public Board(JLabel status) {
        this.status = status;
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "/minesweeper/" + i + ".png";
            var resource = getClass().getResource(path);
            if (resource != null) {
                img[i] = (new ImageIcon(resource).getImage());
            }
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame() {
        int cell;
        var random = new Random();

        inGame = true;
        resultSent = false;
        score = 0;
        minesLeft = N_MINES;
        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        status.setText("Mines: " + Integer.toString(minesLeft) + " | Score: 0");

        int i = 0;
        while (i < N_MINES) {
            int position = (int) (allCells * random.nextDouble());
            if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) {
                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                // Вычисление чисел вокруг мин
                updateNeighbors(position, current_col);
            }
        }
    }

    // Вспомогательный метод для инкрементации соседей
    private void updateNeighbors(int position, int current_col) {
        int cell;
        if (current_col > 0) {
            checkAndIncrement(position - 1 - N_COLS);
            checkAndIncrement(position - 1);
            checkAndIncrement(position + N_COLS - 1);
        }
        checkAndIncrement(position - N_COLS);
        checkAndIncrement(position + N_COLS);
        if (current_col < (N_COLS - 1)) {
            checkAndIncrement(position - N_COLS + 1);
            checkAndIncrement(position + N_COLS + 1);
            checkAndIncrement(position + 1);
        }
    }

    private void checkAndIncrement(int cellIndex) {
        if (cellIndex >= 0 && cellIndex < allCells) {
            if (field[cellIndex] != COVERED_MINE_CELL) {
                field[cellIndex] += 1;
            }
        }
    }

    private void find_empty_cells(int j) {
        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            processCell(j - N_COLS - 1);
            processCell(j - 1);
            processCell(j + N_COLS - 1);
        }

        processCell(j - N_COLS);
        processCell(j + N_COLS);

        if (current_col < (N_COLS - 1)) {
            processCell(j - N_COLS + 1);
            processCell(j + N_COLS + 1);
            processCell(j + 1);
        }
    }

    private void processCell(int cell) {
        if (cell >= 0 && cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                score += 10; // Начисляем очки за каждую открытую ячейку
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) cell = DRAW_MINE;
                    else if (cell == MARKED_MINE_CELL) cell = DRAW_MARK;
                    else if (cell > COVERED_MINE_CELL) cell = DRAW_WRONG_MARK;
                    else if (cell > MINE_CELL) cell = DRAW_COVER;
                } else {
                    if (cell > COVERED_MINE_CELL) cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);
            }
        }

        // Проверка состояния игры
        if (uncover == 0 && inGame) {
            inGame = false;
            status.setText("You won! Score: " + score);
            if (!resultSent) {
                Server.SaveStat("Minesweeper", (long) score);
                Navigation.show(DetailsScreen.showGameDetails("MineSweeper", "Логика", () -> MinesweeperLauncher.start()));
                resultSent = true;
            }
        } else if (!inGame) {
            status.setText("Game Over! Score: " + score);
            if (!resultSent) {
                Server.SaveStat("Minesweeper", (long) score);
                Navigation.show(DetailsScreen.showGameDetails("MineSweeper", "Логика", () -> MinesweeperLauncher.start()));
                resultSent = true;
            }
        }
    }

    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            int cellIdx = (cRow * N_COLS) + cCol;

            boolean doRepaint = false;

            if (!inGame) {
                newGame();
                repaint();
                return;
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (field[cellIdx] > MINE_CELL) {
                        doRepaint = true;
                        if (field[cellIdx] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[cellIdx] += MARK_FOR_CELL;
                                minesLeft--;
                            } else {
                                status.setText("No marks left");
                            }
                        } else {
                            field[cellIdx] -= MARK_FOR_CELL;
                            minesLeft++;
                        }
                        status.setText("Mines: " + minesLeft + " | Score: " + score);
                    }
                }

                else {
                    if (field[cellIdx] > COVERED_MINE_CELL) return;

                    if (field[cellIdx] > MINE_CELL) {
                        field[cellIdx] -= COVER_FOR_CELL;
                        score += 10; // Очки за клик
                        doRepaint = true;

                        if (field[cellIdx] == MINE_CELL) {
                            inGame = false;
                        }
                        if (field[cellIdx] == EMPTY_CELL) {
                            find_empty_cells(cellIdx);
                        }
                        status.setText("Mines: " + minesLeft + " | Score: " + score);
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}