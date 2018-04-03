import gameoflife.Cell;
import gameoflife.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.IntStream;

public class GameOfLifeApp extends JComponent implements KeyEventPostProcessor {
    private static final long WAIT_TIME = 100;
    private static final int MAX_CELL_SIZE = 100;

    private GameOfLife gameOfLife = new GameOfLife();
    private JFrame window = new JFrame();
    private int cellSize = MAX_CELL_SIZE;
    private boolean continueFlag = true;
    private int evolveToggle = 0;
    private boolean automata = true;
    private String path;
    private Cell offset;
    private Cell dimension;
    private int iteration = 0;

    GameOfLifeApp() {
    }

    private GameOfLifeApp(String[] params) {
        if (params.length > 0) {
            setup(params[0]);
        } else {
            throw new RuntimeException("Missing seeds");
        }
    }

    private void setup(String path) {
        this.path = path;
        gameOfLife.seed(loadSeeds(path));
        dimension = gameOfLife.getDimension();
        offset = gameOfLife.getOffset();
        window.setTitle(getTitle(path));
        setupFrame();
        setFocusable(true);
        setupKeyboardListener();
    }

    private void setupKeyboardListener() {
        KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
                .addKeyEventPostProcessor(this);
    }

    String[] loadSeeds(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines()
                    .toArray(String[]::new);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int getCellSize() {
        return Math.min(Math.min(getScreenSize().height * 3 / 4 / dimension.getY(),
                                 getScreenSize().width * 3 / 4 / dimension.getX()),
                        MAX_CELL_SIZE);
    }

    private void setupFrame() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().add(this);
        window.setVisible(true);
    }

    private void adjustFrame() {
        setCellSize(getCellSize());
        dimension = dimension.max(gameOfLife.getDimension());
        offset = gameOfLife.getOffset();

        int width = dimension.getX() * cellSize;
        int height = dimension.getY() * cellSize;

        setSize(width, height);

        window.setBounds(getFramePositionX(width), getFramePositionY(height),
                         getFrameWidth(width), getFrameHeight(height));
    }

    private int getFramePositionX(int panelWidth) {
        return (getScreenSize().width - getFrameWidth(panelWidth)) / 2;
    }

    private int getFramePositionY(int panelHeight) {
        return (getScreenSize().height - getFrameHeight(panelHeight)) / 2;
    }

    private Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private int getFrameWidth(int panelWidth) {
        return panelWidth + getInset(insets -> insets.left + insets.right);
    }

    private int getFrameHeight(int panelHeight) {
        return panelHeight + getInset(insets -> insets.top + insets.bottom);
    }

    private int getInset(Function<Insets, Integer> get) {
        return get.apply(window.getInsets());
    }

    private void run() {
        while (continueFlag) {
            adjustFrame();
            repaint();
            waitAWhile();
            evolve();
        }
    }

    private void evolve() {
        if (automata || evolveToggle == 0) {
            gameOfLife.evolve();
            evolveToggle++;
            window.setTitle(String.format("%s - #%d", path, iteration++));
        }
    }

    @Override
    public void paint(Graphics graphics) {
        IntStream.range(0, dimension.getX())
                .forEach(x -> paint(graphics, x));
    }

    private void paint(Graphics graphics, int x) {
        IntStream.range(0, dimension.getY())
                .forEach(y -> paint(graphics, x, y));
    }

    private void paint(Graphics graphics, int x, int y) {
        paintCell(graphics, x, y);
        paintBorder(graphics, x, y);
    }

    private void paintCell(Graphics graphics, int x, int y) {
        graphics.setColor(getColor(new Cell(x, y)));
        graphics.fillRect(x * cellSize + 1,
                          y * cellSize + 1, cellSize - 2, cellSize - 2);
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

    private void setCellSize(int value) {
        this.cellSize = Math.min(cellSize, value);
    }

    @Override
    public boolean postProcessKeyEvent(KeyEvent e) {
        boolean result = false;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                continueFlag = false;
                System.exit(0);
                break;
            case KeyEvent.VK_SPACE:
                automata = e.isControlDown();
                evolveToggle = (evolveToggle < 2) ? evolveToggle + 1 : 0;
                e.consume();
                result = true;
                break;
        }
        return result;
    }

    private String getTitle(String path) {
        return String.format("Seed: %s", path);
    }

    public static void main(String[] params) {
        new GameOfLifeApp(params).run();
    }
}
