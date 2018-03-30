import gameoflife.Cell;
import gameoflife.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class GameOfLifeApp extends JComponent {
    private static final long WAIT_TIME = 400;

    private GameOfLife gameOfLife = new GameOfLife();
    private JFrame window = new JFrame();
    private int maxIndex;
    private int cellSize = 50;
    private boolean continueFlag = true;

    private GameOfLifeApp(String[] params) {
        if (params.length > 0) {
            setup(params[0]);
        } else {
            throw new RuntimeException("Missing seeds");
        }
    }

    private void setup(String seeds) {
        gameOfLife.seed(seeds);
        maxIndex = gameOfLife.getMaxIndex() + 1;
        cellSize = getCellSize();
        setPanelSize();
        setFocusable(true);
    }

    private int getCellSize() {
        return getScreenSize().height / 2 / maxIndex;
    }

    private void setPanelSize() {
        int frameLength = getPanelLength();
        setSize(frameLength, frameLength);
        setupFrame(frameLength);
    }

    private int getPanelLength() {
        return maxIndex * cellSize;
    }

    private void setupFrame(int panelLength) {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().add(this);
        window.setVisible(true);
        adjustFrameSize(panelLength);
    }

    private void adjustFrameSize(int panelLength) {
        setSize(panelLength, panelLength);
        window.setBounds(getFramePositionX(panelLength), getFramePositionY(panelLength),
                         getFrameWidth(panelLength), getFrameHeight(panelLength));
    }

    private int getFramePositionY(int panelLength) {
        return (getScreenSize().height - getFrameHeight(panelLength)) / 2;
    }

    private Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private int getFramePositionX(int panelLength) {
        return (getScreenSize().width - getFrameWidth(panelLength)) / 2;
    }

    private int getFrameWidth(int panelLength) {
        Insets insets = window.getInsets();
        return panelLength + insets.left + insets.right;
    }

    private int getFrameHeight(int panelLength) {
        Insets insets = window.getInsets();
        return panelLength + insets.top + insets.bottom;
    }

    private void run() {
        while (continueFlag) {
            repaint();
            waitAWhile();
            gameOfLife.setLiveCells(gameOfLife.tick());
            maxIndex = Math.max(gameOfLife.getMaxIndex(), maxIndex);
            cellSize = getCellSize();
            adjustFrameSize(getPanelLength());
        }
    }

    @Override
    public void paint(Graphics graphics) {
        paint((a, b) -> paintBorder(graphics, a, b));
        paint((a, b) -> paintCell(graphics, a, b));
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
        graphics.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize - 2, cellSize - 2);
    }

    private void paintBorder(Graphics  graphics, int x, int y) {
        graphics.setColor(getForeground());
        graphics.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
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
