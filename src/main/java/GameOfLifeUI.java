import gameoflife.Boundary;
import gameoflife.Cell;
import gameoflife.GameOfLife;
import gameoflife.IOHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameOfLifeUI extends JComponent implements KeyEventPostProcessor {
    private static final int WAIT_TIME = 100;
    private static final int MAX_CELL_SIZE = 100;
    private static final int MIN_CELL_SIZE = 6;
    private static final String OPT_STEP = "-s";
    private static final String OPT_WAIT = "-w";

    private GameOfLife gameOfLife = new GameOfLife();
    private JFrame window = new JFrame();
    private int cellSize = MAX_CELL_SIZE;
    private boolean continueFlag = true;
    private int evolveToggle = 1;
    private boolean automata = true;
    private String path;
    private Cell offset;
    private Boundary dimension;
    private int iteration = 0;
    private int waitTime;

    private GameOfLifeUI(String[] params) {
        if (params.length > 0) {
            setup(params);
        } else {
            throw new RuntimeException("Missing seeds");
        }
    }

    private void setup(String[] params) {
        path = params[0];
        waitTime = getWaitTime(params);
        gameOfLife.seed(IOHelper.loadSeeds(path));
        dimension = gameOfLife.getDimension();
        offset = gameOfLife.getOffset();
        automata = isAutomata(params);
        window.setTitle(getTitle(path));
        window.setResizable(false);
        setupFrame();
        setFocusable(true);
        setupKeyboardListener();
    }

    private int getWaitTime(String[] params) {
        return Stream.of(params)
                .filter(param -> param.startsWith(OPT_WAIT))
                .map(param -> Integer.parseInt(param.substring(OPT_WAIT.length())))
                .findFirst()
                .orElse(WAIT_TIME);
    }

    private boolean isAutomata(String[] params) {
        return !Arrays.asList(params).contains(OPT_STEP);
    }

    private void setupKeyboardListener() {
        KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
                .addKeyEventPostProcessor(this);
    }

    private int getCellSize() {
        return Math.max(Math.min(Math.min(getScreenSize().height * 3 / 4 / dimension.getY(),
                                          getScreenSize().width  * 3 / 4 / dimension.getX()),
                                 MAX_CELL_SIZE),
                        MIN_CELL_SIZE);
    }

    private void setupFrame() {
        setCellSize(getCellSize());

        int width = dimension.getX() * cellSize;
        int height = dimension.getY() * cellSize;

        setSize(width, height);
        window.setVisible(true);
        window.setBounds(getHorizontalPosition(width), getVerticalPosition(height),
                         getFrameWidth(width), getFrameHeight(height));

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().add(this);
    }

    private int getHorizontalPosition(int panelWidth) {
        return (getScreenSize().width - getFrameWidth(panelWidth)) / 2;
    }

    private int getVerticalPosition(int panelHeight) {
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
                wait(waitTime);
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
        new GameOfLifeUI(params).run();
    }
}
