import gameoflife.Cell;
import gameoflife.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class GameOfLifeApp extends JComponent {
    private static final int EDGE_LEN = 50;
    private static final long WAIT_TIME = 250;

    private GameOfLife gameOfLife = new GameOfLife();
    private JFrame window = new JFrame();

    private GameOfLifeApp(String[] params) {
        if (params.length > 0) {
            setup(params[0]);
            gameOfLife.seed(params[0]);
        } else {
            throw new RuntimeException("Missing seeds");
        }
    }

    private void setup(String seeds) {
        gameOfLife.seed(seeds);
        setPanelSize();
    }

    private void setPanelSize() {
        int frameLength = gameOfLife.getMaxIndex() * EDGE_LEN;
        setSize(frameLength, frameLength);
        setupFrame(frameLength);
    }

    private void setupFrame(int panelLength) {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(30, 30, panelLength, panelLength);
        window.getContentPane().add(this);
        window.setVisible(true);
    }

    private void run() {
        while (true) {
            repaint();
            gameOfLife.setLiveCells(gameOfLife.tick());
            waitAWhile();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        IntStream.range(0, gameOfLife.getMaxIndex())
                .forEach(x -> paint(graphics, x));
    }

    private void paint(Graphics graphics, int x) {
        IntStream.range(0, gameOfLife.getMaxIndex())
                .forEach(y -> paint(graphics, x, y));
    }

    private void paint(Graphics graphics, int x, int y) {
        graphics.setColor(getForeground());
        graphics.drawRect(x * EDGE_LEN, y * EDGE_LEN, EDGE_LEN, EDGE_LEN);
        graphics.setColor(getColor(new Cell(x, y)));
        graphics.fillRect(x * EDGE_LEN, y * EDGE_LEN, EDGE_LEN, EDGE_LEN);
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
