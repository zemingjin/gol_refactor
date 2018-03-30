import gameoflife.Cell;
import gameoflife.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GameOfLifeApp extends JComponent {
    private static final int EDGE_LEN = 50;
    private static final long WAIT_TIME = 400;

    private GameOfLife gameOfLife = new GameOfLife();
    private JFrame window = new JFrame();
    private int maxIndex;

    private GameOfLifeApp(String[] params) {
        if (params.length > 0) {
            setup(params[0]);
        } else {
            throw new RuntimeException("Missing seeds");
        }
    }

    private void setup(String seeds) {
        gameOfLife.seed(seeds);
        setPanelSize();
        maxIndex = gameOfLife.getMaxIndex();
    }

    private void setPanelSize() {
        int frameLength = gameOfLife.getMaxIndex() * EDGE_LEN;
        setSize(frameLength, frameLength);
        setupFrame(frameLength);
    }

    private void setupFrame(int panelLength) {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(panelLength, panelLength);
        window.setBounds(100, 100, panelLength + 20, panelLength * 4 / 3);
        window.getContentPane().add(this);
        window.setVisible(true);
    }

    private void run() {
        while (true) {
            paint((a, b) -> paintGrid(getGraphics(), a, b));
            paint((a, b) -> paintCell(getGraphics(), a, b));
            waitAWhile();
            gameOfLife.setLiveCells(gameOfLife.tick());
            maxIndex = Math.max(gameOfLife.getMaxIndex(), maxIndex);
        }
    }

    private void paint(BiConsumer<Integer, Integer> consumer) {
        IntStream.range(0, maxIndex)
                .forEach(x -> paint(consumer, x));
    }

    private void paint(BiConsumer<Integer, Integer> consumer, int x) {
        IntStream.range(0, maxIndex)
                .forEach(y -> paint(consumer, x, y));
    }

    private void paint(BiConsumer<Integer, Integer> consumer, int x, int y) {
        consumer.accept(x, y);
    }

    private void paintCell(Graphics graphics, int x, int y) {
        graphics.setColor(getColor(new Cell(x, y)));
        graphics.fillRect(x * EDGE_LEN + 1, y * EDGE_LEN + 1, EDGE_LEN - 2, EDGE_LEN - 2);
    }

    private void paintGrid(Graphics  graphics, int x, int y) {
        graphics.setColor(getForeground());
        graphics.drawRect(x * EDGE_LEN, y * EDGE_LEN, EDGE_LEN, EDGE_LEN);
    }

    private Color getColor(Cell cell) {
        return gameOfLife.isLiveCell(cell) ? getForeground() : getBackground();
    }

    private void waitAWhile() {
        try {
            synchronized (this) {
                wait(WAIT_TIME);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] params) {
        new GameOfLifeApp(params).run();
    }
}
